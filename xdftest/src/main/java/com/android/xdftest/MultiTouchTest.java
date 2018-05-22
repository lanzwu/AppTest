package com.android.xdftest;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.xdftest.R;

import java.lang.ref.WeakReference;

import utils.BaseActivity;
import utils.EinkPresentation;
import utils.TestConstants;

/**
 * Created by zhouxiangyu on 2017/12/22.
 */

public class MultiTouchTest extends BaseActivity {

//    public static PositionHandler positionHandler;
//    ImageView surface;
//    Canvas canvas;
//    Paint positionPaint, rectPaint;
//    Bitmap picture;
//    int epdScreenWidth;
//    int epdScreenHeight;
//    private final static int widthPadding = 150;
//    private final static int heightPadding = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.draw_picture_view);
        setContentView(R.layout.exit);

//        positionPaint = new Paint();
//        positionPaint.setColor(Color.WHITE);
//        positionPaint.setTextSize(40);
//        positionPaint.setAntiAlias(true);
//
//        rectPaint = new Paint();
//        rectPaint.setColor(Color.WHITE);
//        rectPaint.setAntiAlias(true);
//        rectPaint.setStyle(Paint.Style.STROKE);
//
//        epdScreenWidth = EinkPresentation.screenWidth;
//        epdScreenHeight = EinkPresentation.screenHeight;
//
//        surface = findViewById(R.id.drawPicture);
//        newCanvas();
//        surface.setImageBitmap(picture);
        okayManager.setEinkPen(false);
        SystemProperties.set("sys.close.wacomTp","1");

        showPresentation();
        presentation.multiTouch();

        okayManager.setEinkMode(6);
//
//        positionHandler = new PositionHandler(this);

    }

//    public class PositionHandler extends Handler {
//        private final WeakReference<MultiTouchTest> mActivity;
//
//        private PositionHandler(MultiTouchTest activity) {
//            mActivity = new WeakReference<>(activity);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            MultiTouchTest activity = mActivity.get();
//            if (activity != null) {
//
//                newCanvas();
//                int num = msg.arg1;
//                if(num > 0) {
//                    float[] x = msg.getData().getFloatArray(TestConstants.POSITION_X);
//                    float[] y = msg.getData().getFloatArray(TestConstants.POSITION_Y);
//                    if (x != null && y != null) {
//                        for (int a = 0; a < num; a++) {
//                            canvas.drawCircle(x[a] + widthPadding, y[a] + heightPadding, 10, positionPaint);
//                            canvas.drawText(Math.round(x[a]) + "," + Math.round(y[a]), x[a] + widthPadding - 65, y[a] + heightPadding - 20, positionPaint);
//                        }
//                    }
//                }
//                surface.setImageBitmap(picture);
//            }
//        }
//    }
//
//    private void newCanvas() {
//        picture = Bitmap.createBitmap(EinkPresentation.screenWidth + widthPadding * 2, EinkPresentation.screenHeight + heightPadding * 2, Bitmap.Config.ARGB_8888);
//        canvas = new Canvas(picture);
//        canvas.drawRect(widthPadding, heightPadding, EinkPresentation.screenWidth + widthPadding, EinkPresentation.screenHeight + heightPadding, rectPaint);
//    }

    public void exit(View view) {
        Log.d("zhouxiangyu", "MultiTouchTest exit");
        okayManager.setEinkPen(true);
        SystemProperties.set("sys.close.wacomTp","0");
        presentation.drawColor(Color.BLACK);
        okayManager.setEinkMode(1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                MultiTouchTest.this.finish();
            }
        },500);
    }

}
