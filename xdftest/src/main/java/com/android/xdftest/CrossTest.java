package com.android.xdftest;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.lang.ref.WeakReference;
import utils.BaseActivity;

/**
 * Created by zhouxiangyu on 2017/12/22.
 */

public class CrossTest extends BaseActivity {

    public static CrossHandler handler;
    Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exit);

        exit = findViewById(R.id.exit);
        exit.setClickable(false);
        exit.setText(getResources().getString(R.string.testing));

        //MODE_GC16
        okayManager.setEinkMode(2);

        showPresentation();
        presentation.setCross();

        handler = new CrossHandler(this);
    }

    public class CrossHandler extends Handler {
        private final WeakReference<CrossTest> mActivity;

        private CrossHandler(CrossTest activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CrossTest activity = mActivity.get();
            if (activity != null) {
                if (msg.what == 0) {
                    exit.setClickable(true);

                    runOnUiThread(new Runnable() {
                        public void run() {
                            exit.setText(getResources().getString(R.string.complete));
                        }
                    });
                }
            }
        }
    }

    public void exit(View view) {
        Log.d("zhouxiangyu", "CrossTest exit");
        okayManager.setEinkMode(1);
        presentation.drawColor(Color.BLACK);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                CrossTest.this.finish();
            }
        },500);
    }
}
