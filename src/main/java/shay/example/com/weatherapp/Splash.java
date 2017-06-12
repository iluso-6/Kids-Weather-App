package shay.example.com.weatherapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Shay on 25/02/2017.
 */

public class Splash extends AppCompatActivity {
    MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.splash_activity);

        mp = MediaPlayer.create(this, R.raw.intro);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
          @Override
          public void onCompletion(MediaPlayer mp) {
              mp.release();
              Intent intent = new Intent(getApplicationContext(),GetLocationActivity.class);
              startActivity(intent);
              overridePendingTransition(R.transition.enter, R.transition.exit);
              Splash.this.finish();
          }
  });
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mp.start();
            }
        }, 2000);

    }





    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
