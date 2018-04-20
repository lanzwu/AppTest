package utils.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Vibrator;
import android.support.v7.widget.AppCompatImageView;
import android.view.MotionEvent;
import android.view.View;

import com.android.xdftest.TouchAreaTest;

import utils.EinkPresentation;


/**
 * Created by zhouxiangyu on 2017/12/26.
 */

public class TouchAreaTestView extends AppCompatImageView {

    Context context;
    Paint pathPaint, dashPaint, textPaint, rectPaint, whitePaint, linePaint;
    Canvas canvas;
    Bitmap picture;
    private int screenWidth;
    private int screenHeight;
    Rect hArea, vArea, leftArea, topArea, rightArea, bottomArea;
    boolean startFromCenter = false;

    boolean hasDrawFirstLine = false;
    boolean hasDrawSecondLine = false;
    boolean correctClick = false;
    boolean firstVibrate = false;
    boolean secondVibrate = false;

    Path leftToRightPath = new Path();
    Path rightToLeftPath = new Path();

    Path crossPath = new Path();

    private final int LEFT_TOP_PATH = 0;
    private final int RIGHT_TOP_PATH = 1;
    private final int LEFT_BOTTOM_PATH = 2;
    private final int RIGHT_BOTTOM_PATH = 3;
    private final int CENTER_PATH = 4;

    float x1 = 0f, x2 = 0f, x3 = 0f, x4 = 0f, y1 = 0f, y2 = 0f, y3 = 0f, y4 = 0f;
    float crossX, crossY;

    private final int centerToBorderAreaHalfWidth = 150;
    private final int centerToCornerAreaHalfWidth = 150;
    private final int triggerLength = 50;
    private final int vibrateDuration = 100;
    private final int lineRadius = 10;
    private final int crossRadius = 30;

    public TouchAreaTestView(Context context) {
        super(context);
        this.context = context;

        pathPaint = new Paint();
        pathPaint.setColor(Color.BLACK);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeWidth(3);
        pathPaint.setAntiAlias(true);

        dashPaint = new Paint();
        dashPaint.setAntiAlias(true);
        dashPaint.setColor(Color.BLACK);
        DashPathEffect pathEffect = new DashPathEffect(new float[]{1, 2}, 1);
        dashPaint.setStyle(Paint.Style.STROKE);
        dashPaint.setStrokeWidth(3);
        dashPaint.setPathEffect(pathEffect);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(50);
        textPaint.setStrokeWidth(3);

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setColor(Color.BLACK);

        whitePaint = new Paint();
        whitePaint.setAntiAlias(true);
        whitePaint.setColor(Color.WHITE);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(7);

        screenHeight = EinkPresentation.screenHeight;
        screenWidth = EinkPresentation.screenWidth;

        leftArea = new Rect(0, screenHeight / 2 - centerToBorderAreaHalfWidth, triggerLength, screenHeight / 2 + centerToBorderAreaHalfWidth);
        topArea = new Rect(screenWidth / 2 - centerToBorderAreaHalfWidth, 0, screenWidth / 2 + centerToBorderAreaHalfWidth, triggerLength);
        rightArea = new Rect(screenWidth - triggerLength, screenHeight / 2 - centerToBorderAreaHalfWidth, screenWidth, screenHeight / 2 + centerToBorderAreaHalfWidth);
        bottomArea = new Rect(screenWidth / 2 - centerToBorderAreaHalfWidth, screenHeight - triggerLength, screenWidth / 2 + centerToBorderAreaHalfWidth, screenHeight);

        hArea = new Rect(0, screenHeight / 2 - centerToBorderAreaHalfWidth, screenWidth, screenHeight / 2 + centerToBorderAreaHalfWidth);
        vArea = new Rect(screenWidth / 2 - centerToBorderAreaHalfWidth, 0, screenWidth / 2 + centerToBorderAreaHalfWidth, screenHeight);
    }

