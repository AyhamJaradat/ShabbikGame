package com.palteam.shabbik.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.palteam.shabbik.R;
import com.palteam.shabbik.sqllight.ShabbikDAO;
import com.palteam.shabbik.webservice.WebService;

public class PullNotificationService extends Service {

    private Handler mHandler;
    private ShabbikDAO sqlLiteDB;
    private Context context;
    private NotificationsFunctions notificationFunctions;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void ping() {

        // periodic action here.
        new Thread(new Runnable() {
            @Override
            public void run() {
                // check if there is Internet connection
                boolean isConnected = WebService
                        .isConnectedToWeb(context);
                if (isConnected) {
                    checkIfIHaveData();
                }
            }
        }).start();

        scheduleNext();
    }

    private void checkIfIHaveData() {
        int userId = sqlLiteDB.retrieveMainUserId();
        if (userId <= 0)
            return;
        String requestParameters = "/" + userId;
        String url = getResources()
                .getString(R.string.web_service_http_address)
                + getResources()
                .getString(
                        R.string.periodic_pull_notification_web_service_address)
                + requestParameters;

        String jsonString = WebService.requestWebService(url, null);

        // if return null ,, try again for more one time
        if (jsonString == null) {
            jsonString = WebService.requestWebService(url, null);
        }
        if (jsonString != null)
            notificationFunctions.handleNotificationData(jsonString);

    }

    private void scheduleNext() {
        // schedule each two minutes
        int waitTime = 1000 * 60 * 2;
        mHandler.postDelayed(new Runnable() {
            public void run() {
                ping();
            }
        }, waitTime);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandler = new android.os.Handler();

        context = getApplicationContext();
        notificationFunctions = new NotificationsFunctions(context);

        sqlLiteDB = ShabbikDAO.getInstance(context);
        ping();
        return START_STICKY;
    }

}
