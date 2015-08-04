package com.artcom.watchdog.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RebootTime {

    private static final String LOG_TAG = RebootTime.class.getSimpleName();
    private static final String REBOOT_TIME = "reboot_time";
    private static final String REBOOT_ACTIVE = "reboot_active";

    private int mId;
    private long mRebootTime;
    private boolean isActive;

    public RebootTime(int id, long rebootTime, boolean isActive) {
        mId = id;
        mRebootTime = rebootTime;
        this.isActive = isActive;
    }

    public int getId() {
        return mId;
    }

    public long getRebootTime() {
        return mRebootTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setRebootTime(long rebootTime) {
        mRebootTime = rebootTime;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(new Date(mRebootTime));
    }

    public String getTimeLeftString() {
        long timeLeft = mRebootTime - System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return "reboot in " + sdf.format(new Date(timeLeft));
    }

    public void saveToPreferences(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(Integer.toString(mId) + REBOOT_TIME, mRebootTime);
        editor.putBoolean(Integer.toString(mId) + REBOOT_ACTIVE, isActive);
        editor.commit();
    }

    public static RebootTime loadFromPreferences(Context context, String id) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        long rebootTime = sharedPreferences.getLong(id + REBOOT_TIME, -1);
        boolean isActive = sharedPreferences.getBoolean(id + REBOOT_ACTIVE, false);
        return new RebootTime(Integer.parseInt(id), rebootTime, isActive);
    }

}
