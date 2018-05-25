package com.android.xdftest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import java.util.Timer;
import java.util.TimerTask;

import utils.BaseActivity;
import utils.PermissionUtils;

/**
 * Created by zhouxiangyu on 2018/1/2.
 */

public class EinkFunctionTest extends BaseActivity {

    RadioGroup modes;
    Timer refreshTimer;
    TimerTask refreshTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eink_function_test);
        showPresentation();
        PermissionUtils.verifyStoragePermissions(this);

        modes = findViewById(R.id.modes);

        RadioGroup group = findViewById(R.id.functions);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                initModes();
                switch (i) {
                    case R.id.slidePage:
                        presentation.slidePage();
                        refresh();
                        break;
                    case R.id.clickButton:
                        presentation.clickButton();
                        refresh();
                        break;
                    case R.id.dragPicture:
                        presentation.dragPicture();
                        refreshTimer.schedule(refreshTask, 1000, 4000);
                        break;
                    case R.id.scale:
                        presentation.scalePicture();
                        refreshTimer.schedule(refreshTask, 1000, 4000);
                        break;
                    case R.id.showGif:
                        presentation.showGif();
                        refresh();
                        break;
                    case R.id.playVideo:
                        radioGroup.clearCheck();
                        startActivity(new Intent("PlayVideoTest"));
                        presentation.dismiss();
                        presentation = null;
                        break;
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        showPresentation();
    }

    public void initModes() {
        okayManager.setEinkMode(1);
        if(refreshTimer != null){
            refreshTimer.cancel();
            refreshTimer = null;
        }
        refreshTimer = new Timer();
        refreshTask = new TimerTask() {
            @Override
            public void run() {
                okayManager.enableEinkForceUpdate();
            }
        };
    }

    public void exit(View view) {
        Log.d("zhouxiangyu", "EinkFunctionTest exit");
        if(refreshTimer != null){
            refreshTimer.cancel();
            refreshTimer = null;
        }
        presentation.drawColor(Color.BLACK);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                EinkFunctionTest.this.finish();
            }
        },500);
    }
}
