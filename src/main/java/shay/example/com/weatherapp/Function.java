package shay.example.com.weatherapp;

/**
 * Created by Shay on 18/03/2017.
 */


import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

public class Function {


    private static final String OPEN_WEATHER_MAP_URL =
            "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric";

    private static final String OPEN_WEATHER_MAP_API = "ab76296461f49ff0bbe06aef0933e929";

    private static String []getWeatherIcon(int actualId, long sunrise, long sunset){

        int id = actualId /100;
        if(id==5 || id==8){
            id=actualId;
        }
        String icon;
        String night = "false";
        String []rtnWeatherState = new String[2];
        long currentTime = new Date().getTime();
        Log.e("currentTime: ",currentTime+"");
        Log.e("sunrise: ",sunrise+"");
        Log.e("sunset: ",sunset+"");
        if (currentTime>=sunrise && currentTime<sunset) {
            night = "false";
            // Day time default
            switch (id){

                case 2:
                    icon = "darkclouds_lightening";
                    break;
                case 3:
                    icon = "cloud2_rain";
                    break;
                case 6:
                    icon = "darkclouds_snow";
                    break;
                case 7:
                    icon = "fog";
                    break;
                case 9:
                    icon = "blank";
                    break;
                case 500:
                    icon = "cloud_rain_sun";
                    break;
                case 501:
                    icon = "cloud_rain_sun";
                    break;
                case 502:
                    icon = "cloud_rain_sun";
                    break;
                case 503:
                    icon = "cloud_rain_sun";
                    break;
                case 504:
                    icon = "cloud_rain_sun";
                    break;
                case 511:
                    icon = "cloud2_rain";
                    break;
                case 520:
                    icon = "cloud2_rain";
                    break;
                case 521:
                    icon = "cloud2_rain";
                    break;
                case 522:
                    icon = "cloud2_rain";
                    break;
                case 531:
                    icon = "cloud2_rain";
                    break;
                case 800:
                    icon = "sun";
                    break;
                case 801:
                    icon = "cloud_sun";
                    break;
                case 802:
                    icon = "cloud";
                    break;
                case 803:
                    icon = "cloud2";
                    break;
                case 804:
                    icon = "cloud2";
                    break;
                default:
                    icon = "blank"; // fall back in case of unknown id's to prevent error
                    break;
            }
            rtnWeatherState[0] = icon;
            rtnWeatherState[1] = night;
            return rtnWeatherState;

        }else{
            // Night time
            night = "true";
            switch (id){
                case 2:
                    icon = "darkclouds_lightening";
                    break;
                case 3:
                    icon = "cloud2_rain";
                    break;
                case 6:
                    icon = "darkclouds_snow";
                    break;
                case 7:
                    icon = "fog";
                    break;
                case 9:
                    icon = "blank";
                    break;
                case 500:
                    icon = "cloud_rain_moon";
                    break;
                case 501:
                    icon = "cloud_rain_moon";
                    break;
                case 502:
                    icon = "cloud_rain_moon";
                    break;
                case 503:
                    icon = "cloud_rain_moon";
                    break;
                case 504:
                    icon = "cloud_rain_moon";
                    break;
                case 511:
                    icon = "cloud2_rain";
                    break;
                case 520:
                    icon = "cloud2_rain";
                    break;
                case 521:
                    icon = "cloud2_rain";
                    break;
                case 522:
                    icon = "cloud2_rain";
                    break;
                case 531:
                    icon = "cloud2_rain";
                    break;
                case 800:
                    icon = "moon";
                    break;
                case 801:
                    icon = "cloud_moon";
                    break;
                case 802:
                    icon = "cloud";
                    break;
                case 803:
                    icon = "cloud2";
                    break;
                case 804:
                    icon = "cloud2";
                    break;
                default:
                    icon = "blank"; // fall back in case of unknown id's to prevent error
                    break;
            }

            MainActivity.bg.setBackgroundResource(R.drawable.bg_night);
            MainActivity.cityField.setTextColor(Color.WHITE);
            rtnWeatherState[0] = icon;
            rtnWeatherState[1] = night;
            return rtnWeatherState;
        }

    }





    public interface AsyncResponse {

        void processFinish(String output1, String output2, String output3, String output4, String output5,String output6);

    }





    public static class placeIdTask extends AsyncTask<String, Void, JSONObject> {

        public AsyncResponse delegate = null;//Call back interface

        public placeIdTask(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected JSONObject doInBackground(String... params) {
      //  Log.e("currentThread()",Thread.currentThread().getName());
            JSONObject jsonWeather = null;
            try {
                jsonWeather = getWeatherJSON(params[0], params[1]);
            } catch (Exception e) {
                Log.d("Error", "Cannot process JSON results", e);
            }


            return jsonWeather;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if(json != null){
                    Log.e("XXXX WEATHER XXXXX"," "+json);
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = json.getJSONObject("main");
                    JSONObject wind = json.getJSONObject("wind");
                    DateFormat df = DateFormat.getDateTimeInstance();


                    String city = json.getString("name");
                    String description = details.getString("description");
                    String temperature =  main.getInt("temp")+"";
                    int windspeedInt =  wind.getInt("speed");
                    String windspeed = (int)(windspeedInt * 2.23694)+""; // convert from meters per second and cast to integer


                    String []weatherResult = getWeatherIcon(details.getInt("id"),
                            json.getJSONObject("sys").getLong("sunrise") * 1000,
                            json.getJSONObject("sys").getLong("sunset") * 1000);
                    String iconText = weatherResult[0];
                    String night = weatherResult[1];
                    delegate.processFinish(city, description, temperature, windspeed, iconText, night);

                }
            } catch (JSONException e) {
                //Log.e(LOG_TAG, "Cannot process JSON results", e);
            }



        }
    }






    private static JSONObject getWeatherJSON(String lat, String lon){
        try {
            Log.e("currentThread()",Thread.currentThread().getName());
            URL url = new URL(String.format(OPEN_WEATHER_MAP_URL, lat, lon));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key", OPEN_WEATHER_MAP_API);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp;
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;
        }catch(Exception e){
            return null;
        }
    }




}
