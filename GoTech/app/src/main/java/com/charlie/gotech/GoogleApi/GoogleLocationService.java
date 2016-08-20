package com.charlie.gotech.GoogleApi;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;


/**
 * Created by kamrul on 8/20/2016.
 */
public class GoogleLocationService implements LocationListener {

    /* The minimum distance to change updates in meters
    *  depends on application requirement
    */
    private static long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES=0;

    /* The minimum time beetwen updates in milliseconds
    *  depends on application requirement
    */
    private static  long MINIMUM_TIME_BETWEEN_UPDATES=0;

    private static LocationListener activityListener=null;

    private LocationManager locationManager;

    private boolean isGPSEnabled;

    private boolean isNetworkEnabled;

    private boolean locationServiceAvailable=false;

    //check only for api >= 23
    private boolean isPermissionGranted=false;


    private static GoogleLocationService googleLocationService=null;

    private Location LastKnownLocation=null;


    public static GoogleLocationService getLocationManager( Context context,
                                                            LocationListener activityListener,
                                                            long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                                                            long MINIMUM_TIME_BETWEEN_UPDATES){
        if (googleLocationService == null) {

            GoogleLocationService.MINIMUM_DISTANCE_CHANGE_FOR_UPDATES=MINIMUM_DISTANCE_CHANGE_FOR_UPDATES;
            GoogleLocationService.MINIMUM_TIME_BETWEEN_UPDATES=MINIMUM_TIME_BETWEEN_UPDATES;
            googleLocationService = new GoogleLocationService(context);
            GoogleLocationService.activityListener=activityListener;
        }
        return googleLocationService;
    }



    /*  Singleton pattern (Private)
     *  only one object can be instantiated
     *
     */
    private GoogleLocationService(Context context){

        initGoogleLocationService(context);
    }

    private void initGoogleLocationService(Context context){
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            this.isPermissionGranted=true;
            return  ;
        }

        try{
            this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            // Get GPS and network status
            this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            this.isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isNetworkEnabled && !isGPSEnabled)    {
                // cannot get location
                this.locationServiceAvailable = false;
            }
            else{
                this.locationServiceAvailable = true;

                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MINIMUM_TIME_BETWEEN_UPDATES,
                            MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null)   {
                        LastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }
                if (isGPSEnabled)  {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MINIMUM_TIME_BETWEEN_UPDATES,
                            MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null)  {
                        LastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                }
            }





        }catch (Exception e){
            e.printStackTrace();
        }



    }

    public boolean getPermissionStatus(){
        return this.isPermissionGranted;
    }

    public boolean getLocationServiceStatus(){
        return this.locationServiceAvailable;
    }

    public Location getLastKnownLocation(){
        return LastKnownLocation;
    }


    @Override
    public void onLocationChanged(Location location) {
        activityListener.onLocationChanged(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        activityListener.onStatusChanged(provider,status,extras);
    }

    @Override
    public void onProviderEnabled(String provider) {

        activityListener.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(String provider) {

        activityListener.onProviderDisabled(provider);
    }
}
