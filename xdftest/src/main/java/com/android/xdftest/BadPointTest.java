package com.android.xdftest;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.xdftest.R;

import utils.BaseActivity;

/**
 * Created by zhouxiangyu on 2017/12/20.
 */

public class BadPointTest extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bad_point_test);
        showPresentation();
        presentation.drawColor(Color.WHITE);
        //MODE_GC16
        okayManager.setEinkMode(2);
    }

    public void black(View view) {
        presentation.drawColor(Color.BLACK);
    }

    public void white(View view) {
        presentation.drawColor(Color.WHITE);
    }

    public void gray(View view) {
        presentation.drawColor(Color.GRAY);
    }

    public void exit(View view) {
        Log.d("zhouxiangyu", "BadPointTest exit");
        presentation.drawColor(Color.BLACK);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BadPointTest.this.finish();
            }
        },500);
    }

}
