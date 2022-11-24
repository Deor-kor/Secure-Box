package com.example.cj;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public  class CustomAdapter_Video extends RecyclerView.Adapter<CustomAdapter_Video.CustomViewHolder>{

    ArrayList<Ob_List> arrayList;
    Context context;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    long backKeyPressedTime = 0; //뒤로가기 버튼을 누른 시간

    public CustomAdapter_Video(ArrayList<Ob_List> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_info,parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CustomAdapter_Video.CustomViewHolder holder, int position) {

        try {
            holder.name.setText(arrayList.get(position).getName());
            holder.position.setText(String.valueOf(position+1)+".");
            holder.date.setText(arrayList.get(position).getDate());
            Glide.with(holder.itemView).load(arrayList.get(position).getUrl()).into(holder.photo);
        }
        catch (NullPointerException nullPointerException){

        }


    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size():0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView position;
        TextView date;
        View view;
        LinearLayout look;
        TextView delete;
        ImageView photo;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.date = itemView.findViewById(R.id.date);
            this.position = itemView.findViewById(R.id.position);
            this.look = itemView.findViewById(R.id.look);
            this.delete =itemView.findViewById(R.id.delete);
            this.photo = itemView.findViewById(R.id.photo);

            view = itemView;

            database = FirebaseDatabase.getInstance("https://cj-2team-default-rtdb.firebaseio.com/");
            databaseReference = database.getReference("video").child("list");

            look.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getLayoutPosition();

                    Intent intent = new Intent(context,Video_Dialog.class);
                    intent.putExtra("url",arrayList.get(position).getUrl());
                    context.startActivity(intent);


                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(System.currentTimeMillis() > backKeyPressedTime + 2000){
                        backKeyPressedTime = System.currentTimeMillis();
                        Toast.makeText(context, "한 번 더 누르면 삭제됩니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //한 번의 뒤로가기 버튼이 눌린 후 0~2초 사이에 한 번더 눌리게 되면 현재 엑티비티를 호출
                    if(System.currentTimeMillis() <= backKeyPressedTime + 2000){

                        int position = getLayoutPosition();
                        databaseReference.child(arrayList.get(position).getKey()).removeValue();
                        Toast.makeText(context, "삭제 완료", Toast.LENGTH_SHORT).show();
                    }



                }

        });

        }

    }



}