package com.example.rgb.feedme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by bsheridan on 2/25/2017.
 */

public class PostAdapter extends ArrayAdapter<Post> {

    public PostAdapter(Context context, ArrayList<Post> posts) {
        super(context, 0, posts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Post p = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.post_in_feed_view, parent, false);
        }

        TextView eventName = (TextView) convertView.findViewById(R.id.eventNameID);
        TextView foodType = (TextView) convertView.findViewById(R.id.foodTypeID);
        final TextView upvotes = (TextView) convertView.findViewById(R.id.upvotesID);

        eventName.setText(p.eventTitle + ": ");
        foodType.setText(p.foodType);
        upvotes.setText("Votes: " + p.upvotes);

        ToggleButton vote = (ToggleButton) convertView.findViewById(R.id.vote_btn);

        vote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    int votes = p.upvotes + 1;
                    upvotes.setText("Votes: " + votes);
                } else {
                    upvotes.setText("Votes: " + p.upvotes);
                }
            }
        });

        return convertView;
    }
}
