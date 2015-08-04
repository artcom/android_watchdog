package com.artcom.watchdog;

import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TimePicker;

import com.artcom.watchdog.adapter.RebootTimesAdapter;
import com.artcom.watchdog.model.RebootTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RebootTimesFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    private static final String LOG_TAG = RebootTimesFragment.class.getSimpleName();
    private ListView mListView;
    private RebootTimesAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.reboottimes_fragment, null);
        mListView = (ListView) view.findViewById(R.id.lv_reboot_times);
        mAdapter = new RebootTimesAdapter(loadAllRebootTimes());
        mListView.setAdapter(mAdapter);
        Button addButton = (Button) view.findViewById(R.id.btn_add_reboot_time);
        addButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        TimePickerDialog dialog = new TimePickerDialog(getActivity(), this, 0, 0, true);
        dialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        RebootTime rebootTime = new RebootTime(getNextId(), calendar.getTimeInMillis(), true);
        RebootHelper.activateReboot(getActivity(), rebootTime);
        rebootTime.saveToPreferences(getActivity());
        saveIdToPreferences(rebootTime.getId());
        mAdapter.add(rebootTime);
    }

    private void saveIdToPreferences(int id) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Set<String> idSet = preferences.getStringSet("ids", new HashSet<String>());
        idSet.add(Integer.toString(id));
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet("ids", idSet);
        editor.commit();
    }

    private List<RebootTime> loadAllRebootTimes() {
        List<String> idList = getIdList();
        List<RebootTime> rebootTimeList = new ArrayList<>();
        for (String id : idList) {
            RebootTime rebootTime = RebootTime.loadFromPreferences(getActivity(), id);
            rebootTimeList.add(rebootTime);
        }
        return rebootTimeList;
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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Set<String> ids = preferences.getStringSet("ids", new HashSet<String>());
        List<String> idList = new ArrayList<>(ids);
        Collections.sort(idList);
        return idList;
    }
}
