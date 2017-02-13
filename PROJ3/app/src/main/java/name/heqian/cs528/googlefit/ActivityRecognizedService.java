package name.heqian.cs528.googlefit;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;
import android.util.Log;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import static name.heqian.cs528.googlefit.MainActivity.currentActivity;
import static name.heqian.cs528.googlefit.MainActivity.lastDiff;
import static name.heqian.cs528.googlefit.MainActivity.lastTime;


public class ActivityRecognizedService extends IntentService {
    DBHelper mDbHelper = new DBHelper(this);

    //String currentActivity = "";


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
    private void addToDatabase(String activity) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        //get current time
        String timestamp = DateFormat.getTimeInstance().format(DateFormat.LONG);
        long now = new Date().getTime();
        lastDiff = now - lastTime;
        lastTime = now;
        values.put("timestamp", timestamp);
        values.put("activity", activity);

        db.insert("ActivityTracker", null, values);
    }

    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
        for( DetectedActivity activity : probableActivities ) {
            switch( activity.getType() ) {
                case DetectedActivity.IN_VEHICLE: {
                    Log.e( "ActivityRecogition", "In Vehicle: " + activity.getConfidence() );
                    if( !(currentActivity.equals("driving")) && activity.getConfidence() >= 70 ) {
                        addToDatabase("driving");
                        Intent shareIntent = new Intent("msg");
                        shareIntent.putExtra("lastact",currentActivity);
                        currentActivity = "driving";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "Drive");
                        shareIntent.putExtra("timediff", lastDiff);
                        System.out.println("Driving message sent");
                        sendMessage(shareIntent);
                    }
                    break;
                }
                case DetectedActivity.RUNNING: {
                    Log.e( "ActivityRecogition", "Running: " + activity.getConfidence() );
                    if( !(currentActivity.equals("running")) && activity.getConfidence() >= 70 ) {
                        addToDatabase("running");
                        Intent shareIntent = new Intent("msg");
                        shareIntent.putExtra("lastact",currentActivity);
                        currentActivity = "running";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "Run");
                        shareIntent.putExtra("timediff", lastDiff);
                        System.out.println("Running message sent");
                        sendMessage(shareIntent);
                    }
                    break;
                }
                case DetectedActivity.STILL: {
                    Log.e( "ActivityRecogition", "Still: " + activity.getConfidence() );
                    if( !(currentActivity.equals("still")) && activity.getConfidence() >= 70 ) {
                        addToDatabase("still");
                        Intent shareIntent = new Intent("msg");
                        shareIntent.putExtra("lastact",currentActivity);
                        currentActivity = "still";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "Still");
                        shareIntent.putExtra("timediff", lastDiff);
                        System.out.println("Still message sent");
                        sendMessage(shareIntent);
                    }
                    break;
                }
                case DetectedActivity.WALKING: {
                    Log.e( "ActivityRecogition", "Walking: " + activity.getConfidence() );
                    if( !(currentActivity.equals("walking")) && activity.getConfidence() >= 70 ) {
                        addToDatabase("walking");
                        Intent shareIntent = new Intent("msg");
                        shareIntent.putExtra("lastact",currentActivity);
                        currentActivity = "walking";
                        shareIntent.putExtra(Intent.EXTRA_TEXT,"Walk");
                        shareIntent.putExtra("timediff", lastDiff);
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
