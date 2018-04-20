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
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.android.xdftest.HandWriteEffectTest;
import com.android.xdftest.PressureTest;

import java.util.Locale;

import utils.EinkPresentation;

/**
 * Created by zhouxiangyu on 2017/12/26.
 */

public class DrawLineView extends AppCompatImageView {
    long time = 0;
    float pressure = 0f;
    Context context;
    String name;
    Canvas canvas;
    Bitmap picture;
    Paint pathPaint;
    float positionX = 0;
    float positionY = 0;
    Path line = new Path();

    public DrawLineView(Context context) {
        super(context);
    }

    public DrawLineView(Context context, String name) {
        super(context);
        this.context = context;
        this.name = name;
        picture = Bitmap.createBitmap(EinkPresentation.screenWidth, EinkPresentation.screenHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(picture);
        this.setImageBitmap(picture);

        if (name.equals("PressureTest")) {
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("pressure", String.format(Locale.US, "%.3f", pressure));
            message.setData(bundle);
            PressureTest.handler.handleMessage(message);
        }

        pathPaint = new Paint();
        pathPaint.setColor(Color.BLACK);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeWidth(3);
        pathPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(line, pathPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Message message = new Message();
        Bundle bundle = new Bundle();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (name.equals("HandWriteEffectTest")) {
                    time = System.nanoTime();
                    if(event.getToolType(0) == MotionEvent.TOOL_TYPE_FINGER) {
                        positionX = event.getX();
                        positionY = event.getY();
                        line.moveTo(positionX, positionY);
                    }
                } else {
                    if(event.getToolType(0) == MotionEvent.TOOL_TYPE_STYLUS) {
                        pressure = event.getPressure();
                        bundle.putString("pressure", String.format(Locale.US, "%.3f", pressure));
                        message.setData(bundle);
                        PressureTest.handler.handleMessage(message);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (name.equals("HandWriteEffectTest")) {
                    if(event.getToolType(0) == MotionEvent.TOOL_TYPE_FINGER) {
                        line.quadTo(positionX, positionY, event.getX(), event.getY());
                        positionX = event.getX();
                        positionY = event.getY();
                        postInvalidate();
                    }
                    double delta = (System.nanoTime() - time) / 1000000000.0;
                    bundle.putString("time", String.format(Locale.US, "%.2f", delta) + "s");
                    message.setData(bundle);
                    HandWriteEffectTest.handler.handleMessage(message);
                } else if (name.equals("PressureTest")) {
                    if(event.getToolType(0) == MotionEvent.TOOL_TYPE_STYLUS) {
                        pressure = event.getPressure();
                        bundle.putString("pressure", String.format(Locale.US, "%.3f", pressure));
                        message.setData(bundle);
                        PressureTest.handler.handleMessage(message);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (name.equals("PressureTest")) {
                    bundle.putString("pressure", String.format(Locale.US, "%.3f", 0f));
                    message.setData(bundle);
                    PressureTest.handler.handleMessage(message);
                }
                break;
        }
        return true;
    }
}
