package utils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import utils.EinkPresentation;

/**
 * Created by zhouxiangyu on 2018/1/3.
 */

public class DragPictureView extends AppCompatImageView {

    public DragPictureView(Context context) {
        super(context);
    }

    public DragPictureView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public DragPictureView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float positionX = event.getRawX();
        float positionY = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int left = (int) (positionX - getWidth() / 2);
                int top = (int) (positionY - getHeight() / 2);
                int right = (int) (positionX + getWidth() / 2);
                int bottom = (int) (positionY + getHeight() / 2);
                if (left <= 0) {
                    left = 0;
                    right = getWidth();
                }
                if (top <= 0) {
                    top = 0;
                    bottom = getHeight();
                }
                if (right >= EinkPresentation.screenWidth) {
                    right = EinkPresentation.screenWidth;
                    left = EinkPresentation.screenWidth - getWidth();
                }
                if (bottom >= EinkPresentation.screenHeight) {
                    bottom = EinkPresentation.screenHeight;
                    top = EinkPresentation.screenHeight - getHeight();
                }
                layout(left, top, right, bottom);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }

}
