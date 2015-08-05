package com.artcom.watchdog.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class RebootTime {

    private static final String LOG_TAG = RebootTime.class.getSimpleName();
    private static final String REBOOT_TIME_HOUR = "reboot_time_hour";
    private static final String REBOOT_ACTIVE = "reboot_active";
    private static final String REBOOT_IDS = "ids";
    private static final String REBOOT_TIME_MINUTE = "reboot_time_minute";

    private int mId;
    private int mRebootHour;
    private int mRebootMinute;

    private boolean isActive;

    public RebootTime(int id, int rebootHour, int rebootMinute, boolean isActive) {
        mId = id;
        mRebootHour = rebootHour;
        mRebootMinute = rebootMinute;
        this.isActive = isActive;
    }

    public int getId() {
        return mId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getTimeString() {
        return Integer.toString(mRebootHour) + ":" + Integer.toString(mRebootMinute);
    }

    public String getTimeLeftString() {
//        long timeLeft = mRebootTime - System.currentTimeMillis();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(timeLeft);
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.GERMANY);
//        return "reboot in " + sdf.format(new Date(timeLeft));
//        String timeLeftText = "reboot in " + Integer.toString(hour) + "h and " + Integer.toString(minute) + "m";
        return "";
    }


    public long getNextRebootTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, mRebootHour);
        calendar.set(Calendar.MINUTE, mRebootMinute);
        calendar.set(Calendar.SECOND, 0);

        long rebootTimeInMillis = calendar.getTimeInMillis();
        if (rebootTimeInMillis < System.currentTimeMillis()) {
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH) + 1;
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }
        return calendar.getTimeInMillis();
    }

    /**
     *
     * TODO: Move this code to RebootManager
     *
     */
    public void saveToPreferences(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> idSet = sharedPreferences.getStringSet(REBOOT_IDS, new HashSet<String>());
        idSet.add(Integer.toString(mId));
        editor.putStringSet(REBOOT_IDS, idSet);
        editor.putInt(Integer.toString(mId) + REBOOT_TIME_HOUR, mRebootHour);
        editor.putInt(Integer.toString(mId) + REBOOT_TIME_MINUTE, mRebootMinute);
        editor.putBoolean(Integer.toString(mId) + REBOOT_ACTIVE, isActive);
        editor.commit();
    }

    public static RebootTime loadFromPreferences(Context context, String id) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        int rebootTimeHour = sharedPreferences.getInt(id + REBOOT_TIME_HOUR, -1);
        int rebootTimeMinute = sharedPreferences.getInt(id + REBOOT_TIME_MINUTE, -1);
        boolean isActive = sharedPreferences.getBoolean(id + REBOOT_ACTIVE, false);
        return new RebootTime(Integer.parseInt(id), rebootTimeHour, rebootTimeMinute, isActive);
    }

    public void delete(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> idSet = sharedPreferences.getStringSet(REBOOT_IDS, new HashSet<String>());
        idSet.remove(Integer.toString(mId));
        editor.putStringSet(REBOOT_IDS, idSet);
        editor.remove(Integer.toString(mId) + REBOOT_TIME_HOUR);
        editor.remove(Integer.toString(mId) + REBOOT_TIME_MINUTE);
        editor.remove(Integer.toString(mId) + REBOOT_ACTIVE);
        editor.commit();
    }
}
