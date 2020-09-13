/************************************************************
 * Project: ouinfo
 * Program: ProgramAdapter.java
 * Programmer: Yaw Asamoah
 * Initial Date: 12 June 2019
 * Updated Date: 11 September 2020
 * Description: Adapter for recyclerview used in Programs.java and FavPrograms.java. Binds text view (title) with
 *              programNames and sets the text color to schoolColor. On click programName,
 *              programUrl and schoolColor are passed to Programs.java
 * ******************************************************/
package com.example.ouinfo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.MyViewHolder> implements Filterable
{
    ArrayList<String> programUrl;
    ArrayList<String> programNames;
    Context context;
    String schoolColor;
    ArrayList<String> programNamesFull;
    ArrayList<String> programUrlFull;
    ArrayList<String> filteredUrl = new ArrayList<String>();
    ArrayList<String> colorArray = new ArrayList<String> ();

    public ProgramAdapter(Context ct, ArrayList<String> name, ArrayList<String> link,String color)
    {

        context = ct;
        programNames = name;
        programUrl = link;
        schoolColor = color;

        programNamesFull = new ArrayList<>(name);
        programUrlFull = new ArrayList<>(link);
    }

    public ProgramAdapter(Context ct, ArrayList<String> name, ArrayList<String> link, ArrayList<String> color)
    {

        context = ct;
        programNames = name;
        programUrl = link;
        colorArray = color;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        CardView programLayout;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            title = itemView.findViewById(R.id.programTitle);
            programLayout = itemView.findViewById(R.id.programLayout);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.program_row,parent,false);
        return new ProgramAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        if (!colorArray.isEmpty())
        {
            schoolColor = colorArray.get(position);
        }

        holder.title.setText(programNames.get(position));
        holder.title.setTextColor(holder.itemView.getResources().getColor(holder.itemView.getResources().getIdentifier(schoolColor+"2","color","com.example.ouinfo")));
        holder.programLayout.setCardBackgroundColor(holder.itemView.getResources().getColor(holder.itemView.getResources().getIdentifier(schoolColor,"color","com.example.ouinfo")));

        holder.programLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ProgramInfo.class);

                if (!colorArray.isEmpty())
                {
                    schoolColor = colorArray.get(position);
                }

                intent.putExtra("programUrl",programUrl.get(position));
                intent.putExtra("programName",programNames.get(position));
                intent.putExtra("schoolColor",schoolColor);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return programNames.size();
    }

    @Override
    public Filter getFilter() {
        return programNameFilter;
    }

    private Filter programNameFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList <String> filteredNames  = new ArrayList<>();
            filteredUrl = new ArrayList<String>();
            if (charSequence == null || charSequence.length() == 0)
            {
                filteredNames.addAll(programNamesFull);

                filteredUrl.addAll(programUrlFull);

            }
            else
            {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (int i = 0; i < programNamesFull.size(); i++)
                {
                    if (programNamesFull.get(i).toLowerCase().contains(filterPattern))
                    {
                        filteredNames.add(programNamesFull.get(i));
                        filteredUrl.add(programUrlFull.get(i));
                    }
                }

            }
            FilterResults results = new FilterResults();
            results.values = filteredNames;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults)
        {
            programNames.clear();
            programNames.addAll((ArrayList) filterResults.values);

            programUrl.clear();
            programUrl.addAll(filteredUrl);
            notifyDataSetChanged();
        }
    };

}
