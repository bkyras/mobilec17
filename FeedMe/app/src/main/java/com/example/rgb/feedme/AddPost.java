package com.example.rgb.feedme;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Rayan on 2/20/2017.
 */

public class AddPost extends DialogFragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public GoogleApiClient mApiClient;
    public LocationRequest mLocationRequest;
    public DBHelper dbHelper;
    public double currentLatitude;
    public double currentLongitude;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    Location mLastLocation;

    // Empty constructor
    public AddPost() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.add_post, container, false);
        dbHelper = new DBHelper(getContext());

        if (mApiClient == null) {
            mApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(ActivityRecognition.API)
                    .build();
        }

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        Button addButton = (Button) view.findViewById(R.id.submit_post_btn);


        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addToDatabase();
            }});

        return view;
    }


    public void onStart() {
        mApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        mApiClient.disconnect();
        super.onStop();
    }

    public void onResume() {
        mApiClient.connect();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //Disconnect from API onPause()
        if (mApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mApiClient, this);
            mApiClient.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mApiClient);
        if (mLastLocation != null) {
            currentLatitude =  mLastLocation.getLatitude();
            currentLongitude =  mLastLocation.getLongitude();
            Toast.makeText(getActivity(),  "Latitude: " +currentLatitude +   ", Longitude: "+ currentLongitude+ "", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getActivity(), "No location", Toast.LENGTH_LONG).show();

        }




    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    /**
     * If locationChanges change lat and long
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        double latitude = currentLatitude;
        // Getting longitude of the current location
        double longitude = currentLongitude;
        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);
        Toast.makeText(getActivity(), currentLatitude + " WORKS  " + currentLongitude + "", Toast.LENGTH_LONG).show();

    }


    public void addToDatabase() {
        System.out.println("Adding to database");
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        String timestamp = DateFormat.getTimeInstance().format(DateFormat.LONG);
        long now = new Date().getTime();

        TextView eventTitle = (TextView) getView().findViewById(R.id.event_field);
        TextView foodType = (TextView) getView().findViewById(R.id.food_field);
        TextView location = (TextView) getView().findViewById(R.id.location_field);
        TextView time = (TextView) getView().findViewById(R.id.time_field);
        TextView description = (TextView) getView().findViewById(R.id.descripText);

        values.put("eventTitle", eventTitle.getText().toString());
        values.put("foodType", foodType.getText().toString());
        values.put("location", location.getText().toString());
        values.put("latitude", currentLatitude);
        values.put("longitude", currentLongitude);
        values.put("time", time.getText().toString());
        values.put("description", description.getText().toString());

        db.insert("FeedMePosts", null, values);
        dismiss();
    }

}
