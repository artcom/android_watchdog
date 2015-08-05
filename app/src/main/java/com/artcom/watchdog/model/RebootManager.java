package com.artcom.watchdog.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.artcom.watchdog.RebootHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RebootManager {

    private static final String LOG_TAG = RebootManager.class.getSimpleName();

    private Context mContext;

    public RebootManager(Context context) {
        mContext = context;
    }

    private int getNextId() {
        List<String> idList = getIdList();
        if(idList.isEmpty()) {
            return 0;
        }
        String lastId = idList.get(idList.size() - 1);
        return Integer.parseInt(lastId) + 1;
    }

    private List<String> getIdList() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        Set<String> ids = preferences.getStringSet("ids", new HashSet<String>());
        List<String> idList = new ArrayList<>(ids);
        Collections.sort(idList);
        return idList;
    }

    public List<RebootTime> loadAllRebootTimes() {
        List<String> idList = getIdList();
        List<RebootTime> rebootTimeList = new ArrayList<>();
        for (String id : idList) {
            RebootTime rebootTime = RebootTime.loadFromPreferences(mContext, id);
            rebootTimeList.add(rebootTime);
        }
        return rebootTimeList;
    }

    public RebootTime createRebootTime(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        long rebootTimeInMillis = calendar.getTimeInMillis();
        if (rebootTimeInMillis < (System.currentTimeMillis() + 60 * 1000)) {
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH) + 1;
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }

        RebootTime rebootTime = new RebootTime(getNextId(), calendar.getTimeInMillis(), true);
        RebootHelper.activateReboot(mContext, rebootTime);
        rebootTime.saveToPreferences(mContext);
        return rebootTime;
    }

    public void deleteRebootTime(RebootTime rebootTime) {
        RebootHelper.deactivateReboot(mContext, rebootTime);
        rebootTime.delete(mContext);
    }
}
