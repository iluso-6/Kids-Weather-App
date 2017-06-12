package shay.example.com.weatherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;




public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;

    static TextView cityField;
    private TextView windText;
    private TextView description;
    private TextView currentTemperatureField;
    private ImageView weatherIcon;
    private ImageView smileyMain;
    static LinearLayout bg;
    private ProgressBar pBar;

    private String getMyLat() {
        return myLat;
    }

    private void setMyLat(String myLat) {
        this.myLat = myLat;
    }

    private String getMyLong() {
        return myLong;
    }

    private void setMyLong(String myLong) {
        this.myLong = myLong;
    }

    private String myLat;
    private String myLong;

    private void animateSmileyPlayButton(){
        smileyMain.setVisibility(View.VISIBLE);
        Animation pop = AnimationUtils.loadAnimation(getBaseContext(), R.anim.pop);
        smileyMain.startAnimation(pop);
        smileyMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,VideoActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        int callingActivity = getIntent().getIntExtra("calling-activity", 0);

        switch (callingActivity) {
            case ActivityConstants.ACTIVITY_1:
                // started from Activity1 (GetLocationActivity)

                getIntentLocation();
                String lat = getMyLat();
                String lon = getMyLong();
                beginAsyncDownLoad(lat, lon);
                animateSmileyPlayButton();
                break;
            case ActivityConstants.ACTIVITY_3:
                // started from Activity3  (VideoActivity)
                SharedPreferences sharedPref = this.getSharedPreferences("KEY",Context.MODE_PRIVATE);
                String city = sharedPref.getString("weather_city", "city_title_value");
                String windSpeed = sharedPref.getString("windSpeed", "windSpeed"); // FORCED to use string values due to errors in sharedPrefs
                String description = sharedPref.getString("weather_description", "weather_description");
                String temp = sharedPref.getString("weatherTemp", "weatherTemp");
                String drawableId = sharedPref.getString("drawableId", "fog");
                String weather_night = sharedPref.getString("weather_night", "null");
                smileyMain.setVisibility(View.VISIBLE);
                smileyMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),VideoActivity.class);
                        startActivity(intent);

                        MainActivity.this.finish();
                    }
                });
                drawUpdatedView(city, temp, windSpeed, drawableId, description, weather_night);
                break;
        }



        if (broadcastReceiver == null) {
            setNewBroadcastReciever();
        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null) {
              setNewBroadcastReciever();
     //       Toast.makeText(getApplicationContext(), "MAIN ACTIVITY onResume",
     //               Toast.LENGTH_LONG).show();
        }
          registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }

    }

    private void getIntentLocation(){

        Bundle extras = getIntent().getExtras();
        String getLat = extras.getString("lat");
        String getLong = extras.getString("long");

        setMyLat(getLat);
        setMyLong(getLong);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //noinspection ConstantConditions
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_latest);
        pBar = (ProgressBar) findViewById(R.id.progressbar);
        smileyMain = (ImageView)findViewById(R.id.imageViewSmileyBackground2);

        bg =(LinearLayout)findViewById(R.id.mybase);
        Typeface weatherFont = Typeface.createFromAsset(getAssets(), "Kindergarden.ttf");

        cityField = (TextView)findViewById(R.id.textViewCity);
        cityField.setTypeface(weatherFont);

        windText = (TextView)findViewById(R.id.TextView_windSpeed);
        windText.setTypeface(weatherFont);
        currentTemperatureField = (TextView)findViewById(R.id.textView_Temp);
        currentTemperatureField.setTypeface(weatherFont);
        weatherIcon = (ImageView)findViewById(R.id.imageView_Cloud);

        description = (TextView)findViewById(R.id.textView_Description);
        description.setTypeface(weatherFont);


    }

    private void setNewBroadcastReciever() {
        Toast.makeText(getApplicationContext(), "MAIN ACTIVITY setNewBroadcastReciever()",
                Toast.LENGTH_LONG).show();
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                String myLat = intent.getExtras().get("lat")+"";
                String myLon = intent.getExtras().get("long")+"";

                beginAsyncDownLoad(myLat, myLon);

            }
        };

    }


