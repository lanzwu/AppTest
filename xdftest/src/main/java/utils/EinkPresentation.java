package utils;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;

import com.android.internal.widget.PointerLocationView;
import com.android.xdftest.CrossTest;
import com.android.xdftest.PlayVideoTest;
import com.android.xdftest.R;

import java.util.Timer;
import java.util.TimerTask;

import utils.gif.GifView;
import utils.view.DrawLineView;
import utils.view.HandWriteCageView;
import utils.view.LCDControlView;
import utils.view.MultiTouchDrawPathView;
import utils.view.PenTestView;
import utils.view.ScalePictureView;
import utils.view.TouchAreaTestView;

/**
 * Created by zhouxiangyu on 2017/12/22.
 */

public class EinkPresentation extends Presentation implements Dismiss {
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    public static int screenWidth;
    public static int screenHeight;
    private Context context;
    private int i = 0;
    private Timer timer;
    private TimerTask changeTask;

    EinkPresentation(Context outerContext, Display display) {
        super(outerContext, display);
        this.context = outerContext;
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        screenHeight = outMetrics.heightPixels;
        if (getWindow() != null) {
            this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        } else {
            Log.e("zhouxiangyu", "EinkPresentation getWindow is null !");
        }
    }

    @Override
    public void clearDialog() {
        dismiss();
    }

