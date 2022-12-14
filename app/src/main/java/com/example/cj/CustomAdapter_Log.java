package com.example.cj;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class CustomAdapter_Log extends RecyclerView.Adapter<CustomAdapter_Log.CustomViewHolder> {

    ArrayList<Ob_Log> arrayList;
    Context context;

    public CustomAdapter_Log(ArrayList<Ob_Log> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;

    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.list_log,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter_Log.CustomViewHolder holder, int position) {

        try {


            if (position==arrayList.size()-1||position==arrayList.size()-2||position==arrayList.size()-3){

                holder.position.setText("new");
                holder.log.setText(arrayList.get(position).getLog());
                holder.time.setText(arrayList.get(position).getTime());
                if (position==arrayList.size()-1) {
                    holder.log.setTextColor(Color.parseColor("#810000"));
                    holder.position.setTextColor(Color.parseColor("#810000"));
                    holder.time.setTextColor(Color.parseColor("#810000"));
                }
                else if (position==arrayList.size()-2) {
                    holder.log.setTextColor(Color.parseColor("#810000"));
                    holder.position.setTextColor(Color.parseColor("#810000"));
                    holder.time.setTextColor(Color.parseColor("#810000"));
                }
                else if (position==arrayList.size()-3) {
                    holder.log.setTextColor(Color.parseColor("#810000"));
                    holder.position.setTextColor(Color.parseColor("#810000"));
                    holder.time.setTextColor(Color.parseColor("#810000"));
                }
            }
            else {

                holder.log.setText(arrayList.get(position).getLog());
                holder.position.setText(String.valueOf(position+1));
                holder.time.setText(arrayList.get(position).getTime());

                holder.log.setTextColor(Color.parseColor("#cccccc"));
                holder.position.setTextColor(Color.parseColor("#cccccc"));
                holder.time.setTextColor(Color.parseColor("#cccccc"));

            }

        }catch (NullPointerException nullPointerException){

        }

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size():0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView position;
        TextView time;
        TextView log;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.position = itemView.findViewById(R.id.position);
            this.time = itemView.findViewById(R.id.time);
            this.log = itemView.findViewById(R.id.log);
        }
    }
}
