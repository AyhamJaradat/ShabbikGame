//package com.palteam.shabbik.activities;
//
//
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Toast;
//
////import com.facebook.LoggingBehavior;
////import com.facebook.Session;
////import com.facebook.SessionState;
////import com.facebook.Settings;
//import com.palteam.shabbik.R;
//
//import com.palteam.shabbik.beans.FacebookUser;
//import com.palteam.shabbik.beans.User;
//import com.palteam.shabbik.provider.FacebookProvider;
//import com.palteam.shabbik.provider.FacebookProvider.OnFacebookUserProfileLoaded;
//import com.palteam.shabbik.utils.AppUtils;
//import com.palteam.shabbik.utils.RegistrationManager;
//
//public class FacebookLoginActivity extends Activity implements
//        OnFacebookUserProfileLoaded, RegistrationManager.OnRegistrationDoneListener  {
//
//
//    private final String TAG = FacebookLoginActivity.class.getSimpleName();
//
////    private Session.StatusCallback statusCallback = new SessionStatusCallback();
//
//    private FacebookProvider facebookProvider;
//
//    private Context context;
//
//    private ProgressDialog loadingDialog;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
////        created with activity, but no layout here
////        setContentView(R.layout.activity_facebook_login);
////        AppUtils.logHeap(TAG);
//
////        init();
//
////        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
////
////        Session session = Session.getActiveSession();
////        if (session == null) {
////            if (savedInstanceState != null) {
////                session = Session.restoreSession(this, null, statusCallback,
////                        savedInstanceState);
////            }
////            if (session == null) {
////                session = new Session(this);
////            }
////            Session.setActiveSession(session);
////            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
////                session.openForRead(new Session.OpenRequest(this)
////                        .setCallback(statusCallback));
////            }
////        }
//
//        loginToFacebook();
//    }
//    /**
//     * Initialize activity component.
//     */
//    private void init() {
//
//        context = this;
//
//        facebookProvider = new FacebookProvider(this);
//
//        loadingDialog = new ProgressDialog(context);
//        loadingDialog.setMessage(getResources().getString(
//                R.string.please_wait_msg));
//        loadingDialog.show();
//
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
////        Session.getActiveSession().addCallback(statusCallback);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
////        Session.getActiveSession().removeCallback(statusCallback);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
////        Session.getActiveSession().onActivityResult(this, requestCode,
////                resultCode, data);
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
////        Session session = Session.getActiveSession();
////        Session.saveSession(session, outState);
//    }
//
//    /**
//     * Apply login to Facebook.
//     */
//    private void loginToFacebook() {
////        Session session = Session.getActiveSession();
////        if (!session.isOpened() && !session.isClosed()) {
////            session.openForRead(new Session.OpenRequest(this)
////                    .setCallback(statusCallback));
////        } else {
////            Session.openActiveSession(this, true, statusCallback);
////        }
//    }
//
//
////    private class SessionStatusCallback implements Session.StatusCallback {
////        @Override
////        public void call(Session session, SessionState state,
////                         Exception exception) {
////
////            checkFacebookSessionStatus();
////        }
////    }
//
//    /**
//     * Check the Facebook session status (Closed, Opened, ...).
//     */
//    private void checkFacebookSessionStatus() {
//
////        Session session = Session.getActiveSession();
////
////        switch (session.getState()) {
////
////            case CLOSED:
////                finish();
////                break;
////
////            case CLOSED_LOGIN_FAILED:
////                finish();
////                break;
////
////            case OPENED:
////                facebookProvider.getUserProfile(session);
////                break;
////
////            default:
////                break;
////
////        }
//
//    }
//
//    @Override
//    public void onFacebookUserProfileLoaded(FacebookUser facebookUser) {
//
//        User user = convertFacebookUserToUser(facebookUser);
//
//        new RegistrationManager(this, this).register(user);
//    }
//
//    @Override
//    public void onRegistrationSucceed() {
//
//        Toast.makeText(this,
//                getString(R.string.register_successfully),
//                Toast.LENGTH_SHORT).show();
//
//        finish();
//
//    }
//
//    @Override
//    public void onRegistrationFailed() {
//
//        Toast.makeText(this,
//                getString(R.string.failer_message),
//                Toast.LENGTH_SHORT).show();
//
//        finish();
//
//    }
//
//    /**
//     * Convert given facebook user to user object.
//     *
//     * @param facebookUser facebook user to be converted.
//     * @return converted user.
//     */
//    private User convertFacebookUserToUser(FacebookUser facebookUser) {
//
//        User user = new User();
//        user.setUserFBId(facebookUser.getId());
//        user.setUserFirstName(facebookUser.getFirstName());
//        user.setUserLastName(facebookUser.getLastName());
//
//        return user;
//    }
//
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        // dismiss the progress dialog before closing the activity.
//        if (loadingDialog.isShowing()) {
//            loadingDialog.dismiss();
//        }
//    }
//
//
//}
