package com.artcom.watchdog.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeLeft);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.GERMANY);
//        return "reboot in " + sdf.format(new Date(timeLeft));
        String timeLeftText = "reboot in " + Integer.toString(hour) + "h and " + Integer.toString(minute) + "m";
        return "";
    }

    public void saveToPreferences(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> idSet = sharedPreferences.getStringSet("ids", new HashSet<String>());
        idSet.add(Integer.toString(mId));
        editor.putStringSet("ids", idSet);
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

    public void delete(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> idSet = sharedPreferences.getStringSet("ids", new HashSet<String>());
        idSet.remove(Integer.toString(mId));
        editor.putStringSet("ids", idSet);
        editor.remove(Integer.toString(mId) + REBOOT_TIME);
        editor.remove(Integer.toString(mId) + REBOOT_ACTIVE);
        editor.commit();
    }
}
