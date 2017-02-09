package name.heqian.cs528.googlefit;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;
import android.util.Log;
import android.content.Intent;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.List;


/**
 * Created by Paul on 2/1/16.
 */
public class ActivityRecognizedService extends IntentService {

    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
    }

    public ActivityRecognizedService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivities( result.getProbableActivities() );
        }
    }




    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
        for( DetectedActivity activity : probableActivities ) {
            switch( activity.getType() ) {
                case DetectedActivity.IN_VEHICLE: {
                    Log.e( "ActivityRecogition", "In Vehicle: " + activity.getConfidence() );
                        if( activity.getConfidence() >= 75 ) {
                        Intent shareIntent = new Intent("msg");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "Drive");
                        System.out.println("Driving message sent");
                        sendMessage(shareIntent);
                    }
                    break;
                }
                case DetectedActivity.RUNNING: {
                    Log.e( "ActivityRecogition", "Running: " + activity.getConfidence() );
                    if( activity.getConfidence() >= 75 ) {
                        Intent shareIntent = new Intent("msg");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "Run");
                        System.out.println("Running message sent");
                        sendMessage(shareIntent);
                    }
                    break;
                }
                case DetectedActivity.STILL: {
                    Log.e( "ActivityRecogition", "Still: " + activity.getConfidence() );
                    if( activity.getConfidence() >= 50 ) {
                        Intent shareIntent = new Intent("msg");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "Stand");
                        System.out.println("Still message sent");
                        sendMessage(shareIntent);
                    }
                    break;
                }
                case DetectedActivity.WALKING: {
                    Log.e( "ActivityRecogition", "Walking: " + activity.getConfidence() );
                    if( activity.getConfidence() >= 75 ) {
                        Intent shareIntent = new Intent("msg");
                        shareIntent.putExtra(Intent.EXTRA_TEXT,"Walk");
                        System.out.println("Walking message sent");
                        sendMessage(shareIntent);
                    }
                    break;
                }
                case DetectedActivity.UNKNOWN: {
                    Log.e( "ActivityRecogition", "Unknown: " + activity.getConfidence() );
                    break;
                }
            }
        }
    }

    public void sendMessage(Intent intent){
        System.out.println("We in side sendMessage");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