    public void drawColor(final int color) {
        //去除了surfaceHolder==null的判断
        setContentView(R.layout.test_surface_view);
        SurfaceView surfaceView = findViewById(R.id.showPicture);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                canvas = surfaceHolder.lockCanvas();
                canvas.drawColor(color);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });

    }

    public void setGrayLevel() {
        setContentView(R.layout.gray_level_16);
    }

    public void setPicture() {
        setContentView(R.layout.epd_rotate);
    }

    public void setPictures(final int[] res) {
        setContentView(R.layout.test_surface_view);
        final SurfaceView surfaceView = findViewById(R.id.showPicture);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                canvas = surfaceHolder.lockCanvas();
                surfaceHolder.unlockCanvasAndPost(canvas);
                surfaceView.callOnClick();

//                canvas = surfaceHolder.lockCanvas();
//                Bitmap picture = BitmapFactory.decodeResource(getResources(), res[1]);
//                picture = setMatrix(picture);
//                canvas.drawBitmap(picture, 0, 0, null);
//                surfaceHolder.unlockCanvasAndPost(canvas);
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });
        surfaceView.setOnClickListener(new View.OnClickListener() {
            int index = 0;
            int num = res.length;
            Bitmap picture;

            @Override
            public void onClick(View view) {
                index++;
                if (index < num) {
                    canvas = surfaceHolder.lockCanvas();
                    picture = BitmapFactory.decodeResource(getResources(), res[index]);
                    picture = setMatrix(picture);
                    canvas.drawBitmap(picture, 0, 0, null);
                    surfaceHolder.unlockCanvasAndPost(canvas);
                } else {
                    index = 0;
                    canvas = surfaceHolder.lockCanvas();
                    picture = BitmapFactory.decodeResource(getResources(), res[index]);
                    picture = setMatrix(picture);
                    canvas.drawBitmap(picture, 0, 0, null);
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        });

    }

    private Bitmap setMatrix(Bitmap bitmap) {
        return Bitmap.createScaledBitmap(bitmap, screenWidth, screenHeight, true);
    }

    public void setCross() {
        setContentView(R.layout.test_surface_view);
        SurfaceView surfaceView = findViewById(R.id.showPicture);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                timer = new Timer();
                timer.schedule(crossTask, 200, 800);
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });
    }

    private TimerTask crossTask = new TimerTask() {
        private int CROSS_TIMES = 5;

        @Override
        public void run() {
            if (i == CROSS_TIMES) {
                crossTask.cancel();
                timer.cancel();
                i = 0;
                Message message = new Message();
                message.what = 0;
                CrossTest.handler.handleMessage(message);
                return;
            }
            if (i % 2 == 0) {
                canvas = surfaceHolder.lockCanvas();
                Bitmap cross_b = BitmapFactory.decodeResource(getResources(), R.mipmap.cross_b);
                cross_b = setMatrix(cross_b);
                canvas.drawBitmap(cross_b, 0, 0, null);
                surfaceHolder.unlockCanvasAndPost(canvas);
            } else {
                canvas = surfaceHolder.lockCanvas();
                Bitmap cross_w = BitmapFactory.decodeResource(getResources(), R.mipmap.cross_w);
                cross_w = setMatrix(cross_w);
                canvas.drawBitmap(cross_w, 0, 0, null);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
            i++;
        }
    };

    public void multiTouch() {
        //MultiTouchDrawPathView pathView = new MultiTouchDrawPathView(context);
        PointerLocationView pointerLocationView = new PointerLocationView(context);
        setContentView(pointerLocationView);
    }

    public void drawLine(String name) {
        DrawLineView pathView = new DrawLineView(context, name);
        setContentView(pathView);
    }

    public void changeMode(int id) {
        TouchAreaTestView areaView = new TouchAreaTestView(context);
        switch (id) {
            case R.id.drawHLines:
                areaView.drawHorizontalLines();
                break;
            case R.id.drawVLines:
                areaView.drawVerticalLines();
                break;
            case R.id.drawCenterToBorderLines:
                areaView.drawCenterToBorderLines();
                break;
            case R.id.drawCenterToCornerLines:
                areaView.drawCenterToCornerAreas();
                break;
            case R.id.drawCross:
                areaView.drawCrossAndClick();
                break;
            case R.id.daubScreen:
                areaView.daubScreen();
                break;
        }
        setContentView(areaView);
    }

    public void drawCages(String size) {
        HandWriteCageView cageView = new HandWriteCageView(context);
        switch (size) {
            case TestConstants.DRAW_5x5_CAGES:
                cageView.draw5x5Cages();
                break;
            case TestConstants.DRAW_7x7_CAGES:
                cageView.draw7x7Cages();
                break;
        }
        setContentView(cageView);
    }

    public void LCDControl() {
        LCDControlView lcdControlView = new LCDControlView(context);
        setContentView(lcdControlView);
    }

    public void showText() {
        setContentView(R.layout.show_text_test);
    }

    public void slidePage() {
        setContentView(R.layout.eink_function_text);
        final ScrollView scrollView = findViewById(R.id.textScrollView);
        Button next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    public void clickButton() {
        setContentView(R.layout.eink_function_clickbutton);
        final Button lt_btn = findViewById(R.id.lt_btn);
        final Button rt_btn = findViewById(R.id.rt_btn);
        final Button rb_btn = findViewById(R.id.rb_btn);
        final Button lb_btn = findViewById(R.id.lb_btn);
        final Button center_again_btn = findViewById(R.id.center_again_btn);
        rt_btn.setVisibility(View.INVISIBLE);
        rb_btn.setVisibility(View.INVISIBLE);
        lb_btn.setVisibility(View.INVISIBLE);
        center_again_btn.setVisibility(View.INVISIBLE);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.lt_btn:
                        lt_btn.setVisibility(View.INVISIBLE);
                        rt_btn.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rt_btn:
                        rt_btn.setVisibility(View.INVISIBLE);
                        rb_btn.setVisibility(View.VISIBLE);
                        break;
                    case R.id.rb_btn:
                        rb_btn.setVisibility(View.INVISIBLE);
                        lb_btn.setVisibility(View.VISIBLE);
                        break;
                    case R.id.lb_btn:
                        lb_btn.setVisibility(View.INVISIBLE);
                        center_again_btn.setVisibility(View.VISIBLE);
                        break;
                    case R.id.center_again_btn:
                        lt_btn.setVisibility(View.VISIBLE);
                        rt_btn.setVisibility(View.INVISIBLE);
                        rb_btn.setVisibility(View.INVISIBLE);
                        lb_btn.setVisibility(View.INVISIBLE);
                        center_again_btn.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        };
        lt_btn.setOnClickListener(listener);
        rt_btn.setOnClickListener(listener);
        rb_btn.setOnClickListener(listener);
        lb_btn.setOnClickListener(listener);
        center_again_btn.setOnClickListener(listener);
    }

    public void autoChangePicture(final int[] res, boolean stop, final int period) {
        if (!stop) {
            setContentView(R.layout.test_surface_view);
            SurfaceView surfaceView = findViewById(R.id.showPicture);
            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    timer = new Timer();
                    timer.schedule(changeTask, 800, period);
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

                }
            });

            changeTask = new TimerTask() {
                int index = 0;
                int num = res.length;

                @Override
                public void run() {
                    index++;
                    if (index < num) {
                        canvas = surfaceHolder.lockCanvas();
                        Bitmap cross_b = BitmapFactory.decodeResource(getResources(), res[index]);
                        cross_b = setMatrix(cross_b);
                        if (canvas != null) {
                            canvas.drawBitmap(cross_b, 0, 0, null);
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    } else {
                        index = 0;
                        canvas = surfaceHolder.lockCanvas();
                        Bitmap cross_b = BitmapFactory.decodeResource(getResources(), res[index]);
                        cross_b = setMatrix(cross_b);
                        if (canvas != null) {
                            canvas.drawBitmap(cross_b, 0, 0, null);
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }
                }
            };

        } else {
            if (timer != null) {
                changeTask.cancel();
                timer.cancel();
            }
        }
    }

    public void dragPicture() {
        setContentView(R.layout.drag_picture_test);
    }

    public void scalePicture() {
        ScalePictureView scalePictureView = new ScalePictureView(context);
        setContentView(scalePictureView);
    }

    public void showGif() {
        setContentView(R.layout.show_gif_view);
        final GifView gifView = findViewById(R.id.showGif);
        gifView.setGifImage(R.mipmap.smoke);
        gifView.setGifImageType(GifView.GifImageType.COVER);
        gifView.showAnimation();
        gifView.setOnClickListener(new View.OnClickListener() {
            boolean stop = true;

            @Override
            public void onClick(View view) {
                if (stop) {
                    gifView.showCover();
                    stop = false;
                } else {
                    gifView.showAnimation();
                    stop = true;
                }
            }
        });
    }

    public void initVideoView() {
        setContentView(R.layout.play_video);
        //Vitamio.isInitialized(context);
        PlayVideoTest.vitamio = findViewById(R.id.vitamio);
    }

    public void playVideo(final String path) {
        PlayVideoTest.vitamio.setVideoPath(path);
        PlayVideoTest.vitamio.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putLong(TestConstants.DURATION, PlayVideoTest.vitamio.getDuration());
                message.setData(bundle);
                message.what = 0;
                PlayVideoTest.handler.handleMessage(message);
                mp.setLooping(true);
            }
        });
    }

    public void showEMR() {
        setContentView(R.layout.emr_test);
    }

    public void penTest(Context context) {
        PenTestView view = new PenTestView(context);
        setContentView(view);
    }
}
