package com.example.assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private final ArrayList<Model> mList;
    private final Context context;



    public Adapter(Context context, ArrayList<Model> mList){

        this.context = context;
        this.mList = mList;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item , parent ,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Model uploadCurrent = mList.get(position);
        holder.title.setText(uploadCurrent.getImageName());
        Glide.with(context).load(mList.get(position).getImageUri()).into(holder.imageView);



    }

    @Override
    public int getItemCount() {
        return mList.size();

    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView title;
        public MyViewHolder(@NonNull View itemView ) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_1);
            title=itemView.findViewById(R.id.text_1);
        }


    }
}