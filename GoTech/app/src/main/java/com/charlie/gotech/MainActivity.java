package com.charlie.gotech;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //// take button
        Button BgoogleMap=(Button)findViewById(R.id.btnGoogleMap);
        Button BgooglePlace=(Button)findViewById(R.id.btnGooglePlace);
        Button Bgps=(Button)findViewById(R.id.btnGps);

        /// add action listener to button

        BgoogleMap.setOnClickListener(ClickListener);
        BgooglePlace.setOnClickListener(ClickListener);
        Bgps.setOnClickListener(ClickListener);

        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            //Intent gpsIntent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            //startActivityForResult(gpsIntent,GpsServiceEnableCode);
            showGPSDisabledAlertToUser();
        }


    }

    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }



    /*
    * handle button click
    * if view.id=button google map

    */
    private View.OnClickListener ClickListener=new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            switch (v.getId()){
                case R.id.btnGoogleMap:
                    //do some thing
                    Intent GoogleMapIntent=new Intent(MainActivity.this,GoogleMapActivity.class);
                    startActivity(GoogleMapIntent);
                    break;
                case R.id.btnGooglePlace:
                    //do some thing
                    break;
                case R.id.btnGps:
                    //do some thing

                    Intent GpsIntent=new Intent(MainActivity.this,GpsActivity.class);
                    startActivity(GpsIntent);
                    break;
            }
        }
    };
}
