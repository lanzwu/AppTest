package com.android.xdftest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
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

/**
 * Created by zhouxiangyu on 2018/1/5.
 */

public class RotateTest extends Activity {
    private IEpdRotation rotation;
    private IEpdRoListener listener;
    private int deviceOrientation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rotate_test);

        android.provider.Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 1);

        AlbumOrientationEventListener mAlbumOrientationEventListener = new AlbumOrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL);
        if (mAlbumOrientationEventListener.canDetectOrientation()) {
            mAlbumOrientationEventListener.enable();
        } else {
            Log.d("zhouxiangyu", "Can't Detect Orientation");
        }

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
                        try {
                            rotation.updateEpdRotation(0, true);
                        }catch (Exception e){
                            Log.d("zhouxiangyu",e.toString());
                        }
                        break;
                    case R.id.epdV:
                        try {
                            rotation.updateEpdRotation(1, true);
                        }catch (Exception e){
                            Log.d("zhouxiangyu",e.toString());
                        }
                        break;
                    case R.id.epdHReverse:
                        try {
                            rotation.updateEpdRotation(2, true);
                        }catch (Exception e){
                            Log.d("zhouxiangyu",e.toString());
                        }
                        break;
                    case R.id.epdVReverse:
                        try {
                            rotation.updateEpdRotation(3, true);
                        }catch (Exception e){
                            Log.d("zhouxiangyu",e.toString());
                        }
                        break;
                }
            }
        });



        ServiceConnection connection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                rotation = IEpdRotation.Stub.asInterface(service);
                listener = new RotateListener();
                try {
                    rotation.registerListener(listener);
                }catch (Exception e){
                    Log.d("zhouxiangyu",e.toString());
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        Intent intent = new Intent("com.android.systemui.action.EPD_ROTATION");
        intent.setPackage("com.android.systemui");
        this.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private class AlbumOrientationEventListener extends OrientationEventListener {
        public AlbumOrientationEventListener(Context context) {
            super(context);
        }

        public AlbumOrientationEventListener(Context context, int rate) {
            super(context, rate);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
                return;
            }

            //保证只返回四个方向
            int newOrientation = ((orientation + 45) / 90 * 90) % 360;
            if(newOrientation != deviceOrientation){
                deviceOrientation = newOrientation;
                Log.d("zhouxiangyu","deviceOrientation "+deviceOrientation);
            }
        }
    }


    private class RotateListener extends IEpdRoListener.Stub{
        @Override
        public boolean onRotationCompleted() throws RemoteException {
            //to do something here
            Log.d("zhouxiangyu","onRotationCompleted"+rotation.getEpdRotation());
            return true;
        }

        @Override
        public void onRotationNoChanged() throws RemoteException {

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void exit(View view){
        try {
            android.provider.Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0);
            rotation.unregisterListener();
        }catch (Exception e){
            Log.d("zhouxiangyu",e.toString());
        }
        this.finish();
    }
}
