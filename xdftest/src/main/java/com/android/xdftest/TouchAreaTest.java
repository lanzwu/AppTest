package com.android.xdftest;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
    private RadioGroup methods;
    public static boolean modePen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.touch_area_test);
        methods = findViewById(R.id.methods);

        showPresentation();
        presentation.changeMode(R.id.drawHLines);
        startFullScreenHandwrite();

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
