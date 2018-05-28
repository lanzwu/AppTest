package com.android.xdftest;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemProperties;
import android.view.View;
import android.widget.Button;
import utils.BaseActivity;
import utils.TestConstants;

public class MainActivity extends BaseActivity {

    public static boolean enableClick = true;
    private PowerManager.WakeLock wakeLock;
    private Intent keepScreenOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ActivityManager.isUserAMonkey()){
            setAllButtonDisabled(TestConstants.buttons);
        }

        setButtonListener(TestConstants.buttons);

        okayManager.setPartialUpdateRegion(new Rect[]{new Rect(0, 0, 1872, 1404)});
        okayManager.setHandWriteStroke(2);

//        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        if (powerManager != null) {
//            wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "MainActivity");
//            wakeLock.acquire();
//        }

        Button overLoad = findViewById(R.id.overLoadTest);
        if ("open".equals(SystemProperties.get("sys.overload.test"))) {
            overLoad.setVisibility(View.VISIBLE);
        } else {
            overLoad.setVisibility(View.INVISIBLE);
        }

//        Intent intentHide = new Intent();
//        intentHide.setAction("epd.intent.action.IDLELOGO_HIDE");
//        sendBroadcast(intentHide);

        keepScreenOn = new Intent("action.no.sleep");
        keepScreenOn.putExtra("noSleep", true);
        sendBroadcast(keepScreenOn);

        showPresentation();
    }

    private void setAllButtonDisabled(int[] buttons){
        for (int i = 0; i < buttons.length; i++) {
            Button button = findViewById(buttons[i]);
            if(buttons[i] != R.id.mainExit) {
                button.setEnabled(false);
            }
        }
    }

    private void setButtonListener(int[] buttons) {
        for (int i = 0; i < buttons.length; i++) {
            Button button = findViewById(buttons[i]);
            button.setOnClickListener(listener);
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.badPointTest:
                    startActivity(new Intent("BadPointTest"));
                    break;
                case R.id.grayLevelTest:
                    startActivity(new Intent("GrayLevelTest"));
                    break;
                case R.id.showPictureTest:
                    startActivity(new Intent("ShowPictureTest"));
                    break;
                case R.id.crossTest:
                    startActivity(new Intent("CrossTest"));
                    break;
                case R.id.multiTouchTest:
                    startActivity(new Intent("MultiTouchTest"));
                    break;
                case R.id.touchAreaTset:
                    startActivity(new Intent("TouchAreaTest"));
                    break;
                case R.id.showTextTest:
                    startActivity(new Intent("ShowTextTest"));
                    break;
                case R.id.refreshTset:
                    startActivity(new Intent("RefreshTest"));
                    break;
                case R.id.temperatureTest:
                    startActivity(new Intent("TemperatureTest"));
                    break;
                case R.id.handWriteEffectTest:
                    Intent handEffect = new Intent("WriteEffectTest");
                    handEffect.putExtra(TestConstants.MODE, TestConstants.MODE_HAND);
                    startActivity(handEffect);
                    break;
                case R.id.penWriteEffectTest:
                    Intent penEffect = new Intent("WriteEffectTest");
                    penEffect.putExtra(TestConstants.MODE, TestConstants.MODE_PEN);
                    startActivity(penEffect);
                    break;
                case R.id.rotateTest:
                    startActivity(new Intent("RotateTest"));
                    break;
                case R.id.buttonTest:
                    startActivity(new Intent("ButtonTest"));
                    break;
                case R.id.pressureTest:
                    startActivity(new Intent("PressureTest"));
                    break;
                case R.id.handWriteTest:
                    startActivity(new Intent("HandWriteTest"));
                    break;
                case R.id.lcdControlTest:
                    startActivity(new Intent("LCDControlTest"));
                    break;
                case R.id.einkFunctionTest:
                    startActivity(new Intent("EinkFunctionTest"));
                    break;
                case R.id.emrTest:
                    startActivity(new Intent("EMRTest"));
                    break;
                case R.id.ePenTest:
                    startActivity(new Intent("PenTest"));
                    break;
                case R.id.overLoadTest:
                    startActivity(new Intent("OverLoadTest"));
                    break;
                case R.id.mainExit:
                    enableHomeBtn(true);
                    //wakeLock.release();
                    keepScreenOn.putExtra("noSleep", false);
                    sendBroadcast(keepScreenOn);
                    finish();
                    break;
            }
        }
    };

}
