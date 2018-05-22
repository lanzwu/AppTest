package com.android.xdftest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.systemui.okepd.IEpdRoListener;
import com.android.systemui.okepd.IEpdRotation;
import com.example.xdftest.R;

import utils.BaseActivity;
import utils.TestConstants;

/**
 * Created by zhouxiangyu on 2018/1/5.
 */

public class RotateTest extends BaseActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemProperties.set("sys.eink.rotate","true");
        android.provider.Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);

        setContentView(R.layout.rotate_test);

        intent = new Intent("android.intent.epd.rotate");
        intent.putExtra("orientation",3);
        sendBroadcast(intent);

        showPresentation();
        presentation.setPicture();

        RadioGroup mainScreen = findViewById(R.id.mainScreen);
        mainScreen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.mainH:
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        break;
                    case R.id.mainV:
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        break;
                    case R.id.mainHReverse:
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                        break;
                    case R.id.mainVReverse:
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                        break;
                    case R.id.autoRotate:
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                        break;
                }
            }
        });

        RadioGroup epdScreen = findViewById(R.id.epdScreen);
        epdScreen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.epdH:
                        intent.putExtra("orientation",3);
                        sendBroadcast(intent);
                        break;
                    case R.id.epdV:
                        intent.putExtra("orientation",0);
                        sendBroadcast(intent);
                        break;
                    case R.id.epdHReverse:
                        intent.putExtra("orientation",1);
                        sendBroadcast(intent);
                        break;
                    case R.id.epdVReverse:
                        intent.putExtra("orientation",2);
                        sendBroadcast(intent);
                        break;
                }
            }
        });
    }

    public void exit(View view){
        android.provider.Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        presentation.drawColor(Color.BLACK);
        intent.putExtra("orientation",3);
        sendBroadcast(intent);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RotateTest.this.finish();
            }
        },500);
    }
}