private void saveLocationDetails_AsSharedPreferences(String weather_city, String weatherTemp, String windSpeed,String drawableId, String weather_description, String weather_night){
    SharedPreferences sharedPref = this.getSharedPreferences("KEY",Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPref.edit();
Log.e("XXX weather_night XXXX ",weather_night);
    //USING STRING VALUES NOT RESO
  /*  String city_title, String temperature, String mph,int drawableId, String description --- values in R.strings
    editor.putString(getString(R.string.city_title_value), weather_city);
    editor.putString(getString(R.string.temperature_value), weatherTemp);
   editor.putString(getString(R.string.drawable_value), drawableId);
    editor.putString(getString(R.string.drawable_value), drawableId);
    editor.putString(getString(R.string.description_value), weather_description);*/
    editor.putString("weather_city", weather_city);
    editor.putString("weatherTemp", weatherTemp);
    editor.putString("windSpeed", windSpeed);
    editor.putString("drawableId", drawableId);
    editor.putString("weather_description", weather_description);
    editor.putString("weather_night", weather_night);
    editor.apply();

}


    private void drawUpdatedView(String weather_city, String weatherTemp, String windSpeed,String weather_iconText, String weather_description, String weather_night) {



        String weatherTemp_degree = weatherTemp + getString(R.string.degrees);
        String windSpeed_mph = windSpeed + getString(R.string.mph);

        // draw the thermometer color
        ImageView tempUnderlay = (ImageView) findViewById(R.id.imageView_TempUnderlay);
        int[] colorTemp = getColourTemp(Integer.valueOf(weatherTemp)); // get the returned array
        tempUnderlay.setColorFilter(Color.rgb(colorTemp[0], colorTemp[1], colorTemp[2]));


        int drawableIDENT = 0;
        try {
            Class res = R.drawable.class;
            Field field = res.getField(weather_iconText);
            drawableIDENT = field.getInt(null);
        } catch (Exception e) {
            Log.e("MyTag", "Failure to get drawable id.", e);
        }

        if(weather_night.equals("true")){
            bg.setBackgroundResource(R.drawable.bg_night);
            cityField.setTextColor(Color.WHITE);
            Log.e("If weather_night","true");
        }else{
            bg.setBackgroundResource(R.drawable.bg2);
            cityField.setTextColor(Color.BLACK);
            Log.e("If weather_night","false");
        }

        cityField.setText(weather_city);
        currentTemperatureField.setText(weatherTemp_degree);
        windText.setText(windSpeed_mph);
        weatherIcon.setImageResource(drawableIDENT);
        description.setText(weather_description);

        pBar.setVisibility(View.INVISIBLE);




    }

    // Temperature array to set color of current temp
    private int[] getColourTemp (int t){

        int []rtnColor = new int[3];
        if(t<5) {               //  bright blue
            rtnColor[0] = 0;
            rtnColor[1] = 80;
            rtnColor[2] = 255;
        }else if(t>=5 && t<=10){ // light blue
            rtnColor[0] = 0;
            rtnColor[1] = 167;
            rtnColor[2] = 255;
        }else if(t>=11 && t<=15) { // light yellow
            rtnColor[0] = 255;
            rtnColor[1] = 192;
            rtnColor[2] = 30;
        }else if(t>=16 && t<20) { // light orange
            rtnColor[0] = 255;
            rtnColor[1] = 150;
            rtnColor[2] = 0;
        }else if(t>=20 && t<27){ // light red
            rtnColor[0] = 255;
            rtnColor[1] = 107;
            rtnColor[2] = 71;
        }else{                     // bright red
            rtnColor[0] = 255;
            rtnColor[1] = 50;
            rtnColor[2] = 7;
        }


        return rtnColor;
    }

    private void beginAsyncDownLoad(String lat, String lon){

      //  Toast.makeText(getApplicationContext(), "MAIN ACTIVITY beginAsyncDownLoad()",
             //   Toast.LENGTH_LONG).show();


        pBar.setVisibility(View.VISIBLE);

        Function.placeIdTask asyncTask =new Function.placeIdTask(new Function.AsyncResponse() {
            public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_windspeed, String weather_iconText, String weather_night) {


                drawUpdatedView(weather_city, weather_temperature, weather_windspeed, weather_iconText, weather_description, weather_night);

                saveLocationDetails_AsSharedPreferences(weather_city, weather_temperature, weather_windspeed, weather_iconText,weather_description,weather_night);
            }

        });


         asyncTask.execute(lat, lon);

    }
}