/************************************************************
 * Project: ouinfo
 * Program: RequirementsTextAdapter.java
 * Programmer: Yaw Asamoah
 * Initial Date: 12 June 2019
 * Updated Date: 11 September 2020
 * Description: Adapter for recyclerview used in ProgramInfo.java. Binds checkbox with reqInfo.
 *              On click, if all the checkboxes are clicked a toast message is shown.
 * ******************************************************/
package com.example.ouinfo;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RequirementsTextAdapter extends RecyclerView.Adapter<RequirementsTextAdapter.MyViewHolder>
{
    ArrayList<String> reqInfo;
    Context context;
    int checkNum = 0;
    String schoolColor;

    public RequirementsTextAdapter(Context ct, ArrayList<String> information, String color)
    {

        context = ct;
        reqInfo = information;
        schoolColor = color;
    }

    @NonNull
    @Override
    public RequirementsTextAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.req_row,parent,false);
        return new RequirementsTextAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RequirementsTextAdapter.MyViewHolder holder, final int position) {

        holder.checkbox.setText(reqInfo.get(position));

        holder.checkbox.setTextColor(holder.itemView.getResources().getColor(holder.itemView.getResources().getIdentifier(schoolColor+"2","color","com.example.ouinfo")));


        holder.checkbox.setBackgroundColor(holder.itemView.getResources().getColor(holder.itemView.getResources().getIdentifier(schoolColor,"color","com.example.ouinfo")));
        holder.checkbox.setButtonTintList(ColorStateList.valueOf(holder.itemView.getResources().getColor(holder.itemView.getResources().getIdentifier(schoolColor+"2","color","com.example.ouinfo"))));


        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (holder.checkbox.isChecked())
                {
                    checkNum++;

                    //checks if all checkboxes are clicked
                    if (checkNum == reqInfo.size())
                    {
                        Toast.makeText(context, "Congratulations! You Have All The Requirements Needed to Apply!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                  checkNum--;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return reqInfo.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder
    {
       CheckBox checkbox;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            checkbox = itemView.findViewById(R.id.checkBox);


        }
    }


}
