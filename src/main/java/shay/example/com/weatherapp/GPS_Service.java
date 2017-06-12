package shay.example.com.weatherapp;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by Shay on 25/02/2017.
 */
public class GPS_Service extends Service {
    private LocationListener listener;
    private LocationManager locationManager;

    private int minutes = 6000;//60000 ms = 1 minute
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), "GPS onStartCommand",
                Toast.LENGTH_SHORT).show();

        return super.onStartCommand(intent, flags, startId);

    }


    @Override
    public void onCreate() {

        listener = new LocationListener() {


            @Override
            public void onLocationChanged(Location location) {
                Intent i = new Intent("location_update");
                i.putExtra("long", location.getLongitude());
                i.putExtra("lat", location.getLatitude());


                sendBroadcast(i);
                Toast.makeText(getApplicationContext(), "GPS onLocationChanged",
                        Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                //           Toast.makeText(getApplicationContext(), "GPS onStatusChanged",
                //                  Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderEnabled(String s) {
                Toast.makeText(getApplicationContext(), "GPS onProviderEnabled",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //noinspection MissingPermission

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1 * minutes, 0, listener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "GPS onDestroy()",
                Toast.LENGTH_SHORT).show();
        if(locationManager != null){
            //noinspection MissingPermission
            locationManager.removeUpdates(listener);
        }

    }
}
