package com.example.cj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Gallery extends AppCompatActivity {
    RecyclerView recyclerView1,recyclerView2;
    FirebaseDatabase database;
    DatabaseReference databaseReference_photo,databaseReference_video;
    ArrayList<Ob_List> arrayList_photo,arrayList_video;
    RecyclerView.Adapter adapter1,adapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);

        recyclerView1 = (RecyclerView)findViewById(R.id.recyclerviewe1);
        recyclerView2 = (RecyclerView)findViewById(R.id.recyclerviewe2);

        database = FirebaseDatabase.getInstance();

        recyclerView1.setLayoutManager(new GridLayoutManager(this,4));
        recyclerView1.setHasFixedSize(true);
        arrayList_photo = new ArrayList<>();
        databaseReference_photo = database.getReference("photo").child("list");
        databaseReference_photo.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    arrayList_photo.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        arrayList_photo.add(dataSnapshot.getValue(Ob_List.class));
                    }

                    adapter1.notifyDataSetChanged();

                }
                catch (NullPointerException nullPointerException){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter1 = new CustomAdapter_Photo(arrayList_photo, Gallery.this);
        recyclerView1.setAdapter(adapter1);



        recyclerView2.setLayoutManager(new GridLayoutManager(this,4));
        recyclerView2.setHasFixedSize(true);
        arrayList_video= new ArrayList<>();
        databaseReference_video = database.getReference("video").child("list");
        databaseReference_video.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    arrayList_video.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        arrayList_video.add(dataSnapshot.getValue(Ob_List.class));
                    }

                    adapter2.notifyDataSetChanged();

                }
                catch (NullPointerException nullPointerException){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter2 = new CustomAdapter_Photo(arrayList_video, Gallery.this);
        recyclerView2.setAdapter(adapter2);

    }
}