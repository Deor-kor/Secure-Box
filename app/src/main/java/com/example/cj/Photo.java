package com.example.cj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Photo extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    TextView text;
    Button button;
    RecyclerView list;
    ArrayList<Ob_List> arraylist;
    DatabaseReference databaseReference_photo;
    RecyclerView.Adapter adapter;

    DatabaseReference databaseReference_v;
    String value_v;
    TextView check;

    DatabaseReference databaseReference1;
    DatabaseReference databaseReference2;
    String value1,value2;

    Dialog dialog;
    TextView power;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo);


        dialog = new Dialog(Photo.this);  //다이어로그 초기화
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 제거
        dialog.setContentView(R.layout.power_dialog);
        dialog.setCanceledOnTouchOutside(false); //다이얼로그 바깥화면 터치 비활성화 코드
        //    dialog.setCancelable(false); //뒤로가기 비활성화
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //투명
        power = dialog.findViewById(R.id.power);

        check = (TextView)findViewById(R.id.check);
        check.setVisibility(View.INVISIBLE);
        button = (Button)findViewById(R.id.button);
        text = (TextView)findViewById(R.id.text);

        database = FirebaseDatabase.getInstance("https://cj-2team-default-rtdb.firebaseio.com/");
        databaseReference1 =database.getReference("system").child("stop").child("power");
        databaseReference2 =database.getReference("system").child("stop").child("raspi");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    value1 = snapshot.getValue().toString();
                }
                catch (NullPointerException nullPointerException){


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    value2 = snapshot.getValue().toString();

                }
                catch (NullPointerException nullPointerException){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        databaseReference_v = database.getReference("video").child("write").child("power");
        databaseReference_v.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    value_v = snapshot.getValue().toString();

                    //사진
                    database = FirebaseDatabase.getInstance("https://cj-2team-default-rtdb.firebaseio.com/");
                    databaseReference = database.getReference("photo").child("write").child("power");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                String value = snapshot.getValue().toString();
                                text.setText(value);

                                if(value.equals("ON"))
                                {
                                    check.setVisibility(View.VISIBLE);
                                }
                                else if(value.equals("OFF")){
                                    check.setVisibility(View.INVISIBLE);
                                }

                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (value1.equals(value2))
                                        {

                                            if(value_v.equals("ON"))
                                            {
                                                Toast.makeText(Photo.this, "영상 녹화 중에는 사진 촬영 불가", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                if(value.equals("OFF"))
                                                {
                                                    Toast.makeText(Photo.this, "촬영 시작", Toast.LENGTH_SHORT).show();
                                                    databaseReference.setValue("ON");
                                                }

                                            }

                                            if (value.equals("ON"))
                                            {
                                                Toast.makeText(Photo.this, "잠시만 기다려 주세요.", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                        else{
                                            dialog.show();
                                            power.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (value1.equals(value2)){
                                                        dialog.dismiss();
                                                        Toast.makeText(Photo.this, "연결 성공", Toast.LENGTH_SHORT).show();

                                                    }
                                                    else {
                                                        Toast.makeText(Photo.this, "블랙박스 전원이 꺼져있습니다.", Toast.LENGTH_SHORT).show();
                                                    }

                                                }

                                            });
                                            Toast.makeText(Photo.this, "블랙박스를 연결해 주세요.", Toast.LENGTH_SHORT).show();
                                        }






                                    }
                                });

                            }
                            catch (NullPointerException nullPointerException){
                                databaseReference.setValue("OFF");
                            }
                            catch (RuntimeException runtimeException){
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });


                }
                catch (NullPointerException nullPointerException){

                }
                catch (RuntimeException runtimeException){
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });






        list = (RecyclerView)findViewById(R.id.list);
        list.setLayoutManager(new GridLayoutManager(Photo.this,1));
        list.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Photo.this);
        layoutManager.setReverseLayout(true); //리사이클러뷰 역순으로 보여짐
        layoutManager.setStackFromEnd(true);
        list.setLayoutManager(layoutManager);
        arraylist = new ArrayList<>();
        databaseReference_photo = database.getReference("photo").child("list");
        databaseReference_photo.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    arraylist.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        arraylist.add(dataSnapshot.getValue(Ob_List.class));
                    }

                    adapter.notifyDataSetChanged();

                }
                catch (NullPointerException nullPointerException){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new CustomAdapter_List(arraylist,Photo.this);
        list.setAdapter(adapter);




    }
}