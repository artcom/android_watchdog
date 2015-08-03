package com.artcom.watchdog;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateRebootTimeFragment extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = CreateRebootTimeFragment.class.getSimpleName();

    private static final int DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

    private TimePicker mTimePicker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_activity, null);
        mTimePicker = (TimePicker) view.findViewById(R.id.tp_reboot);
        mTimePicker.setIs24HourView(true);
        long rebootTime = loadRebootTime();
        if(rebootTime != -1) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(rebootTime);
            mTimePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
            mTimePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        }

        Button btn = (Button) view.findViewById(R.id.btn_set_time);
        btn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        mTimePicker.clearFocus();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, mTimePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, mTimePicker.getCurrentMinute());

        long rebootTime = calendar.getTimeInMillis();

        Intent rebootIntent = new Intent("com.artcom.watchdog.reboot");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 42, rebootIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, rebootTime, DAY_IN_MILLIS, pendingIntent);

        saveRebootTime(rebootTime);

        getActivity().sendBroadcast(rebootIntent);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Toast.makeText(getActivity(), "reboot time set to " + sdf.format(new Date(rebootTime)), Toast.LENGTH_LONG).show();
    }

    private void saveRebootTime(long rebootTime) {
        Log.d(LOG_TAG, "saveRebootTime");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("reboot_time", rebootTime);
        editor.commit();
    }

    private long loadRebootTime() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return sharedPreferences.getLong("reboot_time", -1);
    }
}
