package com.example.cj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class Real_Video extends AppCompatActivity {

    WebView webview;
    DatabaseReference databaseReference_ip;
    String url;
    WebSettings webSettings;
    TextView refresh;
    FirebaseDatabase database;
    DatabaseReference databaseReference_p;
    DatabaseReference databaseReference_v;
    DatabaseReference databaseReference_auto;
    DatabaseReference databaseReference_motion;
    String value_p,value_v;


    DatabaseReference databaseReference1;
    DatabaseReference databaseReference2;
    String check_power;
    String value;


    Dialog dialog;
    TextView power;

    TextView photo,video,option;

    RecyclerView recyclerview ;
    RecyclerView.Adapter adapter;
    DatabaseReference databaseReference_log;
    ArrayList<Ob_Log> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.real_video);

        dialog = new Dialog(this);  //다이어로그 초기화
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 제거
        dialog.setContentView(R.layout.power_dialog);
        dialog.setCanceledOnTouchOutside(false); //다이얼로그 바깥화면 터치 비활성화 코드
        //    dialog.setCancelable(false); //뒤로가기 비활성화
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //투명
        power = dialog.findViewById(R.id.power);


        refresh = (TextView)findViewById(R.id.refresh);
        refresh.setVisibility(View.GONE);
        database = FirebaseDatabase.getInstance("https://cj-2team-default-rtdb.firebaseio.com/");

        webview = (WebView)findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient());
        webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true); // 웹페이지 자바스크립트 허용 여부
        webSettings.setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        webSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
        webSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        webSettings.setSupportZoom(false); // 화면 줌 허용 여부
        webSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
        databaseReference_ip = database.getReference("system").child("option").child("ip");
        databaseReference_ip.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String value = snapshot.getValue().toString();
                    url= value+"/stream";
                    webview.loadUrl(url);

                }
                catch (NullPointerException nullPointerException){

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        Random random = new Random();
        check_power = String.valueOf(random.nextInt(99999));


        databaseReference_v = database.getReference("video").child("write").child("power");
        databaseReference_p = database.getReference("photo").child("write").child("power");
        databaseReference_auto = database.getReference("system").child("video_auto").child("video_auto").child("power");
        databaseReference_motion = database.getReference("motion").child("motion").child("power");
        databaseReference_p.setValue("OFF");
        databaseReference_v.setValue("OFF");
        databaseReference_auto.setValue("OFF");
        databaseReference_motion.setValue("OFF");

        databaseReference1 =database.getReference("system").child("stop").child("power");
        databaseReference2 =database.getReference("system").child("stop").child("raspi");
        databaseReference1.setValue(check_power); //블랙박스에 신호를 보냄
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    value = snapshot.getValue().toString();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (check_power.equals(value)){//앱에서 보낸신호가 블랙박스랑 일치하면 블랙박스 연결
                                dialog.dismiss();
                                power.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (check_power.equals(value)){

                                            dialog.dismiss();
                                            Toast.makeText(Real_Video.this, "연결 성공", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                            }
                            else{

                                dialog.show();
                                power.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(Real_Video.this, "블랙박스 전원이 꺼져있습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                        }
                    }, 3000);



                    refresh.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            if (check_power.equals(value)){//앱에서 보낸신호가 블랙박스랑 일치하면 블랙박스 연결
                                webview.loadUrl(url);
                                Toast.makeText(Real_Video.this, "새로고침", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                dialog.show();
                                Toast.makeText(Real_Video.this, "블랙박스 전원이 꺼져있습니다.", Toast.LENGTH_SHORT).show();

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


        database = FirebaseDatabase.getInstance("https://cj-2team-default-rtdb.firebaseio.com/");
        databaseReference_p.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    value_p = snapshot.getValue().toString();

                    database = FirebaseDatabase.getInstance("https://cj-2team-default-rtdb.firebaseio.com/");
                    databaseReference_v.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                value_v = snapshot.getValue().toString();

                                if(value_v.equals("OFF")&&value_p.equals("OFF"))
                                {
                                    refresh.setVisibility(View.VISIBLE);

                                }
                                else {
                                    refresh.setVisibility(View.GONE);
                                }
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

        photo = (TextView)findViewById(R.id.photo);
        video = (TextView)findViewById(R.id.video);
        option =(TextView)findViewById(R.id.option);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Real_Video.this,Photo.class);
                startActivity(intent);
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Real_Video.this,Video.class);
                startActivity(intent);
            }
        });
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Real_Video.this,Option.class);
                startActivity(intent);
            }
        });

        recyclerview = (RecyclerView)findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Real_Video.this);
        layoutManager.setReverseLayout(true); //리사이클러뷰 역순으로 보여짐
        layoutManager.setStackFromEnd(true);
        recyclerview.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        databaseReference_log = database.getReference("motion").child("log");
        databaseReference_log.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    arrayList.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        arrayList.add(dataSnapshot.getValue(Ob_Log.class));
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
        adapter = new CustomAdapter_Log(arrayList,Real_Video.this);
        recyclerview.setAdapter(adapter);


    }
}