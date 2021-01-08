package com.palteam.shabbik.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.palteam.shabbik.beans.Game;
import com.palteam.shabbik.beans.Round;
import com.palteam.shabbik.beans.User;
//import com.palteam.shabbik.provider.FacebookProvider;
import com.palteam.shabbik.sqllight.ShabbikDAO;
import com.palteam.shabbik.utils.AppUtils;
import com.palteam.shabbik.utils.IConstants;
import com.palteam.shabbik.utils.ImageLoaderManager;


public class RoundsViewActivity extends Activity implements OnClickListener,
        IConstants {
    private final String TAG = RoundsViewActivity.class.getSimpleName();

    /*
     * UI Elements
     */
    private ImageView firstPlayerImageView, secondPlayerImageView,
            startPlayingImageView;
    private TextView firstPlayerNameTextView, secondPlayerNameTextView,
            firstPlayerScoreTextView, secondPlayerScoreTextView,
            firstRoundFirstPlayerScoreTextView,
            secondRoundFirstPlayerScoreTextView,
            thirdRoundFirstPlayerScoreTextView,
            firstRoundSecondPlayerScoreTextView,
            secondRoundSecondPlayerScoreTextView,
            thirdRoundSecondPlayerScoreTextView, totalFirstUserRoundsScore,
            totalSecondUserRoundsScore, youTurnTextView;

    /*
     * functionality variables
     */
    private int gameId;
    private Game game;
    private User firstUser, secondUser;
    private ArrayList<Round> rounds;
    private ShabbikDAO sqlLiteDB;
    private Context context;
    // private SharedPreferences sharedPreferences;
    private int userId;

//    private FacebookProvider facebookProvider;
    private ImageLoaderManager imageLoaderManager;
    private int totalFirstUserScore = 0, totalSecondUserScor = 0;
//    AdView banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppUtils.logHeap(TAG);
        setContentView(R.layout.activity_rounds_view);
//        setContentView(R.layout.rounds_view_activity);

        init();

//        loadAd(R.id.banner_ad);

        gameId = getIntent().getExtras().getInt(GAME_ID);

        if (gameId <= 0) {

            Intent toActivty = new Intent(context, MainMenuActivity.class);
            toActivty.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            RoundsViewActivity.this.finish();
            context.startActivity(toActivty);
            return;

        } else {
            getGameInfo(gameId);

            fillInformationInUIView();

        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

//        if (banner != null) {
//            banner.destroy();
//            banner = null;
//        }
        RelativeLayout roundslayout =   (   RelativeLayout ) this
                .findViewById(R.id.roundsLayout);
        unbindDrawables(roundslayout );
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
    private void fillInformationInUIView() {

        // users Images
//        imageLoaderManager.loadImage(firstPlayerImageView, FacebookProvider
//                .getFacebookUserProfileURI(firstUser.getUserFBId()));
//
//        imageLoaderManager.loadImage(secondPlayerImageView, FacebookProvider
//                .getFacebookUserProfileURI(secondUser.getUserFBId()));

        // Users Name
        String firstUserFullName = firstUser.getUserFirstName() + " "
                + firstUser.getUserLastName();
        String secondUserFullName = secondUser.getUserFirstName() + " "
                + secondUser.getUserLastName();

        firstPlayerNameTextView.setText(firstUserFullName);
        secondPlayerNameTextView.setText(secondUserFullName);

        String youtTurnSentence = "";
        if (game.getUserTurnId() == firstUser.getId()) {

            // check if I am first user or not
            if (firstUser.getId() == userId) {
                // my turn ,, first user name
                youtTurnSentence = getResources().getString(R.string.your_turn)
                        + " " + firstUser.getUserFirstName();
                youTurnTextView.setText(youtTurnSentence);

            } else {
                // not my turn ,, first user name
                youtTurnSentence = getResources().getString(R.string.his_turn)
                        + " " + firstUser.getUserFirstName();
                youTurnTextView.setText(youtTurnSentence);
            }

        } else {
            // check if I am first user or not
            if (secondUser.getId() == userId) {
                // my turn ,, second user name
                youtTurnSentence = getResources().getString(R.string.your_turn)
                        + " " + secondUser.getUserFirstName();
                youTurnTextView.setText(youtTurnSentence);

            } else {
                // not my turn ,, first user name
                youtTurnSentence = getResources().getString(R.string.his_turn)
                        + " " + secondUser.getUserFirstName();
                youTurnTextView.setText(youtTurnSentence);
            }

        }
        // users Scores
        totalFirstUserScore = 0;
        totalSecondUserScor = 0;

        for (Round round : rounds) {
            totalFirstUserScore += round.getFirstUserScore();
            totalSecondUserScor += round.getSecondUserScore();
        }
        firstPlayerScoreTextView.setText("" + totalFirstUserScore);
        secondPlayerScoreTextView.setText("" + totalSecondUserScor);

        totalFirstUserRoundsScore.setText("" + totalFirstUserScore);
        totalSecondUserRoundsScore.setText("" + totalSecondUserScor);

        // rounds scores
        // first user
        if (rounds.get(0).getFirstUserScore() != 0) {
            firstRoundFirstPlayerScoreTextView.setText(""
                    + rounds.get(0).getFirstUserScore());
        } else {
            firstRoundFirstPlayerScoreTextView.setText("0");
        }

        if (rounds.get(1).getFirstUserScore() != 0) {
            secondRoundFirstPlayerScoreTextView.setText(""
                    + rounds.get(1).getFirstUserScore());
        } else {
            secondRoundFirstPlayerScoreTextView.setText("0");
        }
        if (rounds.get(2).getFirstUserScore() != 0) {
            thirdRoundFirstPlayerScoreTextView.setText(""
                    + rounds.get(2).getFirstUserScore());
        } else {
            thirdRoundFirstPlayerScoreTextView.setText("0");
        }

        // SecondUser
        if (rounds.get(0).getSecondUserScore() != 0) {
            firstRoundSecondPlayerScoreTextView.setText(""
                    + rounds.get(0).getSecondUserScore());
        } else {
            firstRoundSecondPlayerScoreTextView.setText("0");
        }

        if (rounds.get(1).getSecondUserScore() != 0) {
            secondRoundSecondPlayerScoreTextView.setText(""
                    + rounds.get(1).getSecondUserScore());
        } else {
            secondRoundSecondPlayerScoreTextView.setText("0");
        }
        if (rounds.get(2).getSecondUserScore() != 0) {
            // second user has a score in the third round
            // means game finished
            thirdRoundSecondPlayerScoreTextView.setText(""
                    + rounds.get(2).getSecondUserScore());
            putGameFinishedString();

        } else {
            thirdRoundSecondPlayerScoreTextView.setText("0");
        }

    }

    /**
     * when the game finished tell which one win
     */
    private void putGameFinishedString() {
        String theWinnerString = "";
        if (totalFirstUserScore > totalSecondUserScor) {
            theWinnerString = getResources().getString(R.string.the_winner_is)
                    + " " + firstUser.getUserFirstName();
            youTurnTextView.setText(theWinnerString);
        } else if (totalSecondUserScor > totalFirstUserScore) {
            theWinnerString = getResources().getString(R.string.the_winner_is)
                    + " " + secondUser.getUserFirstName();
            youTurnTextView.setText(theWinnerString);
        } else {
            theWinnerString = getResources().getString(R.string.same_score);
            youTurnTextView.setText(theWinnerString);
        }

    }

    private void init() {

        /*
         * initialize functionality variables
         */
        context = getApplicationContext();
        sqlLiteDB = ShabbikDAO.getInstance(context);
        // sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME,
        // MODE_PRIVATE);

        userId = sqlLiteDB.retrieveMainUserId();

//        facebookProvider = new FacebookProvider(this);

        imageLoaderManager = new ImageLoaderManager();
        /*
         * initialize UI Elements
         */
        firstPlayerImageView = (ImageView) findViewById(R.id.leftPlayerImageView);
        secondPlayerImageView = (ImageView) findViewById(R.id.rightPlayerImageView);
        firstPlayerNameTextView = (TextView) findViewById(R.id.leftPlayerNameTextView);
        firstPlayerScoreTextView = (TextView) findViewById(R.id.leftPlayerScoreTextView);
        secondPlayerNameTextView = (TextView) findViewById(R.id.rightPlayerNameTextView);
        secondPlayerScoreTextView = (TextView) findViewById(R.id.rightPlayerScoreTextView);

        firstRoundFirstPlayerScoreTextView = (TextView) findViewById(R.id.firstRoundLeftPlayerScoreTextView);
        secondRoundFirstPlayerScoreTextView = (TextView) findViewById(R.id.secondRoundLeftPlayerScoreTextView);
        thirdRoundFirstPlayerScoreTextView = (TextView) findViewById(R.id.thirdRoundLeftPlayerScoreTextView);
        firstRoundSecondPlayerScoreTextView = (TextView) findViewById(R.id.firstRoundRightPlayerScoreTextView);
        secondRoundSecondPlayerScoreTextView = (TextView) findViewById(R.id.secondRoundRightPlayerScoreTextView);
        thirdRoundSecondPlayerScoreTextView = (TextView) findViewById(R.id.thirdRoundRightPlayerScoreTextView);

        totalFirstUserRoundsScore = (TextView) findViewById(R.id.totalRoundsLeftPlayerScoreTextView);
        totalSecondUserRoundsScore = (TextView) findViewById(R.id.totalRoundsRightPlayerScoreTextView);

        youTurnTextView = (TextView) findViewById(R.id.yourTurnTextView);

        startPlayingImageView = (ImageView) findViewById(R.id.startPlayingImageView);
        startPlayingImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.startPlayingImageView:

                goToNextActivity();

                break;
        }
    }

    public void getGameInfo(int gameId) {

        game = sqlLiteDB.retrieveGameOfId(gameId);

        firstUser = sqlLiteDB.retrieveUserInfo(game.getFirstUserId());
        secondUser = sqlLiteDB.retrieveUserInfo(game.getSecondUserId());
        rounds = sqlLiteDB.retrieveRoundsOfGame(game.getId());

        // check if game is finished or not or if it is not my turn
        if (game.getCurrentRoundId() == FINISHED_GAME_CURRENT_ROUND_ID
                || game.getUserTurnId() != userId) {
            // change play game button , to done button
            startPlayingImageView
                    .setImageResource(R.drawable.done_button_selector);

        }

    }

    private void goToNextActivity() {
        Intent toActivty;
        // check if game is finished or if it is not my turn
        if (game.getCurrentRoundId() == FINISHED_GAME_CURRENT_ROUND_ID
                || game.getUserTurnId() != userId) {
            // game is finished ,, go to main menu
            toActivty = new Intent(context, MainMenuActivity.class);
            toActivty.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else {
            // start a game

            if (game.getGameMode() == WORD_GAME_VALUE) {
                toActivty = new Intent(context, WordGameActivity.class);
            } else {
                toActivty = new Intent(context, SentenceGameActivity.class);
            }

            toActivty.putExtra(GAME_ID, game.getId());
            toActivty.putExtra(PLAY_WITH, PLAY_WITH_FACEBOOK_USER);
            toActivty.putExtra(GAME_CURRENT_ROUND_ID, game.getCurrentRoundId());
            toActivty.putExtra(GAME_USER_TURN_ID, game.getUserTurnId());

        }
        startActivity(toActivty);
        RoundsViewActivity.this.finish();

    }


}
