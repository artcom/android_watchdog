package com.artcom.watchdog.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.artcom.watchdog.R;
import com.artcom.watchdog.RebootHelper;
import com.artcom.watchdog.model.RebootTime;

class RebootTimeViewHolder implements CompoundButton.OnCheckedChangeListener {

    private static final String LOG_TAG = RebootTimeViewHolder.class.getSimpleName();

    private TextView mTimeText;
    private TextView mTimeLeftText;
    private Switch mIsActiveSwitch;
    private Context mContext;
    private RebootTime mRebootTime;


    RebootTimeViewHolder(View view) {
        mTimeText = (TextView) view.findViewById(R.id.tv_item_reboot_time);
        mTimeLeftText = (TextView) view.findViewById(R.id.tv_item_time_left);
        mIsActiveSwitch = (Switch) view.findViewById(R.id.sw_item_onoff);
        mIsActiveSwitch.setOnCheckedChangeListener(this);
        mContext = view.getContext();
    }

    void fillContent(RebootTime rebootTime) {
        mRebootTime = rebootTime;
        mTimeText.setText(mRebootTime.getTimeString());
        mTimeLeftText.setText(mRebootTime.getTimeLeftString());
        mIsActiveSwitch.setChecked(mRebootTime.isActive());
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isRebootActive) {
        if(mRebootTime != null) {
            if(isRebootActive && !mRebootTime.isActive()) {
                RebootHelper.activateReboot(mContext, mRebootTime);
                mRebootTime.setActive(isRebootActive);
                mRebootTime.saveToPreferences(mContext);
            }
            if(!isRebootActive && mRebootTime.isActive()) {
                RebootHelper.deactivateReboot(mContext, mRebootTime);
                mRebootTime.setActive(isRebootActive);
                mRebootTime.saveToPreferences(mContext);
            }
        }
    }

}
