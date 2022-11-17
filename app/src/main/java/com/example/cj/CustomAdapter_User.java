package com.example.cj;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter_User extends RecyclerView.Adapter<CustomAdapter_User.CustomViewHolder> {

    Context context;
    ArrayList<Ob_User> arrayList;


    @NonNull
    @Override
    public CustomAdapter_User.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter_User.CustomViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


}
