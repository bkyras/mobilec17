package com.example.rgb.feedme;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import static com.example.rgb.feedme.DBHelper.TABLE_NAME;

/**
 * Created by bsheridan on 2/25/2017.
 */

public class PostAdapter extends ArrayAdapter<Post> {
    DBHelper dbHelper;

    public PostAdapter(Context context, ArrayList<Post> posts) {
        super(context, 0, posts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        dbHelper = new DBHelper(getContext());

        final Post p = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.post_in_feed_view, parent, false);
        }

        TextView eventName = (TextView) convertView.findViewById(R.id.eventNameID);
        TextView foodType = (TextView) convertView.findViewById(R.id.foodTypeID);
        final TextView upvotes = (TextView) convertView.findViewById(R.id.upvotesID);

        eventName.setText(p.eventTitle);
        foodType.setText(p.foodType);
        upvotes.setText("Votes: " + p.upvotes);

        final ToggleButton upvote = (ToggleButton) convertView.findViewById(R.id.upvote_btn);
        final ToggleButton downvote = (ToggleButton) convertView.findViewById(R.id.downvote_btn);

        upvote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    if(downvote.isChecked()){
                        p.upvotes += 1;
                        downvote.setChecked(false);
                    }
                    else{
                        p.upvotes += 1;
                    }
                }
                else{
                    p.upvotes -= 1;
                }
                upvotes.setText("Votes: " + p.upvotes);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("UPDATE " + TABLE_NAME + " SET upvotes = "+ p.upvotes + " WHERE rowid = " + p.postID);


            }
        });
        downvote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    if(upvote.isChecked()){
                        p.upvotes -= 1;
                        upvote.setChecked(false);
                    }
                    else{
                        p.upvotes -= 1;
                    }
                }
                else{
                    p.upvotes += 1;
                }
                upvotes.setText("Votes: " + p.upvotes);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                db.execSQL("UPDATE " + TABLE_NAME + " SET upvotes = "+ p.upvotes + " WHERE rowid = " + p.postID);

            }
        });



        return convertView;
    }
}
