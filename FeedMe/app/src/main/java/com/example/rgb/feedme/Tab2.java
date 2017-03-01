package com.example.rgb.feedme;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static com.example.rgb.feedme.Tab1.newPosts;


/**
 * Created by Rayan on 2/18/2017.
 */

public class Tab2 extends android.support.v4.app.Fragment implements  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, LocationListener {



    public GoogleApiClient mApiClient;
    public LocationRequest mLocationRequest;
    public double currentLatitude;
    public double currentLongitude;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    public static GoogleMap mMap;
    private ArrayList<Post> posts;
    DBHelper dbHelper;

    private LatLngBounds wpiBound = new LatLngBounds(
            new LatLng(42.272934, -71.813831), new LatLng(42.275255, -71.803986));
    //near the fountain 42.274495, -71.807911
    private static final CameraPosition wpi_CAMERA = new CameraPosition.Builder()
            .target(new LatLng(42.274495, -71.807911)).zoom(17.0f).bearing(0).tilt(0).build();






        //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab2, container, false);

        dbHelper = new DBHelper(getContext());

        // Create an instance of GoogleAPIClient.
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


        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Post p = new Post();
        p.eventTitle = "Free Pizza Party";
        p.foodType = "Pizza";
        p.longitude = -71.813831;
        p.latitude = 42.272934 ;
        p.time = "20";
        p.description = "There is lots of good free food time fun here at pizza land";

        Post p2 = new Post();
        p2.eventTitle = "Free Cookie Party";
        p2.foodType = "Cookies";
        p2.longitude =  -71.807911;
        p2.latitude = 42.274495;
        p2.time = "20";
        p2.description = "There is lots of good free food time fun here at cookie land";

        posts = new ArrayList<Post>();
        posts.add(p);
        posts.add(p2);

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return v;
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



// Set the camera to the greatest possible zoom level that includes the
        //42.275255, -71.803986 northeast
        //42.272934, -71.813831 southwest
        //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(wpiBound, 0));

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }




    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
        mMap.setLatLngBoundsForCameraTarget(wpiBound);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(wpi_CAMERA));
        //add pins from the given list of posts
        listPostDetails();
        dropPins(mMap,newPosts);



    }


    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);

        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        //Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;




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
        // Showing the current location in Google Map
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        // Zoom in the Google Map
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        // Toast.makeText(this, currentLatitude + " WORKS updated " + currentLongitude + "", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }


    public static void dropPins(GoogleMap mMap, ArrayList<Post> posts){
        for(Post p: posts){
            int min = -2;
            int max = 2;
            int rand1 = ThreadLocalRandom.current().nextInt(min,max+1);
            int rand2 = ThreadLocalRandom.current().nextInt(min,max+1);

            double setLat = p.latitude + (double) rand1/50000;
            double setLon = p.longitude + (double) rand2/50000;
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(setLat,setLon))
                    .title(p.eventTitle));

        }

    }


    public  void listPostDetails() {
        newPosts = new ArrayList<Post>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                "eventTitle",
                "foodType",
                "location",
                "latitude",
                "longitude",
                "time",
                "description",
                "upvotes",
                "rowid"
        };

        // Filter results WHERE "title" = 'My Title'
        //String selection = "*"
        //String[] selectionArgs = { "My Title" };

        // How you want the results sorted in the resulting Cursor
        // String sortOrder =
        //FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

        Cursor cursor = db.query(
                "FeedMePosts",                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                "ROWID DESC"                                 // The sort order
        );

        //List itemIds = new ArrayList<>();
        while(cursor.moveToNext()) {
            String title = cursor.getString(
                    cursor.getColumnIndexOrThrow("eventTitle"));
            String food = cursor.getString(
                    cursor.getColumnIndexOrThrow("foodType"));
            String loc = cursor.getString(
                    cursor.getColumnIndexOrThrow("location"));
            Double lat = cursor.getDouble(
                    cursor.getColumnIndexOrThrow("latitude"));
            Double lon = cursor.getDouble(
                    cursor.getColumnIndexOrThrow("longitude"));
            String time = cursor.getString(
                    cursor.getColumnIndexOrThrow("time"));
            String description = cursor.getString(
                    cursor.getColumnIndexOrThrow("description"));
            int upvotes = cursor.getInt(
                    cursor.getColumnIndexOrThrow("upvotes"));
            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow("rowid"));


            Post p = new Post();
            p.eventTitle = title;
            p.foodType = food;
            p.location = loc;
            p.latitude = lat;
            p.longitude = lon;
            p.time = time;
            p.description = description;
            p.postID = id;
            p.upvotes = upvotes;

            newPosts.add(p);
        }
        cursor.close();
    }

}
