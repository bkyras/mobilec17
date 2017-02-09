package name.heqian.cs528.googlefit;

import android.app.PendingIntent;
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
import com.google.android.gms.wallet.wobs.TimeInterval;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    MediaPlayer mediaPlayer;
    public GoogleApiClient mApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mediaPlayer = MediaPlayer.create(this, R.raw.beat_02);

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mApiClient.connect();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("msg"));

        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.beat_02);
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
                    mediaPlayer.start();
                } else if(sharedText.equals("Walk")){
                    System.out.println("Walking message received");
                    pic.setImageResource(R.drawable.walking);
                    actText.setText(R.string.walking);
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
