package com.uzflsoft.vphapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;


public class MiddleMenu extends AppCompatActivity {

    static Context context;
    ExListAdapter expandableListAdapter;
    ExpandableListView expandableListView;

    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middlemenu);

        context = this;

        position = getIntent().getIntExtra("position", 0);


        expandableListView = (ExpandableListView) findViewById(R.id.exlistview);

        expandableListAdapter = new ExListAdapter(this, position);
        expandableListView.setAdapter(expandableListAdapter);


    }



}
