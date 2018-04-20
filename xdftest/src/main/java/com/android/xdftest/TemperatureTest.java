package com.android.xdftest;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.example.xdftest.R;

import utils.BaseActivity;

/**
 * Created by zhouxiangyu on 2018/1/3.
 */

public class TemperatureTest extends BaseActivity {
    private int[] res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refresh_test);

        res = new int[]{R.mipmap.a, R.mipmap.b, R.mipmap.c, R.mipmap.d, R.mipmap.e};
        showPresentation();
        presentation.autoChangePicture(res,false, 1000);

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
                        okayManager.setEinkMode(6);
                        break;
                }
            }
        });
    }

    public void exit(View view){
        Log.d("zhouxiangyu","TemperatureTest exit");
        okayManager.setEinkMode(1);
        presentation.autoChangePicture(res,true, 1000);
        this.finish();
    }
}
