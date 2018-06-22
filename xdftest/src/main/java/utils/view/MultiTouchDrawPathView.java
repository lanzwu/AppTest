package utils.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;

import com.android.internal.widget.PointerLocationView;
import com.android.xdftest.MultiTouchTest;

import java.util.ArrayList;

import utils.EinkPresentation;
import utils.TestConstants;

/**
 * Created by zhouxiangyu on 2017/12/25.
 */

public class MultiTouchDrawPathView extends PointerLocationView {

    float[] positionX;
    float[] positionY;
    boolean canMove = true;

    public MultiTouchDrawPathView(Context context) {
        super(context);
    }

    private void readAndSendPositions(int num, float[] x, float[] y, MotionEvent motionEvent) {
        Message message = new Message();
        Bundle bundle = new Bundle();

        if (x != null && num > 0 && num == x.length) {
            for (int a = 0; a < num; a++) {
                x[a] = motionEvent.getX(a);
                y[a] = motionEvent.getY(a);
            }
            bundle.putFloatArray(TestConstants.POSITION_X, x);
            bundle.putFloatArray(TestConstants.POSITION_Y, y);
        }
        message.arg1 = num;
        message.setData(bundle);
        //MultiTouchTest.positionHandler.handleMessage(message);

    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int pointNum = motionEvent.getPointerCount();
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if(!isFocused()){
                    requestFocus();
                }
                if (isAllPointersFinger(motionEvent)) {
                    positionX = new float[pointNum];
                    positionY = new float[pointNum];
                    readAndSendPositions(pointNum, positionX, positionY, motionEvent);
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (isAllPointersFinger(motionEvent)) {
                    positionX = new float[pointNum];
                    positionY = new float[pointNum];
                    readAndSendPositions(pointNum, positionX, positionY, motionEvent);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isAllPointersFinger(motionEvent)) {
                    readAndSendPositions(pointNum, positionX, positionY, motionEvent);
                }
                break;
            case MotionEvent.ACTION_UP:
                readAndSendPositions(0, null, null, motionEvent);
                break;
        }
        return true;
    }

    private boolean isAllPointersFinger(MotionEvent event) {
        boolean result = true;
        for (int i = 0; i < event.getPointerCount(); i++) {
            result = result && (event.getToolType(i) == MotionEvent.TOOL_TYPE_FINGER);
        }
        return result;
    }
}
