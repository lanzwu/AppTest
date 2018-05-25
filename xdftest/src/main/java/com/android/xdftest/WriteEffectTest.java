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
import utils.TestConstants;

/**
 * Created by zhouxiangyu on 2017/12/26.
 */

public class WriteEffectTest extends BaseActivity {
    private TextView textView;
    public static TimeHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.button_and_text);
        textView = findViewById(R.id.buttonTest);
        textView.setText(getResources().getString(R.string.handWriteDrawLine));

        startFullScreenHandwrite();
        if(getIntent().getStringExtra(TestConstants.MODE).equals(TestConstants.MODE_PEN)){
            okayManager.setEinkFinger(false);
            SystemProperties.set("sys.close.subTp","1");
        }else{
            okayManager.setEinkPen(false);
            SystemProperties.set("sys.close.wacomTp","1");
        }

        showPresentation();
        presentation.drawLine("WriteEffectTest");

        handler = new TimeHandler(this);
    }

    public class TimeHandler extends Handler {
        private final WeakReference<WriteEffectTest> mActivity;

        private TimeHandler(WriteEffectTest activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(final Message msg) {
            WriteEffectTest activity = mActivity.get();
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
        Log.d("zhouxiangyu", "WriteEffectTest exit");
        SystemProperties.set("sys.close.wacomTp","0");
        SystemProperties.set("sys.close.subTp","0");
        okayManager.setEinkPen(true);
        okayManager.setEinkFinger(true);
        closeHandWrite();
        presentation.drawColor(Color.BLACK);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WriteEffectTest.this.finish();
            }
        },500);
    }
}
