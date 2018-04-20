package com.android.xdftest;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.example.xdftest.R;

import utils.BaseActivity;

/**
 * Created by zhouxiangyu on 2017/12/21.
 */

public class ShowTextTest extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exit);
        showPresentation();
        presentation.showText();
    }

    public void exit(View view){
        Log.d("zhouxiangyu","ShowTextTest exit");
        presentation.setColor(Color.BLACK);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ShowTextTest.this.finish();
            }
        },500);
    }
}
