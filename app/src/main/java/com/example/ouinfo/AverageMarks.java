/************************************************************
 * Project: ouinfo
 * Program: AverageMarks.java
 * Programmer: Yaw Asamoah
 * Initial Date: 12 June 2019
 * Updated Date: 11 September 2020
 * Description: Sets adapter for each spinner and if user selects the non default value (N/A) then
 *              the corresponding edit text becomes visible. Based on how man edit text are visible
 *              and the number in each edit text the average is calculated and shown upon pressing
 *              calc button.
 * ******************************************************/
package com.example.ouinfo;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;


public class AverageMarks extends BaseActivity implements OnItemSelectedListener {

    //Declare variables
    Spinner [] courseSpinner;
    EditText [] gradeEditText;
    Button calc;
    TextView avgText;
    ArrayAdapter<String> adapter;
    int marks;
    int numOfClass;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //set layout file for this activity to be activity_average_programs
        FrameLayout contentFrameLayout = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(R.layout.activity_average_marks, contentFrameLayout);

        //Initialize variables
        calc = findViewById(R.id.calc);
        avgText = findViewById(R.id.avg);
        courseSpinner = new Spinner[6];
        gradeEditText = new EditText[6];

        courseSpinner[0] = findViewById(R.id.course1);
        courseSpinner[1] = findViewById(R.id.course2);
        courseSpinner[2] = findViewById(R.id.course3);
        courseSpinner[3] = findViewById(R.id.course4);
        courseSpinner[4] = findViewById(R.id.course5);
        courseSpinner[5] = findViewById(R.id.course6);

        gradeEditText[0] = findViewById(R.id.courseMark1);
        gradeEditText[1] = findViewById(R.id.courseMark2);
        gradeEditText[2] = findViewById(R.id.courseMark3);
        gradeEditText[3] = findViewById(R.id.courseMark4);
        gradeEditText[4] = findViewById(R.id.courseMark5);
        gradeEditText[5] = findViewById(R.id.courseMark6);

        //Set toolbar title
        toolbar.setTitle("Average Calculator");
        setSupportActionBar(toolbar);

        // set up adapter for spinner
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.course));
        adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);

        // for each spinner view, the adapter is set and an onclick listener is attached
        for (Spinner spin: courseSpinner)
        {
            spin.setAdapter(adapter);
            spin.setOnItemSelectedListener(this);
        }

        calc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                marks = 0;
                numOfClass = 0;
                for (int y = 0; y < gradeEditText.length; y++)
                {
                    String editText = gradeEditText[y].getText().toString();
                    if (!editText.isEmpty())
                    {
                       marks = marks + Integer.valueOf(editText);
                       numOfClass++;
                    }

                }

                if (numOfClass == 0)
                {
                    Toast.makeText(AverageMarks.this,"No Course Added",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    double avg = (double) marks/numOfClass;
                    avgText.setText(String.valueOf(avg));
                }

            }
        });


        }// end of oncreate

    @Override
    public void onItemSelected(AdapterView<?> parent,
                               View selectedItemView, int position, long id)
    {
        for (int x = 0; x < courseSpinner.length; x++)
        {
            if (courseSpinner[x].getId() == parent.getId())
            {
                if (courseSpinner[x].getSelectedItem().toString().equalsIgnoreCase("N/A"))
                {
                    gradeEditText[x].setVisibility(View.INVISIBLE);
                }
                else
                {
                    gradeEditText[x].setVisibility(View.VISIBLE);
                }
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {

    }
}


