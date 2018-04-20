package utils.view;

import android.content.Context;
import android.graphics.PointF;
import android.os.Message;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.MotionEvent;

import com.android.xdftest.LCDControlTest;

/**
 * Created by zhouxiangyu on 2018/3/26.
 */

public class LCDControlView extends AppCompatImageView {

    public static int FINGER_NUM = 1;
    private boolean confirm = false;
    public static boolean left_tested = false;
    public static boolean right_tested = false;
    public static boolean top_tested = false;
    public static boolean bottom_tested = false;
    private PointF[] startPoints;

    public LCDControlView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        calculateDistance(motionEvent);
        return true;
    }

    private void calculateDistance(MotionEvent motionEvent) {
        if (motionEvent.getToolType(motionEvent.getPointerCount() - 1) == MotionEvent.TOOL_TYPE_FINGER) {
            int pointNum = motionEvent.getPointerCount();
            float[] distanceX, distanceY;
            if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN || motionEvent.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
                startPoints = new PointF[pointNum];
                for (int i = 0; i < pointNum; i++) {
                    startPoints[i] = new PointF(motionEvent.getX(i), motionEvent.getY(i));
                }
            }
            if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP) {
                confirm = false;
            }
            if (motionEvent.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {
                confirm = true;
            }
            if (motionEvent.getActionMasked() == MotionEvent.ACTION_MOVE && pointNum == FINGER_NUM) {
                if (!confirm) {
                    distanceX = new float[pointNum];
                    distanceY = new float[pointNum];
                    if(left_tested && right_tested && top_tested && bottom_tested){
                        left_tested = false;
                        right_tested = false;
                        top_tested = false;
                        bottom_tested = false;
                    }
                    for (int i = 0; i < pointNum; i++) {
                        distanceX[i] = motionEvent.getX(i) - startPoints[i].x;
                        distanceY[i] = motionEvent.getY(i) - startPoints[i].y;
                        if (distanceX[i] < 0 && Math.abs(distanceX[i]) >= 400 && Math.abs(distanceY[i]) >= 0 && Math.abs(distanceY[i]) <= 100 && !left_tested) {
                            //left
                            confirm = true;
                            left_tested = true;
                            Message message = new Message();
                            message.what = 1;
                            LCDControlTest.controlTestHandler.handleMessage(message);
                        } else if (distanceX[i] > 0 && Math.abs(distanceX[i]) >= 400 && Math.abs(distanceY[i]) >= 0 && Math.abs(distanceY[i]) <= 100 && !right_tested) {
                            //right
                            confirm = true;
                            right_tested = true;
                            Message message = new Message();
                            message.what = 2;
                            LCDControlTest.controlTestHandler.handleMessage(message);
                        } else if (distanceY[i] < 0 && Math.abs(distanceY[i]) >= 400 && Math.abs(distanceX[i]) >= 0 && Math.abs(distanceX[i]) <= 100 && !top_tested) {
                            //top
                            confirm = true;
                            top_tested = true;
                            Message message = new Message();
                            message.what = 3;
                            LCDControlTest.controlTestHandler.handleMessage(message);
                        } else if (distanceY[i] > 0 && Math.abs(distanceY[i]) >= 400 && Math.abs(distanceX[i]) >= 0 && Math.abs(distanceX[i]) <= 100 && !bottom_tested) {
                            //bottom
                            confirm = true;
                            bottom_tested = true;
                            Message message = new Message();
                            message.what = 4;
                            LCDControlTest.controlTestHandler.handleMessage(message);
                        }
                    }
                }
            }
        }
    }
}
