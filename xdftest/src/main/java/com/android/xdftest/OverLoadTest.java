package com.android.xdftest;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.example.xdftest.R;

import utils.BaseActivity;

/**
 * Created by zhouxiangyu on 2018/2/26.
 */

public class OverLoadTest extends BaseActivity {
    private int[] overload_res,super_overload_res,res;
    private int period = 5000;
    private boolean isAutoChangeMode = true;
    private RadioGroup timeCell;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overload_test);

        //MODE_GC16
        okayManager.setEinkMode(2);

        overload_res = new int[]{R.mipmap.pvi050, R.mipmap.pvi051};
        super_overload_res = new int[]{R.mipmap.dot_a, R.mipmap.dot_b};
        res = overload_res;

        showPresentation();
        presentation.autoChangePicture(res, false, period);

        RadioGroup overLoadTestMode = findViewById(R.id.overLoadTestMode);
        overLoadTestMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.autoTestMode){
                    isAutoChangeMode = true;
                    timeCell.setVisibility(View.VISIBLE);
                }else if(i == R.id.handTestMode){
                    isAutoChangeMode = false;
                    timeCell.setVisibility(View.INVISIBLE);
                }
                startChange(isAutoChangeMode);
            }
        });

        RadioGroup overLoadKind = findViewById(R.id.overLoadKind);
        overLoadKind.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.overLoadMode){
                    res = overload_res;
                }else if(i == R.id.superOverLoadtMode){
                    res = super_overload_res;
                }
                startChange(isAutoChangeMode);
            }
        });

        timeCell = findViewById(R.id.timeCell);
        timeCell.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.timeCell_1){
                    period = 1000;
                }else if(i == R.id.timeCell_2){
                    period = 2000;
                }else if(i == R.id.timeCell_5){
                    period = 5000;
                }
                startChange(true);
            }
        });
    }

    public void startChange(boolean isAutoChangeMode){
        presentation.autoChangePicture(res, true, period);
        if(isAutoChangeMode){
            presentation.autoChangePicture(res, false, period);
        }else{
            presentation.setPictures(res);
        }
    }

    public void exit(View view){
        Log.d("zhouxiangyu","OverLoadTest exit");
        okayManager.setEinkMode(1);
        if(isAutoChangeMode){
            presentation.autoChangePicture(res, true, period);
        }
        presentation.drawColor(Color.BLACK);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                OverLoadTest.this.finish();
            }
        },500);
    }
}
