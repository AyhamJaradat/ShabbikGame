package com.palteam.shabbik.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.palteam.shabbik.receivers.GcmBroadcastReceiver;
import com.palteam.shabbik.utils.IConstants;

public class GcmIntentService extends IntentService implements IConstants {
    private final String TAG = GcmIntentService.class.getSimpleName();

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

//        Context context = getApplicationContext();
//        NotificationsFunctions notificationFunctions = new NotificationsFunctions(context);
//        Bundle extras = intent.getExtras();
//        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
//        // The getMessageType() intent parameter must be the intent you received
//        // in your BroadcastReceiver.
//        String messageType = gcm.getMessageType(intent);
//
//        if (!extras.isEmpty()) { // has effect of unparcelling Bundle
//            /*
//             * Filter messages based on message type. Since it is likely that
//             * GCM will be extended in the future with new message types, just
//             * ignore any message types you're not interested in, or that you
//             * don't recognize.
//             */
//            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
//                    .equals(messageType)) {
//                Log.i(TAG, "Send error: " + extras.toString());
//            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
//                    .equals(messageType)) {
//                Log.i(TAG, "Deleted messages on server: " + extras.toString());
//                // If it's a regular GCM message, do some work.
//            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
//                    .equals(messageType)) {
//
//                // Post notification of received message.
//                String notificationJsonData = intent.getExtras().getString(
//                        NOTIFICATION_DATA_KEY);
//                if (notificationJsonData != null)
//                    notificationFunctions.handleNotificationData(notificationJsonData);
//            }
//        }
//        // Release the wake lock provided by the WakefulBroadcastReceiver.
//        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
}
