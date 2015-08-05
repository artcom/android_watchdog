package com.artcom.watchdog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RebootSignalReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = RebootSignalReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "now I would perform a reboot :D");
        RebootHelper.reboot();
    }

}
