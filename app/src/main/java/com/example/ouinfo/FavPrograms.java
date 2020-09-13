/************************************************************
 * Project: ouinfo
 * Program: FavPrograms.java
 * Programmer: Yaw Asamoah
 * Initial Date: 12 June 2019
 * Updated Date: 11 September 2020
 * Description: Retrieves the program name (name), program url (setUrl) and program color (setColor)
 *              from shared preferences. Displays recyclerview list of each program based off
 *              those data and using programAdapter.
 * ******************************************************/
package com.example.ouinfo;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FavPrograms extends BaseActivity {

    //Declare variables
    TextView textView;
    RecyclerView favRecycle;
    ProgramAdapter adapt;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Set <String> favPrograms;
    ArrayList<String> programName,programUrl,programColor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //set layout file for this activity to be activity_fav_programs
        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_fav_programs,contentFrameLayout);

        //Initialize variables
        textView = findViewById(R.id.textView);
        favRecycle = findViewById(R.id.favRecycle);
        prefs = getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = prefs.edit();
        favPrograms = prefs.getStringSet("favPrograms", new HashSet<String>());

        //set toolbar title
        toolbar.setTitle("Favourites");
        setSupportActionBar(toolbar);

        //call setFavProgram method
        setFavProgram();

    } // end of on create


    //when user clicks back onto this activity, this method is called which ensures favRecycle is updated
    @Override
    protected void onRestart()
    {
        super.onRestart();
        //call setFavProgram method
        setFavProgram();
    }

    //Retrieves data from and sharedPreferences and adds it to its respective arrayList
    public void setFavProgram()
    {
        programName = new ArrayList<String>();
        programUrl = new ArrayList<String>();
        programColor = new ArrayList<String>();

        if (favPrograms.isEmpty())
        {
            favRecycle.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);

        }
        else
        {
            for (String name: favPrograms)
            {
                String progUrl = prefs.getString(name,"No Url");
                String progColor = prefs.getString(progUrl,"No Color");

                programName.add(name);
                programUrl.add(progUrl);
                programColor.add(progColor);

            }
            adapt = new ProgramAdapter(this,programName,programUrl,programColor);
            favRecycle.setAdapter(adapt);
            favRecycle.setLayoutManager(new LinearLayoutManager(FavPrograms.this));
            textView.setVisibility(View.GONE);
        }
    }
}
