package com.palteam.shabbik.receivers;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.legacy.content.WakefulBroadcastReceiver;

import com.palteam.shabbik.services.GcmIntentService;
import com.palteam.shabbik.sqllight.IDatabase;
import com.palteam.shabbik.utils.IConstants;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver implements
        IConstants, IDatabase {

    @Override
    public void onReceive(Context context, Intent intent) {



        // Explicitly specify that GcmIntentService will handle the intent.
        String notificationJsonData = intent.getExtras().getString(
                NOTIFICATION_DATA_KEY);

        if (notificationJsonData != null&&!notificationJsonData.equals("")) {

            ComponentName comp = new ComponentName(context.getPackageName(),
                    GcmIntentService.class.getName());

            // Start the service, keeping the device awake while it is
            // launching.
            startWakefulService(context, (intent.setComponent(comp)));

            setResultCode(Activity.RESULT_OK);

        }

    }

    /**
     * this function will direct to appropriate game
     *
     * @param gameId
     */
    // public void startGameWithGameId(int gameId) {
    //
    // Game game = sqlLiteDB.retrieveGameOfId(gameId);
    //
    // // check if game is finished or not
    // if (game.getCurrentRoundId() == FINISHED_GAME_CURRENT_ROUND_ID) {
    //
    // // game has finished ,,, show report Page
    // } else {
    // // start a game
    //
    // Intent toActivty;
    // if (game.getGameMode() == WORD_GAME_VALUE) {
    // toActivty = new Intent(context, WordGameActivity.class);
    // } else {
    // toActivty = new Intent(context, SentenceGameActivity.class);
    // }
    //
    // toActivty.putExtra(GAME_ID, game.getId());
    // toActivty.putExtra(PLAY_WITH, PLAY_WITH_FACEBOOK_USER);
    // toActivty.putExtra(GAME_CURRENT_ROUND_ID, game.getCurrentRoundId());
    // toActivty.putExtra(GAME_USER_TURN_ID, game.getUserTurnId());
    // toActivty.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    // context.startActivity(toActivty);
    //
    // }
    //
    // }
}
