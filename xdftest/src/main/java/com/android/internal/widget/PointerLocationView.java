package com.android.internal.widget;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManagerPolicy.PointerEventListener;

/**
 * Created by zhouxiangyu on 2018/5/21.
 */

public class PointerLocationView extends View implements PointerEventListener {
    public PointerLocationView(Context context){
        super(context);
    }

    @Override
    public void onPointerEvent(MotionEvent motionEvent) {

    }
}
