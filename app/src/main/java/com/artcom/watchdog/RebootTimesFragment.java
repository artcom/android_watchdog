package com.artcom.watchdog;

import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

public class RebootTimesFragment extends Fragment implements TimePickerDialog.OnTimeSetListener {

    private static final String LOG_TAG = RebootTimesFragment.class.getSimpleName();
    private ListView mListView;
    private RebootTimesAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.reboottimes_fragment, null);
        mListView = (ListView) view.findViewById(R.id.lv_reboot_times);
        mAdapter = new RebootTimesAdapter(loadAllRebootTimes());
        mListView.setAdapter(mAdapter);
        registerForContextMenu(mListView);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actionbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.mi_add) {
            TimePickerDialog dialog = new TimePickerDialog(getActivity(), this, 0, 0, true);
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case 1:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                RebootTime rebootTime = mAdapter.getItem(info.position);
                RebootHelper.deactivateReboot(getActivity(), rebootTime);
                mAdapter.remove(rebootTime);
                rebootTime.delete(getActivity());
                break;
            case 2:
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId() == R.id.lv_reboot_times) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle("Options");
//            menu.add(0, 2, 0, "edit");
            menu.add(0, 1, 1, "delete");
        } else {
            super.onCreateContextMenu(menu, v, menuInfo);
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        RebootTime rebootTime = new RebootTime(getNextId(), calendar.getTimeInMillis(), true);
        RebootHelper.activateReboot(getActivity(), rebootTime);
        rebootTime.saveToPreferences(getActivity());
        mAdapter.add(rebootTime);
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
