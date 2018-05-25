package com.android.xdftest;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import utils.BaseActivity;

/**
 * Created by zhouxiangyu on 2017/12/26.
 */

public class ButtonTest extends BaseActivity {
    private TextView textView;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.button_and_text);

        textView = findViewById(R.id.buttonTest);
        textView.setText("点击按键开始");

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if(event.getAction() == KeyEvent.ACTION_UP){
            return true;
        }

        if (event.getKeyCode() == 3) {
            textView.setText("HOME键");
            vibrate();
        } else if (event.getKeyCode() == 4) {
            textView.setText("返回键");
            vibrate();
        } else if (event.getKeyCode() == 521) {
            textView.setText("智能键");
            vibrate();
        } else if (event.getKeyCode() == 24) {
            textView.setText("音量上键");
            vibrate();
        } else if (event.getKeyCode() == 25) {
            textView.setText("音量下键");
            vibrate();
        } else if (event.getKeyCode() == 26) {
            textView.setText("电源键");
            vibrate();
        }
        return super.dispatchKeyEvent(event);
    }

    public void vibrate() {
        if (vibrator != null) {
            vibrator.vibrate(100);
        }
    }

    public void exit(View view) {
        Log.d("zhouxiangyu", "ButtonTest exit");
        MainActivity.enableClick = true;
        this.finish();
    }
}
