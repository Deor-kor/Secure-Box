package com.example.cj;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Option extends AppCompatActivity {

    EditText ip,auto_time,auto_count,motion_time,motion_count,video_time;
    TextView save_ip,save_auto,save_motion,save_video, option_ref;
    TextView save_video_check, save_auto_check, save_motion_check, save_ip_check; // 변경사항 확인

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference_autoTime;
    DatabaseReference databaseReference_autoCount;
    DatabaseReference databaseReference_motionTime;
    DatabaseReference databaseReference_motionCount;
    DatabaseReference databaseReference_videoTime;

    String AUTO_TIME,AUTO_COUNT,MOTION_TIME,MOTION_COUNT,VIDEO_TIME;
    LinearLayout option_visible1,option_visible2,option_visible3,option_click1,option_click2,option_click3;

    TextView ar_up1,ar_down1,ar_up2,ar_down2,ar_up3,ar_down3;
    TextView option_back;

    int count1, count2, count3 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option);

        option_back = findViewById(R.id.option_back);
        option_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ar_up1 = findViewById(R.id.ar_up1);
        ar_down1 = findViewById(R.id.ar_down1);
        ar_down1.setVisibility(View.GONE);
        option_visible1 = findViewById(R.id.option_visible1);
        option_visible1.setVisibility(View.GONE);
        option_click1 = findViewById(R.id.option_click1);
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


        ar_up2 = findViewById(R.id.ar_up2);
        ar_down2 = findViewById(R.id.ar_down2);
        ar_down2.setVisibility(View.GONE);
        option_visible2 = findViewById(R.id.option_visible2);
        option_visible2.setVisibility(View.GONE);
        option_click2 = findViewById(R.id.option_click2);
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

        ar_up3 = findViewById(R.id.ar_up3);
        ar_down3 = findViewById(R.id.ar_down3);
        ar_down3.setVisibility(View.GONE);
        option_visible3 = findViewById(R.id.option_visible3);
        option_visible3.setVisibility(View.GONE);
        option_click3 = findViewById(R.id.option_click3);
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
        save_ip_check = (TextView)findViewById(R.id.save_ip_check);
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
                            save_ip_check.setText("IP 설정값을 변경했습니다.");
                            save_ip_check.setTextColor(Color.parseColor("#cccccc"));
                            save_ip.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#669966")));
                            ip.setTextColor(Color.parseColor("#669966"));
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    save_ip_check.setText("");
                                    save_ip_check.setTextColor(Color.parseColor("#666666"));
                                    ip.setTextColor(Color.parseColor("#666666"));
                                    save_ip.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#666666")));
                                }
                            },2000);
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
                save_auto_check.setText("녹화 옵션이 변경되었습니다.");
                save_auto_check.setTextColor(Color.parseColor("#cccccc"));
                auto_time.setTextColor(Color.parseColor("#669966"));
                auto_count.setTextColor(Color.parseColor("#669966"));
                save_auto.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#669966")));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        save_auto_check.setText("");
                        save_auto_check.setTextColor(Color.parseColor("#cccccc"));
                        auto_time.setTextColor(Color.parseColor("#666666"));
                        auto_count.setTextColor(Color.parseColor("#666666"));
                        save_auto.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#666666")));
                    }
                },2000);
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
                save_motion_check.setText("녹화 옵션이 변경되었습니다.");
                save_motion_check.setTextColor(Color.parseColor("#cccccc"));
                motion_time.setTextColor(Color.parseColor("#669966"));
                motion_count.setTextColor(Color.parseColor("#669966"));
                save_motion.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#669966")));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        save_motion_check.setText("");
                        save_motion_check.setTextColor(Color.parseColor("#cccccc"));
                        motion_time.setTextColor(Color.parseColor("#666666"));
                        motion_count.setTextColor(Color.parseColor("#666666"));
                        save_motion.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#666666")));
                    }
                },2000);
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
                save_video_check.setText("녹화 옵션이 변경되었습니다.");
                save_video_check.setTextColor(Color.parseColor("#cccccc"));
                video_time.setTextColor(Color.parseColor("#669966"));
                save_video.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#669966")));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        save_video_check.setText("");
                        save_video_check.setTextColor(Color.parseColor("#cccccc"));
                        video_time.setTextColor(Color.parseColor("#666666"));
                        save_video.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#666666")));
                    }
                },2000);
            }
        });

        save_video_check = (TextView)findViewById(R.id.save_video_check);
        save_auto_check = (TextView)findViewById(R.id.save_auto_check);
        save_motion_check = (TextView)findViewById(R.id.save_motion_check);

        save_video_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        save_auto_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        save_motion_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        option_ref = (TextView)findViewById(R.id.option_ref);
        option_ref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video_time.setText("60"); // 일반 녹화 초 설정 초기화
                auto_time.setText("60"); auto_count.setText("10"); // 자동 녹화 초, 횟수 설정 초기화
                motion_time.setText("60"); motion_count.setText("10"); // 모션 녹화 초, 횟수 설정 초기화
                video_time.setTextColor(Color.parseColor("#669966"));
                auto_time.setTextColor(Color.parseColor("#669966"));
                auto_count.setTextColor(Color.parseColor("#669966"));
                motion_time.setTextColor(Color.parseColor("#669966"));
                motion_count.setTextColor(Color.parseColor("#669966"));
                save_video_check.setText("기본 설정값입니다.");
                save_auto_check.setText("기본 설정값입니다.");
                save_motion_check.setText("기본 설정값입니다.");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        video_time.setTextColor(Color.parseColor("#666666"));
                        auto_time.setTextColor(Color.parseColor("#666666"));
                        auto_count.setTextColor(Color.parseColor("#666666"));
                        motion_time.setTextColor(Color.parseColor("#666666"));
                        motion_count.setTextColor(Color.parseColor("#666666"));
                    }
                },2000);
                databaseReference_videoTime.setValue(Integer.parseInt(video_time.getText().toString())); // 일반 녹화 설정
                databaseReference_autoTime.setValue(Integer.parseInt(auto_time.getText().toString())); // 자동 녹화 설정
                databaseReference_autoCount.setValue(Integer.parseInt(auto_count.getText().toString())); // 자동 녹화 설정
                databaseReference_motionTime.setValue(Integer.parseInt(motion_time.getText().toString())); // 모션 감지 녹화 설정
                databaseReference_motionCount.setValue(Integer.parseInt(motion_count.getText().toString())); // 모션 감지 녹화 설정
            }
        });



    }
}