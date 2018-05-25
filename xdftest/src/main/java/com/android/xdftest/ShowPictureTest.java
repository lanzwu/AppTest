package com.android.xdftest;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import utils.BaseActivity;
import utils.TestConstants;

/**
 * Created by zhouxiangyu on 2017/12/20.
 */

public class ShowPictureTest extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exit);

        showPresentation();
        presentation.setPictures(TestConstants.res);
        Log.d("zhouxiangyu", "onCreate");
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        okayManager.disableFastHandWrite();
//        presentation.dismiss();
//        Log.d("zhouxiangyu", " mPresentation.dismiss();");
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                presentation.show();
//                Log.d("zhouxiangyu", "show mPresentation");
//            }
//        }, 3000);
//        okayManager.setEinkMode(1);
//        okayManager.setHandWriteArea(new Rect[]{new Rect(71, 420, 1828, 1359)});
//        okayManager.enableFastHandWrite();
//
//    }

    public void exit(View view) {
        Log.d("zhouxiangyu", "ShowPictureTest exit");
        presentation.drawColor(Color.BLACK);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ShowPictureTest.this.finish();
            }
        }, 500);
    }

}
