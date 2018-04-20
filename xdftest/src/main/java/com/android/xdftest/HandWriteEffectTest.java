package com.android.xdftest;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.xdftest.R;

import java.lang.ref.WeakReference;

import utils.BaseActivity;

/**
 * Created by zhouxiangyu on 2017/12/26.
 */

public class HandWriteEffectTest extends BaseActivity {
    private TextView textView;
    public static TimeHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.button_and_text);
        textView = findViewById(R.id.buttonTest);
        textView.setText(getResources().getString(R.string.handWriteDrawLine));
        showPresentation();
        presentation.drawLine("HandWriteEffectTest");

        startFullScreenHandwrite();

        handler = new TimeHandler(this);
    }

    public class TimeHandler extends Handler {
        private final WeakReference<HandWriteEffectTest> mActivity;

        private TimeHandler(HandWriteEffectTest activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(final Message msg) {
            HandWriteEffectTest activity = mActivity.get();
            if (activity != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String time = getResources().getString(R.string.handWriteDrawLineTime) + msg.getData().getString("time");
                        textView.setText(time);
                    }
                });
            }
        }
    }

    public void exit(View view) {
        Log.d("zhouxiangyu", "HandWriteEffectTest exit");
        closeHandWrite();
        presentation.drawColor(Color.BLACK);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                HandWriteEffectTest.this.finish();
            }
        },500);
    }
}
