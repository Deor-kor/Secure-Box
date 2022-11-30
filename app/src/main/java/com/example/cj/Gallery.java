package com.example.cj;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView log_back, gallery_capture, galley_recording, all_delete_photo, all_delete_video;
    long backKeyPressedTime = 0; //뒤로가기 버튼을 누른 시간


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);

        recyclerView1 = (RecyclerView)findViewById(R.id.recyclerviewe1);
        recyclerView2 = (RecyclerView)findViewById(R.id.recyclerviewe2);
        recyclerView2.setVisibility(GONE);
        database = FirebaseDatabase.getInstance();

        recyclerView1.setLayoutManager(new GridLayoutManager(this,3));
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



        recyclerView2.setLayoutManager(new GridLayoutManager(this,3));
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
        log_back = (TextView)findViewById(R.id.log_back);
        log_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gallery_capture = (TextView)findViewById(R.id.galley_capture);
        galley_recording = (TextView)findViewById(R.id.galley_recording);
        gallery_capture.setBackground(View.);
        gallery_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all_delete_photo.setVisibility(View.GONE);
                recyclerView1.setVisibility(View.VISIBLE);
                recyclerView2.setVisibility(View.GONE);
            }
        });
        galley_recording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all_delete_video.setVisibility(View.VISIBLE);
                recyclerView1.setVisibility(View.GONE);
                recyclerView2.setVisibility(View.VISIBLE);
            }
        });

        all_delete_photo = (TextView)findViewById(R.id.all_delete_photo);
        all_delete_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(System.currentTimeMillis() > backKeyPressedTime + 2000){
                    backKeyPressedTime = System.currentTimeMillis();
                    Toast.makeText(Gallery.this, "한 번 더 누르면 모두 삭제됩니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                //한 번의 뒤로가기 버튼이 눌린 후 0~2초 사이에 한 번더 눌리게 되면 현재 엑티비티를 호출
                if(System.currentTimeMillis() <= backKeyPressedTime + 2000){
                    databaseReference_photo.removeValue();
                    Toast.makeText(Gallery.this, "모두 삭제 완료", Toast.LENGTH_SHORT).show();
                }
            }
        });
        all_delete_video = (TextView)findViewById(R.id.all_delete_video);
        all_delete_video.setVisibility(View.GONE);
        all_delete_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(System.currentTimeMillis() > backKeyPressedTime + 2000){
                    backKeyPressedTime = System.currentTimeMillis();
                    Toast.makeText(Gallery.this, "한 번 더 누르면 모두 삭제됩니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                //한 번의 뒤로가기 버튼이 눌린 후 0~2초 사이에 한 번더 눌리게 되면 현재 엑티비티를 호출
                if(System.currentTimeMillis() <= backKeyPressedTime + 2000){
                    databaseReference_video.removeValue();
                    Toast.makeText(Gallery.this, "모두 삭제 완료", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}