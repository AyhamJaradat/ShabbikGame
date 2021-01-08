package com.palteam.shabbik.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.palteam.shabbik.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.palteam.shabbik.beans.Game;
import com.palteam.shabbik.beans.Round;
import com.palteam.shabbik.beans.User;
import com.palteam.shabbik.sqllight.IDatabase;
import com.palteam.shabbik.sqllight.ShabbikDAO;
import com.palteam.shabbik.utils.AppUtils;
import com.palteam.shabbik.utils.IConstants;
//import com.palteam.shabbik.utils.Log;
import com.palteam.shabbik.utils.RegistrationManager;
import com.palteam.shabbik.webservice.WebService;

public class SecondMenuActivity extends Activity implements IConstants,
        IDatabase, OnClickListener  {
    private final String TAG = SecondMenuActivity.class.getSimpleName();

    private ImageView trainImageView, playWithFBUserImageView,
            playWithUserImageView;
    private ImageView gameModeTitleImageView;
    private int gameMode = 0;

    private Context context;
    // private SharedPreferences sharedPreferences;

    private ShabbikDAO sqlLiteDB;

//    private AdView banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppUtils.logHeap(TAG);
        setContentView(R.layout.activity_second_menu);
//        setContentView(R.layout.second_menu_activity);

        init();

//        loadAd(R.id.banner_ad);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

//        if (banner != null) {
//            banner.destroy();
//            banner = null;
//        }

        android.util.Log.i("Im in second destroy", "");
        RelativeLayout mainlayouttt =   (RelativeLayout) this
                .findViewById(R.id.second_activity);
        unbindDrawables(mainlayouttt);
    }
    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
                android.util.Log.i("view3", "" + i);
            }
            ((ViewGroup) view).removeAllViews();
            view.setBackgroundResource(0);
        }
    }
    private void init() {


        context = getApplicationContext();

        // sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME,
        // MODE_PRIVATE);

        sqlLiteDB = ShabbikDAO.getInstance(context);

        gameMode = getIntent().getExtras().getInt(GAME_MODE);
        gameModeTitleImageView = (ImageView) findViewById(R.id.imagegametype);
        if (gameMode == WORD_GAME_VALUE) {
            gameModeTitleImageView.setImageResource(R.drawable.shabik_kalemat);
        } else {
            gameModeTitleImageView.setImageResource(R.drawable.shabik_jomal);
        }

        trainImageView = (ImageView) findViewById(R.id.trainImageView);
        trainImageView.setOnClickListener(this);

        playWithFBUserImageView = (ImageView) findViewById(R.id.playWithFBUserImageView);
        playWithFBUserImageView.setOnClickListener(this);

        playWithUserImageView = (ImageView) findViewById(R.id.playWithRandomUserImageView);
        playWithUserImageView.setOnClickListener(this);

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
    public void onClick(View v) {
        Intent toActivty;

        switch (v.getId()) {

            case R.id.trainImageView:

                if (gameMode == WORD_GAME_VALUE) {
                    toActivty = new Intent(getApplicationContext(),
                            WordGameActivity.class);
                } else {
                    toActivty = new Intent(getApplicationContext(),
                            SentenceGameActivity.class);
                }
                toActivty.putExtra(PLAY_WITH, PRACTICE_PLAYING);
                startActivity(toActivty);
                SecondMenuActivity.this.finish();

                break;

            case R.id.playWithFBUserImageView:

                validateConditionsAndViewUsers(PLAY_WITH_FACEBOOK_USER);

                break;

            case R.id.playWithRandomUserImageView:

                validateConditionsAndViewUsers(PLAY_WITH_RANDOM_USER);
                break;

        }

    }

    /**
     * function to check if there is an Internet connection and to check if user
     * is register
     *
     * @param playWith
     */

    private void validateConditionsAndViewUsers(int playWith) {

        // check InternetConnection
        // check if there is Internet connection
        boolean isConnected = WebService.isConnectedToWeb(context);

        if (!isConnected) {
            // no Internet connection
            Toast.makeText(context,
                    getResources().getString(R.string.internet_failer),
                    Toast.LENGTH_LONG).show();
            return;

        } else {

            // check if already register
            boolean isRegistered = sqlLiteDB.isMainUserRegistor();

            if (playWith == PLAY_WITH_FACEBOOK_USER) {

                if (isFacebookRegistered(isRegistered)) {

                    showFaceBookFriends();

                } else {

                    RegistrationManager.showFacebookRegistrationDialog(this);
                }

            } else if (playWith == PLAY_WITH_RANDOM_USER) {

                if (isRegistered) {

                    chooseRandomUserFromDataBase();

                } else {


                    RegistrationManager.showFacebookManualRegistrationDialog(this);

                }

            }

        }

    }

    private boolean isFacebookRegistered(boolean isRegistered) {

        if (isRegistered) {

            User user = sqlLiteDB.retrieveUserInfo(sqlLiteDB
                    .retrieveMainUserId());

            if (user.getUserFBId() != null
                    && !user.getUserFBId().equals(NULL_VALUE)) {

                return true;
            }
        }

        return false;
    }

    /**
     *
     */
    private void goToSingUpActivity() {

        Intent toActivty = new Intent(getApplicationContext(),
                SignUpActivity.class);

        // SecondMenuActivity.this.finish();
        startActivity(toActivty);

    }

    /**
     * function to retrieve a random user from dataBase to play with
     */
    private void chooseRandomUserFromDataBase() {

        int userId = sqlLiteDB.retrieveMainUserId();

        if (userId > 0) {
            // bring random User data
            User user = new User();
            user.setId(userId);
            new GetUserAsyncTask(user).execute();
        }

    }

    /**
     * function to show user faceBook Friends
     */
    private void showFaceBookFriends() {
        int userId = sqlLiteDB.retrieveMainUserId();
        if (userId > 0) {

//            Intent toFriendsActivity = new Intent(getApplicationContext(),
//                    FriendsActivity.class);
//            toFriendsActivity.putExtra(GAME_MODE, gameMode);
//            startActivity(toFriendsActivity);
            // bring faceBook Users
            // bring selected user information and them to local database
            // add game row and three round rows to globalDB and LOcal DB
            // and start playing - use gameMode variable to know where to go

            // @Ahmad : please startActivityForResult which retrieve facebook
            // friends , then call a function(ArrayList<User>) , make sure each
            // user has at least facebookId field ,declare
            // otherFunction(ArrayList<User>)
            // which i will call when i get info from server, when player select
            // a user, let the Activity for result return userId of selected
            // user as result
        }

    }

    /**
     * this function will be called when randomUser is retrieved
     *
     * @param user
     */
    public void handleRetrievedUser(User user) {
        // bring selected user information and add them to local
        // database

        // Add user Record To localDatabase if not existed
        boolean isUserExist = sqlLiteDB.isUserExist(user);
        if (!isUserExist) {
            if (user.getUserFBId() == null || user.getUserFBId() == "")
                user.setUserFBId(NULL_VALUE);

            user.setUserEmail(NULL_VALUE);
            boolean isSaved = sqlLiteDB.insertNewUser(user);
        } else {
            // update user facebook ID
            if (user.getUserFBId() != null && user.getUserFBId() != "")
                sqlLiteDB.updateUserInfo(user);
        }

        // add game row and three round rows to globalDB
        Game game = new Game();
        int firstUserId = sqlLiteDB.retrieveMainUserId();
        game.setFirstUserId(firstUserId);
        game.setSecondUserId(user.getId());

        // get current Time
        // Time timeNow = new Time(Time.getCurrentTimezone());
        // timeow.setToNow();
        // game.setTimeObj(timeNow);
        game.setGameMode(gameMode);

        new AddNewGameInfoAsyncTask(game).execute();

    }

    /**
     * this function will be called when game info is added to global database
     *
     * @param game
     * @param rounds
     */
    public void addGameInfoToLocalDBAndStartGame(Game game,
                                                 ArrayList<Round> rounds) {

        game.setUserTurnId(game.getFirstUserId());
        game.setCurrentRoundId(rounds.get(0).getId());
        game.setIsDirty(NO_VALUE);
        game.setIsFinished(NO_VALUE);

        for (Round round : rounds) {
            round.setIsDirty(NO_VALUE);
        }
        // and LOcal DB
        // and start playing - use gameMode variable to know where to go
        boolean isGameExist = sqlLiteDB.isGameExist(game);
        if (!isGameExist) {
            sqlLiteDB.insertNewGame(game);
            sqlLiteDB.insertNewRounds(rounds);
        }

        // gameMode here should be equal to retrieved gameMode
        // (game.getGameMode)
        Intent toActivty;
        toActivty = new Intent(getApplicationContext(),
                RoundsViewActivity.class);

        toActivty.putExtra(GAME_ID, game.getId());

        SecondMenuActivity.this.finish();
        startActivity(toActivty);

    }

    // / private inner class
    private class AddNewGameInfoAsyncTask extends AsyncTask<Void, Void, String> {

        private ProgressDialog dialog;
        private Game game;

        public AddNewGameInfoAsyncTask(Game game) {
            this.dialog = new ProgressDialog(SecondMenuActivity.this);
            this.game = game;
        }

        @Override
        protected void onPreExecute() {

            dialog.setMessage(getResources()
                    .getString(R.string.please_wait_msg));
            dialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {

            String requestParameters = "/" + game.getFirstUserId() + "/"
                    + game.getSecondUserId() + "/" + gameMode;

            String url = getResources().getString(
                    R.string.web_service_http_address)
                    + getResources().getString(
                    R.string.add_new_game_info_web_service_address)
                    + requestParameters;

            String jsonString = WebService.requestWebService(url, null);

            // if return null ,, try again for more one time
            if (jsonString == null) {
                jsonString = WebService.requestWebService(url, null);
            }
            if (isCancelled()) {

                return null;

            }

            return jsonString;

        }

        protected void onPostExecute(String results) {
            if (results != null) {

                Log.e("JSON result :: ", results);
                boolean status = false;
                JSONObject jsonObject = null;
                ArrayList<Round> rounds = null;
                int size = 0;
                try {

                    // dismiss the progress dialog
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }

                    jsonObject = new JSONObject(results);
                    status = jsonObject.getBoolean(JSON_STATUS);
                    if (status) {
                        // i received one game object and three round objects
                        String gameObject = jsonObject.getString(GAME_OBJECT);
                        Game gsonGame = new Gson().fromJson(gameObject,
                                Game.class);

                        size = jsonObject.getInt(JSON_ARRAY_SIZE);
                        if (size > 0) {
                            rounds = new ArrayList<Round>();
                            for (int i = 1; i < size + 1; i++) {
                                String strKey = JSON_OBJECT_KEY + i;

                                String json = jsonObject.getString(strKey);
                                Round gsonRound = new Gson().fromJson(json,
                                        Round.class);

                                rounds.add(gsonRound);

                            }
                        }
                        addGameInfoToLocalDBAndStartGame(gsonGame, rounds);
                    } else {
                        Toast.makeText(context,
                                getString(R.string.failer_message),
                                Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    Log.e("at GetUserAsyncTask",
                            "JSONException at onPostExecute ");
                    status = false;
                }

            } else {
                // when response is null
                // dismiss the progress dialog
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.i(TAG, "could not Add new game to dataBase");
                Toast.makeText(context, getString(R.string.failer_message),
                        Toast.LENGTH_SHORT).show();
            }

        }

    }

    // / private inner class
    private class GetUserAsyncTask extends AsyncTask<Void, Void, String> {

        private ProgressDialog dialog;
        private User user;

        public GetUserAsyncTask(User user) {
            this.dialog = new ProgressDialog(SecondMenuActivity.this);
            this.user = user;
        }

        @Override
        protected void onPreExecute() {

            dialog.setMessage(getResources()
                    .getString(R.string.please_wait_msg));
            dialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {

            String requestParameters = "/" + user.getId();

            String url = getResources().getString(
                    R.string.web_service_http_address)
                    + getResources().getString(
                    R.string.retrieve_random_user_web_service_address)
                    + requestParameters;

            String jsonString = WebService.requestWebService(url, null);

            // if return null ,, try again for more one time
            if (jsonString == null) {
                jsonString = WebService.requestWebService(url, null);
            }
            if (isCancelled()) {

                return null;

            }

            return jsonString;

        }

        protected void onPostExecute(String results) {
            if (results != null) {

                Log.e("JSON result :: ", results);
                boolean status = false;
                JSONObject jsonObject = null;
                int id = 0;

                try {

                    jsonObject = new JSONObject(results);
                    status = jsonObject.getBoolean(JSON_STATUS);

                    // dismiss the progress dialog
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (status) {

                        String UserObject = jsonObject.getString(USER_OBJECT);
                        User gsonUser = new Gson().fromJson(UserObject,
                                User.class);

                        handleRetrievedUser(gsonUser);

                    } else {
                        Toast.makeText(context,
                                getString(R.string.failer_message),
                                Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    Log.e("at GetUserAsyncTask",
                            "JSONException at onPostExecute ");
                    status = false;
                }

            } else {
                // when response is null
                // dismiss the progress dialog
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                Log.i(TAG, "Random User could Not be retrieved from DataBase");
                Toast.makeText(context, getString(R.string.failer_message),
                        Toast.LENGTH_SHORT).show();
            }

        }

    }

    /**
     * Show dialog to get user verification to register new account.
     */
    private void showRegisterDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.register_dialog_title)
                .setMessage(R.string.register_dialog_message)
                .setPositiveButton(R.string.ok_button,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                goToSingUpActivity();

                            }
                        }).setNegativeButton(R.string.cancel_button, null);

        Dialog dialog = builder.create();
        dialog.show();

    }

    /**
     * Show dialog to get user verification to login to Facebook.
     */
    private void showFacebookLoginDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.facebook_register_dialog_title)
                .setMessage(R.string.facebook_register_dialog_message)
                .setPositiveButton(R.string.ok_button,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                goToSingUpActivity();

                            }
                        }).setNegativeButton(R.string.cancel_button, null);

        Dialog dialog = builder.create();
        dialog.show();
    }


}
