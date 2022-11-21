package com.example.cj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
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

public class Video extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    TextView text1,text2;
    Button button1,button2;
    RecyclerView list;
    ArrayList<Ob_List> arraylist;
    DatabaseReference databaseReference_video;
    RecyclerView.Adapter adapter;

    DatabaseReference databaseReference_p;

    String value_p;
    TextView check;

    DatabaseReference databaseReference1;
    DatabaseReference databaseReference2;
    DatabaseReference databaseReference_auto;
    String value1,value2,value3,value4;

    Dialog dialog;
    TextView power;

    TextView option;

    TextView motion;
    String mt;
    DatabaseReference databaseReference_motion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video);


        dialog = new Dialog(Video.this);  //다이어로그 초기화
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 제거
        dialog.setContentView(R.layout.power_dialog);
        dialog.setCanceledOnTouchOutside(false); //다이얼로그 바깥화면 터치 비활성화 코드
        //    dialog.setCancelable(false); //뒤로가기 비활성화
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //투명
        power = dialog.findViewById(R.id.power);

        check = (TextView)findViewById(R.id.check);
        check.setVisibility(View.INVISIBLE);
        button1 = (Button)findViewById(R.id.button1);
        text1 = (TextView)findViewById(R.id.text1);
        button2 = (Button)findViewById(R.id.button2);
        text2 = (TextView)findViewById(R.id.text2);
        motion = (TextView)findViewById(R.id.motion);
        option = (TextView)findViewById(R.id.option);
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Video.this,Option.class);
                startActivity(intent);
            }
        });

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



        databaseReference_p = database.getReference("photo").child("write").child("power");
        databaseReference_p.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    value_p = snapshot.getValue().toString();

                    //동영상
                    database = FirebaseDatabase.getInstance("https://cj-2team-default-rtdb.firebaseio.com/");
                    databaseReference = database.getReference("video").child("write").child("power");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                value3 = snapshot.getValue().toString();
                                text1.setText(value3);

                                databaseReference_auto =database.getReference("system").child("video_auto").child("video_auto").child("power");
                                databaseReference_auto.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        try {
                                            value4 = snapshot.getValue().toString();
                                            text2.setText(value4);



                                            databaseReference_motion = database.getReference("motion").child("motion").child("power");
                                            databaseReference_motion.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    try {
                                                        mt = snapshot.getValue().toString();
                                                        if (mt.equals("OFF")){

                                                            motion.setText("동작 감지 모드 OFF");
                                                        }
                                                        else if(mt.equals("ON")){
                                                            motion.setText("동작 감지 모드 ON");

                                                        }


                                                        //영상 촬영 중 바
                                                        if(value4.equals("ON")||value3.equals("ON")||mt.equals("ON"))
                                                        {
                                                            check.setVisibility(View.VISIBLE);
                                                        }
                                                        else if(value4.equals("OFF")||value3.equals("OFF")||mt.equals("OFF")){
                                                            check.setVisibility(View.INVISIBLE);
                                                        }

                                                        //동작 감지 모드
                                                        motion.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                //블랙박스 연결확인
                                                                if (value1.equals(value2))
                                                                {
                                                                    if (value_p.equals("ON"))
                                                                    {
                                                                        Toast.makeText(Video.this, "사진 촬영 중에는 녹화 불가", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else if(value4.equals("ON")||value3.equals("ON")){
                                                                        Toast.makeText(Video.this, "녹화 중에는 동작 감지 불가", Toast.LENGTH_SHORT).show();

                                                                    }
                                                                    else{
                                                                        if(mt.equals("OFF"))
                                                                        {
                                                                            Toast.makeText(Video.this, "동작 감지 모드 시작", Toast.LENGTH_SHORT).show();
                                                                            databaseReference_motion.setValue("ON");
                                                                        }
                                                                    }

                                                                    if (mt.equals("ON"))
                                                                    {
                                                                        Toast.makeText(Video.this, "잠시만 기다려 주세요.", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                }

                                                                else{
                                                                    dialog.show();
                                                                    power.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            if (value1.equals(value2)){
                                                                                dialog.dismiss();
                                                                                Toast.makeText(Video.this, "연결 성공", Toast.LENGTH_SHORT).show();

                                                                            }
                                                                            else {
                                                                                Toast.makeText(Video.this, "블랙박스 전원이 꺼져있습니다.", Toast.LENGTH_SHORT).show();
                                                                            }

                                                                        }

                                                                    });
                                                                    Toast.makeText(Video.this, "블랙박스를 연결해 주세요.", Toast.LENGTH_SHORT).show();
                                                                }



                                                            }
                                                        });




                                                        //일반 동영상
                                                        button1.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                //블랙박스 연결확인
                                                                if (value1.equals(value2))
                                                                {
                                                                    if (value_p.equals("ON"))
                                                                    {
                                                                        Toast.makeText(Video.this, "사진 촬영 중에는 영상 녹화 불가", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else if(value4.equals("ON")){
                                                                        Toast.makeText(Video.this, "자동 녹화 중에는 일반 녹화 불가", Toast.LENGTH_SHORT).show();

                                                                    }
                                                                    else if(mt.equals("ON")){
                                                                        Toast.makeText(Video.this, "동작 감지 모드 중에는 녹화 불가", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else{
                                                                        if(value3.equals("OFF"))
                                                                        {
                                                                            Toast.makeText(Video.this, "촬영 시작", Toast.LENGTH_SHORT).show();
                                                                            databaseReference.setValue("ON");
                                                                        }
                                                                    }

                                                                    if (value3.equals("ON"))
                                                                    {
                                                                        Toast.makeText(Video.this, "잠시만 기다려 주세요.", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                }

                                                                else{
                                                                    dialog.show();
                                                                    power.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            if (value1.equals(value2)){
                                                                                dialog.dismiss();
                                                                                Toast.makeText(Video.this, "연결 성공", Toast.LENGTH_SHORT).show();

                                                                            }
                                                                            else {
                                                                                Toast.makeText(Video.this, "블랙박스 전원이 꺼져있습니다.", Toast.LENGTH_SHORT).show();
                                                                            }

                                                                        }

                                                                    });
                                                                    Toast.makeText(Video.this, "블랙박스를 연결해 주세요.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });




                                                        //자동 동영상
                                                        button2.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                //블랙박스 연결확인
                                                                if (value1.equals(value2))
                                                                {
                                                                    if (value_p.equals("ON"))
                                                                    {
                                                                        Toast.makeText(Video.this, "사진 촬영 중에는 영상 녹화 불가", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else if(value3.equals("ON")){
                                                                        Toast.makeText(Video.this, "일반 녹화 중에는 자동 녹화 불가", Toast.LENGTH_SHORT).show();

                                                                    }
                                                                    else if(mt.equals("ON")){
                                                                        Toast.makeText(Video.this, "동작 감지 모드 중에는 녹화 불가", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else{
                                                                        if(value4.equals("OFF"))
                                                                        {
                                                                            Toast.makeText(Video.this, "촬영 시작", Toast.LENGTH_SHORT).show();
                                                                            databaseReference_auto.setValue("ON");
                                                                        }
                                                                    }

                                                                    if (value4.equals("ON"))
                                                                    {
                                                                        Toast.makeText(Video.this, "잠시만 기다려 주세요.", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                }

                                                                else{
                                                                    dialog.show();
                                                                    power.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            if (value1.equals(value2)){
                                                                                dialog.dismiss();
                                                                                Toast.makeText(Video.this, "연결 성공", Toast.LENGTH_SHORT).show();

                                                                            }
                                                                            else {
                                                                                Toast.makeText(Video.this, "블랙박스 전원이 꺼져있습니다.", Toast.LENGTH_SHORT).show();
                                                                            }

                                                                        }

                                                                    });
                                                                    Toast.makeText(Video.this, "블랙박스를 연결해 주세요.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });








                                                    }
                                                    catch (NullPointerException nullPointerException){


                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });






                                        }
                                        catch (NullPointerException nullPointerException){
                                            databaseReference_auto.setValue("OFF");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

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




        list = (RecyclerView)findViewById(R.id.list1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Video.this);
        layoutManager.setReverseLayout(true); //리사이클러뷰 역순으로 보여짐
        layoutManager.setStackFromEnd(true);
        list.setLayoutManager(layoutManager);
        arraylist = new ArrayList<>();
        databaseReference_video = database.getReference("video").child("list");
        databaseReference_video.addValueEventListener(new ValueEventListener() {
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

        adapter = new CustomAdapter_List2(arraylist,Video.this);
        list.setAdapter(adapter);



    }
}