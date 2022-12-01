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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
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
    RecyclerView list;
    ArrayList<Ob_List> arraylist;
    DatabaseReference databaseReference_photo;
    RecyclerView.Adapter adapter;

    DatabaseReference databaseReference_v;
    String value_v;
    TextView check;

    DatabaseReference databaseReference1;
    DatabaseReference databaseReference2;
    DatabaseReference databaseReference_auto;
    String value1,value2,value_auto;

    Dialog dialog;
    LinearLayout power;

    String mt;
    DatabaseReference databaseReference_motion;
    TextView all_delete;
    long backKeyPressedTime = 0; //뒤로가기 버튼을 누른 시간
    TextView log_back;




    @SuppressLint("MissingInflatedId")
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
        text = (TextView)findViewById(R.id.text);
        all_delete = (TextView)findViewById(R.id.all_delete);

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


        list = (RecyclerView)findViewById(R.id.list);
        list.setLayoutManager(new GridLayoutManager(this,3));
        list.setHasFixedSize(true);
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

        adapter = new CustomAdapter_Photo(arraylist,Photo.this);
        list.setAdapter(adapter);

        text.setBackground(getResources().getDrawable(R.drawable.photo_recording_on));
        databaseReference_v = database.getReference("video").child("video").child("power");
        databaseReference_v.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    value_v = snapshot.getValue().toString();

                    //사진
                    database = FirebaseDatabase.getInstance("https://cj-2team-default-rtdb.firebaseio.com/");
                    databaseReference = database.getReference("photo").child("photo").child("power");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                String value = snapshot.getValue().toString();
                              //  text.setText(value);

                                if(value.equals("ON"))
                                {
                                    text.setBackground(getResources().getDrawable(R.drawable.photo_recording_on));
                                    check.setVisibility(View.VISIBLE);
                                }
                                else if(value.equals("OFF")){
                                    text.setBackground(getResources().getDrawable(R.drawable.photo_recording_off));
                                    check.setVisibility(View.INVISIBLE);
                                }



                                databaseReference_auto =database.getReference("video_auto").child("video_auto").child("power");
                                databaseReference_auto.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        try {
                                            value_auto = snapshot.getValue().toString();



                                            databaseReference_motion = database.getReference("motion").child("motion").child("power");
                                            databaseReference_motion.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    try {
                                                        mt = snapshot.getValue().toString();

                                                        all_delete.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                if(System.currentTimeMillis() > backKeyPressedTime + 2000){
                                                                    backKeyPressedTime = System.currentTimeMillis();
                                                                    Toast.makeText(Photo.this, "한 번 더 누르면 모두 삭제됩니다.", Toast.LENGTH_SHORT).show();
                                                                    return;
                                                                }
                                                                //한 번의 뒤로가기 버튼이 눌린 후 0~2초 사이에 한 번더 눌리게 되면 현재 엑티비티를 호출
                                                                if(System.currentTimeMillis() <= backKeyPressedTime + 2000){

                                                                    databaseReference_photo.removeValue();
                                                                    Toast.makeText(Photo.this, "모두 삭제 완료", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                        text.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                if (value1.equals(value2))
                                                                {

                                                                    if(value_v.equals("ON")||value_auto.equals("ON")||mt.equals("ON"))
                                                                    {
                                                                        Toast.makeText(Photo.this, "영상 녹화 중에는 사진 촬영 불가", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                    else{
                                                                        if(value.equals("OFF"))
                                                                        {
                                                                            databaseReference.setValue("ON");
                                                                            Toast.makeText(Photo.this, "촬영 시작", Toast.LENGTH_SHORT).show();
                                                                        }

                                                                    }

                                                                    if (value.equals("ON"))
                                                                    {

                                                                        Toast.makeText(Photo.this, "잠시만 기다려 주세요.", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                }
                                                                else{
                                                                    try {
                                                                        dialog.show();
                                                                    }
                                                                    catch (WindowManager.BadTokenException e){

                                                                    }
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


                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

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

        log_back = (TextView)findViewById(R.id.log_back);
        log_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}