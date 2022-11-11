package com.example.cj;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public  class CustomAdapter_List extends RecyclerView.Adapter<CustomAdapter_List.CusromViewHolder>{

    ArrayList<Ob_List> arrayList;
    Context context;

    public CustomAdapter_List(ArrayList<Ob_List> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CusromViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_url,parent,false);
        CusromViewHolder holder = new CusromViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CustomAdapter_List.CusromViewHolder holder, int position) {

        try {
            holder.name.setText(arrayList.get(position).getName());
            holder.position.setText(String.valueOf(position+1)+".");
            holder.date.setText(arrayList.get(position).getDate());
        }
        catch (NullPointerException nullPointerException){

        }


    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size():0);
    }

    public class CusromViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView position;
        TextView date;
        View view;

        public CusromViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.date = itemView.findViewById(R.id.date);
            this.position = itemView.findViewById(R.id.position);

            view = itemView;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getLayoutPosition();

                    Intent intent = new Intent(context,Photo_Dialog.class);
                    intent.putExtra("url",arrayList.get(position).getUrl());
                    context.startActivity(intent);


                }
            });

        }

    }


}