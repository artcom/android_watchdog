package com.artcom.watchdog.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.artcom.watchdog.RebootHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RebootManager {

    private static final String LOG_TAG = RebootManager.class.getSimpleName();

    private static final String REBOOT_TIME_HOUR = "reboot_time_hour";
    private static final String REBOOT_ACTIVE = "reboot_active";
    private static final String REBOOT_IDS = "ids";
    private static final String REBOOT_TIME_MINUTE = "reboot_time_minute";

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
        RebootTime rebootTime = new RebootTime(getNextId(), hour, minute, true);
        RebootHelper.activateReboot(mContext, rebootTime);
        rebootTime.saveToPreferences(mContext);
        return rebootTime;
    }

    public void deleteRebootTime(RebootTime rebootTime) {
        RebootHelper.deactivateReboot(mContext, rebootTime);
        rebootTime.delete(mContext);
    }
}
