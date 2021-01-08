//package com.palteam.shabbik.provider;
//
//import android.app.Activity;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//
//import com.facebook.Request;
//import com.facebook.Request.GraphUserListCallback;
//import com.facebook.Response;
//import com.facebook.Session;
//import com.facebook.model.GraphUser;
//import com.palteam.shabbik.beans.FacebookUser;
//import com.palteam.shabbik.utils.IConstants;
//
//public class FacebookProvider {
//
//    public static class FacebookConstants {
//
//        public static class Keys {
//
//            // Users info
//            public static String FIELDS = "fields";
//
//            // Invitation dialog.
//            public static String MESSAGE = "message";
//            public static String REQUEST = "request";
//        }
//
//        public static class Fields {
//
//            // Users info.
//            public static String ID = "id";
//            public static String FIRST_NAME = "first_name";
//            public static String LAST_NAME = "last_name";
//
//        }
//
//    }
//
//    private final String TAG = FacebookProvider.class.getSimpleName();
//
//    private OnFacebookUserProfileLoaded onFacebookUserProfileLoaded;
//    private OnFacebookFriendsLoaded onFacebookFriendsLoaded;
//
//    private Activity activity;
//
//    public FacebookProvider(Activity activity) {
//        super();
//        this.activity = activity;
//    }
//
//    /**
//     * Get Facebook user data asynchronously.
//     *
//     * @param session Facebook active session.
//     */
//    public void getUserProfile(final Session session) {
//
//        Request request = Request.newMeRequest(session,
//                new Request.GraphUserCallback() {
//                    @Override
//                    public void onCompleted(GraphUser user, Response response) {
//
//                        // If the response is successful
//                        if (session == Session.getActiveSession()) {
//                            if (user != null) {
//
//                                FacebookUser facebookUser = new FacebookUser();
//                                facebookUser.setId(user.getId());
//                                facebookUser.setFirstName(user.getFirstName());
//                                facebookUser.setLastName(user.getLastName());
//
//                                onFacebookUserProfileLoaded = (OnFacebookUserProfileLoaded) activity;
//                                onFacebookUserProfileLoaded
//                                        .onFacebookUserProfileLoaded(facebookUser);
//                            }
//                        }
//
//                    }
//                });
//        request.executeAsync();
//    }
//
//    /**
//     * Get user Facebook friends asynchronously.
//     *
//     * @param session Facebook active session.
//     */
//    public void getFacebookFriends(final Session session) {
//
//        Request request = Request.newMyFriendsRequest(session,
//                new GraphUserListCallback() {
//
//                    @Override
//                    public void onCompleted(List<GraphUser> users,
//                                            Response response) {
//
//                        if (response.getError() == null) {
//
//                            onFacebookFriendsLoaded = (OnFacebookFriendsLoaded) activity;
//                            onFacebookFriendsLoaded
//                                    .onFacebookFriendsLoaded(users);
//
//                        } else {
//
//                            Log.i(TAG,
//                                    "Loading Friends error = "
//                                            + response.getError());
//                        }
//
//                    }
//                });
//
//        Bundle bundle = request.getParameters();
//        bundle.putString(FacebookConstants.Keys.FIELDS,
//                FacebookConstants.Fields.ID + ","
//                        + FacebookConstants.Fields.FIRST_NAME + ","
//                        + FacebookConstants.Fields.LAST_NAME);
//
//        request.executeAsync();
//    }
//
//    /**
//     * Interface to send data for the caller activity. Used to update activity
//     * UI when data is ready.
//     *
//     * @author Ahmad Abu Rjeila.
//     */
//    public interface OnFacebookUserProfileLoaded {
//
//        /**
//         * Called when the Facebook user profile data is ready.
//         *
//         * @param facebookUser Facebook user model.
//         */
//        public void onFacebookUserProfileLoaded(FacebookUser facebookUser);
//
//    }
//
//    /**
//     * Interface to Facebook user friends for the caller activity. Used to
//     * update activity UI when data is ready.
//     *
//     * @author Ahmad Abu Rjeila.
//     */
//    public interface OnFacebookFriendsLoaded {
//
//        /**
//         * Called when the Facebook user friends data is ready.
//         *
//         * @param friends list of Facebook user friends.
//         */
//        public void onFacebookFriendsLoaded(List<GraphUser> friends);
//    }
//
//    /**
//     * Check if user currently logged in to Facebook or not.
//     *
//     * @return true if user logged in with active session, false if not.
//     */
//    public boolean isFacebookLoggedIn() {
//
//        Session session = Session.getActiveSession();
//
//        if (session != null && session.isOpened()) {
//
//            return true;
//        }
//
//        return false;
//    }
//
//    /**
//     * Get Facebook profile picture URI from Facebook id.
//     *
//     * @param facebookId user id on facebook.
//     * @return Facebook user profile picture.
//     */
//    public static Uri getFacebookUserProfileURI(String facebookId) {
//
//        if (facebookId != null && !facebookId.equals(IConstants.NULL_VALUE)) {
//
//            return Uri.parse("http://graph.facebook.com/" + facebookId
//                    + "/picture?type=large");
//        } else {
//
//            return Uri.parse("");
//        }
//
//    }
//}
