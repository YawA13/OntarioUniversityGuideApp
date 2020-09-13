/************************************************************
 * Project: ouinfo
 * Program: ProgramInfo.java
 * Programmer: Yaw Asamoah
 * Initial Date: 12 June 2019
 * Updated Date: 11 September 2020
 * Description: Parses webUrl and retrieves information and requirements of the program
 *              (chosen by user). Displays the info of the program the user selected in a textview
 *              (infoText) and program requirements in a recyclerview(reqList). The data above is
 *              also saved in a shared prefences,if the user clicks on the star icon (star)
 * ******************************************************/
package com.example.ouinfo;


import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import android.content.Intent;
import android.net.Uri;


public class ProgramInfo extends BaseActivity {

    //Declare variables
    RecyclerView reqList;
    TextView infoText;
    Button button;
    ProgressBar progressBar;
    CardView infoCard, reqCard;
    TextView infoTitle,reqTitle;
    MenuItem star;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    RequirementsTextAdapter reqAdapt;
    boolean clickedAlready;
    Set<String> favPrograms;
    String programName,programUrl;
    ArrayList<String> reqInfo = new ArrayList<String>();
    ArrayList <String> programInfo = new ArrayList<String>();
    String webUrl;
    String colorString;
    int schoolColorMain,schoolColorSecondary;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //set layout file for this activity to be activity_program_info
        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_program_info,contentFrameLayout);

        // Initialize variables
        progressBar = findViewById(R.id.progressBar2);
        reqList = findViewById(R.id.reqList);
        infoText = findViewById(R.id.infoText);
        button = findViewById(R.id.viewMoreButton);
        infoCard = findViewById(R.id.infoCard);
        reqCard = findViewById(R.id.reqCard);
        infoTitle = findViewById(R.id.infoTitle);
        reqTitle = findViewById(R.id.reqTitle);
        colorString = getIntent().getStringExtra("schoolColor");
        schoolColorMain = getResources().getColor(this.getResources().getIdentifier(colorString,"color",this.getPackageName()));
        schoolColorSecondary = getResources().getColor(this.getResources().getIdentifier(colorString+"2","color",this.getPackageName()));
        prefs = getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = prefs.edit();
        favPrograms = prefs.getStringSet("favPrograms", new HashSet<String>());
        programName = getIntent().getStringExtra("programName");
        programUrl = getIntent().getStringExtra("programUrl");

        //combines main website url and the specific program url
        webUrl = getResources().getString(R.string.main_url)+ programUrl;

        //set background color of both cardViews
        infoCard.setCardBackgroundColor(schoolColorMain);
        reqCard.setCardBackgroundColor(schoolColorMain);

        //set text color of titles in both cardView and the title of info card
        infoTitle.setTextColor(schoolColorSecondary);
        reqTitle.setTextColor(schoolColorSecondary);
        infoText.setTextColor(schoolColorSecondary);

        //set navigation drawer toggle button (hamburger icon) color
        toggle.getDrawerArrowDrawable().setColor(schoolColorMain);

        //set toolbar title,background color and text color
        toolbar.setTitle(programName);
        toolbar.setBackgroundColor(schoolColorSecondary);
        toolbar.setTitleTextColor(schoolColorMain);
        setSupportActionBar(toolbar);

        //on click of button, opens weburl in new tab in user browser
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Uri uriUrl = Uri.parse(webUrl);
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });

        new Content().execute();

    }


    private class Content extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            String title,detail;
            try
            {
                Document doc = Jsoup.connect(webUrl).get();
                Elements list = doc.getElementsByClass("tabbed-region");
                for (Element lists : list)
                {

                    Elements repo = lists.getElementsByClass("tabbed-subsection");
                    Elements repositories = repo.select("dd");
                    Elements tab = repo.select("dt");

                    for (int i = 0;i<tab.size();i++)
                    {
                        title = tab.get(i).select("dt").text();
                        detail = repositories.get(i).select("dd").text();
                        programInfo.add(title+": "+detail);
                    }

                }

                //only one element so only first element needed
                Element requirements = doc.getElementById("requirements").getElementsByClass("tabbed-subsection").first();
                Elements p = requirements.select("p");
                Elements li = requirements.select("li");

                for (Element liInfo : li)
                {
                    reqInfo.add(liInfo.select("li").text());
                }

            }// end of try

            catch (IOException e)
            {
                e.printStackTrace();
            } // end of catch

            return null;
        } //end of do in background

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            String data = "";
            for (int i = 0;i<programInfo.size();i++)
            {

                data = data+"\n"+programInfo.get(i)+"\n";

            }

            infoText.setText(data);
            reqAdapt = new RequirementsTextAdapter(ProgramInfo.this,reqInfo,colorString);
            reqList.setAdapter(reqAdapt);
            reqList.setLayoutManager(new LinearLayoutManager(ProgramInfo.this));
            progressBar.setVisibility(View.GONE);
            star.setVisible(true);
            star.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem)
                {

                    if (clickedAlready)
                    {
                        star.setIcon(R.drawable.ic_star_border);
                        clickedAlready = false;
                        favPrograms.remove(programName);
                        editor.remove(programName);
                        editor.remove(programUrl);
                    }

                    else
                    {
                        star.setIcon(R.drawable.ic_star);
                        clickedAlready = true;
                        favPrograms.add(programName);
                        editor.putString(programName,programUrl);
                        editor.putString(programUrl,colorString);
                    }

                    editor.putStringSet("favPrograms", favPrograms);
                    editor.apply();
                    return false;
                }
            });

        } // end of post execute
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fav_menu,menu);
        star = menu.findItem(R.id.star);

        //checks if programName is saved as one of the favourite programs
        if (favPrograms.contains(programName))
        {
            star.setIcon(R.drawable.ic_star);
            clickedAlready = true;
        }
        clickedAlready = false;
        return super.onCreateOptionsMenu(menu);
    }
}
