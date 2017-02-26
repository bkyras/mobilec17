package com.example.rgb.feedme;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rayan on 2/18/2017.
 */

public class Tab1 extends android.support.v4.app.Fragment {

    DBHelper dbHelper;
    View v;

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("resuming!");
    }
    //Overridden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("In tab1 oncreate view");
        Log.d("Tag","In tab1");

        v  = inflater.inflate(R.layout.tab1, container, false);

        dbHelper = new DBHelper(getContext());
        listPostDetails();
        ListView listView = (ListView) v.findViewById(R.id.feedList);

        Button addButton = (Button) v.findViewById(R.id.addPost_btn);
        Button delButton = (Button) v.findViewById(R.id.wipeDb_btn);


        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager fm = getFragmentManager();
                AddPost ad = new AddPost();
                ad.show(fm, "Add Post Fragment");
                fm.executePendingTransactions();
                ad.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        listPostDetails();
                    }
                });

            }});

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("delete from FeedMePosts");
                listPostDetails();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post item = (Post) parent.getItemAtPosition(position);
                FragmentManager fm = getFragmentManager();
                Log.d("New","About to get new instance");
                PostDetail pd = passArgs(item);
                pd.show(fm, "Post Add Fragment");

            }
        });
        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return v;
    }


    public static PostDetail passArgs(Post post) {
        PostDetail f = new PostDetail();

        String eventTitle = post.eventTitle;
        String foodType = post.foodType;
        double longitude = post.longitude;
        double latitude = post.latitude;
        String location = post.location;
        String time = post.time;
        String description = post.description;

        Bundle args = new Bundle();
        args.putString("eventTitle", eventTitle);
        args.putString("location", location);
        args.putString("foodType",foodType);
        args.putDouble("long",longitude);
        args.putDouble("lat",latitude);
        args.putString("time", time);
        args.putString("description",description);

        f.setArguments(args);
        return f;
    }

    public void listPostDetails() {
        ArrayList<Post> newPosts = new ArrayList<Post>();
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
                "description"
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
                null                                 // The sort order
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

            Post p = new Post();
            p.eventTitle = title;
            p.foodType = food;
            p.location = loc;
            p.latitude = lat;
            p.longitude = lon;
            p.time = time;
            p.description = description;
            newPosts.add(p);
        }
        cursor.close();
        PostAdapter adapter = new PostAdapter(getContext(), newPosts);
        ListView listView = (ListView) v.findViewById(R.id.feedList);
        listView.setAdapter(adapter);
    }


}
