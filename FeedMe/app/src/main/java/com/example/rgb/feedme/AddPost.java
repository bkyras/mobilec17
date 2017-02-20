package com.example.rgb.feedme;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Rayan on 2/20/2017.
 */

public class AddPost extends android.support.v4.app.Fragment{


    // Empty constructor

    static AddPost newInstance() {
        return new AddPost();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.add_post, container, false);
        return view;
    }


}
