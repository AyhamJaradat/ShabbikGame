package com.palteam.shabbik.activities;



import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
import com.palteam.shabbik.R;

import java.util.ArrayList;
import java.util.List;

import com.palteam.shabbik.beans.Game;
import com.palteam.shabbik.beans.Round;
import com.palteam.shabbik.beans.User;
//import com.palteam.shabbik.provider.FacebookProvider;
import com.palteam.shabbik.services.PullNotificationService;
import com.palteam.shabbik.sqllight.ShabbikDAO;
import com.palteam.shabbik.utils.AppUtils;
import com.palteam.shabbik.utils.IConstants;
import com.palteam.shabbik.utils.RegistrationManager;

public class MainMenuActivity extends Activity implements OnClickListener,
        IConstants {

    private final String TAG = MainMenuActivity.class.getSimpleName();

    private ImageView playWordGameImageView, playSentenceGameImageView,
            challengeImageView, profileImageView;

//    private FacebookProvider facebookProvider;

    // private SharedPreferences sharedPreferences;

//    private AdView banner;

    private ShabbikDAO sqlLiteDB;

    private TextView numberOfChallengesTextView;

    int numberOfUserChallenges = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Im in on create ","");
        AppUtils.logHeap(TAG);
        setContentView(R.layout.activity_main_menu);
//        setContentView(R.layout.main_menu_activity);

        init();

//        loadAd(R.id.banner_ad);

        // start Pull Notification service
        startPullNotificationService();

        // Intent generateService = new Intent(getApplicationContext(),
        // GeneratePuzzleWordsService.class);
        // startService(generateService);

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Im in Restart","");
    }

    /*
    remove layout resource
     */
    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
                Log.i("view",""+i);
            }
            ((ViewGroup) view).removeAllViews();
            view.setBackgroundResource(0);
        }
    }
    /**
     * Load advertisement in current view.
     */
//    private void loadAd(int viewId) {
//
//        try {
//
//            // Load ads.
//            banner = (AdView) findViewById(viewId);
//            AdRequest adRequest = new AdRequest.Builder().build();
//            banner.loadAd(adRequest);
//
//        } catch (Exception e) {
//
//            Log.i(TAG, "" + e.getMessage());
//        }
//    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub

        super.onDestroy();
//        if (banner != null) {
//            banner.destroy();
//            banner = null;
//        }

        Log.i("Im in main destroy", "");
        RelativeLayout mainlayouttt =   (RelativeLayout) this
                .findViewById(R.id.main_Layout);
        unbindDrawables(mainlayouttt);

    }

    // start monitoring service
    public void startPullNotificationService() {

        // check if its already running or not
        if (!isMyServiceRunning("words.maker.words.services.PullNotificationService")) {

            Log.i("Service status", "Not Running");
            Intent monitoringIntent = new Intent(getApplicationContext(),
                    PullNotificationService.class);
            this.startService(monitoringIntent);

        }

    }

    // check if the service running or not
    private boolean isMyServiceRunning(String serviceFullName) {

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (serviceFullName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void init() {

        playWordGameImageView = (ImageView) findViewById(R.id.playWordGameImageView);
        playWordGameImageView.setOnClickListener(this);

        playSentenceGameImageView = (ImageView) findViewById(R.id.playSentenceGameImageView);
        playSentenceGameImageView.setOnClickListener(this);

        challengeImageView = (ImageView) findViewById(R.id.challengeImageView);
        challengeImageView.setOnClickListener(this);

        profileImageView = (ImageView) findViewById(R.id.profileImageView);
        profileImageView.setOnClickListener(this);

        numberOfChallengesTextView = (TextView) findViewById(R.id.textViewForNumberOfChallenges);

//        facebookProvider = new FacebookProvider(this);

        // sharedPreferences = getSharedPreferences(
        // IConstants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        sqlLiteDB = ShabbikDAO.getInstance(this);

    }

    @Override
    public void onClick(View v) {

        Intent toActivty;
        switch (v.getId()) {

            case R.id.playWordGameImageView:
                toActivty = new Intent(getApplicationContext(),
                        SecondMenuActivity.class);
                toActivty.putExtra(GAME_MODE, WORD_GAME_VALUE);
                startActivity(toActivty);
                break;
            case R.id.playSentenceGameImageView:
                toActivty = new Intent(getApplicationContext(),
                        SecondMenuActivity.class);
                toActivty.putExtra(GAME_MODE, SENTENCE_GAME_VALUE);
                startActivity(toActivty);
                break;

            case R.id.profileImageView:

                // Check if user registered or not.
                if (sqlLiteDB.isMainUserRegistor()) {

                    toActivty = new Intent(getApplicationContext(),
                            ProfileActivity.class);

                    startActivity(toActivty);

                } else {

                    // Not registered, show registration dialog.
                    RegistrationManager.showFacebookManualRegistrationDialog(this);

                }

                break;

            case R.id.challengeImageView:

                // Check if user registered or not.
                if (sqlLiteDB.isMainUserRegistor()) {

                    toActivty = new Intent(getApplicationContext(),
                            ChallengeActivity.class);
                    startActivity(toActivty);

                } else {

                    // Not registered, show registration dialog.
                    RegistrationManager.showFacebookManualRegistrationDialog(this);

                }

                break;

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        numberOfUserChallenges = 0;

        // Set number of user challenges if it's larger than 0.
        retrieveAllGameInfo();

        if (numberOfUserChallenges > 0) {

            numberOfChallengesTextView.setVisibility(View.VISIBLE);
            numberOfChallengesTextView.setText("" + numberOfUserChallenges);

        } else {
            numberOfChallengesTextView.setVisibility(View.GONE);
        }
    }

    // Challenges data
    private void retrieveAllGameInfo() {

        // retrieve all Games from LocalDataBase
        List<Game> allGames = sqlLiteDB.retrieveAllGamesInfo();

        calculateNumberOfUserChallenges(allGames);

        for (Game game : allGames) {
            // retrieve users info for each game
            User firstUser = sqlLiteDB.retrieveUserInfo(game.getFirstUserId());
            User secondUser = sqlLiteDB
                    .retrieveUserInfo(game.getSecondUserId());
            game.setFirstUser(firstUser);
            game.setSecondUser(secondUser);

            // retrieve rounds info for each game
            ArrayList<Round> rounds = sqlLiteDB.retrieveRoundsOfGame(game
                    .getId());
            game.setFirstRound(rounds.get(0));
            game.setSecondRound(rounds.get(1));
            game.setThirdRound(rounds.get(2));
        }

    }

    /**
     * @param allGames
     * @return
     */
    private void calculateNumberOfUserChallenges(List<Game> allGames) {

        int userId = sqlLiteDB.retrieveMainUserId();

        if (userId != -1) {

            for (Game game : allGames) {

                // retrieve rounds info for each game
                ArrayList<Round> rounds = sqlLiteDB.retrieveRoundsOfGame(game
                        .getId());
                game.setFirstRound(rounds.get(0));
                game.setSecondRound(rounds.get(1));
                game.setThirdRound(rounds.get(2));

                int contactTurnId = game.getUserTurnId();

                if (game.getCurrentRoundId() != IConstants.FINISHED_GAME_CURRENT_ROUND_ID
                        && contactTurnId == userId) {

                    numberOfUserChallenges++;
                }

            }

        } else {
            Log.i(TAG, "Failed to get user id from shared preferences");
        }

    }

}