    OnTouchListener onTouchListener = new OnTouchListener() {
        float positionX;
        float positionY;
        Path line = new Path();

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (!judgeMode(motionEvent)) {
                return true;
            }
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    positionX = motionEvent.getX();
                    positionY = motionEvent.getY();
                    line.moveTo(positionX, positionY);
                    vibrate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    line.quadTo(positionX, positionY, motionEvent.getX(), motionEvent.getY());
                    canvas.drawPath(line, pathPaint);
                    setPicture(false);
                    positionX = motionEvent.getX();
                    positionY = motionEvent.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    vibrate();
                    break;
            }
            return true;
        }
    };

    public void drawHorizontalLines() {
        newCanvas();
        int numberLines = (int) Math.floor(screenHeight / 30);
        for (int y = 50, i = 1; y <= screenHeight; y = y + numberLines, i++) {
            if (i < 31) {
                Path path = new Path();
                path.moveTo(0, y);
                path.lineTo(screenWidth, y);
                canvas.drawPath(path, dashPaint);
                canvas.drawText(String.valueOf(i), 10, y, textPaint);
                canvas.drawText(String.valueOf(i), screenWidth - 60, y, textPaint);
            }
        }
        this.setImageBitmap(picture);
        this.setOnTouchListener(onTouchListener);
    }

    public void drawVerticalLines() {
        newCanvas();
        int numberLines = (int) Math.floor(screenWidth / 30.5);
        for (int x = 35, i = 1; x <= screenWidth; x = x + numberLines, i++) {
            if (i < 31) {
                Path path = new Path();
                path.moveTo(x, 0);
                path.lineTo(x, screenHeight);
                canvas.drawPath(path, dashPaint);
                canvas.drawText(String.valueOf(i), x, 50, textPaint);
                canvas.drawText(String.valueOf(i), x, screenHeight - 30, textPaint);
            }
        }
        this.setImageBitmap(picture);
        this.setOnTouchListener(onTouchListener);
    }

    public void drawCenterToBorderLines() {
        newCanvas();
        final Path path = new Path();
        path.moveTo(screenWidth / 2 - centerToBorderAreaHalfWidth, 0);
        path.lineTo(screenWidth / 2 - centerToBorderAreaHalfWidth, screenHeight);
        canvas.drawPath(path, dashPaint);

        path.moveTo(screenWidth / 2 + centerToBorderAreaHalfWidth, 0);
        path.lineTo(screenWidth / 2 + centerToBorderAreaHalfWidth, screenHeight);
        canvas.drawPath(path, dashPaint);

        path.moveTo(0, screenHeight / 2 - centerToBorderAreaHalfWidth);
        path.lineTo(screenWidth, screenHeight / 2 - centerToBorderAreaHalfWidth);
        canvas.drawPath(path, dashPaint);

        path.moveTo(0, screenHeight / 2 + centerToBorderAreaHalfWidth);
        path.lineTo(screenWidth, screenHeight / 2 + centerToBorderAreaHalfWidth);
        canvas.drawPath(path, dashPaint);

        path.moveTo(screenWidth / 2, screenHeight / 2);
        path.lineTo(screenWidth / 2 - centerToBorderAreaHalfWidth, screenHeight / 2 + centerToBorderAreaHalfWidth);
        canvas.drawPath(path, dashPaint);
        path.moveTo(screenWidth / 2, screenHeight / 2);
        path.lineTo(screenWidth / 2 + centerToBorderAreaHalfWidth, screenHeight / 2 + centerToBorderAreaHalfWidth);
        canvas.drawPath(path, dashPaint);
        path.moveTo(screenWidth / 2, screenHeight / 2);
        path.lineTo(screenWidth / 2 + centerToBorderAreaHalfWidth, screenHeight / 2 - centerToBorderAreaHalfWidth);
        canvas.drawPath(path, dashPaint);
        path.moveTo(screenWidth / 2, screenHeight / 2);
        path.lineTo(screenWidth / 2 - centerToBorderAreaHalfWidth, screenHeight / 2 - centerToBorderAreaHalfWidth);
        canvas.drawPath(path, dashPaint);

        this.setImageBitmap(picture);
        this.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!judgeMode(motionEvent)) {
                    return true;
                }
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (motionEvent.getX() >= screenWidth / 2 - centerToBorderAreaHalfWidth && motionEvent.getX() <= screenWidth / 2 + centerToBorderAreaHalfWidth
                                && motionEvent.getY() >= screenHeight / 2 - centerToBorderAreaHalfWidth && motionEvent.getY() <= screenHeight / 2 + centerToBorderAreaHalfWidth) {
                            startFromCenter = true;
                            firstVibrate = true;
                            vibrate();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if ((pointInArea(vArea, motionEvent) || pointInArea(hArea, motionEvent)) && startFromCenter) {
                            if (pointInArea(leftArea, motionEvent)) {
                                path.moveTo(screenWidth / 2, screenHeight / 2);
                                path.lineTo(screenWidth / 2 - centerToBorderAreaHalfWidth, screenHeight / 2 - centerToBorderAreaHalfWidth);
                                path.lineTo(screenWidth / 2 - centerToBorderAreaHalfWidth, screenHeight / 2 + centerToBorderAreaHalfWidth);
                                path.close();
                                canvas.drawPath(path, rectPaint);
                                canvas.drawRect(0, screenHeight / 2 - centerToBorderAreaHalfWidth, screenWidth / 2 - centerToBorderAreaHalfWidth, screenHeight / 2 + centerToBorderAreaHalfWidth, rectPaint);
                                setPicture(true);
                                secondVibrate = true;
                            } else if (pointInArea(topArea, motionEvent)) {
                                path.moveTo(screenWidth / 2, screenHeight / 2);
                                path.lineTo(screenWidth / 2 - centerToBorderAreaHalfWidth, screenHeight / 2 - centerToBorderAreaHalfWidth);
                                path.lineTo(screenWidth / 2 + centerToBorderAreaHalfWidth, screenHeight / 2 - centerToBorderAreaHalfWidth);
                                path.close();
                                canvas.drawPath(path, rectPaint);
                                canvas.drawRect(screenWidth / 2 - centerToBorderAreaHalfWidth, 0, screenWidth / 2 + centerToBorderAreaHalfWidth, screenHeight / 2 - centerToBorderAreaHalfWidth, rectPaint);
                                setPicture(true);
                                secondVibrate = true;
                            } else if (pointInArea(rightArea, motionEvent)) {
                                path.moveTo(screenWidth / 2, screenHeight / 2);
                                path.lineTo(screenWidth / 2 + centerToBorderAreaHalfWidth, screenHeight / 2 + centerToBorderAreaHalfWidth);
                                path.lineTo(screenWidth / 2 + centerToBorderAreaHalfWidth, screenHeight / 2 - centerToBorderAreaHalfWidth);
                                path.close();
                                canvas.drawPath(path, rectPaint);
                                canvas.drawRect(screenWidth / 2 + centerToBorderAreaHalfWidth, screenHeight / 2 - centerToBorderAreaHalfWidth, screenWidth, screenHeight / 2 + centerToBorderAreaHalfWidth, rectPaint);
                                setPicture(true);
                                secondVibrate = true;
                            } else if (pointInArea(bottomArea, motionEvent)) {
                                path.moveTo(screenWidth / 2, screenHeight / 2);
                                path.lineTo(screenWidth / 2 - centerToBorderAreaHalfWidth, screenHeight / 2 + centerToBorderAreaHalfWidth);
                                path.lineTo(screenWidth / 2 + centerToBorderAreaHalfWidth, screenHeight / 2 + centerToBorderAreaHalfWidth);
                                path.close();
                                canvas.drawPath(path, rectPaint);
                                canvas.drawRect(screenWidth / 2 - centerToBorderAreaHalfWidth, screenHeight / 2 + centerToBorderAreaHalfWidth, screenWidth / 2 + centerToBorderAreaHalfWidth, screenHeight, rectPaint);
                                setPicture(true);
                                secondVibrate = true;
                            }
                        } else {
                            startFromCenter = false;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        startFromCenter = false;
                        firstVibrate = secondVibrate = false;
                        break;
                }
                //must true
                return true;
            }
        });
    }

    public Path getPath(int p) {
        Path path = new Path();
        switch (p) {
            case LEFT_TOP_PATH:
                path.moveTo(screenWidth, 0);
                path.lineTo(screenWidth, screenHeight);
                path.lineTo(0, screenHeight);
                path.lineTo(0, screenHeight - centerToCornerAreaHalfWidth);
                path.lineTo(screenWidth - centerToCornerAreaHalfWidth, 0);
                path.close();
                path.op(leftToRightPath, Path.Op.REVERSE_DIFFERENCE);
                return path;
            case RIGHT_TOP_PATH:
                path.moveTo(0, 0);
                path.lineTo(0, screenHeight);
                path.lineTo(screenWidth, screenHeight);
                path.lineTo(screenWidth, screenHeight - centerToCornerAreaHalfWidth);
                path.lineTo(centerToCornerAreaHalfWidth, 0);
                path.close();
                path.op(rightToLeftPath, Path.Op.REVERSE_DIFFERENCE);
                return path;
            case LEFT_BOTTOM_PATH:
                path.moveTo(0, 0);
                path.lineTo(screenWidth, 0);
                path.lineTo(screenWidth, screenHeight);
                path.lineTo(screenWidth - centerToCornerAreaHalfWidth, screenHeight);
                path.lineTo(0, centerToCornerAreaHalfWidth);
                path.close();
                path.op(rightToLeftPath, Path.Op.REVERSE_DIFFERENCE);
                return path;
            case RIGHT_BOTTOM_PATH:
                path.moveTo(0, 0);
                path.lineTo(screenWidth, 0);
                path.lineTo(screenWidth, centerToCornerAreaHalfWidth);
                path.lineTo(centerToCornerAreaHalfWidth, screenHeight);
                path.lineTo(0, screenHeight);
                path.close();
                path.op(leftToRightPath, Path.Op.REVERSE_DIFFERENCE);
                return path;
            case CENTER_PATH:
                path.moveTo(0, 0);
                path.lineTo(centerToCornerAreaHalfWidth, 0);
                path.lineTo(screenWidth, screenHeight - centerToCornerAreaHalfWidth);
                path.lineTo(screenWidth, screenHeight);
                path.lineTo(screenWidth - centerToCornerAreaHalfWidth, screenHeight);
                path.lineTo(0, centerToCornerAreaHalfWidth);
                path.close();
                path.op(rightToLeftPath, Path.Op.INTERSECT);
                return path;
        }
        return null;
    }

    public boolean pointInPathArea(int x, int y, Path path) {
        RectF r = new RectF();
        Region region = new Region();
        path.computeBounds(r, true);
        region.setPath(path, new Region((int) r.left, (int) r.top, (int) r.right, (int) r.bottom));
        return region.contains(x, y);
    }

    public void drawCenterToCornerAreas() {
        newCanvas();
        RectF r = new RectF();
        final Region totalRegion = new Region();
        final Path totalPath = new Path();

        leftToRightPath.moveTo(0, 0);
        leftToRightPath.lineTo(centerToCornerAreaHalfWidth, 0);
        leftToRightPath.lineTo(screenWidth, screenHeight - centerToCornerAreaHalfWidth);
        leftToRightPath.lineTo(screenWidth, screenHeight);
        leftToRightPath.lineTo(screenWidth - centerToCornerAreaHalfWidth, screenHeight);
        leftToRightPath.lineTo(0, centerToCornerAreaHalfWidth);
        leftToRightPath.close();

        rightToLeftPath.moveTo(screenWidth, 0);
        rightToLeftPath.lineTo(screenWidth, centerToCornerAreaHalfWidth);
        rightToLeftPath.lineTo(centerToCornerAreaHalfWidth, screenHeight);
        rightToLeftPath.lineTo(0, screenHeight);
        rightToLeftPath.lineTo(0, screenHeight - centerToCornerAreaHalfWidth);
        rightToLeftPath.lineTo(screenWidth - centerToCornerAreaHalfWidth, 0);
        rightToLeftPath.close();

        totalPath.addPath(leftToRightPath);
        totalPath.addPath(rightToLeftPath);

        totalPath.computeBounds(r, true);
        totalRegion.setPath(totalPath, new Region((int) r.left, (int) r.top, (int) r.right, (int) r.bottom));

        canvas.drawPath(totalPath, dashPaint);

        this.setImageBitmap(picture);
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int x = (int) motionEvent.getX();
                int y = (int) motionEvent.getY();
                if (!judgeMode(motionEvent)) {
                    return true;
                }
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (pointInPathArea(x, y, getPath(CENTER_PATH))) {
                            startFromCenter = true;
                            canvas.drawPath(getPath(CENTER_PATH), rectPaint);
                            firstVibrate = true;
                            setPicture(true);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (pointInPathArea(x, y, totalPath) && startFromCenter) {
                            if (x >= 0 && x <= triggerLength && y >= 0 && y <= triggerLength) {
                                canvas.drawPath(getPath(LEFT_TOP_PATH), rectPaint);
                                canvas.drawPath(getPath(CENTER_PATH), whitePaint);
                                setPicture(true);
                                secondVibrate = true;
                            } else if (x >= screenWidth - triggerLength && x <= screenWidth && y >= 0 && y <= triggerLength) {
                                canvas.drawPath(getPath(RIGHT_TOP_PATH), rectPaint);
                                canvas.drawPath(getPath(CENTER_PATH), whitePaint);
                                setPicture(true);
                                secondVibrate = true;
                            } else if (x >= 0 && x <= triggerLength && y >= screenHeight - triggerLength && y <= screenHeight) {
                                canvas.drawPath(getPath(LEFT_BOTTOM_PATH), rectPaint);
                                canvas.drawPath(getPath(CENTER_PATH), whitePaint);
                                setPicture(true);
                                secondVibrate = true;
                            } else if (x >= screenWidth - triggerLength && x <= screenWidth && y >= screenHeight - triggerLength && y <= screenHeight) {
                                canvas.drawPath(getPath(RIGHT_BOTTOM_PATH), rectPaint);
                                canvas.drawPath(getPath(CENTER_PATH), whitePaint);
                                setPicture(true);
                                secondVibrate = true;
                            }
                        } else {
                            startFromCenter = false;
                            canvas.drawPath(getPath(CENTER_PATH), whitePaint);
                            firstVibrate = secondVibrate = true;
                            setPicture(true);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        startFromCenter = false;
                        canvas.drawPath(getPath(CENTER_PATH), whitePaint);
                        firstVibrate = secondVibrate = true;
                        setPicture(true);
                        firstVibrate = secondVibrate = false;
                        break;
                }
                return true;
            }
        });
    }

    public void drawCrossAndClick() {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float pointX = motionEvent.getX();
                float pointY = motionEvent.getY();
                if (!judgeMode(motionEvent)) {
                    return true;
                }
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (hasDrawFirstLine && hasDrawSecondLine && !correctClick) {
                            if (pointInPathArea((int) pointX, (int) pointY, crossPath)) {
                                hasDrawFirstLine = hasDrawSecondLine = false;
                                newCanvas();
                                setPicture(true);
                                correctClick = true;
                                return true;
                            }
                        }
                        if (!hasDrawFirstLine) {
                            newCanvas();
                            x1 = pointX;
                            y1 = pointY;
                            canvas.drawCircle(x1, y1, lineRadius, rectPaint);
                            setPicture(true);
                        } else if (!hasDrawSecondLine) {
                            x3 = pointX;
                            y3 = pointY;
                            canvas.drawCircle(x3, y3, lineRadius, rectPaint);
                            setPicture(true);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (correctClick) {
                            correctClick = false;
                            return true;
                        }
                        if (!hasDrawFirstLine) {
                            x2 = pointX;
                            y2 = pointY;
                            canvas.drawLine(x1, y1, pointX, pointY, linePaint);
                            canvas.drawCircle(x2, y2, lineRadius, rectPaint);
                            setPicture(true);
                            hasDrawFirstLine = true;
                        } else if (!hasDrawSecondLine) {
                            x4 = pointX;
                            y4 = pointY;
                            canvas.drawLine(x3, y3, pointX, pointY, linePaint);
                            canvas.drawCircle(x4, y4, lineRadius, rectPaint);
                            setPicture(true);
                            hasDrawSecondLine = true;
                        }

                        if (hasDrawFirstLine && hasDrawSecondLine) {
                            crossX = ((x1 - x2) * (x3 * y4 - x4 * y3) - (x3 - x4) * (x1 * y2 - x2 * y1))
                                    / ((x3 - x4) * (y1 - y2) - (x1 - x2) * (y3 - y4));
                            crossY = ((y1 - y2) * (x3 * y4 - x4 * y3) - (x1 * y2 - x2 * y1) * (y3 - y4))
                                    / ((y1 - y2) * (x3 - x4) - (x1 - x2) * (y3 - y4));
                            if (crossX >= 0 && crossX <= screenWidth && crossY >= 0 && crossY <= screenHeight) {
                                canvas.drawCircle(crossX, crossY, crossRadius, rectPaint);
                                canvas.drawLine(x2, y2, crossX, crossY, dashPaint);
                                canvas.drawLine(x4, y4, crossX, crossY, dashPaint);
                                setPicture(false);
                                crossPath.reset();
                                crossPath.addCircle(crossX, crossY, crossRadius, Path.Direction.CW);
                            } else {
                                hasDrawFirstLine = hasDrawSecondLine = false;
                                newCanvas();
                                setPicture(true);
                            }

                        }
                        break;
                }
                return true;
            }
        });
    }

    public void daubScreen() {
        newCanvas();
        pathPaint.setStrokeWidth(400);
        this.setOnTouchListener(onTouchListener);
    }

    public void newCanvas() {
        picture = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(picture);
    }

    public void setPicture(boolean needVibrate) {
        this.setImageBitmap(picture);
        if (needVibrate) {
            vibrate();
        }
    }

    public boolean pointInArea(Rect area, MotionEvent motionEvent) {
        return motionEvent.getX() >= area.left && motionEvent.getX() <= area.right && motionEvent.getY() >= area.top && motionEvent.getY() <= area.bottom;
    }

    public boolean judgeMode(MotionEvent motionEvent) {
        if (motionEvent.getToolType(0) == MotionEvent.TOOL_TYPE_STYLUS && TouchAreaTest.modePen) {
            return true;
        } else {
            return motionEvent.getToolType(0) == MotionEvent.TOOL_TYPE_FINGER && !TouchAreaTest.modePen;
        }
    }

    public void vibrate() {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (!firstVibrate || !secondVibrate && startFromCenter) {
            if (vibrator != null) {
                vibrator.vibrate(vibrateDuration);
            }
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
