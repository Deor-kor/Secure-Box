package com.example.cj;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Photo extends AppCompatActivity {

    WebView webview;
    WebSettings webSettings;

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    LinearLayout text;
    RecyclerView list;
    ArrayList<Ob_List> arraylist;
    RecyclerView.Adapter adapter;

    DatabaseReference databaseReference1;
    DatabaseReference databaseReference2;
    DatabaseReference databaseReference_auto;
    DatabaseReference databaseReference_v;
    DatabaseReference databaseReference_motion;
    DatabaseReference databaseReference_photo;
    String raspi_power1,raspi_power2,value_1,value_2,value_3,photo_power;

    Dialog dialog;
    LinearLayout power;

    TextView log_back,photo_on_off_text,photo_on_off,link_gallery,all_delete,check;

    long backKeyPressedTime = 0; //뒤로가기 버튼을 누른 시간

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo);

        //블랙박스와 연결 확인용 다이얼로그
        dialog = new Dialog(Photo.this);  //다이어로그 초기화
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 제거
        dialog.setContentView(R.layout.power_dialog);
        dialog.setCanceledOnTouchOutside(false); //다이얼로그 바깥화면 터치 비활성화 코드
        //    dialog.setCancelable(false); //뒤로가기 비활성화
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //투명
        power = dialog.findViewById(R.id.power);

        check = (TextView)findViewById(R.id.check);
        check.setVisibility(View.INVISIBLE);
        text = (LinearLayout)findViewById(R.id.text);
        all_delete = (TextView)findViewById(R.id.all_delete);

        //실시간 영상을 보여줄 웹뷰
        webview = (WebView)findViewById(R.id.webview);
        webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true); // 웹페이지 자바스크립트 허용 여부
        webSettings.setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        webSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
        webSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        webSettings.setSupportZoom(false); // 화면 줌 허용 여부
        webSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
        webview.loadUrl(getIntent().getStringExtra("url"));

        //블랙박스와 앱의 연결 확인
        database = FirebaseDatabase.getInstance("https://cj-2team-default-rtdb.firebaseio.com/");
        databaseReference1 =database.getReference("system").child("stop").child("power");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try
                {
                    raspi_power1 = snapshot.getValue().toString();
                }
                catch (NullPointerException nullPointerException){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //블랙박스와 앱의 연결 확인
        databaseReference2 =database.getReference("system").child("stop").child("raspi");
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try
                {
                    raspi_power2 = snapshot.getValue().toString();
                }
                catch (NullPointerException nullPointerException){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //저장되어있는 사진 목록 불러오기
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
                        arraylist.clear(); //리스트 초기화
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {   //데이터베이스에서 불러온 데이터 리스트에 담기
                            arraylist.add(dataSnapshot.getValue(Ob_List.class));
                        }
                        //리사이클러뷰 어뎁터 갱신
                        adapter.notifyDataSetChanged();

                }
                catch (NullPointerException nullPointerException){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //리사이클러뷰 어뎁터 생성
        adapter = new CustomAdapter_Photo(arraylist,Photo.this);
        list.setAdapter(adapter); //어뎁터와 리사이클러뷰 연결

        //수동 녹화 ON/OFF 전원 확인용
        photo_on_off = (TextView)findViewById(R.id.photo_on_off);
        databaseReference_v = database.getReference("video").child("video").child("power");
        databaseReference_v.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    value_1 = snapshot.getValue().toString();

                    //사진 촬영 ON/OFF 전원 확인용
                    photo_on_off_text = (TextView)findViewById(R.id.photo_on_off_text);
                    database = FirebaseDatabase.getInstance("https://cj-2team-default-rtdb.firebaseio.com/");
                    databaseReference = database.getReference("photo").child("photo").child("power");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                photo_power = snapshot.getValue().toString();

                                if(photo_power.equals("ON"))
                                {   //해당 기능 전원이 ON일때 버튼을 구분해줌
                                    photo_on_off.setBackground(getResources().getDrawable(R.drawable.photo_recording_off));
                                    photo_on_off.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#810000")));
                                    photo_on_off_text.setTextColor(Color.parseColor("#810000"));
                                    check.setVisibility(View.VISIBLE);
                                }
                                else if(photo_power.equals("OFF")){
                                    //해당 기능 전원이 OFF일때 버튼을 구분해줌
                                    photo_on_off.setBackground(getResources().getDrawable(R.drawable.photo_recording_on));
                                    photo_on_off.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#666666")));
                                    photo_on_off_text.setTextColor(Color.parseColor("#666666"));
                                    check.setVisibility(View.INVISIBLE);
                                }

                                //자동 녹화 ON/OFF 전원 확인용
                                databaseReference_auto =database.getReference("video_auto").child("video_auto").child("power");
                                databaseReference_auto.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        try {
                                            value_2 = snapshot.getValue().toString();

                                            //모션 감지 녹화 ON/OFF 전원 확인용
                                            databaseReference_motion = database.getReference("motion").child("motion").child("power");
                                            databaseReference_motion.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    try {
                                                        value_3 = snapshot.getValue().toString();
                                                        //모든 파일 삭제 버튼
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

                                                        //사진 촬영 버튼
                                                        text.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                //블랙박스와 앱이 연결되어야 촬영 가능
                                                                if (raspi_power1.equals(raspi_power2))
                                                                {
                                                                    //녹화 기능과 출동여부 확인(녹화중에는 사진캡쳐기능 사용할 수 없음)
                                                                    if(value_1.equals("ON")||value_2.equals("ON")||value_3.equals("ON"))
                                                                    {
                                                                        check.setVisibility(View.VISIBLE);
                                                                        check.setText("녹화를 종료하고 시도해주세요.");
                                                                        check.setTextColor(Color.parseColor("#810000"));
                                                                        new Handler().postDelayed(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                check.setText("촬영중입니다.");
                                                                                check.setTextColor(Color.parseColor("#cccccc"));
                                                                            }
                                                                        },2000);
                                                                    }

                                                                    else{
                                                                        if(photo_power.equals("OFF"))
                                                                        {
                                                                            databaseReference.setValue("ON");
                                                                            Toast.makeText(Photo.this, "촬영 시작", Toast.LENGTH_SHORT).show();
                                                                        }

                                                                    }

                                                                    if (photo_power.equals("ON"))
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
                                                                            if (raspi_power1.equals(raspi_power2)){
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

        link_gallery = (TextView)findViewById(R.id.link_gallery);
        link_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Photo.this,Gallery.class);
                startActivity(intent);
            }
        });

    }
}