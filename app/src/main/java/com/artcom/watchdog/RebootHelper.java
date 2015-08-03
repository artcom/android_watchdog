package com.artcom.watchdog;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.artcom.watchdog.model.RebootTime;

import java.io.IOException;
import java.io.OutputStreamWriter;

public abstract class RebootHelper {

    private static final String LOG_TAG = RebootHelper.class.getSimpleName();
    private static final int DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
    private static final String INTENT_ACTION_REBOOT = "com.artcom.watchdog.reboot";

    public static void reboot() {
        try {
            Process su = Runtime.getRuntime().exec("su");
            OutputStreamWriter out = new OutputStreamWriter(su.getOutputStream());
            out.write("reboot");
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deactivateReboot(Context context, RebootTime rebootTime) {
        Intent rebootIntent = new Intent(INTENT_ACTION_REBOOT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, rebootTime.getId(), rebootIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public static void activateReboot(Context context, RebootTime rebootTime) {
        Intent rebootIntent = new Intent(INTENT_ACTION_REBOOT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, rebootTime.getId(), rebootIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, rebootTime.getRebootTime(), DAY_IN_MILLIS, pendingIntent);
    }

}
