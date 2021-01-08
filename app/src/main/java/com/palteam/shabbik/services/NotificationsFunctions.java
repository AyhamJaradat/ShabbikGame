package com.palteam.shabbik.services;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.palteam.shabbik.R;
import com.palteam.shabbik.activities.SplashActivity;
import com.palteam.shabbik.beans.Game;
import com.palteam.shabbik.beans.Puzzle;
import com.palteam.shabbik.beans.Round;
import com.palteam.shabbik.beans.User;
import com.palteam.shabbik.sqllight.IDatabase;
import com.palteam.shabbik.sqllight.ShabbikDAO;
import com.palteam.shabbik.utils.IConstants;
import com.palteam.shabbik.utils.Methods;
import com.palteam.shabbik.webservice.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

//import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;








public class NotificationsFunctions implements IConstants,
        IDatabase {

    private final String TAG = NotificationsFunctions.class.getSimpleName();
    private Context context;
    private ShabbikDAO sqlLiteDB;
    private int notificationId = 0;
    private SharedPreferences sharedPreferences;

    /**
     * Constructor to initialize object with context
     *
     * @param context service context
     */
    public NotificationsFunctions(Context context) {
        this.context = context;
        this.sqlLiteDB = ShabbikDAO.getInstance(context);
        this.sharedPreferences = context.getSharedPreferences(
                SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    /**
     * receive json data within notification and process them
     * <p/>
     * retrieve notificationId , whoAmI
     *
     * @param notificationJsonData jason data
     */
    public void handleNotificationData(String notificationJsonData) {

        JSONObject jsonObject = null;
        int whoAmI = 0;
        int roundNumber = 0;
        int currentRoundId = 0;
        notificationId = -1;

        try {
            jsonObject = new JSONObject(notificationJsonData);
            notificationId = jsonObject.getInt(MY_NOTIFICATION_ID);
            // notification Id should be greater than zero to be accepted as notification
            if (notificationId <= 0) {
                Log.i("pull Notification", "there is no update for me");
                return;
            }
            // check which user Am I
            whoAmI = jsonObject.getInt(WHO_AM_I);

            /**
             * if am First User, receive Round object with ( roundId ,secondUserScore,time)
             * , update score and time for previous round
             * , update CurrentRoundId and userTurn
             */
            if (whoAmI == I_AM_FIRST_USER) {
                String roundObject = jsonObject.getString(ROUND_OBJECT);
                Round round = new Gson().fromJson(roundObject, Round.class);
                // check if I received this notification before, if yes send acknowledgment and ignore the notification
                if (checkIfOldNotificationFirstUser(round.getId())) {
                    sendNotificationAcknowledgment();
                    return;
                } else {
                    notification_FirstUser(round);
                }

            } else if (whoAmI == I_AM_SECOND_USER) {
                // am second User
                currentRoundId = jsonObject.getInt(ROUND_ID);
                // check if I received this notification before, if yes send acknowledgment and ignore the notification
                if (checkIfOldNotificationSecondUser(currentRoundId)) {
                    sendNotificationAcknowledgment();
                    return;
                }
                // check Round Number
                roundNumber = jsonObject.getInt(ROUND_NUMBER);
                /**
                 * if this is first round ,  i got game object, firstUser object and three rounds
                 */
                if (roundNumber == 1) {
                    String gameObject = jsonObject.getString(GAME_OBJECT);
                    Game game = new Gson().fromJson(gameObject, Game.class);
                    String userObject = jsonObject.getString(USER_OBJECT);
                    User user = new Gson().fromJson(userObject, User.class);
                    int size = jsonObject.getInt(JSON_ARRAY_SIZE);
                    ArrayList<Round> rounds;
                    if (size > 0) {
                        rounds = new ArrayList<Round>();
                        for (int i = 1; i < size + 1; i++) {
                            String strKey = JSON_OBJECT_KEY + i;

                            String json = jsonObject.getString(strKey);
                            Round gsonRound = new Gson().fromJson(json,
                                    Round.class);
                            rounds.add(gsonRound);
                        }
                    } else {
                        // some thing missing in the request response
                        // ignore it
                        return;
                    }
                    notification_SecondUser_FirstTime(game, user, rounds,
                            currentRoundId);

                } else {
                    // not first round, so I got one round object
                    String roundObject = jsonObject.getString(ROUND_OBJECT);
                    Round round = new Gson().fromJson(roundObject, Round.class);
                    notification_SecondUser_NotFirstTime(round);

                }
            }

        } catch (JSONException e) {
            Log.e("at hndlNotificationData", "JSONException ");
        }

    }

    /**
     * if both firstUserScore and secondUserScore are not null
     * then it is an old notification ,, discard it
     *
     * @param currentRoundId the id of the round to check if its notification received before
     * @return true or false based on values in local database
     */
    private boolean checkIfOldNotificationFirstUser(int currentRoundId) {

        return sqlLiteDB.isBothScoreExists(currentRoundId);

    }

    /**
     * if the first user score is not as initialized then this notifications retrieved before
     *
     * @param currentRoundId the id of the round to check if its notification received before
     * @return true or false based on values in local database
     */
    private boolean checkIfOldNotificationSecondUser(int currentRoundId) {

        return sqlLiteDB.isFirstUserScoreExists(currentRoundId);

    }


    /**
     * a notification received to first user
     * update SecondUserScore and time for round on localDB , update
     * CurrentRoundId and userTurn for this game , and start it
     *
     * @param round object containing data about previous finished round
     */
    private void notification_FirstUser(Round round) {

        FirebaseCrashlytics.getInstance().log("INFO::"+TAG+"::"+"Notification to first user " + round.getRoundConfigration());

        // update round info
        round.setIsDirty(NO_VALUE);
        sqlLiteDB.updateRoundInfo(round);

        //check if first user ID of this game is the same as userId
        int currentUserTurnId = sqlLiteDB.retrieveMainUserId();
        Game game = sqlLiteDB.retrieveGameOfId(round.getGameId());
        if (game == null || game.getFirstUserId() != currentUserTurnId) {
            // the user of this device is not the first user of the game
            // he might uninstall the app and reinstall it again .
            return;
        }


        sqlLiteDB.updateGameCurrentUserTurnId(round.getGameId(),
                currentUserTurnId);

        // update Current Round ID
        int currentRoundNumber = round.getRoundNumber();
        int nextRoundNumber;
        if (currentRoundNumber < 3) {
            nextRoundNumber = currentRoundNumber + 1;
            int nextCurrentRoundId = sqlLiteDB.getRoundId(round.getGameId(),
                    nextRoundNumber);
            if (nextCurrentRoundId > 0) {
                sqlLiteDB.updateGameCurrentRoundId(round.getGameId(),
                        nextCurrentRoundId);
            }

        } else {
            // last round already played
            sqlLiteDB.updateGameCurrentRoundId(round.getGameId(),
                    FINISHED_GAME_CURRENT_ROUND_ID);
            nextRoundNumber = FINISHED_GAME_CURRENT_ROUND_ID;

        }

        // get second User name
        String secondUserName = sqlLiteDB.getFullUserName(round.getGameId(),
                I_AM_SECOND_USER);

        // you can start the game now .
        // but we should wait for user to click on notification
        sendNotification(round.getGameId(), secondUserName, nextRoundNumber);

    }

    /**
     * second user receive a notification about round , in a previous game,
     * update round info to localDataBase and start Game
     *
     * @param round object containing data about round
     */
    private void notification_SecondUser_NotFirstTime(Round round) {


        FirebaseCrashlytics.getInstance().log("INFO::"+TAG+"::"+" Notification second user not first "
                + round.getRoundConfigration());

        // update round Info
        round.setIsDirty(NO_VALUE);

        sqlLiteDB.updateRoundInfo(round);
        // update this game , current Round Id
        sqlLiteDB.updateGameCurrentRoundId(round.getGameId(), round.getId());
        // update this game and round , change turnId
        int userId = sqlLiteDB.retrieveMainUserId();
        //check if second user ID of this game is the same as userId
        Game game = sqlLiteDB.retrieveGameOfId(round.getGameId());
        if (game == null || game.getSecondUserId() != userId) {
            // the user of this device is not the second user of the game
            // he might uninstalled the app and reinstalled it again .
            return;
        }
        sqlLiteDB.updateGameCurrentUserTurnId(round.getGameId(), userId);

        String firstUserFullName = sqlLiteDB.getFullUserName(round.getGameId(),
                I_AM_FIRST_USER);


        if (game.getGameMode() == WORD_GAME_VALUE) {
            String firstPuzzle = round.getRoundConfigration();
            String secondPuzzle = Methods.getRevertedPuzzle(firstPuzzle);
            String AllWords = Methods.generatePuzzleFromDictionary(context,
                    firstPuzzle);
            String secondAllWords = Methods.generatePuzzleFromDictionary(
                    context, secondPuzzle);

            Puzzle puzzle = new Puzzle(round.getId(), firstPuzzle, AllWords,
                    secondPuzzle, secondAllWords);

            boolean result = sqlLiteDB.insertNewPuzzle(puzzle);

            if (result) {

                // you can start the game now .
                // but we should wait for user to click on notification
                sendNotification(round.getGameId(), firstUserFullName,
                        round.getRoundNumber());

            } else {


                FirebaseCrashlytics.getInstance().log("INFO::"+TAG+"::"+"Notification to second user not first time (Cant save to database) "
                        + puzzle.toString());

                Log.i(TAG,
                        "xxx Akalna 5ara not first user Puzzle = "
                                + puzzle.toString());
            }
        } else {

            // you can start the game now .
            // but we should wait for user to click on notification
            sendNotification(round.getGameId(), firstUserFullName,
                    round.getRoundNumber());

        }

    }

    /**
     * Second user receive notification about new game add game , firstUser ,
     * rounds to local data base and start playing the game with round equal to
     * currentRoundId set TurnId to me
     *
     * @param game           game object
     * @param firstUser      first user object
     * @param rounds         three rounds objects
     * @param currentRoundId current round Id to be played
     */
    private void notification_SecondUser_FirstTime(Game game, User firstUser,
                                                   ArrayList<Round> rounds, int currentRoundId) {

        // add the New User to sqlLite db
        if (!sqlLiteDB.isUserExist(firstUser)) {
            sqlLiteDB.insertNewUser(firstUser);
        }
        // add the new game to sqlLite db
        if (!sqlLiteDB.isGameExist(game)) {
            game.setIsDirty(NO_VALUE);
            game.setIsFinished(NO_VALUE);
            int userId = sqlLiteDB.retrieveMainUserId();
            //check if second user ID of this game is the same as userId
            if (game.getSecondUserId() != userId) {
                // the user of this device is not the second user of the game
                // he might uninstalled the app and reinstalled it again .
                return;
            }
            game.setCurrentRoundId(currentRoundId);
            game.setUserTurnId(userId);
            sqlLiteDB.insertNewGame(game);

            // add the three rounds to sqlLite db
            for (Round round : rounds) {
                round.setFinished(false);
                round.setIsDirty(NO_VALUE);
            }
            sqlLiteDB.insertNewRounds(rounds);


            FirebaseCrashlytics.getInstance().log("INFO::"+TAG+"::"+ "Notification to second user first time "
                    + rounds.get(0).getRoundConfigration());
            // to make sure rounds[0] is the first round
            Log.i(TAG, "the next values should always be equal");
            Log.e(TAG, "rounds[0] ID: " + rounds.get(0).getId());
            Log.e(TAG, "current round ID:  " + currentRoundId);

            String firstUserFullName = firstUser.getUserFirstName()
                    + " " + firstUser.getUserLastName();

            if (game.getGameMode() == WORD_GAME_VALUE) {

                String firstPuzzle = rounds.get(0).getRoundConfigration();

                String secondPuzzle = Methods.getRevertedPuzzle(firstPuzzle);

                String firstAllWords = Methods.generatePuzzleFromDictionary(
                        context, firstPuzzle);
                String secondAllWords = Methods.generatePuzzleFromDictionary(
                        context, secondPuzzle);

                Puzzle puzzle = new Puzzle(rounds.get(0).getId(), firstPuzzle,
                        firstAllWords, secondPuzzle, secondAllWords);

                boolean result = sqlLiteDB.insertNewPuzzle(puzzle);


                if (result) {
                    // you can start the game now .
                    // but we should wait for user to click on notification
                    sendNotification(game.getId(), firstUserFullName, 1);

                } else {
                    FirebaseCrashlytics.getInstance().log("INFO::"+TAG+"::"+ "Notification to second user first time (Cant save to database) "
                            + puzzle.toString());
                    Log.i(TAG, "xxx Akalna 5ara Puzzle = " + puzzle.toString());
                }
            } else {
                // sentence game ,, no need for puzzle generation
                // you can start the game now .
                // but we should wait for user to click on notification
                sendNotification(game.getId(), firstUserFullName, 1);
            }
        }

    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.

    /**
     * @param gameId      game id as stored database
     * @param senderName  name of notification sender
     * @param roundNumber use round number as follow if ==1 ;; first time invited if ==
     *                    2 || 3 ;; second player finished , your turn is no if == -1 ;;
     *                    game finished ,, see who won
     */
    private void sendNotification(int gameId, String senderName, int roundNumber) {

        Log.i("", "xxx GCM notification");


        // before you fire a notification ,, tell the server it is received
        sendNotificationAcknowledgment();
        String msg;
        if (roundNumber == -1) {
            // Game is finished
            msg = String.format(context.getString(R.string.notification_message_done),
                    senderName);

        } else {
            msg = String.format(context.getString(R.string.notification_message),
                    senderName);
        }

        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Intent dataIntent = new Intent(context, SplashActivity.class);
        dataIntent.putExtra(GAME_ID, gameId);
        dataIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                dataIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(
                        context.getResources().getString(R.string.notification_title))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg).setAutoCancel(true)
                .setContentIntent(contentIntent).setSound(alarmSound);

        // Increment notification id in shared preferences.
        int notifiId = sharedPreferences.getInt(
                IConstants.Preferences.NOTIFICAION_ID, -1);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(IConstants.Preferences.NOTIFICAION_ID, notifiId++);
        editor.commit();

        mNotificationManager.notify(notifiId, mBuilder.build());
    }

    /**
     * method to send the server acknowledgment request about received notification
     */
    private void sendNotificationAcknowledgment() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                // check if there is Internet connection
                boolean isConnected = WebService
                        .isConnectedToWeb(context);
                if (isConnected) {
                    if (notificationId <= 0)
                        return;
                    String requestParameters = "/" + notificationId;
                    String url = context.getResources()
                            .getString(R.string.web_service_http_address)
                            + context.getResources().getString(
                            R.string.server_notification_ack_web_service_address)
                            + requestParameters;

                    String jsonString = WebService.requestWebService(url, null);

                    // if return null ,, try again for more one time
                    if (jsonString == null) {
                        WebService.requestWebService(url, null);
                    }

                }

            }
        }).start();
    }


}
