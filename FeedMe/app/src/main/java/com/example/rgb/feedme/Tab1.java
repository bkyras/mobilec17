package com.example.rgb.feedme;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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


    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v  = inflater.inflate(R.layout.tab1, container, false);

        Post p = new Post();
        p.eventTitle = "Free Pizza Party";
        p.foodType = "Pizza";
        p.description = "There is lots of good free food time fun here at pizza land";

        Post p2 = new Post();
        p2.eventTitle = "Free Cookie Party";
        p2.foodType = "Cookies";
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
            }});

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post item = (Post) parent.getItemAtPosition(position);

                FragmentManager fm = getFragmentManager();
               // PostDetail pd = newInstance(item);
               // pd.show(fm, "Post Add Fragment");

            }
        });
        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return v;
    }

    public static PostDetail newInstance(String post) {
        PostDetail f = new PostDetail();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("post", post);
        f.setArguments(args);
        return f;
    }




}
