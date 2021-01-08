package com.palteam.shabbik.gcm;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.palteam.shabbik.utils.AppUtils;
import com.palteam.shabbik.utils.PreferencesManager;


public class GCMManager {

    private final String TAG = GCMManager.class.getSimpleName();
//
//    private GoogleCloudMessaging gcm;
    private FirebaseMessaging gcm;
    private PreferencesManager preferencesManager;
//
    private Activity activity;
    private Context context;
//
    private String regId;
//
    private OnGCMUserRegister onGCMUserRegister;
//
    /**
     * Project number on the application created on google console.
     */
    private static final String PROJECT_NUMBER = "61977969674";
//
    public GCMManager(Activity activity) {

        this.activity = activity;
        this.context = activity.getApplicationContext();

//        gcm = GoogleCloudMessaging.getInstance(context);
        gcm = FirebaseMessaging.getInstance();
        preferencesManager = new PreferencesManager(context);

    }
//
    /**
     * Register user in GCM service.
     */
    public void registerUser(OnGCMUserRegister onGCMUserRegister) {

        this.onGCMUserRegister = onGCMUserRegister;

        // Check device for Play Services APK.
        if (AppUtils.checkPlayServices(activity)) {

            regId = preferencesManager.getRegistrationId();

            if (regId.isEmpty()) {

                registerInBackground();

            } else {

                onGCMUserRegister.onGCMUserRegister(regId);

            }

        } else {

            Log.i(TAG, "No valid Google Play Services APK found.");
        }

    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {

        new RegisterInBackgroundAsyncTask().execute();

    }

    /**
     * Async task to apply GCM registration in background.
     */
    class RegisterInBackgroundAsyncTask extends
            AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String msg = "";

                if (gcm == null) {
//                    gcm = GoogleCloudMessaging.getInstance(context);
                    gcm = FirebaseMessaging.getInstance();
                }
                //                regId = gcm.register(PROJECT_NUMBER);

                gcm.getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                    return;
                                }

                                // Get new FCM registration token
                                String token = task.getResult();
                                Log.d(TAG, "FCM TOKEN IS: "+token);
                                regId =token;
                                // Persist the regID - no need to register again.
                                preferencesManager.storeRegistrationId(regId);

                                onGCMUserRegister.onGCMUserRegister(regId);

                            }
                        });
            return msg;
        }

        @Override
        protected void onPostExecute(String msg) {
            super.onPostExecute(msg);

        }

    }

    /**
     * Call back interface to update caller activity when registration done
     * successfully.
     *
     * @author Ahmad Abu Rjeila.
     */
    public interface OnGCMUserRegister {

        /**
         * Called when the GCM registration done successfully.
         *
         * @param regId user GCM registration id.
         */
        public void onGCMUserRegister(String regId);
    }

}
