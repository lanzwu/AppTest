package utils.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;

import com.android.xdftest.HandWriteTest;

import org.w3c.dom.Node;

import java.util.ArrayList;

import utils.EinkPresentation;
import utils.TestConstants;

/**
 * Created by zhouxiangyu on 2017/12/29.
 */

public class HandWriteCageView extends AppCompatImageView {

    private int screenWidth;
    private int screenHeight;
    int marginLeft;
    int marginTop;
    Canvas canvas;
    Bitmap picture;
    Paint paint;
    Rect cage5x5, cage7x7;
    Message message = new Message();
    Bundle bundle = new Bundle();

    public HandWriteCageView(Context context) {
        super(context);
        screenHeight = EinkPresentation.screenHeight;
        screenWidth = EinkPresentation.screenWidth;

        marginLeft = screenWidth / 3;
        marginTop = screenHeight / 4;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);

        cage5x5 = new Rect(marginLeft, marginTop, marginLeft + 50, marginTop + 50);
        cage7x7 = new Rect(marginLeft, marginTop, marginLeft + 70, marginTop + 70);

        newCanvas();
    }


    public void draw5x5Cages() {
        newCanvas();
        int num = 0;
        for (int i = 0; i < screenWidth - marginLeft; i = i + 130) {
            for (int j = 0; j < screenHeight - marginTop; j = j + 150) {
                if ((cage5x5.top + j) <= (screenHeight / 4 * 3) && (cage5x5.right + i) <= (screenWidth / 3 * 2)) {
                    canvas.drawRect(cage5x5.left + i, cage5x5.top + j, cage5x5.right + i, cage5x5.bottom + j, paint);
                    bundle.putIntArray(TestConstants.CAGE, new int[]{cage5x5.left + i - 5, cage5x5.top + j - 5, cage5x5.right + i + 5, cage5x5.bottom + j + 5});
                    message.setData(bundle);
                    num++;
                    message.what = num;
                    HandWriteTest.handWriteTestHandler.handleMessage(message);
                }
            }
        }
        setPicture();
    }

    public void draw7x7Cages() {
        newCanvas();
        int num = 0;
        for (int i = 0; i < screenWidth - marginLeft; i = i + 130) {
            for (int j = 0; j < screenHeight - marginTop; j = j + 150) {
                if ((cage7x7.top + j) <= (screenHeight / 4 * 3) && (cage7x7.right + i) <= (screenWidth / 3 * 2)) {
                    canvas.drawRect(cage7x7.left + i, cage7x7.top + j, cage7x7.right + i, cage7x7.bottom + j, paint);
                    bundle.putIntArray(TestConstants.CAGE, new int[]{cage7x7.left + i - 10, cage7x7.top + j - 10, cage7x7.right + i + 10, cage7x7.bottom + j + 10});
                    message.setData(bundle);
                    num++;
                    message.what = num;
                    HandWriteTest.handWriteTestHandler.handleMessage(message);
                }
            }
        }
        setPicture();
    }

    public void setPicture() {
        this.setImageBitmap(picture);
    }

    public void newCanvas() {
        picture = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(picture);
    }
}
