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
    private RadioGroup methods, mode;
    private Button begin;
    public static boolean modePen = false;
    private int id = R.id.drawHLines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.touch_area_test);
        methods = findViewById(R.id.methods);
        mode = findViewById(R.id.mode);
        begin = findViewById(R.id.beginDrawLine);
        begin.setClickable(false);
        begin.setText(getResources().getString(R.string.cannotDrawLine));

        showPresentation();
        presentation.changeMode(id);

        methods.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                presentation.changeMode(i);
                refresh();
                id = i;
                disablePenWrite();
                switch (i) {
                    case R.id.drawHLines:
                        if (modePen) {
                            enablePenWrite();
                        }
                        break;
                    case R.id.drawVLines:
                        if (modePen) {
                            enablePenWrite();
                        }
                        break;
                    case R.id.drawCenterToBorderLines:

                        break;
                    case R.id.drawCenterToCornerLines:

                        break;
                    case R.id.drawCross:
                        break;
                    case R.id.daubScreen:
                        break;
                }
            }
        });

        mode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                modePen = (i == R.id.pen);
                if (modePen) {
                    begin.setClickable(true);
                    begin.setText(getResources().getString(R.string.startDrawLine));
                    presentation.setColor(Color.WHITE);
                    presentation.changeMode(id);
                    setDisablePenWriteItem();
                } else {
                    disablePenWrite();
                    presentation.setColor(Color.WHITE);
                    presentation.changeMode(id);
                }
            }
        });

    }

    public void setDisablePenWriteItem() {
        int id = methods.getCheckedRadioButtonId();
        if (id == R.id.drawCenterToBorderLines || id == R.id.drawCenterToCornerLines
                || id == R.id.drawCross || id == R.id.daubScreen) {
            disablePenWrite();
        }
    }

    public void begin(View view) {
        startFullScreenHandwrite();
    }

    public void disablePenWrite() {
        begin.setClickable(false);
        begin.setText(getResources().getString(R.string.cannotDrawLine));
        closeHandWrite();
    }

    public void enablePenWrite() {
        begin.setClickable(true);
        begin.setText(getResources().getString(R.string.startDrawLine));
    }

    public void exit(View view) {
        Log.d("zhouxiangyu", "TouchAreaTest exit");
        disablePenWrite();
        presentation.drawColor(Color.BLACK);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TouchAreaTest.this.finish();
            }
        },500);
    }
}
