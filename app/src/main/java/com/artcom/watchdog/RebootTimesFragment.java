package com.artcom.watchdog;

import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
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
import com.artcom.watchdog.model.RebootManager;
import com.artcom.watchdog.model.RebootTime;

public class RebootTimesFragment extends Fragment implements TimePickerDialog.OnTimeSetListener {

    private static final String LOG_TAG = RebootTimesFragment.class.getSimpleName();
    private ListView mListView;
    private RebootTimesAdapter mAdapter;
    private RebootManager mRebootManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRebootManager = new RebootManager(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.reboottimes_fragment, null);
        mListView = (ListView) view.findViewById(R.id.lv_reboot_times);
        mAdapter = new RebootTimesAdapter(mRebootManager.loadAllRebootTimes());
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
        if (item.getItemId() == R.id.mi_add) {
            TimePickerDialog dialog = new TimePickerDialog(getActivity(), this, 0, 0, true);
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                RebootTime rebootTime = mAdapter.getItem(info.position);
                mAdapter.remove(rebootTime);
                mRebootManager.deleteRebootTime(rebootTime);
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
        if (v.getId() == R.id.lv_reboot_times) {
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
        RebootTime rebootTime = mRebootManager.createRebootTime(hour, minute);
        mAdapter.add(rebootTime);
    }

}
