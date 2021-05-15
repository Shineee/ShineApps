package com.shine.apps.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.shine.apps.BuildConfig;
import com.shine.apps.R;

public class AboutActivity extends AppCompatActivity {
    private TextView tvVersionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_about);
        initView();
        initData();
    }

    private void initView() {
        tvVersionName = findViewById(R.id.tvVersionName);
    }

    private void initData() {
        tvVersionName.setText("版本：" + BuildConfig.VERSION_NAME);
    }
}