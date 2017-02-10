package name.heqian.cs528.googlefit;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.widget.ImageView;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionApi;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    MediaPlayer mediaPlayer;
    public GoogleApiClient mApiClient;
    public Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create an instance of GoogleAPIClient.
        if (mApiClient == null) {
            mApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(ActivityRecognition.API)
                    .build();
        }
        mApiClient.connect();
        mediaPlayer = MediaPlayer.create(this, R.raw.beat_02);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("msg"));

        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.beat_02);
    }

    protected void onStart() {
        mApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mApiClient.disconnect();
				if (mediaPlayer != null) mediaPlayer.release();
        super.onStop();
    }
		
		protected void onResume() {
			mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.beat_02);
		}
		
		@Override
		public void onDestroy() {
       if (mediaPlayer != null) mediaPlayer.release();
   }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Intent intent = new Intent(this, ActivityRecognizedService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mApiClient, 3000, pendingIntent);

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
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mApiClient);


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();


//
//
//        if(mLastLocation != null){
//            double latitude = mLastLocation.getLatitude();
//            // Getting longitude of the current location
//            double longitude = mLastLocation.getLongitude();
//            // Creating a LatLng object for the current location
//            LatLng latLng = new LatLng(latitude, longitude);
//            // Showing the current location in Google Map
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//            // Zoom in the Google Map
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
//        }


    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);

        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        mMap.animateCamera(CameraUpdateFactory.zoomTo(20));

        //Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
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

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("Inside receiver");
            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            ImageView pic = (ImageView) findViewById(R.id.ImageView);
            TextView actText = (TextView) findViewById(R.id.textView2);
            if (sharedText != null){
                if(sharedText.equals("Drive")){
                    System.out.println("Driving message received");
                    pic.setImageResource(R.drawable.in_vehicle);
                    actText.setText(R.string.driving);
                    mediaPlayer.stop();
                } else if(sharedText.equals("Run")){
                    System.out.println("Running message received");
                    pic.setImageResource(R.drawable.running);
                    actText.setText(R.string.running);
										mediaPlayer.prepare();
                    mediaPlayer.start();
                } else if(sharedText.equals("Walk")){
                    System.out.println("Walking message received");
                    pic.setImageResource(R.drawable.walking);
                    actText.setText(R.string.walking);
										mediaPlayer.prepare();
                    mediaPlayer.start();
                } else if (sharedText.equals("Stand")){
                    System.out.println("Standing message received");
                    pic.setImageResource(R.drawable.still);
                    actText.setText(R.string.still);
                    //Toast.makeText(this.parent.getActivity(), "You were walking for", Toast.LENGTH_SHORT).show();
                    mediaPlayer.stop();
                }
            }
        }
    };

}

