package shay.example.com.weatherapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Shay on 25/02/2017.
 */

public class GetLocationActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiverSplash;
    private ImageView magnifyer;
    private ImageView tick;
    private MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    //    SharedPreferences settings = getApplicationContext().getSharedPreferences("KEY", Context.MODE_PRIVATE);
     //   settings.edit().clear().commit();
        getSupportActionBar().hide();
        setContentView(R.layout.init_activity_loader);

        magnifyer = (ImageView) findViewById(R.id.imageViewMagnifyer);
        Animation anim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.repeatzoom);
        tick = (ImageView) findViewById(R.id.imageViewTick);
        magnifyer.startAnimation(anim);

        mp = MediaPlayer.create(this, R.raw.found);

        if (!runtime_permissions())
            enable_Service();


    }

    //-----------------------------------    // GPS Location Services ---------------------------




    @Override
    protected void onStart() {
        super.onStart();
       //       Toast.makeText(getApplicationContext(), "onStart",
       //               Toast.LENGTH_LONG).show();
        if (broadcastReceiverSplash == null) {
            setNewBroadcastReciever();
        }
        registerReceiver(broadcastReceiverSplash, new IntentFilter("location_update"));

    }


    @Override
    protected void onRestart() {
        super.onRestart();
    //    Toast.makeText(getApplicationContext(), "onRestart",
     //           Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiverSplash == null) {
            setNewBroadcastReciever();
        }
        registerReceiver(broadcastReceiverSplash, new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiverSplash != null) {
            unregisterReceiver(broadcastReceiverSplash);
        }
        Toast.makeText(getApplicationContext(), "SPLASH onDestroy disable_Service();",
                Toast.LENGTH_LONG).show();
    //    disable_Service();
    }


    private void enable_Service() {
        Intent i = new Intent(getApplicationContext(), GPS_Service.class);
        startService(i);
    }

    private void disable_Service() {
        Intent i = new Intent(getApplicationContext(), GPS_Service.class);
        stopService(i);
    }

    private boolean runtime_permissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

            return true;
        }
        return false;
    }

    private void startTransition(final String lat, final String lon){


        ImageView lookSmiley = (ImageView) findViewById(R.id.imageViewSplashSmiley);
        lookSmiley.setImageResource(R.drawable.look_r);
        magnifyer.clearAnimation();


                magnifyer.setVisibility(View.INVISIBLE);
                tick.setVisibility(View.VISIBLE);
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {


                        Intent intentMain = new Intent(GetLocationActivity.this, MainActivity.class);
                        intentMain.putExtra("lat",lat);
                        intentMain.putExtra("long",lon);
                        intentMain.putExtra("calling-activity", ActivityConstants.ACTIVITY_1);

                        //  ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(GetLocationActivity.this, findViewById(R.id.imageViewSplashSmiley), "shareSmiley");
                        overridePendingTransition(R.transition.enter, R.transition.exit);
                        startActivity(intentMain);

                        GetLocationActivity.this.finish();
                        mp.release();
                    }
                });
                mp.start();
                //speech bubble here cancel here
            }




    private void setNewBroadcastReciever() {

        broadcastReceiverSplash = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, final Intent intent) {
                String lat = intent.getExtras().get("lat")+"";
                String lon = intent.getExtras().get("long")+"";

                startTransition(lat,lon);

            }



        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                enable_Service();
            } else {
                runtime_permissions();
            }
        }

    }
}
