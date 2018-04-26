package com.android.xdftest;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.example.xdftest.R;

import utils.BaseActivity;

/**
 * Created by zhouxiangyu on 2017/12/31.
 */

public class RefreshTest extends BaseActivity {
    int[] res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refresh_test);
        res = new int[]{R.mipmap.a, R.mipmap.b, R.mipmap.c, R.mipmap.d, R.mipmap.e};
        showPresentation();
        presentation.setPictures(res);

        RadioGroup modes = findViewById(R.id.modes);
        modes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.mode1:
                        okayManager.setEinkMode(1);
                        break;
                    case R.id.mode2:
                        okayManager.setEinkMode(2);
                        break;
                    case R.id.mode5:
                        okayManager.setEinkMode(5);
                        break;
                    case R.id.mode6:
                        okayManager.setEinkMode(6);
                        //优化从GC16切换到A2头几张图片模糊的问题
                        presentation.drawColor(Color.BLACK);
                        refresh();
                        break;
                }
            }
        });
    }

    public void refresh() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                presentation.setPictures(res);
            }
        },800);
    }

    public void exit(View view){
        Log.d("zhouxiangyu","RefreshTest exit");
        android.provider.Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
        presentation.drawColor(Color.BLACK);
        okayManager.setEinkMode(1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RefreshTest.this.finish();
            }
        },500);
    }
}
