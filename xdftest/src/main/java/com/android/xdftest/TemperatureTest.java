package com.android.xdftest;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import utils.BaseActivity;
import utils.TestConstants;

/**
 * Created by zhouxiangyu on 2018/1/3.
 */

public class TemperatureTest extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refresh_test);

        showPresentation();
        presentation.autoChangePicture(TestConstants.res,false, 1000);

        RadioGroup modes = findViewById(R.id.modes);
        modes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.mode1:
                        okayManager.setEinkMode(1);
                        break;
                    case R.id.mode2:
                        okayManager.setEinkMode(2);
                        break;
                    case R.id.mode5:
                        okayManager.setEinkMode(5);
                        break;
                    case R.id.mode6:
                        //优化从GC16切换到A2头几张图片模糊的问题
                        okayManager.setEinkMode(6);
                        presentation.autoChangePicture(TestConstants.res,true, 0);
                        presentation.drawColor(Color.BLACK);
                        refresh();
                        break;
                }
            }
        });
    }

    public void refresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                presentation.autoChangePicture(TestConstants.res,false, 1000);
            }
        },800);
    }

    public void exit(View view){
        Log.d("zhouxiangyu","TemperatureTest exit");
        okayManager.setEinkMode(1);
        presentation.autoChangePicture(TestConstants.res,true, 0);
        presentation.drawColor(Color.BLACK);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TemperatureTest.this.finish();
            }
        },500);
    }
}
