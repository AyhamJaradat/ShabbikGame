package com.palteam.shabbik.services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.palteam.shabbik.R;
import com.palteam.shabbik.beans.Game;
import com.palteam.shabbik.beans.Round;
import com.palteam.shabbik.sqllight.IDatabase;
import com.palteam.shabbik.sqllight.ShabbikDAO;
import com.palteam.shabbik.utils.IConstants;
import com.palteam.shabbik.webservice.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OnConnectService extends Service implements IConstants, IDatabase {

    private ShabbikDAO sqlLiteDB;

    private SharedPreferences sharedPreferences;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
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
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (serviceFullName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startPullNotificationService();

        sqlLiteDB = ShabbikDAO.getInstance(getApplicationContext());
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME,
                MODE_PRIVATE);
        ArrayList<Round> dirtyRounds = sqlLiteDB.getAllDirtyRounds();

        for (final Round round : dirtyRounds) {

            new Thread(new Runnable() {

                @Override
                public void run() {
                    // check if there is Internet connection
                    boolean isConnected = WebService
                            .isConnectedToWeb(getApplicationContext());
                    if (!isConnected) {// do nth
                    } else {
                        UpdateGameInfo(round);
                    }

                }
            }).start();

        }

        return Service.START_NOT_STICKY;
    }

    public void UpdateGameInfo(Round round) {

        int whoAmI = 0;

        Map<String, String> parameters = new HashMap<>();
        // check if I am First User or Second User
        // get first UserId and compare it with my ID
        int userId = sqlLiteDB.retrieveMainUserId();
        Game game = sqlLiteDB.retrieveGameOfId(round.getGameId());
        int score;
        if (userId == game.getFirstUserId()) {
            whoAmI = I_AM_FIRST_USER;
            score = round.getFirstUserScore();
        } else if (userId == game.getSecondUserId()) {
            whoAmI = I_AM_SECOND_USER;
            score = round.getSecondUserScore();
        } else {
            Log.e("ERROR", "is thould never be happened");
            return;
        }
        parameters.put(WHO_AM_I, whoAmI + "");
        parameters.put(ROUND_ID, round.getId() + "");
        parameters.put(ROUND_SCORE, score + "");
        parameters.put(ROUND_CONFIG, round.getRoundConfigration());
        parameters.put(ROUND_SENTENCE, round.getRoundSentence() + "");

        String parametersString = WebService
                .createQueryStringForParameters(parameters);
        String url = getResources()
                .getString(R.string.web_service_http_address)
                + getResources().getString(
                R.string.update_round_game_info_web_service_address);

        String jsonString = WebService.requestWebService(url, parametersString);

        // if return null ,, try again for more one time
        if (jsonString == null) {
            jsonString = WebService.requestWebService(url, parametersString);
        }

        onRequestResponceReceived(jsonString, round);

    }

    private void onRequestResponceReceived(String results, Round round) {

        if (results != null) {

            Log.e("frm srvs JSON", results);
            boolean status = false;
            JSONObject jsonObject = null;

            try {

                jsonObject = new JSONObject(results);
                status = jsonObject.getBoolean(JSON_STATUS);
                if (status) {
                    String roundDate = jsonObject.getString(ROUND_DATE);
                    round.setTimeString(roundDate);
                    updateLocalDataBase(round);

                } else {
                    String errorMessage = jsonObject
                            .getString(JSON_ERROR_MESSAGE);
                    Log.e("errormessage:", errorMessage);
                }

            } catch (JSONException e) {
                Log.e("at oRRR:",
                        "JSONException at onPostExecute ");
                status = false;

            }

        } else {

        }

    }

    private void updateLocalDataBase(Round round) {

        // update is dirty , time
        round.setIsDirty(NO_VALUE);
        sqlLiteDB.updateRoundDateField(round);
        sqlLiteDB.updateRoundDirtyField(round);

    }
}
