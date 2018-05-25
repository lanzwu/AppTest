package com.android.xdftest;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import utils.BaseActivity;
import utils.view.LCDControlView;

/**
 * Created by zhouxiangyu on 2017/12/29.
 */

public class LCDControlTest extends BaseActivity {

    public static LCDControlTestHandler controlTestHandler;
    private CheckBox slipToTop, slipToBottom, slipToLeft, slipToRight;
    private ArrayList<TextView> fingerList;
    private ArrayList<CheckBox> slipList;
    private Vibrator vibrator;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lcdcontrol_test);
        enableBackBtn(true);

        initViews();

        controlTestHandler = new LCDControlTestHandler(this);
        showPresentation();
        presentation.LCDControl();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        LCDControlView.left_tested = false;
        LCDControlView.right_tested = false;
        LCDControlView.top_tested = false;
        LCDControlView.bottom_tested = false;
        LCDControlView.FINGER_NUM = 1;
    }


    private void initViews() {
        TextView oneFinger = findViewById(R.id.oneFinger);
        TextView twoFinger = findViewById(R.id.twoFinger);
        TextView threeFinger = findViewById(R.id.threeFinger);
        TextView fourFinger = findViewById(R.id.fourFinger);
        TextView fiveFinger = findViewById(R.id.fiveFinger);

        fingerList = new ArrayList<>();
        fingerList.add(oneFinger);
        fingerList.add(twoFinger);
        fingerList.add(threeFinger);
        fingerList.add(fourFinger);
        fingerList.add(fiveFinger);
        for (TextView text : fingerList) {
            text.setOnClickListener(listener);
        }

        oneFinger.setBackgroundColor(Color.parseColor("#666666"));

        slipToTop = findViewById(R.id.slipToTop);
        slipToBottom = findViewById(R.id.slipToBottom);
        slipToLeft = findViewById(R.id.slipToLeft);
        slipToRight = findViewById(R.id.slipToRight);
        slipList = new ArrayList<>();
        slipList.add(slipToTop);
        slipList.add(slipToBottom);
        slipList.add(slipToLeft);
        slipList.add(slipToRight);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            changeColor((TextView) view);
            if (view.getId() == R.id.oneFinger) {
                LCDControlView.FINGER_NUM = 1;
            } else if (view.getId() == R.id.twoFinger) {
                LCDControlView.FINGER_NUM = 2;
            } else if (view.getId() == R.id.threeFinger) {
                LCDControlView.FINGER_NUM = 3;
            } else if (view.getId() == R.id.fourFinger) {
                LCDControlView.FINGER_NUM = 4;
            } else if (view.getId() == R.id.fiveFinger) {
                LCDControlView.FINGER_NUM = 5;
            }
            LCDControlView.left_tested = false;
            LCDControlView.right_tested = false;
            LCDControlView.top_tested = false;
            LCDControlView.bottom_tested = false;
            count = 0;
        }
    };

    private void changeColor(TextView textView) {
        for (TextView text : fingerList) {
            if (text.getId() == textView.getId()) {
                text.setBackgroundColor(Color.parseColor("#666666"));
            } else {
                text.setBackgroundColor(Color.WHITE);
            }
        }
        for (CheckBox checkBox : slipList) {
            checkBox.setChecked(false);
            checkBox.setBackgroundColor(Color.WHITE);
        }
    }

    public class LCDControlTestHandler extends Handler {
        private final WeakReference<LCDControlTest> mActivity;

        private LCDControlTestHandler(LCDControlTest activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LCDControlTest activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 1:
                        slipToLeft.setChecked(true);
                        slipToLeft.setBackgroundColor(Color.parseColor("#999999"));
                        vibrate();
                        count++;
                        break;
                    case 2:
                        slipToRight.setChecked(true);
                        slipToRight.setBackgroundColor(Color.parseColor("#999999"));
                        vibrate();
                        count++;
                        break;
                    case 3:
                        slipToTop.setChecked(true);
                        slipToTop.setBackgroundColor(Color.parseColor("#999999"));
                        vibrate();
                        count++;
                        break;
                    case 4:
                        slipToBottom.setChecked(true);
                        slipToBottom.setBackgroundColor(Color.parseColor("#999999"));
                        vibrate();
                        count++;
                        break;
                }
                if (count == 4) {
                    count = 0;
                    if(LCDControlView.FINGER_NUM == 5){
                        LCDControlView.FINGER_NUM = 1;
                    }else {
                        LCDControlView.FINGER_NUM++;
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            changeColor(fingerList.get(LCDControlView.FINGER_NUM - 1));
                        }
                    },500);
                }

            }
        }
    }

    public void vibrate() {
        if (vibrator != null) {
            vibrator.vibrate(100);
        }
    }

    public void exit(View view) {
        Log.d("zhouxiangyu", "LCDControlTest exit");
        this.finish();
    }
}
