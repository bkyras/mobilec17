package com.example.rgb.feedme;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
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
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

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
    String sort_option ;
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
        //String title, String food, String location, String desc, int votes, double lat, double longi
        //42.274495, -71.807911
        addDummyPost("Nutella with ISC", "Nutella", "Fountain", "Come enjoy nutella on foods with ISC at the fountain.", 3, 42.274495, -71.807911);
        //42.274952, -71.806562
        addDummyPost("UPE's Burning the Midnight Oil", "Pancakes, snacks", "Fuller Commons", "Study and eat free food!", 2, 42.274952,  -71.806562);
        //42.273501, -71.810576
        addDummyPost("Pizza for People", "Pizza!!!", "Wedge", "EAT PIZZA WITH US, REAL PEOPLE, TODAY, RIGHT NOW", 4, 42.273501, -71.810576);
        //42.275697, -71.805313
        addDummyPost("Crab Hunters", "Pizza and crabs", "Institute Park", "Free crabs", -2, 42.275697, -71.805313);
        //42.274716, -71.808339
        addDummyPost("Free Bagels by Hillel", "Bagels", "Campus Center", "Take a break from studying and enjoy free bagels", 1, 42.274716, -71.808339);

//        SQLiteDatabase wdb = dbHelper.getWritableDatabase();
//        dbHelper.onUpgrade(wdb, 1, 2);
        //listPostDetails();
        sort_option = "recent";

        listOrdered(sort_option);

        ListView listView = (ListView) v.findViewById(R.id.feedList);

        FloatingActionButton addButton = (FloatingActionButton) v.findViewById(R.id.addPost_btn);
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
                        listOrdered(sort_option);
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
                listOrdered(sort_option);            }
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


        //sort drop down
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
                switch (input) {

                    case "Event Title":
                        sort_option = "eventTitle";
                        break;
                    case "Recent":
                        sort_option = "recent";
                        break;
                    case "Food Type":
                        sort_option = "foodType";
                        break;
                    case "Location":
                        sort_option = "location";
                        break;
                    case "Votes":
                        sort_option = "upvotes";
                        break;


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

    public void addDummyPost(String title, String food, String location, String desc, int votes, double lat, double longi) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("eventTitle", title);
        values.put("foodType", food);
        values.put("location", location);
        values.put("latitude", lat);
        values.put("longitude", longi);
        //values.put("time", time.getText().toString());
        values.put("time", 3600);
        values.put("description", desc);
        values.put("upvotes", votes);

        db.insert("FeedMePosts", null, values);
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
            p.upvotes = upvotes;
            p.postID = id;
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
                "upvotes",
                "rowid"
        };

        // Filter results WHERE "title" = 'My Title'
        //String selection = "*"
        //String[] selectionArgs = { "My Title" };
        String sortOrder;
        // How you want the results sorted in the resulting Cursor
        if(sorter.equals("upvotes")){
             sortOrder = sorter + " DESC";

        }
        else if(sorter.equals("recent")){
            sortOrder = "ROWID DESC";
        }
        else{
             sortOrder = sorter + " ASC";

        }
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
        while (cursor.moveToNext()) {
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
            p.upvotes = upvotes;

            p.postID = id;
            newPosts.add(p);
        }
        cursor.close();
        PostAdapter adapter = new PostAdapter(getContext(), newPosts);
        ListView listView = (ListView) v.findViewById(R.id.feedList);
        listView.setAdapter(adapter);
    }

    public void addUpvote() {
        ArrayList<Post> newPosts = new ArrayList<Post>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {"upvotes"};
    }


}
