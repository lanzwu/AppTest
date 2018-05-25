package com.android.xdftest;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import utils.BaseActivity;

/**
 * Created by zhouxiangyu on 2018/1/12.
 */

public class EMRTest extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exit);
        okayManager.setEinkMode(2);
        showPresentation();
        presentation.showEMR();
    }

    public void exit(View view) {
        Log.d("zhouxiangyu", "EMRTest exit");
        presentation.drawColor(Color.BLACK);
        okayManager.setEinkMode(1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                EMRTest.this.finish();
            }
        },500);
    }
}
