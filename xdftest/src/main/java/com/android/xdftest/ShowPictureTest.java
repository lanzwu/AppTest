package com.android.xdftest;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.example.xdftest.R;

import utils.BaseActivity;

/**
 * Created by zhouxiangyu on 2017/12/20.
 */

public class ShowPictureTest extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exit);

        int[] res = new int[]{R.mipmap.a, R.mipmap.b, R.mipmap.c, R.mipmap.d, R.mipmap.e};
        showPresentation();
        presentation.setPictures(res);

    }

    public void exit(View view){
        Log.d("zhouxiangyu","ShowPictureTest exit");
        presentation.drawColor(Color.BLACK);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ShowPictureTest.this.finish();
            }
        },500);
    }

}
