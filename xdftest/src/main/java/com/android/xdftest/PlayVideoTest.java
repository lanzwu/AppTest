package com.android.xdftest;

import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.xdftest.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


import utils.BaseActivity;
import utils.TestConstants;

/**
 * Created by zhouxiangyu on 2018/1/6.
 */

public class PlayVideoTest extends BaseActivity {
    public static VideoView vitamio;
    public static PlayVideoHandler handler;
    private ArrayList<Item> nameList = new ArrayList<>();
    private ImageButton startBtn;
    private TextView currentTime, endTime;
    private SeekBar timeSeekBar, volumeSeekBar;
    private long duration = 0;
    private int progress = 0;
    private AudioManager audioManager;
    private Timer timer;
    private boolean isTracking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_list);
        enableBackBtn(true);

        handler = new PlayVideoHandler(this);
        currentTime = findViewById(R.id.currentTime);
        endTime = findViewById(R.id.endTime);
        ListView videoList = findViewById(R.id.videoList);
        VideoListAdapter adapter = new VideoListAdapter();

        copyFilesFromRawToSdcard(this, R.raw.pacific, "pacific.mp4");
        //copyFilesFromRawToSdcard(this, R.raw.wildlife, "wildlife.wmv");

        File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
        initFiles(root);
        videoList.setAdapter(adapter);

        showPresentation();
        refresh();
        presentation.initVideoView();

        startBtn = findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vitamio != null && vitamio.isPlaying()) {
                    view.setBackgroundResource(R.mipmap.play);
                    vitamio.pause();
                } else if (vitamio != null) {
                    vitamio.start();
                    view.setBackgroundResource(R.mipmap.pause);
                }

            }
        });

        timeSeekBar = findViewById(R.id.timeSeekBar);
        timeSeekBar.setMax(100);
        timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTracking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //vitamio.seekTo((long) ((double) progress / 100 * duration));
                vitamio.seekTo((int) ((double) progress / 100 * duration));
                isTracking = false;
                startBtn.setBackgroundResource(R.mipmap.pause);
            }
        });

        volumeSeekBar = findViewById(R.id.volumeSeekBar);
        if (initManager()) {
            int current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            volumeSeekBar.setMax(maxVolume);
            volumeSeekBar.setProgress(current);
        }
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (audioManager != null) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        isAllowBtnAndSeekBar(false);
    }

    private boolean initManager() {
        audioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        return audioManager != null;
    }

    private void copyFilesFromRawToSdcard(Context context, int id, String fileName) {
        InputStream inputStream = context.getResources().openRawResource(id);
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + fileName);
        if (file.exists()){
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, length);
            }
            fos.flush();
            fos.close();
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initFiles(File root) {
        File[] files = root.listFiles();
        for (File file : files) {
            if (isFormatsSupported(file.getName())) {
                nameList.add(new Item(file.getAbsolutePath(), file.getName()));
            }
        }
    }

    public boolean isFormatsSupported(String name) {
//        if (name.contains("flv") || name.contains("mp4") ||
//                name.contains("3gp") || name.contains("avi")
//                || name.contains("mov") || name.contains("rmvb")
//                || name.contains("mpeg") || name.contains("mpg")
//                || name.contains("wmv")) {
//            return true;
//        }

        return name.contains("mp4");
    }

    private void isAllowBtnAndSeekBar(boolean isAllow) {
        startBtn.setEnabled(isAllow);
        timeSeekBar.setEnabled(isAllow);
    }

    private class Item {
        private String path;
        private String name;

        private Item(String path, String name) {
            this.path = path;
            this.name = name;
        }

    }

    private class VideoListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return nameList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(PlayVideoTest.this).inflate(R.layout.video_list_item, null);
                holder = new ViewHolder();
                holder.videoName = convertView.findViewById(R.id.videoName);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.videoName.setText(nameList.get(position).name);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presentation.playVideo(nameList.get(position).path);
                    startBtn.setBackgroundResource(R.mipmap.pause);
                    isAllowBtnAndSeekBar(true);
                }
            });

            return convertView;
        }

        class ViewHolder {
            TextView videoName;
        }
    }

    private TimerTask timeTask = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.handleMessage(message);
        }
    };

    public class PlayVideoHandler extends Handler {
        private final WeakReference<PlayVideoTest> mActivity;

        private PlayVideoHandler(PlayVideoTest activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(final Message msg) {
            PlayVideoTest activity = mActivity.get();
            if (activity != null) {
                if (msg.what == 0) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            duration = msg.getData().getLong(TestConstants.DURATION);
                            endTime.setText(getDuration(duration));
                            if (timer == null) {
                                timer = new Timer();
                                timer.schedule(timeTask, 0, 100);
                            }
                        }
                    });
                } else if (msg.what == 1) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            currentTime.setText(getDuration(vitamio.getCurrentPosition()));
                            if (!isTracking) {
                                timeSeekBar.setProgress(getProgress(vitamio.getCurrentPosition(), duration));
                            }
                        }
                    });
                }
            }
        }
    }

    private String getDuration(long milliSecondTime) {

        long hour = milliSecondTime / (60 * 60 * 1000);
        long minute = (milliSecondTime - hour * 60 * 60 * 1000) / (60 * 1000);
        int seconds = (int) Math.ceil((milliSecondTime - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000);

        if (seconds >= 60) {
            seconds = seconds % 60;
            minute += seconds / 60;
        }
        if (minute >= 60) {
            minute = minute % 60;
            hour += minute / 60;
        }

        String sh = "";
        String sm = "";
        String ss = "";
        if (hour < 10) {
            sh = "0" + String.valueOf(hour);
        } else {
            sh = String.valueOf(hour);
        }
        if (minute < 10) {
            sm = "0" + String.valueOf(minute);
        } else {
            sm = String.valueOf(minute);
        }
        if (seconds < 10) {
            ss = "0" + String.valueOf(seconds);
        } else {
            ss = String.valueOf(seconds);
        }

        return sh + ":" + sm + ":" + ss;
    }

    private int getProgress(long current, long duration) {
        return (int) (((float) current / duration) * 100);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                presentation.drawColor(Color.BLACK);
                break;
            default:
                break;
        }
        return super.dispatchKeyEvent(event);
    }
}
