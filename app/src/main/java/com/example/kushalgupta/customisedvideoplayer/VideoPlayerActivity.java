package com.example.kushalgupta.customisedvideoplayer;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.Chronometer;
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
public class VideoPlayerActivity extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl, MediaPlayer.OnCompletionListener {

    SurfaceView videoSurface;
    MediaPlayer player, player2;
    VideoControllerView controller;
    TextView dura, dura2,NoOfSets;
    Boolean count;
    int screenTime,screenTime2;
    CountDownTimer countDownTimer;
    public static final String TAG = "chla";
    ProgressDialog progressDialog;
    Button skipIntroBtn;
    int noOfSets = 2;
    String videoName;
    int IntroReal;
    int videoNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        dura = findViewById(R.id.duration);
        dura2 = findViewById(R.id.duration2);
        skipIntroBtn = findViewById(R.id.btn_skip_intro);
        skipIntroBtn.setOnClickListener(skipIntriListner);
NoOfSets = findViewById(R.id.no_of_sets);

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
        dura2.setVisibility(View.INVISIBLE);
        NoOfSets.setVisibility(View.INVISIBLE);
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
        Log.d(TAG, "onPrepared: 1");
        controller.setMediaPlayer(this);
        controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer),IntroReal ,noOfSets,videoName);
        player.start();
       // player.setLooping(true);
        progressDialog.dismiss();
        //dura.setVisibility(View.VISIBLE);
        Log.d(TAG, "onPrepared: " + getDuration());
        int gy = getDuration();
        if(IntroReal == 1 ){
            gy = gy * noOfSets;
        }
        if(countDownTimer !=null){
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(gy, 1000) {                     //geriye sayma

            public void onTick(long millisUntilFinished) {

                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                if (IntroReal == 0 ) {
                    dura.setVisibility(View.VISIBLE);
                    dura2.setVisibility(View.GONE);
                    NoOfSets.setVisibility(View.INVISIBLE);
                    NoOfSets.setText("Remaining Sets : " +String.valueOf(noOfSets));
                    dura.setText(videoName + "\n" + "Starts in " + f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                    dura2.setText("Total Time Remaining : \n" +f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                    skipIntroBtn.setVisibility(View.VISIBLE);
                }
                else if (IntroReal == 1 ){
                    dura.setVisibility(View.GONE);
                    skipIntroBtn.setVisibility(View.GONE);
                    dura2.setVisibility(View.VISIBLE);
                    NoOfSets.setVisibility(View.VISIBLE);
                    NoOfSets.setText("Remaining Sets : " +String.valueOf(noOfSets));
                    dura.setText(videoName + "\n" + "Starts in " + f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                    dura2.setText("Total Time Remaining : \n" +f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));

                }


            }

            public void onFinish() {
                dura.setText("00:00:00");
                dura2.setText("00:00:00");
                if(IntroReal == 0 && videoNo == 0){
                    IntroReal = 1;
                    videoNo =0;
                }
                else if(IntroReal == 1 && videoNo == 0){
                    IntroReal = 0;
                    videoNo =1;
                }
                else if(IntroReal == 0 && videoNo == 1){
                    IntroReal =1;
                    videoNo =1;
                }
                startNext();
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
        dura2.setVisibility(View.GONE);
        NoOfSets.setVisibility(View.INVISIBLE);
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
//        dura.setVisibility(View.VISIBLE);
        if(countDownTimer != null){
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(screenTime, 1000) {                     //geriye sayma

            public void onTick(long millisUntilFinished) {

                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;

                if (IntroReal == 0 ) {
                    dura.setVisibility(View.VISIBLE);
                    dura2.setVisibility(View.GONE);
                    NoOfSets.setVisibility(View.INVISIBLE);
                    NoOfSets.setText("Remaining Sets : " +String.valueOf(noOfSets));
                    dura.setText(videoName + "\n" + "Starts in " + f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                    dura2.setText("Total Time Remaining : \n" +f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                    skipIntroBtn.setVisibility(View.VISIBLE);
                }
                else if (IntroReal == 1 ){
                    dura.setVisibility(View.GONE);
                    skipIntroBtn.setVisibility(View.GONE);
                    dura2.setVisibility(View.VISIBLE);
                    NoOfSets.setVisibility(View.VISIBLE);
                    NoOfSets.setText("Remaining Sets : " +String.valueOf(noOfSets));
                    dura.setText(videoName + "\n" + "Starts in " + f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
                    dura2.setText("Total Time Remaining : \n" +f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));

                }
            }

            public void onFinish() {
                dura.setText("00:00:00");
                if(IntroReal == 0 && videoNo == 0){
                    IntroReal = 1;
                    videoNo =0;
                }
                else if(IntroReal == 1 && videoNo == 0){
                    IntroReal = 0;
                    videoNo =1;
                }
                else if(IntroReal == 0 && videoNo == 1){
                    IntroReal =1;
                    videoNo =1;
                }
                startNext();
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
        if(IntroReal == 1){
            int tempTotalDuration = getDuration();
            int ku= tempTotalDuration - time ;
            screenTime = tempTotalDuration*noOfSets-ku+1000;



        }
        else {
            screenTime = time + 1000;
        }

    }

    @Override
    public void nextVideo() {
        if (player != null) {

            if(IntroReal == 0 && videoNo == 0){
                IntroReal = 1;
                videoNo =0;
            }
            else if(IntroReal == 1 && videoNo == 0){
                IntroReal = 0;
                videoNo =1;
            }
            else if(IntroReal == 0 && videoNo == 1){
                IntroReal =1;
                videoNo =1;
            }
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


    @Override
    public void prevVideo(){
        if (player != null) {

            if(IntroReal == 0 && videoNo == 0){
                IntroReal = 0;
                videoNo =1;
            }
            else if(IntroReal == 1 && videoNo == 0){
                IntroReal = 0;
                videoNo =0;
            }
            else if(IntroReal == 0 && videoNo == 1){
                IntroReal =0;
                videoNo =0;
            }
            else if(IntroReal == 1 && videoNo == 1){
                IntroReal = 0;
                videoNo = 1;
            }
            startNext();


        }
    }

    public void startNext() {
        if (player == null) {
            player = new MediaPlayer();
            try {
                //   player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                videoName = "Big Buck Bunny";
                IntroReal = 0;
                videoNo = 0;
                player.setDataSource("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
                // player.setOnPreparedListener(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(IntroReal == 1 && videoNo == 0){
            player.reset();
            try {
                videoName = "Big Buck Bunny";
                IntroReal = 1;
                videoNo = 0;
                if(countDownTimer != null){
                    countDownTimer.cancel();
                }
                player.setDataSource("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
                player.prepareAsync();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
                else if(IntroReal == 0 && videoNo == 1){
            player.reset();
            try {
                videoName = "Toy Story";
                IntroReal = 0;
                videoNo =1;
                if(countDownTimer != null){
                    countDownTimer.cancel();
                }
                player.setDataSource("http://www.html5videoplayer.net/videos/toystory.mp4");
                player.prepareAsync();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if(IntroReal == 1 && videoNo == 1){
            player.reset();
            try {
                videoName = "Toy Story";
                IntroReal = 1;
                videoNo =1;
                if(countDownTimer != null){
                    countDownTimer.cancel();
                }
                player.setDataSource("http://www.html5videoplayer.net/videos/toystory.mp4");
                player.prepareAsync();


            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        try {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please Wait");
            progressDialog.setMessage("Loading ... ");
            progressDialog.show();

            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setOnPreparedListener(this);
            player.setOnCompletionListener(this);


        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
if(IntroReal == 1) {
    if(noOfSets > 0) {
        player.seekTo(0);
        player.start();
        noOfSets--;
    }
    else {

        if(IntroReal == 1 && videoNo == 0){
            IntroReal = 0;
            videoNo =1;
            noOfSets =2;
            startNext();
        }

        if(IntroReal == 1 && videoNo == 1){
           IntroReal =0;
           videoNo =0;
           noOfSets =2;
           startNext();
        }



    }

}

        if(IntroReal == 0 && videoNo == 0){
            IntroReal = 1;
            videoNo =0;
            startNext();

        }

       if(IntroReal == 0 && videoNo == 1){
            IntroReal =1;
            videoNo =1;
            startNext();
        }
    //    startNext();


    }

    private View.OnClickListener skipIntriListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dura.setVisibility(View.INVISIBLE);
            if( videoNo == 0) {
                IntroReal = 1;
                videoNo = 0;
            }
            else if(videoNo == 1){
                IntroReal =1;
                videoNo =1;
            }
            startNext();
        }
    };


}


// End VideoMediaController.MediaPlayerControl


