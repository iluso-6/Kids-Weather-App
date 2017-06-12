package shay.example.com.weatherapp;

/*
  Created by Shay on 22/03/2017.
 */

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;


public class VideoActivity extends AppCompatActivity implements View.OnClickListener {

    private VideoView videoView;


    private int currentVideo;

    private ImageView overlay;

    private View last;

    public void onClick(View v) {
  //      BitmapDrawable ob = new BitmapDrawable(getResources(), String.valueOf(R.drawable.bitmap_underlay));
        if(videoView.isPlaying()){
            videoView.stopPlayback();
            if(last==v){ // same Image being clicked
            //    v.setBackgroundColor(Color.argb(65,255,0,0));
                v.setBackgroundResource(R.drawable.bitmap_underlay_pause);
                return;// -- leave the video stopped
            }
        }
        switch (v.getId()) {
            case  R.id.imageButtonCloud: {
                currentVideo = R.raw.cloud;
                break;
            }

            case R.id.imageButtonLightning: {
                currentVideo = R.raw.lightning;
                break;
            }
            case  R.id.imageButtonRain: {
                currentVideo = R.raw.rain;
                break;
            }

            case R.id.imageButtonSun: {
                currentVideo = R.raw.sun;
                break;
            }
            case  R.id.imageButtonSnow: {
                currentVideo = R.raw.snow;
                break;
            }

            case R.id.imageButtonFog: {
                currentVideo = R.raw.fog;
                break;
            }
            default:{
                currentVideo = R.raw.cloud;
                break;
            }
        }


        last.setBackgroundColor(Color.argb(0,0,0,0)); // clear the last view background
        v.setBackgroundResource(R.drawable.bitmap_underlay_play); // set the new view background
        last = v; // store the last used view

        videoView.setVideoPath("android.resource://" + getPackageName()+ "/" + currentVideo);
        videoView.start();
        videoView.requestFocus();
        overlay.setVisibility(View.INVISIBLE); // smiley and curtains overlay when stopped
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //noinspection ConstantConditions
        getSupportActionBar().hide();
        setContentView(R.layout.video_original);

        currentVideo =  (R.raw.cloud); // default fall back video

        //   int buttonCloudField =  (R.id.imageButtonCloud);
        ImageView buttonCloud = (ImageView) findViewById(R.id.imageButtonCloud);
        buttonCloud.setOnClickListener(this);


        ImageView buttonLightning = (ImageView) findViewById(R.id.imageButtonLightning);
        buttonLightning.setOnClickListener(this);

        ImageView buttonRain = (ImageView) findViewById(R.id.imageButtonRain);
        buttonRain.setOnClickListener(this);

        ImageView buttonSun = (ImageView) findViewById(R.id.imageButtonSun);
        buttonSun.setOnClickListener(this);

        ImageView buttonSnow = (ImageView) findViewById(R.id.imageButtonSnow);
        buttonSnow.setOnClickListener(this);

        ImageView buttonFog = (ImageView) findViewById(R.id.imageButtonFog);
        buttonFog.setOnClickListener(this);

        videoView = (VideoView)findViewById(R.id.videoView);
        overlay = (ImageView)findViewById(R.id.imageViewOverlay) ;
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                overlay.setVisibility(View.VISIBLE);
            }
        });

        last = buttonCloud;
        buttonCloud.setBackgroundResource(R.drawable.bitmap_underlay_play); // initial icon selected
        videoView.setVideoPath("android.resource://" + getPackageName()+ "/" + currentVideo);
        videoView.start();
        videoView.requestFocus();


    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(VideoActivity.this,MainActivity.class);
        intent.putExtra("calling-activity", ActivityConstants.ACTIVITY_3);
        startActivity(intent);
        overridePendingTransition(R.transition.enter_left, R.transition.exit_left);
        VideoActivity.this.finish();
    }

}
