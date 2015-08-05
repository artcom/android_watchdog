package com.artcom.watchdog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.artcom.watchdog.model.RebootManager;
import com.artcom.watchdog.model.RebootTime;

import java.util.List;

public class RebootCompletedReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = RebootCompletedReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        RebootManager rebootManager = new RebootManager(context);
        List<RebootTime> rebootTimes = rebootManager.loadAllRebootTimes();
        for(RebootTime rebootTime : rebootTimes) {
            RebootHelper.activateReboot(context, rebootTime);
        }
    }
}
