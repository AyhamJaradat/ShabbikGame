package com.palteam.shabbik.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.palteam.shabbik.services.OnConnectService;


public class ConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(ConnectivityReceiver.class.getSimpleName(),
                "action: " + intent.getAction());

        if (haveNetworkConnection(context)) {
            Log.d("in receiver", "there is a connection");
            // start the service
            Intent i = new Intent(context, OnConnectService.class);
            context.startService(i);

        }

        // Log.d("app","Network connectivity change");
        // if(intent.getExtras()!=null) {
        //
        // NetworkInfo ni=(NetworkInfo)
        // intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
        // if(ni!=null && ni.getState()==NetworkInfo.State.CONNECTED) {
        //
        // Log.i("app","Network "+ni.getTypeName()+" connected");
        // }
        // }
        // if(intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY,Boolean.FALSE))
        // {
        // Log.d("app","There's no network connectivity");
        // }

    }

    private boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
