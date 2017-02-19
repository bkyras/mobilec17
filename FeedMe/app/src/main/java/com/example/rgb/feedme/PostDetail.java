package com.example.rgb.feedme;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ggao on 2/19/2017.
 */

public class PostDetail extends Fragment {


    // Empty constructor

    public PostDetail(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_post_detail, container, false);
        return view;
    }
}