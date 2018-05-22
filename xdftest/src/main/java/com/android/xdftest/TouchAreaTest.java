package com.android.xdftest;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemProperties;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.xdftest.R;

import utils.BaseActivity;

/**
 * Created by zhouxiangyu on 2017/12/26.
 */

public class TouchAreaTest extends BaseActivity {
    public static boolean modePen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.touch_area_test);
        RadioGroup methods = findViewById(R.id.methods);
        RadioGroup mode = findViewById(R.id.mode);

        showPresentation();
        presentation.changeMode(R.id.drawHLines);
        startFullScreenHandwrite();

        okayManager.setEinkPen(false);
        SystemProperties.set("sys.close.wacomTp","1");

        mode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.hand:
                        okayManager.setEinkFinger(true);
                        SystemProperties.set("sys.close.subTp","0");
                        okayManager.setEinkPen(false);
                        SystemProperties.set("sys.close.wacomTp","1");
                        break;
                    case R.id.pen:
                        okayManager.setEinkPen(true);
                        SystemProperties.set("sys.close.wacomTp","0");
                        okayManager.setEinkFinger(false);
                        SystemProperties.set("sys.close.subTp","1");
                        break;
                }
            }
        });
        methods.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                presentation.changeMode(i);
                refresh();
                switch (i) {
                    case R.id.drawHLines:
                        startFullScreenHandwrite();
                        break;
                    case R.id.drawVLines:
                        startFullScreenHandwrite();
                        break;
                    case R.id.drawCenterToBorderLines:
                        closeHandWrite();
                        break;
                    case R.id.drawCenterToCornerLines:
                        closeHandWrite();
                        break;
                    case R.id.drawCross:
                        closeHandWrite();
                        break;
                    case R.id.daubScreen:
                        closeHandWrite();
                        break;
                }
            }
        });

    }

    public void exit(View view) {
        Log.d("zhouxiangyu", "TouchAreaTest exit");
        SystemProperties.set("sys.close.wacomTp","0");
        SystemProperties.set("sys.close.subTp","0");
        okayManager.setEinkPen(true);
        okayManager.setEinkFinger(true);
        presentation.drawColor(Color.BLACK);
        closeHandWrite();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TouchAreaTest.this.finish();
            }
        }, 500);
    }
}
