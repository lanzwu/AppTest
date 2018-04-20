package utils.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.MotionEvent;

import com.android.xdftest.LCDControlTest;
import com.android.xdftest.MultiTouchTest;

import java.util.ArrayList;

import utils.EinkPresentation;
import utils.TestConstants;

/**
 * Created by zhouxiangyu on 2017/12/25.
 */

public class MultiTouchDrawPathView extends AppCompatImageView {
    Canvas canvas;
    Paint pathPaint, circlePaint;
    Bitmap picture;
    float[] positionX;
    float[] positionY;
    ArrayList<Path> path = new ArrayList<>();

    public MultiTouchDrawPathView(Context context) {
        super(context);
        createPath();
        pathPaint = new Paint();
        pathPaint.setColor(Color.BLACK);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeWidth(5);
        pathPaint.setAntiAlias(true);

        circlePaint = new Paint();
        circlePaint.setColor(Color.BLACK);
        circlePaint.setAntiAlias(true);
    }

    private void createPath() {
        path.clear();
        for (int a = 0; a < 5; a++) {
            path.add(new Path());
        }
    }

    private void readAndSendPositions(int num, float[] x, float[] y, MotionEvent motionEvent) {
        Message message = new Message();
        Bundle bundle = new Bundle();

        if (num > 0) {
            for (int a = 0; a < num; a++) {
                x[a] = motionEvent.getX(a);
                y[a] = motionEvent.getY(a);
            }
            bundle.putFloatArray(TestConstants.POSITION_X, x);
            bundle.putFloatArray(TestConstants.POSITION_Y, y);
        }
        message.arg1 = num;
        message.setData(bundle);
        MultiTouchTest.positionHandler.handleMessage(message);

    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int pointNum = motionEvent.getPointerCount();
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (motionEvent.getToolType(motionEvent.getPointerCount() - 1) == MotionEvent.TOOL_TYPE_FINGER) {
                    positionX = new float[pointNum];
                    positionY = new float[pointNum];
                    readAndSendPositions(pointNum, positionX, positionY, motionEvent);
                } else {
                    cleanScreen();
                    Log.d("zhouxiangyu","pendown");
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (motionEvent.getToolType(motionEvent.getPointerCount() - 1) == MotionEvent.TOOL_TYPE_FINGER) {
                    if (pointNum <= 5) {
                        positionX = new float[pointNum];
                        positionY = new float[pointNum];
                        readAndSendPositions(pointNum, positionX, positionY, motionEvent);
                    }

                    if (pointNum == 5) {
                        picture = Bitmap.createBitmap(EinkPresentation.screenWidth, EinkPresentation.screenHeight, Bitmap.Config.ARGB_8888);
                        canvas = new Canvas(picture);
                        createPath();
                        for (int a = 0; a < pointNum; a++) {
                            canvas.drawCircle(positionX[a], positionY[a], 10, circlePaint);
                            path.get(a).moveTo(positionX[a], positionY[a]);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (motionEvent.getToolType(motionEvent.getPointerCount() - 1) == MotionEvent.TOOL_TYPE_FINGER) {
                    if (pointNum == 5) {
                        for (int a = 0; a < pointNum; a++) {
                            path.get(a).quadTo(positionX[a], positionY[a], motionEvent.getX(a), motionEvent.getY(a));
                            canvas.drawPath(path.get(a), pathPaint);
                            this.setImageBitmap(picture);
                        }
                    }
                    if (pointNum <= 5) {
                        readAndSendPositions(pointNum, positionX, positionY, motionEvent);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                cleanScreen();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;

        }
        return true;
    }

    private void cleanScreen(){
        readAndSendPositions(0, null, null, null);
        picture = Bitmap.createBitmap(EinkPresentation.screenWidth, EinkPresentation.screenHeight, Bitmap.Config.ARGB_8888);
        this.setImageBitmap(picture);
    }
}
