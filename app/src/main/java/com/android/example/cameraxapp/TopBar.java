package com.android.example.cameraxapp;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TopBar extends LinearLayout {

    private ImageView backIcon, menuIcon;
    private TextView topBarText;

    public TopBar(Context context) {
        this(context, null);
    }

    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.top_bar, this, true);
        init();
    }

    public void init(){
        backIcon = findViewById(R.id.back_icon);
        menuIcon = findViewById(R.id.menu_icon);
        topBarText = findViewById(R.id.top_bar_text);

//        Set default click listener for backIcon
        backIcon.setOnClickListener(view -> {
            if (getContext() instanceof Activity) {
                ((Activity) getContext()).onBackPressed();
            }
        });
    }

    public void setTitle(String title) {
        topBarText.setText(title);
    }

    public void setBackIconClickListener(View.OnClickListener listener) {
        backIcon.setOnClickListener(listener);
    }

    public void setMenuIconClickListener(View.OnClickListener listener) {
        menuIcon.setOnClickListener(listener);
    }
}
