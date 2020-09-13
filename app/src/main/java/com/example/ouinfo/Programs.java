/************************************************************
 * Project: ouinfo
 * Program: Programs.java
 * Programmer: Yaw Asamoah
 * Initial Date: 12 June 2019
 * Updated Date: 11 September 2020
 * Description: Parses webUrl and retrieves the name and url of each program and store each in its
 *              respective arraylist(programName and link). Displays recyclerview list of each program based off
 *              programName,link and schoolColor.
 * ******************************************************/
package com.example.ouinfo;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;

public class Programs extends BaseActivity
{
    //Declare variables
    RecyclerView recyclerView;
    ProgressBar progressBar;
    MenuItem searchItem;
    ProgramAdapter adapter;
    ArrayList<String> link = new ArrayList<>();
    ArrayList<String> programName = new ArrayList<>();
    String webUrl;
    String colorString;
    int schoolColorMain,schoolColorSecondary;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //set layout file for this activity to be activity_programs
        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_programs,contentFrameLayout);

        //Initialize variables
        recyclerView = findViewById(R.id.recyclerview);
        progressBar = findViewById(R.id.progressBar);
        colorString = getIntent().getStringExtra("resName");
        schoolColorMain = getResources().getColor(this.getResources().getIdentifier(colorString,"color",this.getPackageName()));
        schoolColorSecondary = getResources().getColor(this.getResources().getIdentifier(colorString+"2","color",this.getPackageName()));

        //combines main website url and the university ending
        webUrl = getResources().getString(R.string.main_url)+ getIntent().getStringExtra("schoolUrl");

        //Set Toolbar title,background color and text color
        toolbar.setTitle(getIntent().getStringExtra("schoolName"));
        toolbar.setBackgroundColor(schoolColorSecondary);
        toolbar.setTitleTextColor(schoolColorMain);
        setSupportActionBar(toolbar);

        //set navigation drawer toggle button (hamburger icon) color
        toggle.getDrawerArrowDrawable().setColor(schoolColorMain);

        new Content().execute();


    }

    private class Content extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                //Connect to the website
                Document doc = Jsoup.connect(webUrl).get();

                boolean multiUrl = false;

                //checks if there are any letter ranges that the programs are split into
                Elements repositories = doc.getElementsByClass("pagination");

                String[] range = new String[1];

                //if there are letter ranges this code will run
                for (Element repository : repositories)
                {
                    Elements link = repository.select("a[href]");

                    //array of the different letter ranges used
                    range = link.text().split(" ");

                    //sets that the programs are split into multiple ranges
                    multiUrl = true;
                    break;
                }

                for (int x = 0; x < range.length; x++)
                {
                    String currentUrl = webUrl;

                    if (multiUrl)
                    {
                        //sets url to specific letter range
                        currentUrl = webUrl + "?group=" + range[x].toLowerCase();
                    }

                    doc = Jsoup.connect(currentUrl).get();
                    Elements repo = doc.getElementsByClass("result result-program");
                    for (Element repository : repo)
                    {
                        //program title
                        String program = (repository.getElementsByClass("result-heading").text());
                        //program url
                        String programUrl = repository.select("a[href]").first().attr("href");
                        programName.add(program);
                        link.add(programUrl);
                    }
                }
            }//end of try

            catch (IOException e)
            {
                e.printStackTrace();
            }  //end of catch

            return null;

        }// end of do in background

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            adapter = new ProgramAdapter(Programs.this,programName,link,colorString);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(Programs.this));
            searchItem.setVisible(true);

        }//end of on post execute

    }// end of async task

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.programs_menu,menu);

        searchItem = menu.findItem((R.id.action_search));
        SearchView searchView = (SearchView) searchItem.getActionView();

        //change enter icon on keyboard to done icon
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        //set color of editText of seachView
        EditText txtSearch = ((EditText)searchView.findViewById(androidx.appcompat.R.id.search_src_text));
        txtSearch.setTextColor(schoolColorMain);
        txtSearch.setHintTextColor(schoolColorMain);
        txtSearch.setHint("Search");

        //set color for search icon (ic__search)
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.ic_search);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable,schoolColorMain );
        searchItem.setIcon(wrappedDrawable);

        //set color of close button for searchView
        ImageView searchClose = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        searchClose.setColorFilter(schoolColorMain);

        //listener for when typing in searchView textbox
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               adapter.getFilter().filter(newText);
               return false;
            }
        });
        return true;
    }
}
