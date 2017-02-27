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
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.rgb.feedme.Tab2.dropPins;
import static com.example.rgb.feedme.Tab2.mMap;

/**
 * Created by Rayan on 2/18/2017.
 */

public class Tab1 extends android.support.v4.app.Fragment {
    public static ArrayList<Post> newPosts;
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
        SQLiteDatabase wdb = dbHelper.getWritableDatabase();
        //dbHelper.onUpgrade(wdb, 1, 2);
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
                        if(mMap != null){
                            mMap.clear();
                            dropPins(mMap,newPosts);
                        }
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


        Spinner spinner = (Spinner) v.findViewById(R.id.sort_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.sort_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){



            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String input = parent.getItemAtPosition(position).toString();
                String sort_option;
                switch (input) {

                    case "Event Title":
                        sort_option = "eventTitle";
                        break;
                    case "Food":
                        sort_option = "foodType";
                        break;
                    case "Location":
                        sort_option = "location";
                        break;
                    case "Votes":
                        sort_option = "upvotes";
                        break;
                    case "Time":
                        sort_option = "time";
                        break;
                    default:
                        sort_option = "eventTitle";
                }
                listOrdered(sort_option);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }});
        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return v;
    }


    public static PostDetail passArgs(Post post) {
        PostDetail pd = new PostDetail();

        String eventTitle = post.eventTitle;
        String foodType = post.foodType;
        double longitude = post.longitude;
        double latitude = post.latitude;
        String location = post.location;
        String time = post.time;
        String description = post.description;
        int upvotes = post.upvotes;

        Bundle args = new Bundle();
        args.putString("eventTitle", eventTitle);
        args.putString("location", location);
        args.putString("foodType", foodType);
        args.putDouble("long", longitude);
        args.putDouble("lat", latitude);
        args.putString("time", time);
        args.putString("description",description);
        args.putInt("upvotes", upvotes);

        pd.setArguments(args);
        return pd;
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
                "upvotes"
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

    public  void listOrdered(String sorter) {
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
                "upvotes"
        };

        // Filter results WHERE "title" = 'My Title'
        //String selection = "*"
        //String[] selectionArgs = { "My Title" };

        // How you want the results sorted in the resulting Cursor
         String sortOrder = sorter + " DESC";
        //FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

        Cursor cursor = db.query(
                "FeedMePosts",                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
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
