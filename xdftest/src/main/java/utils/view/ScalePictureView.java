package utils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.xdftest.R;

import utils.EinkPresentation;

/**
 * Created by zhouxiangyu on 2018/1/4.
 */

public class ScalePictureView extends AppCompatImageView {

    private float originalDistance;
    private float movingDistance;
    float[] matrixValue = new float[9];
    Matrix matrix = new Matrix();
    Matrix currentMatrix = new Matrix();
    Matrix originalMatrix = new Matrix();
    PointF midPoint;
    PointF startPoint = new PointF();
    boolean Mode_Move = false;
    boolean firstDraw = false;

    public ScalePictureView(Context context) {
        super(context);
        setImageResource(R.mipmap.ic_launcher_round);
        firstDraw = true;
    }

    public ScalePictureView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public ScalePictureView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        setScaleType(ScaleType.MATRIX);
        matrix.setScale(3.0f, 3.0f);
        //108 was pre-get from onDraw
        matrix.postTranslate((EinkPresentation.screenWidth / 2 - 108), (EinkPresentation.screenHeight / 2 - 108));
        originalMatrix.set(matrix);
        this.setImageMatrix(matrix);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //get the original Translate here
//        Log.d("zhouxiangyu",""+getDrawable().getBounds().width());
//        if(firstDraw){
//            matrix.postTranslate((EinkPresentation.screenWidth/2 - getDrawable().getBounds().width()*3/2),
//                    (EinkPresentation.screenHeight/2 - getDrawable().getBounds().height()*3/2));
//            originalMatrix.set(matrix);
//            this.setImageMatrix(matrix);
//            firstDraw = false;
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float positionX = event.getX();
        float positionY = event.getY();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Mode_Move = true;
                startPoint.set(positionX, positionY);
                currentMatrix.set(getImageMatrix());
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Mode_Move = false;
                if (event.getPointerCount() == 2) {
                    originalDistance = distance(event);
                    currentMatrix.set(getImageMatrix());
                    midPoint = midPoint(event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 2 && !Mode_Move) {
                    //scale
                    matrix.getValues(matrixValue);
                    movingDistance = distance(event);
                    float scale = movingDistance / originalDistance;

                    if (matrixValue[Matrix.MSCALE_X] * scale <= 1.0f) {
                        scale = 1.0f / matrixValue[Matrix.MSCALE_X];
                    }
                    if (matrixValue[Matrix.MSCALE_X] * scale >= 9.0f) {
                        scale = 9.0f / matrixValue[Matrix.MSCALE_X];
                    }

                    matrix.set(currentMatrix);
                    matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                    this.setImageMatrix(matrix);
                } else if (Mode_Move) {
                    //move
                    matrix.set(currentMatrix);
                    matrix.postTranslate(positionX - startPoint.x, positionY - startPoint.y);
                    this.setImageMatrix(matrix);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                currentMatrix.set(getImageMatrix());
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    private PointF midPoint(MotionEvent event) {
        float midX = (event.getX(1) + event.getX(0)) / 2;
        float midY = (event.getY(1) + event.getY(0)) / 2;
        return new PointF(midX, midY);
    }

    private float distance(MotionEvent event) {

        float x1 = event.getX();
        float x2 = event.getX(1);
        float y1 = event.getY();
        float y2 = event.getY(1);

        return (float) Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }

    //get range of the picture
//    private void getPicturePosition(){
//        Matrix matrix = getImageMatrix();
//        RectF rectF = new RectF();
//        Drawable drawable = getDrawable();
//        if (drawable != null) {
//            rectF.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//            matrix.mapRect(rectF);
//        }
//    }
}
