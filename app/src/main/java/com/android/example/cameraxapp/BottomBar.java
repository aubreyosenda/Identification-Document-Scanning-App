package com.android.example.cameraxapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class BottomBar extends LinearLayout {

    private View homeIndicator, settingsIndicator, monitorIndicator, moreIndicator;

    public BottomBar(Context context) {
        this(context, null);
    }

    public BottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.bottom_bar, this, true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // Initialize views after the BottomBar is attached to the window
        homeIndicator = findViewById(R.id.home_indicator);
        settingsIndicator = findViewById(R.id.settings_indicator);
        monitorIndicator = findViewById(R.id.monitor_indicator);
        moreIndicator = findViewById(R.id.more_indicator);

        findViewById(R.id.home_container).setOnClickListener(view -> selectTab(homeIndicator));
        findViewById(R.id.settings_container).setOnClickListener(view -> selectTab(settingsIndicator));
        findViewById(R.id.monitor_container).setOnClickListener(view -> selectTab(monitorIndicator));
        findViewById(R.id.more_container).setOnClickListener(view -> selectTab(moreIndicator));
    }

    private void selectTab(View selectedIndicator) {
        homeIndicator.setVisibility(View.GONE);
        settingsIndicator.setVisibility(View.GONE);
        monitorIndicator.setVisibility(View.GONE);
        moreIndicator.setVisibility(View.GONE);

        selectedIndicator.setVisibility(View.VISIBLE);
    }
}