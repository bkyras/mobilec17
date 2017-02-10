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


public class ActivityRecognizedService extends IntentService {
    DBHelper mDbHelper = new DBHelper(this);
    long lastTime = new Date().getTime();
    long lastDiff = 0;
    String currentActivity = "";

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
                        if( currentActivity != "driving" && activity.getConfidence() >= 75 ) {
													addToDatabase("driving");
													if(currentActivity != "")
														Toast.makeText(this, "You were " + currentActivity + " for " + lastDiff, Toast.LENGTH_SHORT).show();
													currentActivity = "driving";
													Intent shareIntent = new Intent("msg");
													shareIntent.putExtra(Intent.EXTRA_TEXT, "Drive");
													//shareIntent.putExtra("timediff", lastDiff);
													System.out.println("Driving message sent");
													sendMessage(shareIntent);
											}
                    break;
                }
                case DetectedActivity.RUNNING: {
                    Log.e( "ActivityRecogition", "Running: " + activity.getConfidence() );
                    if( currentActivity != "running" && activity.getConfidence() >= 75 ) {
                        addToDatabase("running");
												if(currentActivity != "")
													Toast.makeText(this, "You were " + currentActivity + " for " + lastDiff, Toast.LENGTH_SHORT).show();
                        currentActivity = "running";
                        Intent shareIntent = new Intent("msg");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "Run");
                        System.out.println("Running message sent");
                        sendMessage(shareIntent);
                    }
                    break;
                }
                case DetectedActivity.STILL: {
                    Log.e( "ActivityRecogition", "Still: " + activity.getConfidence() );
                    if( currentActivity != "still" && activity.getConfidence() >= 75 ) {
                        addToDatabase("still");
												if(currentActivity != "")
													Toast.makeText(this, "You were " + currentActivity + " for " + lastDiff, Toast.LENGTH_SHORT).show();
                        currentActivity = "still";
                        Intent shareIntent = new Intent("msg");
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "Stand");
                        System.out.println("Still message sent");
                        sendMessage(shareIntent);
                    }
                    break;
                }
                case DetectedActivity.WALKING: {
                    Log.e( "ActivityRecogition", "Walking: " + activity.getConfidence() );
                    if( currentActivity != "walking" && activity.getConfidence() >= 75 ) {
                        addToDatabase("walking");
												if(currentActivity != "")
													Toast.makeText(this, "You were " + currentActivity + " for " + lastDiff, Toast.LENGTH_SHORT).show();
                        currentActivity = "walking";
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
