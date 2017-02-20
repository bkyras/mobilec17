package com.example.rgb.feedme;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Rayan on 2/20/2017.
 */

public class AddPost  extends DialogFragment {


    // Empty constructor
    public AddPost(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.add_post, container, false);
        return view;
    }


}
