package com.artcom.watchdog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.artcom.watchdog.adapter.RebootTimesAdapter;

public class RebootTimesFragment extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = RebootTimesFragment.class.getSimpleName();
    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.reboottimes_fragment, null);
        mListView = (ListView) view.findViewById(R.id.lv_reboot_times);
        mListView.setAdapter(new RebootTimesAdapter());
        Button addButton = (Button) view.findViewById(R.id.btn_add_reboot_time);
        addButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {

    }
}
