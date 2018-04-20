package com.android.xdftest;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.xdftest.R;

import java.util.Timer;
import java.util.TimerTask;

import utils.BaseActivity;

/**
 * Created by zhouxiangyu on 2017/12/20.
 */

public class GrayLevelTest extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exit);
        okayManager.setEinkMode(2);
        showPresentation();
        presentation.setGrayLevel();

    }

    public void exit(View view) {
        Log.d("zhouxiangyu", "GrayLevelTest exit");
        presentation.drawColor(Color.BLACK);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                GrayLevelTest.this.finish();
            }
        },600);
    }
}
