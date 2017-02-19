package com.example.rgb.feedme;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Rayan on 2/18/2017.
 */

public class Tab1 extends android.support.v4.app.Fragment {



    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v  = inflater.inflate(R.layout.tab1, container, false);


        String[] posts = {"pizza", "pasta", "chocolate", "pizza", "pasta", "chocolate","pizza", "pasta", "chocolate", "pizza", "pasta", "chocolate"};
        ArrayAdapter adapter = new ArrayAdapter<String>(getContext(), R.layout.post_view, posts);
        ListView listView = (ListView) v.findViewById(R.id.feedList);
        listView.setAdapter(adapter);

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return v;
    }





}