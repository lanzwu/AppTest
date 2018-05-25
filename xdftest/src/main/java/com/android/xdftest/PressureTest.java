package com.android.xdftest;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.lang.ref.WeakReference;

import utils.BaseActivity;

/**
 * Created by zhouxiangyu on 2017/12/28.
 */

public class PressureTest extends BaseActivity {
    private TextView textView;
    public static PressureHandler handler;
    private String pressureText;
    private int pressure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.button_and_text);
        textView = findViewById(R.id.buttonTest);
        textView.setText(getResources().getString(R.string.handWritePressure));

        startFullScreenHandwrite();
        okayManager.setEinkFinger(false);
        SystemProperties.set("sys.close.subTp","1");

        handler = new PressureHandler(this);

        showPresentation();
        presentation.drawLine("PressureTest");
    }

    public class PressureHandler extends Handler {
        private final WeakReference<PressureTest> mActivity;

        private PressureHandler(PressureTest activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(final Message msg) {
            PressureTest activity = mActivity.get();
            if (activity != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pressure = (int)(Float.parseFloat(msg.getData().getString("pressure")) * 4096);
                        pressureText = getResources().getString(R.string.handWritePressure) + pressure;
                        textView.setText(pressureText);
                    }
                });
            }
        }
    }

    public void exit(View view){
        Log.d("zhouxiangyu","PressureTest exit");
        SystemProperties.set("sys.close.subTp","0");
        okayManager.setEinkFinger(true);
        closeHandWrite();
        presentation.drawColor(Color.BLACK);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PressureTest.this.finish();
            }
        },500);
    }
}
