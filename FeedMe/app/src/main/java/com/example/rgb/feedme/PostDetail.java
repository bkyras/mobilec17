package com.example.rgb.feedme;


import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ggao on 2/19/2017.
 */


public class PostDetail extends DialogFragment {
    // Empty constructor

    public PostDetail(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle input) {

        View v  = inflater.inflate(R.layout.fragment_post_detail, container, false);

        input = getArguments();
        String post = input.getString("post");

        TextView textview = (TextView) getView().findViewById(R.id.foodText);

        textview.setText(post);

        System.out.println("Creating a view");

        return v;
    }


}