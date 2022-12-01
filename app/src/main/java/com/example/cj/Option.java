package com.example.cj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Option extends AppCompatActivity {

    EditText ip,auto_time,auto_count,motion_time,motion_count,video_time;
    TextView save_ip,save_auto,save_motion,save_video;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference_autoTime;
    DatabaseReference databaseReference_autoCount;
    DatabaseReference databaseReference_motionTime;
    DatabaseReference databaseReference_motionCount;
    DatabaseReference databaseReference_videoTime;
    String AUTO_TIME,AUTO_COUNT,MOTION_TIME,MOTION_COUNT,VIDEO_TIME;
    LinearLayout option_visible1,option_visible2,option_visible3,option_click1,option_click2,option_click3;
    int count1, count2, count3 = 0;
    TextView ar_up1,ar_down1,ar_up2,ar_down2,ar_up3,ar_down3;
    TextView option_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option);
        option_visible1 = findViewById(R.id.option_visible1);
        option_visible2 = findViewById(R.id.option_visible2);
        option_visible3 = findViewById(R.id.option_visible3);
        option_visible1.setVisibility(View.GONE);
        option_visible2.setVisibility(View.GONE);
        option_visible3.setVisibility(View.GONE);

        option_click1 = findViewById(R.id.option_click1);
        option_click2 = findViewById(R.id.option_click2);
        option_click3 = findViewById(R.id.option_click3);
        option_back = findViewById(R.id.option_back);

        ar_up1 = findViewById(R.id.ar_up1);
        ar_down1 = findViewById(R.id.ar_down1);
        ar_down1.setVisibility(View.GONE);
        ar_up2 = findViewById(R.id.ar_up2);
        ar_down2 = findViewById(R.id.ar_down2);
        ar_down2.setVisibility(View.GONE);
        ar_up3 = findViewById(R.id.ar_up3);
        ar_down3 = findViewById(R.id.ar_down3);
        ar_down3.setVisibility(View.GONE);

        option_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        option_click1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (count1%2==0){
                    option_visible1.setVisibility(View.VISIBLE);
                    ar_up1.setVisibility(View.GONE);
                    ar_down1.setVisibility(View.VISIBLE);
                    count1++;
                }
                else if(count1%2==1){
                    option_visible1.setVisibility(View.GONE);
                    ar_up1.setVisibility(View.VISIBLE);
                    ar_down1.setVisibility(View.GONE);
                    count1++;
                }
            }
        });
        option_click2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (count2%2==0){
                    option_visible2.setVisibility(View.VISIBLE);
                    ar_up2.setVisibility(View.GONE);
                    ar_down2.setVisibility(View.VISIBLE);
                    count2++;
                }
                else if(count2%2==1){
                    option_visible2.setVisibility(View.GONE);
                    ar_up2.setVisibility(View.VISIBLE);
                    ar_down2.setVisibility(View.GONE);
                    count2++;
                }
            }
        });
        option_click3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (count3%2==0){
                    option_visible3.setVisibility(View.VISIBLE);
                    ar_up3.setVisibility(View.GONE);
                    ar_down3.setVisibility(View.VISIBLE);
                    count3++;
                }
                else if(count3%2==1){
                    option_visible3.setVisibility(View.GONE);
                    ar_up3.setVisibility(View.VISIBLE);
                    ar_down3.setVisibility(View.GONE);
                    count3++;
                }
            }
        });


        ip = (EditText)findViewById(R.id.ip);
        save_ip = (TextView)findViewById(R.id.save_ip);
        database = FirebaseDatabase.getInstance("https://cj-2team-default-rtdb.firebaseio.com/");
        databaseReference = database.getReference("system").child("option").child("ip");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String value = snapshot.getValue().toString();
                    ip.setText(value);

                    save_ip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            databaseReference.setValue(ip.getText().toString());
                            Toast.makeText(Option.this, "IP 변경 완료", Toast.LENGTH_SHORT).show();
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

        auto_time = (EditText)findViewById(R.id.auto_time);
        auto_count = (EditText)findViewById(R.id.auto_count);
        save_auto = (TextView)findViewById(R.id.save_auto);
        databaseReference_autoTime = database.getReference("video_auto").child("video_auto").child("time");
        databaseReference_autoTime.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    AUTO_TIME = snapshot.getValue().toString();
                    auto_time.setText(AUTO_TIME);
                }
                catch (NullPointerException nullPointerException){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference_autoCount = database.getReference("video_auto").child("video_auto").child("count");
        databaseReference_autoCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    AUTO_COUNT = snapshot.getValue().toString();
                    auto_count.setText(AUTO_COUNT);
                }
                catch (NullPointerException nullPointerException){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        save_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference_autoTime.setValue(Integer.parseInt(auto_time.getText().toString()));
                databaseReference_autoCount.setValue(Integer.parseInt(auto_count.getText().toString()));
                Toast.makeText(Option.this, "자동 녹화 옵션 설정 완료", Toast.LENGTH_SHORT).show();
            }
        });





        motion_time = (EditText)findViewById(R.id.motion_time);
        motion_count = (EditText)findViewById(R.id.motion_count);
        save_motion = (TextView)findViewById(R.id.save_motion);
        databaseReference_motionTime = database.getReference("motion").child("motion").child("time");
        databaseReference_motionTime.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    MOTION_TIME = snapshot.getValue().toString();
                    motion_time.setText(MOTION_TIME);
                }
                catch (NullPointerException nullPointerException){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference_motionCount = database.getReference("motion").child("motion").child("count");
        databaseReference_motionCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    MOTION_COUNT = snapshot.getValue().toString();
                    motion_count.setText(MOTION_COUNT);
                }
                catch (NullPointerException nullPointerException){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        save_motion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference_motionTime.setValue(Integer.parseInt(motion_time.getText().toString()));
                databaseReference_motionCount.setValue(Integer.parseInt(motion_count.getText().toString()));
                Toast.makeText(Option.this, "모션 감지 녹화 옵션 설정 완료", Toast.LENGTH_SHORT).show();
            }
        });



        video_time = (EditText)findViewById(R.id.video_time);
        save_video = (TextView)findViewById(R.id.save_video);
        databaseReference_videoTime = database.getReference("video").child("video").child("time");
        databaseReference_videoTime.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    VIDEO_TIME = snapshot.getValue().toString();
                    video_time.setText(VIDEO_TIME);
                }
                catch (NullPointerException nullPointerException){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        save_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference_videoTime.setValue(Integer.parseInt(video_time.getText().toString()));
                Toast.makeText(Option.this, "일반 녹화 옵션 설정 완료", Toast.LENGTH_SHORT).show();
            }
        });



    }
}