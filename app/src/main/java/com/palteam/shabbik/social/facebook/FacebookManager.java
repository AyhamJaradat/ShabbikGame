//package com.palteam.shabbik.social.facebook;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.content.Context;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.palteam.shabbik.R;
////import com.palteam.shabbik.provider.FacebookProvider.FacebookConstants;
//
//import com.facebook.FacebookException;
//import com.facebook.FacebookOperationCanceledException;
//import com.facebook.Session;
//import com.facebook.widget.WebDialog;
//import com.facebook.widget.WebDialog.OnCompleteListener;
//
//public class FacebookManager {
//
//    private final String TAG = FacebookManager.class.getSimpleName();
//
//    private Activity activity;
//    private Context context;
//
//    public FacebookManager(Activity activity) {
//        super();
//        this.activity = activity;
//        this.context = activity.getApplicationContext();
//    }
//
//    /**
//     * @return True if the session has refreshed successfully.
//     */
//    public boolean refreshConnection() {
//
//        Session session = Session.openActiveSessionFromCache(activity
//                .getApplicationContext());
//
//        if (session != null) {
//
//            Log.i(TAG, "xxx Session refreshed successfully");
//            return true;
//        }
//
//        Log.i(TAG, "xxx Session refreshed failed");
//        return false;
//
//    }
//
//    /**
//     * Send invitation to Facebook friend using given id.
//     *
//     * @param friendId Facebook id for friend to invite.
//     */
//    public void inviteFriend(String friendId) {
//
//        Bundle params = new Bundle();
//        params.putString(FacebookConstants.Keys.MESSAGE, activity
//                .getResources().getString(R.string.facebook_invitation_message));
//
//        WebDialog requestsDialog = (new WebDialog.RequestsDialogBuilder(
//                activity, Session.getActiveSession(), params)).setTo(friendId)
//                .setOnCompleteListener(new OnCompleteListener() {
//
//                    @Override
//                    public void onComplete(Bundle values,
//                                           FacebookException error) {
//                        if (error != null) {
//                            if (error instanceof FacebookOperationCanceledException) {
//                                Toast.makeText(context, "Request cancelled",
//                                        Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(context, "Network Error",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            final String requestId = values
//                                    .getString(FacebookConstants.Keys.REQUEST);
//                            if (requestId != null) {
//                                Toast.makeText(context, "Request sent",
//                                        Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(context, "Request cancelled",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//
//                }).build();
//        requestsDialog.show();
//    }
//
//    public static void shareOnFacebook(Activity activity) {
//
//        Bundle params = new Bundle();
//        params.putString("name", activity.getString(R.string.post_name));
//        params.putString("caption", activity.getString(R.string.post_caption));
//        params.putString("description",
//                activity.getString(R.string.post_description));
//        params.putString("link", String.format(activity
//                .getString(R.string.post_link), activity.getApplication()
//                .getPackageName()));
//        params.putString("picture", activity.getString(R.string.post_image));
//
//        WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(activity,
//                Session.getActiveSession(), params)).build();
//        feedDialog.show();
//    }
//
//}
