package com.artcom.watchdog;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class MainActivity extends Activity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        setContentView(R.layout.main_activity);
        return super.onCreateView(name, context, attrs);
    }
}
