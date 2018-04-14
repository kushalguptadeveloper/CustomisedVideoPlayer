package com.example.kushalgupta.customisedvideoplayer;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
    MediaPlayer player, player2;
    VideoControllerView controller;
    TextView dura;
    private ProgressBar screenProgress;
    private Boolean mDragging;
    StringBuilder mFormatBuilder;
    Formatter mFormatter;
    private int remainingTime;
    int screenTime;
    CountDownTimer countDownTimer;
public static final String TAG = "chla";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        dura = findViewById(R.id.duration);

        videoSurface = (SurfaceView) findViewById(R.id.videoSurface);
        SurfaceHolder videoHolder = videoSurface.getHolder();
        videoHolder.addCallback(this);

        // player = new MediaPlayer();
        controller = new VideoControllerView(this);
        startNext();
//
//        try {
//
//            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//             //player.setDataSource(this, Uri.parse("http://dl2.n3.22.cdn.perfectgirls.net/mp4/HkW0SBQNq1yMVYiNUuiiMA==,1523648541/526/180/526180-full.mp4"));
//
//            player.setDataSource(this, Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"));
//            player.setOnPreparedListener(this);
////player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
////    @Override
////    public void onCompletion(MediaPlayer mediaPlayer) {
////
////       startNext();
////
////    }
////});
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//        } catch (SecurityException e) {
//            e.printStackTrace();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        controller.show();
        player.pause();
        dura.setVisibility(View.INVISIBLE);
        if (countDownTimer != null) {
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
        Log.d(TAG, "onPrepared: 2");
        controller.setMediaPlayer(this);
        controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
        player.start();
        dura.setVisibility(View.VISIBLE);
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
        int dur = player.getDuration();
//        dura.setText(Integer.toString(dur));
        return dur;
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public void pause() {

        dura.setVisibility(View.INVISIBLE);
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        player.pause();
    }

    @Override
    public void seekTo(int i) {
        player.seekTo(i);
    }

    @Override
    public void start() {
        //https://drive.google.com/file/d/19-QXY7dFSHGzDMgmQ2KFQnySfGznjnC1/view?usp=sharing
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
    public void setOnScreenTime(int time) {
        // dura.setText(time);
        screenTime = time + 1000;


    }

    @Override
    public void nextVideo() {
        if (player != null) {

            startNext();


        }
        //  player.stop();
//            player.reset();
//           // player.release();
//         //   player = null;
//
////player=new MediaPlayer();
//            try {
//
//              // Uri u=Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
//                //player=MediaPlayer.create(this,u);
//                player = new MediaPlayer();
//                SurfaceHolder videoHolder = videoSurface.getHolder();
//                videoHolder.addCallback(this);
//
//                controller = new VideoControllerView(this);
//                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                player.setDataSource(this, Uri.parse("http://www.html5videoplayer.net/videos/toystory.mp4"));
//                player.setOnPreparedListener(this);
////                player.prepareAsync();
//
//
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//            } catch (SecurityException e) {
//                e.printStackTrace();
//            } catch (IllegalStateException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
    }

    public void startNext() {
        if (player == null) {
            player = new MediaPlayer();
            try {
             //   player.setAudioStreamType(AudioManager.STREAM_MUSIC);

                player.setDataSource("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
               // player.setOnPreparedListener(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            player.reset();
            try {
            //    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setDataSource("http://www.html5videoplayer.net/videos/toystory.mp4");
                player.prepareAsync();


               // player.setOnPreparedListener(this);
                // player.setOnPreparedListener(this);
//                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                    @Override
//                    public void onPrepared(MediaPlayer mediaPlayer) {
//                        Log.d(TAG, "onPrepared: chla");
//                        player.start();
//                        dura.setVisibility(View.VISIBLE);
//                        Toast.makeText(VideoPlayerActivity.this, "" + getDuration(), Toast.LENGTH_SHORT).show();
//                        countDownTimer = new CountDownTimer(getDuration(), 1000) {                     //geriye sayma
//
//                            public void onTick(long millisUntilFinished) {
//
//                                NumberFormat f = new DecimalFormat("00");
//                                long hour = (millisUntilFinished / 3600000) % 24;
//                                long min = (millisUntilFinished / 60000) % 60;
//                                long sec = (millisUntilFinished / 1000) % 60;
//
//                                dura.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
//                            }
//
//                            public void onFinish() {
//                                dura.setText("00:00:00");
//                            }
//                        }.start();
//                    }
//                });


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //player = new MediaPlayer();
        try {

            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            // player.setDataSource(this, Uri.parse("http://dl1.n3.23.cdn.perfectgirls.net/mp4/-h29-35Ni7qOjZT7hZGzdA==,1523201699/525/011/525011-full.mp4"));
            // player.setDataSource(this, Uri.parse("http://dl2.n3.22.cdn.perfectgirls.net/mp4/HkW0SBQNq1yMVYiNUuiiMA==,1523648541/526/180/526180-full.mp4"));

            //  player.setDataSource(this, Uri.parse("http://dl1.n3.23.cdn.perfectgirls.net/mp4/-h29-35Ni7qOjZT7hZGzdA==,1523201699/525/011/525011-full.mp4"));
            player.setOnPreparedListener(this);


        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();

        }
    }}






// End VideoMediaController.MediaPlayerControl


