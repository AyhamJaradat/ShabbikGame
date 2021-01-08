package com.palteam.shabbik.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.palteam.shabbik.R;
import com.palteam.shabbik.activities.DialogActivityForRegistrationChooser;
import com.palteam.shabbik.beans.User;
import com.palteam.shabbik.gcm.GCMManager;
import com.palteam.shabbik.sqllight.ShabbikDAO;
import com.palteam.shabbik.webservice.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationManager implements GCMManager.OnGCMUserRegister, IConstants {
    private final String TAG = RegistrationManager.class.getSimpleName();

    private User user;
    private GCMManager gcmManager;
    private Activity activity;
    private ShabbikDAO sqlLiteDB;

    private OnRegistrationDoneListener onRegistrationDoneListener;


    public RegistrationManager(Activity activity, OnRegistrationDoneListener onRegistrationDoneListenerListener) {

        this.activity = activity;
        this.onRegistrationDoneListener = onRegistrationDoneListenerListener;

        // Initialize needed components.
        user = new User();
        gcmManager = new GCMManager(activity);
        sqlLiteDB = ShabbikDAO.getInstance(activity.getApplicationContext());

    }
//kdsaldk;lasdk;lasd
    /**
     * Register give user.
     *
     * @param user user to be registered.
     */
    public void register(User user) {

        this.user = user;

        gcmManager.registerUser(this);
    }


    @Override
    public void onGCMUserRegister(String regId) {

        // Add gcm key to user object.
        user.setUserKey(regId);
        Log.i("RegistrationManager","regId:"+regId);
        // Check if user already registered or not.
        if (sqlLiteDB.isMainUserRegistor()) {

            // Already registered, update user data on server.
            FirebaseCrashlytics.getInstance().log("INFO::"+TAG+"::"+"GCM load,,, user ID : "
                    + sqlLiteDB.retrieveMainUserId());

            user.setId(sqlLiteDB.retrieveMainUserId());
            updateUserOnServer(user);

        } else {

            // New user, add to server.
            saveUserOnServer(user);
        }

    }

    /**
     * Called to send user data to back-end server.
     *
     * @param user app user to be registered on the back-end server.
     */
    private void saveUserOnServer(User user) {
        Log.i("RegistrationManager","Save User On Server");
        new SignUpAsyncTask(user).execute();

    }

    /**
     * Update user info on the server.
     *
     * @param user user to be updated.
     */
    private void updateUserOnServer(User user) {
        Log.i("RegistrationManager","update User On Server");
        new UpdateUserInfoAsyncTask(user).execute();

    }

    /**
     * Async task to register new user on server.
     */
    private class SignUpAsyncTask extends AsyncTask<Void, Void, String> {

        // private ProgressDialog dialog;
        private User user;

        public SignUpAsyncTask(User user) {
            this.user = user;
        }

        @Override
        protected String doInBackground(Void... params) {

            Map<String, String> parameters = new HashMap<>();
            parameters.put(USER_F_NAME, user.getUserFirstName());
            parameters.put(USER_L_NAME, user.getUserLastName());
            parameters.put(USER_EMAIL, user.getUserEmail());
            parameters.put(USER_FACEBOOK_ID, user.getUserFBId());
            parameters.put(USER_KEY, user.getUserKey());

            String parametersString = WebService
                    .createQueryStringForParameters(parameters);
            String url = activity.getResources().getString(
                    R.string.web_service_http_address)
                    + activity.getResources().getString(
                    R.string.sign_up_web_service_address);

            String jsonString = WebService.requestWebService(url,
                    parametersString);

            // if return null ,, try again for more one time
            if (jsonString == null) {
                jsonString = WebService
                        .requestWebService(url, parametersString);
            }

            return jsonString;

        }

        protected void onPostExecute(String results) {

            // Done saving on server.
            if (results != null) {

                // Results not null.
                try {

                    JSONObject jsonObject = new JSONObject(results);
                    Log.i(TAG,jsonObject.toString());
                    boolean status = jsonObject.getBoolean(JSON_STATUS);

                    if (status) {
                        Log.i(TAG,"status is true");
                        // User successfully registered on server.
//                        JSONObject jsonData= jsonObject.getJSONObject("data");
//                        int id = jsonData.getInt("id");
                        int id = jsonObject.getInt(USER_ID);

                        user.setId(id);

                        addUserToLocalDatabase();

                        return;
                    }

                } catch (JSONException e) {

                    Log.e("at SignUpAsyncTask",
                            "JSONException at onPostExecute ");

                }

            }

            // Failed to register user on the server.
            onRegistrationDoneListener.onRegistrationFailed();

        }

        /**
         * Add new user data to local database.
         */
        private void addUserToLocalDatabase() {

            // save data to database.
            sqlLiteDB.saveMainUser(user);

            // Add user Record To localDatabase
            boolean isSaved = sqlLiteDB.insertNewUser(user);

            if (isSaved) {

                // Successfully saved to local database.
                onRegistrationDoneListener.onRegistrationSucceed();

            } else {

                // Failed to save to local database;
                onRegistrationDoneListener.onRegistrationFailed();

            }

        }

    }

    /**
     * Async task to update user info.
     */
    private class UpdateUserInfoAsyncTask extends AsyncTask<Void, Void, String> {

        private User user;

        public UpdateUserInfoAsyncTask(User user) {
            this.user = user;
        }

        @Override
        protected String doInBackground(Void... params) {

            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put(USER_ID, user.getId() + "");
            parameters.put(USER_F_NAME, user.getUserFirstName());
            parameters.put(USER_L_NAME, user.getUserLastName());
            parameters.put(USER_FACEBOOK_ID, user.getUserFBId());

            String parametersString = WebService
                    .createQueryStringForParameters(parameters);
            String url = activity.getResources().getString(
                    R.string.web_service_http_address)
                    + activity.getResources().getString(
                    R.string.update_user_info_web_service_address);

            String jsonString = WebService.requestWebService(url,
                    parametersString);

            // if return null ,, try again for more one time
            if (jsonString == null) {
                jsonString = WebService
                        .requestWebService(url, parametersString);
            }

            return jsonString;

        }

        protected void onPostExecute(String results) {

            // Check server results.
            if (results != null) {

                // Results not null.
                try {

                    JSONObject jsonObject = new JSONObject(results);
                    boolean status = jsonObject.getBoolean(JSON_STATUS);

                    if (status) {

                        // Successfully updating user info on server.
                        int id = jsonObject.getInt(USER_ID);

                        // set the user ID;
                        user.setId(id);

                        updateUserInfoOnLocalDatabase();

                        return;
                    }

                } catch (JSONException e) {
                    Log.e("at UUserInfoAsyncTask",
                            "JSONException at onPostExecute ");
                }

            }

            // Failed to update user info on the server.
            onRegistrationDoneListener.onRegistrationFailed();

        }

        /**
         * Update the user info on local database.
         */
        private void updateUserInfoOnLocalDatabase() {

            // save data to database.
            sqlLiteDB.saveMainUser(user);

            // Update User record in localDatabase
            boolean isSaved = sqlLiteDB.updateUserInfo(user);

            if (isSaved) {

                onRegistrationDoneListener.onRegistrationSucceed();

            } else {

                onRegistrationDoneListener.onRegistrationFailed();
            }

        }
    }


    /**
     * Interface work as call-back to notify registration caller about the registration result (Succeed, Failed).
     */
    public interface OnRegistrationDoneListener {

        /**
         * Called when registration done successfully.
         */
        public void onRegistrationSucceed();

        /**
         * Called when registration failed.
         */
        public void onRegistrationFailed();
    }


    /**
     * Show dialog activity with Facebook login option only.
     *
     * @param activity activity to show dialog activity over.
     */
    public static void showFacebookRegistrationDialog(Activity activity) {

        Intent registrationTypeChooserDialogActivity = new Intent(
                activity,
                DialogActivityForRegistrationChooser.class);
        registrationTypeChooserDialogActivity.putExtra(
                IConstants.REGISTRATION_TYPE_EXTRA,
                IConstants.RegistrationType.FACEBOOK.ordinal());
        activity.startActivity(registrationTypeChooserDialogActivity);

    }


    /**
     * Show dialog activity with Manual & Facebook login options.
     *
     * @param activity activity to show dialog activity over.
     */
    public static void showFacebookManualRegistrationDialog(Activity activity) {

        Intent registrationTypeChooserDialogActivity = new Intent(
                activity,
                DialogActivityForRegistrationChooser.class);
        registrationTypeChooserDialogActivity.putExtra(
                IConstants.REGISTRATION_TYPE_EXTRA,
                IConstants.RegistrationType.MANUAL.ordinal());
        activity.startActivity(registrationTypeChooserDialogActivity);

    }
}
