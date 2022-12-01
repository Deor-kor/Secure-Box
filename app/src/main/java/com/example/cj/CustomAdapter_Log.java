package com.example.cj;

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
            holder.log.setText(arrayList.get(position).getLog());
            holder.position.setText(String.valueOf(position+1));
            holder.time.setText(arrayList.get(position).getTime());
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
