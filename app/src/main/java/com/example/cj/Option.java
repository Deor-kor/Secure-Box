package com.example.cj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Option extends AppCompatActivity {

    EditText ip,auto_time,auto_count;
    TextView save_ip,save_auto;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference_autoTime;
    DatabaseReference databaseReference_autoCount;
    String time,count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option);

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
        databaseReference_autoTime = database.getReference("system").child("video_auto").child("video_auto").child("time");
        databaseReference_autoTime.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    time = snapshot.getValue().toString();
                    auto_time.setText(time);
                }
                catch (NullPointerException nullPointerException){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference_autoCount = database.getReference("system").child("video_auto").child("video_auto").child("count");
        databaseReference_autoCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    count = snapshot.getValue().toString();
                    auto_count.setText(count);
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





    }
}