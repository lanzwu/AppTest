package com.android.xdftest;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import utils.BaseActivity;
import utils.TestConstants;

/**
 * Created by zhouxiangyu on 2017/12/29.
 */

public class HandWriteTest extends BaseActivity {

    Button draw5x5, draw7x7;
    public static HandWriteTestHandler handWriteTestHandler;
    private int cagesNum;
    Rect[] cages;
    ArrayList<Rect> rectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hand_write_test);
        draw5x5 = findViewById(R.id.draw5x5);
        draw7x7 = findViewById(R.id.draw7x7);

        draw5x5.setOnClickListener(listener);
        draw7x7.setOnClickListener(listener);

        handWriteTestHandler = new HandWriteTestHandler(this);

        rectList = new ArrayList<>();
        showPresentation();

        okayManager.setEinkFinger(false);
        SystemProperties.set("sys.close.subTp","1");
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            rectList.clear();
            switch (view.getId()) {
                case R.id.draw5x5:
                    presentation.drawCages(TestConstants.DRAW_5x5_CAGES);

                    draw5x5.setEnabled(false);
                    draw5x5.setTextColor(getResources().getColor(R.color.white, null));

                    draw7x7.setEnabled(true);
                    draw7x7.setTextColor(getResources().getColor(R.color.black, null));
                    break;
                case R.id.draw7x7:
                    presentation.drawCages(TestConstants.DRAW_7x7_CAGES);

                    draw5x5.setEnabled(true);
                    draw5x5.setTextColor(getResources().getColor(R.color.black, null));

                    draw7x7.setEnabled(false);
                    draw7x7.setTextColor(getResources().getColor(R.color.white, null));

                    break;
            }
        }
    };

    public Rect[] readCagesFromList() {
        cages = new Rect[cagesNum];
        for (int i = 0; i < cagesNum; i++) {
            cages[i] = rectList.get(i);
            Log.d("zhouxiangyu", rectList.get(i).toString());
        }
        return cages;
    }

    public class HandWriteTestHandler extends Handler {
        private final WeakReference<HandWriteTest> mActivity;

        private HandWriteTestHandler(HandWriteTest activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            HandWriteTest activity = mActivity.get();
            cagesNum = msg.what;
            if (activity != null) {
                int[] oneCage = msg.getData().getIntArray(TestConstants.CAGE);
                if (oneCage != null) {
                    rectList.add(new Rect(oneCage[0], oneCage[1], oneCage[2], oneCage[3]));
                }
            }
            if (cagesNum == 25) {
                startHandWriteArea(readCagesFromList());
            }
        }
    }

    public void exit(View view) {
        Log.d("zhouxiangyu", "HandWriteTest exit");
        closeHandWrite();
        SystemProperties.set("sys.close.subTp","0");
        okayManager.setEinkFinger(true);
        presentation.drawColor(Color.BLACK);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                HandWriteTest.this.finish();
            }
        }, 500);
    }
}
