package com.android.xdftest;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.example.xdftest.R;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import utils.BaseActivity;
import utils.PermissionUtils;
import utils.TestConstants;

/**
 * Created by zhouxiangyu on 2018/1/2.
 */

public class EinkFunctionTest extends BaseActivity {

    RadioGroup modes;

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
                        break;
                    case R.id.clickButton:
                        presentation.clickButton();
                        break;
                    case R.id.refreshModes:
                        modes.setVisibility(View.VISIBLE);
                        presentation.autoChangePicture(TestConstants.res, false, 1000);
                        break;
                    case R.id.dragPicture:
                        presentation.dragPicture();
                        break;
                    case R.id.scale:
                        presentation.scalePicture();
                        break;
                    case R.id.showGif:
                        presentation.showGif();
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

        modes.setVisibility(View.INVISIBLE);
        modes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
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
        if (presentation != null) {
            presentation.autoChangePicture(TestConstants.res, true , 1000);
        }
        modes.setVisibility(View.INVISIBLE);
        okayManager.setEinkMode(1);
        modes.check(R.id.mode1);
    }

    public void exit(View view) {
        Log.d("zhouxiangyu", "EinkFunctionTest exit");
        presentation.drawColor(Color.BLACK);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                EinkFunctionTest.this.finish();
            }
        },500);
    }
}
