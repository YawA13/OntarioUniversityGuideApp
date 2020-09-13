/************************************************************
 * Project: ouinfo
 * Program: UniversityAdapter.java
 * Programmer: Yaw Asamoah
 * Initial Date: 12 June 2019
 * Updated Date: 11 September 2020
 * Description: Adapter for recyclerview used in MainActivity.java. Binds image view(img)
 *              and text view (title) with schoolName and images. On click schoolName, schoolUrl
 *              and resName are passed to Programs.java
 * ******************************************************/
package com.example.ouinfo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class UniversityAdapter extends RecyclerView.Adapter <UniversityAdapter.MyViewHolder>
{

    String schoolNames[];
    String schoolUrl [];
    int images[];
    Context context;
    String [] resName;

    public UniversityAdapter(Context ct, String uniNames[], String webUrl [], int img[],String [] resN)
    {
        context = ct;
        schoolNames = uniNames;
        images = img;
        schoolUrl = webUrl;
        resName = resN;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.universitycard,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.title.setText(schoolNames[position]);
        holder.img.setImageResource(images[position]);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Programs.class);
                intent.putExtra("schoolUrl",schoolUrl[position]);
                intent.putExtra("schoolName",schoolNames[position]);
                intent.putExtra("resName",resName [position]);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        ImageView img;
        CardView mainLayout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.uniName);
            img = itemView.findViewById(R.id.uniPhoto);
            mainLayout = itemView.findViewById(R.id.mainLayout);

        }
    }
}
