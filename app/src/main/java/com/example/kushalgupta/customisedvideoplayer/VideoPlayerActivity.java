package com.example.kushalgupta.customisedvideoplayer;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by kushalgupta on 04/04/18.
 */
public class VideoPlayerActivity extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {

    SurfaceView videoSurface;
    MediaPlayer player;
    VideoControllerView controller;
    TextView dura;
    private ProgressBar screenProgress;
    private Boolean mDragging;
    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    private int remainingTime;
    int screenTime;
CountDownTimer countDownTimer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        dura = findViewById(R.id.duration);

        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        SurfaceHolder videoHolder = videoSurface.getHolder();
        videoHolder.addCallback(this);

        player = new MediaPlayer();
        controller = new VideoControllerView(this);



//        screenProgress = (ProgressBar) findViewById(R.id.activity_seekbar);
//        if (screenProgress != null) {
//            if (screenProgress instanceof SeekBar) {
//                SeekBar seeker = (SeekBar) screenProgress;
//                seeker.setOnSeekBarChangeListener(mSeekListener);
//            }
//            screenProgress.setMax(1000);
//        }
//
//        mFormatBuilder = new StringBuilder();
//        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

        try {

            player.setAudioStreamType(AudioManager.STREAM_MUSIC);

           player.setDataSource(this, Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
            player.setOnPreparedListener(this);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
//        @Override
//        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//
//            long duration = getDuration();
//            long newposition = (duration * i) / 1000L;
//            //seekTo( (int) newposition);
//            screenProgress.setProgress((int)newposition);
//            if (dura != null)
//                remainingTime = (int)duration-(int)newposition+1000;
//            dura.setText(stringForTime(remainingTime));
//
//        }
//
//        public void onStartTrackingTouch(SeekBar bar) {
//            setPos();
//
//            mDragging = true;
//
//        }
//
//        @Override
//        public void onStopTrackingTouch(SeekBar seekBar) {
//            setPos();
//        }
//    };
//
//    private String stringForTime(int timeMs) {
//        int totalSeconds = timeMs / 1000;
//
//        int seconds = totalSeconds % 60;
//        int minutes = (totalSeconds / 60) % 60;
//        int hours   = totalSeconds / 3600;
//
////        mFormatBuilder.setLength(0);
//        if (hours > 0) {
//            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
//        } else {
//            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
//        }
//    }
//
//    private int setPos(){
//
//        int duration = getDuration();
//        int position = getCurrentPosition();
//        int rem = duration-position+1000;
//
//        if (screenProgress != null) {
//            if (duration > 0) {
//                // use long to avoid overflow
//                long pos = 1000L * position / duration;
//
//                screenProgress.setProgress((int) pos);
//
//                int percent = getBufferPercentage();
//                screenProgress.setSecondaryProgress(percent * 10);
//                dura.setText(stringForTime(rem));
//            }
//        }
//    return position;}


            @Override
    public boolean onTouchEvent(MotionEvent event) {
        controller.show();
        player.pause();
        dura.setVisibility(View.INVISIBLE);
        if(countDownTimer !=null) {
            countDownTimer.cancel();
        }

        return false;
    }


    // Implement SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player.setDisplay(holder);
        player.prepareAsync();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    // End SurfaceHolder.Callback


    // Implement MediaPlayer.OnPreparedListener
    @Override
    public void onPrepared(MediaPlayer mp) {
        controller.setMediaPlayer(this);
        controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
        player.start();
        countDownTimer = new CountDownTimer(getDuration(), 1000) {                     //geriye sayma

            public void onTick(long millisUntilFinished) {

                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;

                dura.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            }

            public void onFinish() {
                dura.setText("00:00:00");
            }
        }.start();
    }
// End MediaPlayer.OnPreparedListener


    // Implement VideoMediaController.MediaPlayerControl
    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        int dur= player.getDuration();
//        dura.setText(Integer.toString(dur));
        return dur;
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public void seekTo(int i) {
        player.seekTo(i);
    }

    @Override
    public void start() {
        player.start();
        dura.setVisibility(View.VISIBLE);

        countDownTimer = new CountDownTimer(screenTime, 1000) {                     //geriye sayma

            public void onTick(long millisUntilFinished) {

                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;

                dura.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            }

            public void onFinish() {
                dura.setText("00:00:00");
            }
        }.start();
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void toggleFullScreen() {

    }

    @Override
    public void setOnScreenTime(int time){
       // dura.setText(time);
        screenTime=time;


    }



// End VideoMediaController.MediaPlayerControl
}

