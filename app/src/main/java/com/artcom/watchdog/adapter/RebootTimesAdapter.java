package com.artcom.watchdog.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.artcom.watchdog.R;
import com.artcom.watchdog.model.RebootManager;
import com.artcom.watchdog.model.RebootTime;

import java.util.ArrayList;
import java.util.List;

public class RebootTimesAdapter extends BaseAdapter {

    private static final String LOG_TAG = RebootTimesAdapter.class.getSimpleName();
    private final RebootManager mRebootManager;

    private List<RebootTime> mRebootTimeList = new ArrayList<>();

    public RebootTimesAdapter(RebootManager rebootManager) {
        mRebootManager = rebootManager;
        mRebootTimeList = rebootManager.loadAllRebootTimes();
    }

    @Override
    public int getCount() {
        return mRebootTimeList.size();
    }

    @Override
    public RebootTime getItem(int position) {
        return mRebootTimeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RebootTimeViewHolder viewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reboot_time_item, null);
            viewHolder = new RebootTimeViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (RebootTimeViewHolder) convertView.getTag();
        }

        viewHolder.fillContent(mRebootTimeList.get(position));
        return convertView;
    }

    public void add(RebootTime rebootTime) {
        mRebootTimeList.add(rebootTime);
        notifyDataSetChanged();
    }

    public void remove(RebootTime rebootTime) {
        mRebootTimeList.remove(rebootTime);
        notifyDataSetChanged();
    }
}
