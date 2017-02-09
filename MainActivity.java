package name.heqian.cs528.googlefit;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.widget.ImageView;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionApi;
import com.google.android.gms.wallet.wobs.TimeInterval;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public GoogleApiClient mApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if(Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendImage(intent);
            }
        }
        */

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mApiClient.connect();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("msg"));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Intent intent = new Intent( this, ActivityRecognizedService.class );
        PendingIntent pendingIntent = PendingIntent.getService( this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates( mApiClient, 3000, pendingIntent );
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // Handles an image being passed through an intent
    void handleSendImage(Intent intent){
        PendingIntent pendingIntent = PendingIntent.getService( this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates( mApiClient, 3000, pendingIntent );

        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null){
            if(sharedText.equals("Drive")){
                ImageView pic = (ImageView) findViewById(R.id.ImageView);
                pic.setImageResource(R.drawable.in_vehicle);
            } else if(sharedText.equals("Run")){
                ImageView pic = (ImageView) findViewById(R.id.ImageView);
                pic.setImageResource(R.drawable.running);
            } else if(sharedText.equals("Walk")){
                ImageView pic = (ImageView) findViewById(R.id.ImageView);
                pic.setImageResource(R.drawable.walking);
            } else if (sharedText.equals("Stand")){
                ImageView pic = (ImageView) findViewById(R.id.ImageView);
                pic.setImageResource(R.drawable.still);
            }
        }
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
                } else if(sharedText.equals("Run")){
                    System.out.println("Running message received");
                    pic.setImageResource(R.drawable.running);
                    actText.setText(R.string.running);
                } else if(sharedText.equals("Walk")){
                    System.out.println("Walking message received");
                    pic.setImageResource(R.drawable.walking);
                    actText.setText(R.string.walking);
                } else if (sharedText.equals("Stand")){
                    System.out.println("Standing message received");
                    pic.setImageResource(R.drawable.still);
                    actText.setText(R.string.still);
                }
            }


        }
    };

}
