package com.example.kyra.testbed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] posts = {"pizza", "pasta", "chocolate"};
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.post_view, posts);
        ListView listView = (ListView) findViewById(R.id.feedList);
        listView.setAdapter(adapter);
    }
}
