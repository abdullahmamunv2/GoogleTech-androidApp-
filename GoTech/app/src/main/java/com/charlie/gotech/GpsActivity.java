package com.charlie.gotech;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.charlie.gotech.GoogleApi.GoogleLocationService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GpsActivity extends AppCompatActivity implements LocationListener,OnItemSelectedListener {

    private LocationManager locationManager;
    private int GpsPresmissionCode=100;
    private Circle cicle;
    private int GpsServiceEnableCode=1010;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    GoogleMap googleMap;
    private  LatLng location;
    private CircleOptions circleOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        Spinner spinner=(Spinner)findViewById(R.id.mapType);
        spinner.setOnItemSelectedListener(this);


        GoogleLocationService service=GoogleLocationService.getLocationManager(getApplicationContext(), GpsActivity.this,0,1);

        // if permission is not granted or location service is not ready then stop the activaty
        if( !service.getPermissionStatus() || !service.getLocationServiceStatus()){
            onStop();
        }

        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.gpsMap)).getMap();
            }

            location=new LatLng(service.getLastKnownLocation().getLatitude(),service.getLastKnownLocation().getLongitude());

            //googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(service.getLastKnownLocation().getLatitude(), service.getLastKnownLocation().getLongitude()), 15));
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            circleOptions = new CircleOptions()
                    .radius(50)   //set radius in meters
                    .fillColor(Color.BLUE) //default
                    .strokeColor(Color.WHITE)
                    .strokeWidth(5);
            circleOptions.center(location);
            cicle=googleMap.addCircle(circleOptions);

        }catch(Exception e){
            e.printStackTrace();
        }



    }



    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GpsServiceEnableCode){
            final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
            if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                Log.e("des","des");
                onStop();
            }

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults){
        if (requestCode==GpsPresmissionCode) {
            if (grantResults[0] == getPackageManager().PERMISSION_GRANTED && grantResults[1] == getPackageManager().PERMISSION_GRANTED) {


            }
        }


    }



    @Override
    public void onLocationChanged(Location location) {
        if(cicle !=null)
            cicle.remove();
        cicle=googleMap.addCircle(circleOptions.center(new LatLng(location.getLatitude(),location.getLongitude())));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.e("", "changed");
        if(id==R.id.mapType){

            googleMap.setMapType(position);
            Log.e("", "changed");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
