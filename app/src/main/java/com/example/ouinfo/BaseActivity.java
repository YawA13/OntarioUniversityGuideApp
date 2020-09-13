/************************************************************
 * Project: ouinfo
 * Program: BaseActivity.java
 * Programmer: Yaw Asamoah
 * Initial Date: 12 June 2019
 * Updated Date: 11 September 2020
 * Description: Activity that all other activities will extend from. Sets up the navigation drawer
 *              which redirects each on click of the 3 menu items to their respective activities.
 *              However no new activity is started when menu item clicked and activity are the same.
 * ******************************************************/
package com.example.ouinfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import java.util.Arrays;

public class BaseActivity extends AppCompatActivity {

    // Declare variables
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    NavigationView navView;
    Toolbar toolbar;
    Intent activity;
    String className;
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        //Initialize variables
        navView = findViewById(R.id.navigation);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Open, R.string.Close);
        className = this.getClass().getSimpleName();

        drawerLayout.addDrawerListener(toggle);
        setSupportActionBar(toolbar);

        // set navigation drawer menu listener
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                id = item.getItemId();
                switch (id) {
                    case R.id.schoolTab:
                        if (!Arrays.asList("MainActivity", "Programs", "Programinfo").contains(className))
                        {
                            activity = new Intent(BaseActivity.this, MainActivity.class);
                            startActivity(activity);
                        }
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.favTab:
                        if (!className.equalsIgnoreCase("FavPrograms"))
                        {
                            activity = new Intent(BaseActivity.this, FavPrograms.class);
                            startActivity(activity);
                        }
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.marksTab:
                        if (!className.equalsIgnoreCase("AverageMarks"))
                        {
                            activity = new Intent(BaseActivity.this, AverageMarks.class);
                            startActivity(activity);
                        }
                        drawerLayout.closeDrawers();
                        break;

                }
                return false;
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        return toggle.onOptionsItemSelected(item);
    }
}

