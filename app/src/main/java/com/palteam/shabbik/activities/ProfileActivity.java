package com.palteam.shabbik.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.palteam.shabbik.R;

import com.palteam.shabbik.beans.FacebookUser;
import com.palteam.shabbik.beans.User;
//import com.palteam.shabbik.provider.FacebookProvider;
//import com.palteam.shabbik.provider.FacebookProvider.OnFacebookUserProfileLoaded;
import com.palteam.shabbik.sqllight.ShabbikDAO;
import com.palteam.shabbik.utils.EmailUtils;
import com.palteam.shabbik.utils.FileUtils;
import com.palteam.shabbik.utils.ImageLoaderManager;

public class ProfileActivity extends Activity /*implements
        OnFacebookUserProfileLoaded*/{
    private final String TAG = ProfileActivity.class.getSimpleName();

    private ImageView profilePictureImageView;
    private TextView userNameTextView, lastScoreTextView, highestScoreTextView,
            numberOfSentencesTextView, numberOfWordsTextView;

    private ImageLoaderManager imageLoaderManager;
    private ShabbikDAO sqlLiteDB;

    private Context context;
    private User user;

    // private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sqlLiteDB = ShabbikDAO.getInstance(context);

        user = getUserInfo();

        if (user != null) {

            init();

            fillUI();
        } else {
            Intent toActivity = new Intent(context, SignUpActivity.class);

            startActivity(toActivity);
            ProfileActivity.this.finish();

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LinearLayout profilelayout =   (   LinearLayout) this
                .findViewById(R.id.profile_layout);
        unbindDrawables(profilelayout );
    }
    /*
            remove layout resources
             */
    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
                android.util.Log.i("view", "" + i);
            }
            ((ViewGroup) view).removeAllViews();
            view.setBackgroundResource(0);
        }
    }

    /**
     * Initialize activity components.
     */
    private void init() {

        profilePictureImageView = (ImageView) findViewById(R.id.profilePictureForUserPhoto);

        // Disable sending database-log
        // profilePictureImageView.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // // TODO Auto-generated method stub
        // new CopyFilesInBackground().execute();
        // }
        // });

        userNameTextView = (TextView) findViewById(R.id.textViewForUserName);

        lastScoreTextView = (TextView) findViewById(R.id.textViewForLastScoreValue);
        highestScoreTextView = (TextView) findViewById(R.id.textViewForHighScoreValue);
        numberOfSentencesTextView = (TextView) findViewById(R.id.textViewForNumberOfSentencesValue);
        numberOfWordsTextView = (TextView) findViewById(R.id.textViewForNumberOfWordsValue);

        context = getApplicationContext();

        imageLoaderManager = new ImageLoaderManager();

    }

    /**
     * Get the application user info from local database;
     *
     * @return application user info.
     */
    private User getUserInfo() {

        if (sqlLiteDB.isMainUserRegistor()) {

            int userId = sqlLiteDB.retrieveMainUserId();

            if (userId != -1) {
                return sqlLiteDB.getUserProfileInfo(sqlLiteDB
                        .retrieveUserInfo(userId));
            } else {
                return null;
            }

        } else {
            return null;
        }
    }

    /**
     * Fill UI with user info.
     */
    private void fillUI() {

//        imageLoaderManager.loadImage(profilePictureImageView,
//                FacebookProvider.getFacebookUserProfileURI(user.getUserFBId()));

        userNameTextView.setText(user.getUserFirstName() + " "
                + user.getUserLastName());

        lastScoreTextView.setText("" + user.getLatestScore());
        highestScoreTextView.setText("" + user.getHighestScore());
        numberOfSentencesTextView.setText("" + user.getNumberOfSentences());
        numberOfWordsTextView.setText("" + user.getNumberOfWords());

    }

//    @Override
//    public void onFacebookUserProfileLoaded(FacebookUser facebookUser) {
//
//        user.setUserFBId(facebookUser.getId());
//        user.setUserFirstName(facebookUser.getFirstName());
//        user.setUserLastName(facebookUser.getLastName());
//
//        // sqlLiteDB.registerUser(user);
//
//        fillUI();
//
//    }


    /**
     * Copy Database-Log files in background to external storage.
     *
     * @author EXALT Technologies.
     */
    private class CopyFilesInBackground extends
            AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            FileUtils.prepareDatabaseLogToSend(ProfileActivity.this);

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Hide copying dialog
            // copyingDialog.dismiss();

            EmailUtils.sendDatabaseLog(ProfileActivity.this);
        }

    }

}
