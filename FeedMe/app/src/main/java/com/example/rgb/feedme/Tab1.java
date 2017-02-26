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

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("resuming!");
    }
    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("In tab1 oncreate view");
        Log.d("Tag","In tab1");

        View v  = inflater.inflate(R.layout.tab1, container, false);

        dbHelper = new DBHelper(getContext());
        Post p = new Post();
        p.eventTitle = "Free Pizza Party";
        p.foodType = "Pizza";
        p.location = "Fountain";
        p.longitude = 2.0;
        p.latitude = 2.0;
        p.time = "20";
        p.description = "There is lots of good free food time fun here at pizza land";

        Post p2 = new Post();
        p2.eventTitle = "Free Cookie Party";
        p2.foodType = "Cookies";
        p2.location = "Fountain";
        p2.longitude = 2.0;
        p2.latitude = 2.0;
        p2.time = "20";
        p2.description = "There is lots of good free food time fun here at cookie land";

        ArrayList<Post> posts = new ArrayList<Post>();
        posts.add(p);
        posts.add(p2);

        PostAdapter adapter = new PostAdapter(getContext(), posts);
        ListView listView = (ListView) v.findViewById(R.id.feedList);
        listView.setAdapter(adapter);

        Button addButton = (Button) v.findViewById(R.id.addPost_btn);


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
                        SQLiteDatabase db = dbHelper.getReadableDatabase();

                        // Define a projection that specifies which columns from the database
                        // you will actually use after this query.
                        String[] projection = {
                                "eventTitle",
                                "foodType"
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
                            System.out.println(title);
                            System.out.println(food);
                        }
                        cursor.close();
                    }
                });
            }});

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




}
