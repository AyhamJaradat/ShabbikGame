package com.palteam.shabbik.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PreferencesManager {

    private String TAG = PreferencesManager.class.getSimpleName();

    private final String SHARED_PREFERENCES_NAME = "shabbik_shared_preferences";
    private SharedPreferences sharedPreferences;
    private Context context;

    /**
     * GCM user id.
     */
    public static final String PROPERTY_GCM_REGISTRATION_ID = "registration_id";

    /**
     * Application version.
     */
    private static final String PROPERTY_APP_VERSION = "app_version";

    public PreferencesManager(Context context) {

        this.context = context;

        sharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    public String getRegistrationId() {

        String registrationId = sharedPreferences.getString(
                PROPERTY_GCM_REGISTRATION_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = sharedPreferences.getInt(PROPERTY_APP_VERSION,
                Integer.MIN_VALUE);
        int currentVersion = AppUtils.getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     *
     * @param regId   registration ID
     */
    public void storeRegistrationId(String regId) {
        int appVersion = AppUtils.getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PROPERTY_GCM_REGISTRATION_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();

    }

}
