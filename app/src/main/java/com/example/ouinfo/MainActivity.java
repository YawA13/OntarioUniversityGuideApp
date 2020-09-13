/************************************************************
 * Project: ouinfo
 * Program: MainActivity.java
 * Programmer: Yaw Asamoah
 * Initial Date: 12 June 2019
 * Updated Date: 11 September 2020
 * Description: Displays recyclerview of each ontario university with both school image and name.
 * ******************************************************/

package com.example.ouinfo;

import android.os.Bundle;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
public class MainActivity extends BaseActivity
{

    //Declare variables
    RecyclerView recyclerView;
    UniversityAdapter adapter;
    String[] universityName;
    String[] universityUrl;
    String[] resName;
    int[] images;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //set layout file for this activity to be activity_main
        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_main, contentFrameLayout);

        //Initialize variables
        universityName = getResources().getStringArray(R.array.university_name);
        universityUrl = getResources().getStringArray(R.array.university_url);
        resName = getResources().getStringArray(R.array.resNameArray);
        recyclerView = findViewById(R.id.recyclerview);
        images = new int[resName.length];

        //set toolbar title
        setSupportActionBar(toolbar);
        toolbar.setTitle("Universities");

        //sets image array to the drawable resource based on string resource resName
        for (int i = 0; i < resName.length; i++)
        {
            images[i] = this.getResources().getIdentifier(resName[i], "drawable", this.getPackageName());
        }

        adapter = new UniversityAdapter(MainActivity.this, universityName,
                universityUrl, images, resName);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


}
