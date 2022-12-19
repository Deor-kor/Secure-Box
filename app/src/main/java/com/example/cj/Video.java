package com.example.cj;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
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

public class Video extends AppCompatActivity {

    WebView webview;
    WebSettings webSettings;

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    LinearLayout text1,text2,text3;
    RecyclerView list;
    ArrayList<Ob_List> arraylist;
    RecyclerView.Adapter adapter;

    DatabaseReference databaseReference1;
    DatabaseReference databaseReference2;
    DatabaseReference databaseReference_auto;
    DatabaseReference databaseReference_motion;
    DatabaseReference databaseReference_p;
    DatabaseReference databaseReference_video;
    String raspi_power1,raspi_power2,value_1,value_2,value_3;

    Dialog dialog;
    LinearLayout power;

    String photo_power;

    LinearLayout stop1, stop2, stop3;
    TextView all_delete,check,log_back,link_gallery;
    long backKeyPressedTime = 0; //뒤로가기 버튼을 누른 시간

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video);

        //블랙박스와 연결 확인용 다이얼로그
        dialog = new Dialog(Video.this);  //다이어로그 초기화
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 제거
        dialog.setContentView(R.layout.power_dialog);
        dialog.setCanceledOnTouchOutside(false); //다이얼로그 바깥화면 터치 비활성화 코드
        //    dialog.setCancelable(false); //뒤로가기 비활성화
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //투명
        power = dialog.findViewById(R.id.power);

        all_delete = (TextView)findViewById(R.id.all_delete);
        stop1 = (LinearLayout)findViewById(R.id.stop1);
        stop1.setVisibility(View.GONE);
        stop2 = (LinearLayout)findViewById(R.id.stop2);
        stop2.setVisibility(View.GONE);
        stop3 = (LinearLayout)findViewById(R.id.stop3);
        stop3.setVisibility(View.GONE);
        check = (TextView)findViewById(R.id.check);
        check.setVisibility(View.INVISIBLE);
        text1 = (LinearLayout)findViewById(R.id.text1);
        text2 = (LinearLayout)findViewById(R.id.text2);
        text3  = (LinearLayout)findViewById(R.id.text3);

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
                try {
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
                try {
                    raspi_power2 = snapshot.getValue().toString();

                }
                catch (NullPointerException nullPointerException){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //저장되어있는 녹화파일 목록
        list = (RecyclerView)findViewById(R.id.list1);
        list.setLayoutManager(new GridLayoutManager(this,3));
        list.setHasFixedSize(true);
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

        adapter = new CustomAdapter_Video(arraylist,Video.this);
        list.setAdapter(adapter);

        //사진 촬영 ON/OFF 전원 확인용
        databaseReference_p = database.getReference("photo").child("photo").child("power");
        databaseReference_p.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    photo_power = snapshot.getValue().toString();

                    //수동 녹화 ON/OFF 전원 확인용
                    database = FirebaseDatabase.getInstance("https://cj-2team-default-rtdb.firebaseio.com/");
                    databaseReference = database.getReference("video").child("video").child("power");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                value_1 = snapshot.getValue().toString();

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
//                                                      //활성화 중인 녹화 기능을 제외하고는 비활성으로 표시
                                                        if (value_1.equals("ON")||value_2.equals("ON")||value_3.equals("ON")){
                                                            stop1.setVisibility(View.VISIBLE);
                                                            stop2.setVisibility(View.VISIBLE);
                                                            stop3.setVisibility(View.VISIBLE);
                                                            text1.setVisibility(View.GONE);
                                                            text2.setVisibility(View.GONE);
                                                            text3.setVisibility(View.GONE);
                                                            if (value_1.equals("ON")){
                                                                stop1.setVisibility(View.VISIBLE);
                                                                stop2.setVisibility(View.GONE);
                                                                stop3.setVisibility(View.GONE);
                                                                text1.setVisibility(View.GONE);
                                                                text2.setVisibility(View.VISIBLE);
                                                                text3.setVisibility(View.VISIBLE);
                                                            }
                                                            else if (value_2.equals("ON")){
                                                                stop1.setVisibility(View.GONE);
                                                                stop2.setVisibility(View.VISIBLE);
                                                                stop3.setVisibility(View.GONE);
                                                                text1.setVisibility(View.VISIBLE);
                                                                text2.setVisibility(View.GONE);
                                                                text3.setVisibility(View.VISIBLE);
                                                            }
                                                            else if (value_3.equals("ON")){
                                                                stop1.setVisibility(View.GONE);
                                                                stop2.setVisibility(View.GONE);
                                                                stop3.setVisibility(View.VISIBLE);
                                                                text1.setVisibility(View.VISIBLE);
                                                                text2.setVisibility(View.VISIBLE);
                                                                text3.setVisibility(View.GONE);
                                                            }
                                                        }
                                                        else if(value_1.equals("OFF")&&value_2.equals("OFF")&&value_3.equals("OFF")){
                                                            stop1.setVisibility(View.GONE);
                                                            stop2.setVisibility(View.GONE);
                                                            stop3.setVisibility(View.GONE);
                                                            text1.setVisibility(View.VISIBLE);
                                                            text2.setVisibility(View.VISIBLE);
                                                            text3.setVisibility(View.VISIBLE);
                                                        }
                                                        stop1.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                databaseReference.setValue("OFF");
                                                                databaseReference_auto.setValue("OFF");
                                                                databaseReference_motion.setValue("OFF");
                                                            }
                                                        });
                                                        stop2.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                databaseReference.setValue("OFF");
                                                                databaseReference_auto.setValue("OFF");
                                                                databaseReference_motion.setValue("OFF");
                                                            }
                                                        });
                                                        stop3.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                databaseReference.setValue("OFF");
                                                                databaseReference_auto.setValue("OFF");
                                                                databaseReference_motion.setValue("OFF");
                                                            }
                                                        });

                                                        //모든 파일 삭제 버튼
                                                        all_delete.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                if(System.currentTimeMillis() > backKeyPressedTime + 2000){
                                                                    backKeyPressedTime = System.currentTimeMillis();
                                                                    Toast.makeText(Video.this, "한 번 더 누르면 모두 삭제됩니다.", Toast.LENGTH_SHORT).show();
                                                                    return;
                                                                }
                                                                ////한 번의 뒤로가기 버튼이 눌린 후 0~2초 사이에 한 번더 눌리게 되면 현재 엑티비티를 호출
                                                                if(System.currentTimeMillis() <= backKeyPressedTime + 2000){

                                                                    databaseReference_video.removeValue();
                                                                    Toast.makeText(Video.this, "모두 삭제 완료", Toast.LENGTH_SHORT).show();
                                                                }

                                                            }
                                                        });

                                                        //영상 촬영 중 바
                                                        if(value_2.equals("ON")||value_1.equals("ON")||value_3.equals("ON"))
                                                        {
                                                            check.setVisibility(View.VISIBLE);

                                                        }
                                                        else if(value_2.equals("OFF")||value_1.equals("OFF")||value_3.equals("OFF")){
                                                            check.setVisibility(View.INVISIBLE);
                                                        }

                                                        //동작 감지 모드
                                                        text3.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                //블랙박스와 앱이 연결되어야 촬영 가능
                                                                if (raspi_power1.equals(raspi_power2))
                                                                {
                                                                    if (photo_power.equals("ON"))
                                                                    {
                                                                        check.setText("녹화를 종료하고 시도해주세요.");
                                                                        check.setTextColor(Color.parseColor("#810000"));
                                                                        new Handler().postDelayed(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                check.setText("녹화를 종료하려면 한번 더 눌러주세요.");
                                                                                check.setTextColor(Color.parseColor("#cccccc"));
                                                                            }
                                                                        },2000);
                                                                        // Toast.makeText(Video.this, "사진 촬영 중에는 녹화 불가", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else if(value_2.equals("ON")||value_1.equals("ON")){
                                                                        check.setText("녹화를 종료하고 시도해주세요.");
                                                                        check.setTextColor(Color.parseColor("#810000"));
                                                                        new Handler().postDelayed(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                check.setText("녹화를 종료하려면 한번 더 눌러주세요.");
                                                                                check.setTextColor(Color.parseColor("#cccccc"));
                                                                            }
                                                                        },2000);
                                                                        // Toast.makeText(Video.this, "녹화 중에는 동작 감지 불가", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else{
                                                                        if(value_3.equals("OFF"))
                                                                        {

                                                                            Toast.makeText(Video.this, "동작 감지 모드 시작", Toast.LENGTH_SHORT).show();
                                                                            databaseReference_motion.setValue("ON");
                                                                        }
                                                                    }

                                                                    if (value_3.equals("ON"))
                                                                    {
                                                                        Toast.makeText(Video.this, "잠시만 기다려 주세요.", Toast.LENGTH_SHORT).show();
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


                                                        //일반 녹화
                                                        text1.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                //블랙박스와 앱이 연결되어야 촬영 가능
                                                                if (raspi_power1.equals(raspi_power2))
                                                                {
                                                                    if (photo_power.equals("ON"))
                                                                    {
                                                                        check.setText("녹화를 종료하고 시도해주세요.");
                                                                        check.setTextColor(Color.parseColor("#810000"));
                                                                        new Handler().postDelayed(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                check.setText("녹화를 종료하려면 한번 더 눌러주세요.");
                                                                                check.setTextColor(Color.parseColor("#cccccc"));
                                                                            }
                                                                            },2000);
                                                                        // Toast.makeText(Video.this, "사진 촬영 중에는 영상 녹화 불가", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else if(value_2.equals("ON")){
                                                                        check.setText("녹화를 종료하고 시도해주세요.");
                                                                        check.setTextColor(Color.parseColor("#810000"));
                                                                        new Handler().postDelayed(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                check.setText("녹화를 종료하려면 한번 더 눌러주세요.");
                                                                                check.setTextColor(Color.parseColor("#cccccc"));
                                                                            }
                                                                        },2000);
                                                                        // Toast.makeText(Video.this, "자동 녹화 중에는 일반 녹화 불가", Toast.LENGTH_SHORT).show();

                                                                    }
                                                                    else if(value_3.equals("ON")){
                                                                        check.setText("녹화를 종료하고 시도해주세요.");
                                                                        check.setTextColor(Color.parseColor("#810000"));
                                                                        new Handler().postDelayed(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                check.setText("녹화를 종료하려면 한번 더 눌러주세요.");
                                                                                check.setTextColor(Color.parseColor("#cccccc"));
                                                                            }
                                                                        },2000);
                                                                        // Toast.makeText(Video.this, "동작 감지 모드 중에는 녹화 불가", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else{
                                                                        if(value_1.equals("OFF"))
                                                                        {
                                                                            Toast.makeText(Video.this, "촬영 시작", Toast.LENGTH_SHORT).show();
                                                                            databaseReference.setValue("ON");
                                                                        }
                                                                    }

                                                                    if (value_1.equals("ON"))
                                                                    {
                                                                        Toast.makeText(Video.this, "잠시만 기다려 주세요.", Toast.LENGTH_SHORT).show();
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


                                                        //자동 녹화
                                                        text2.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                //블랙박스와 앱이 연결되어야 촬영 가능
                                                                if (raspi_power1.equals(raspi_power2))
                                                                {
                                                                    if (photo_power.equals("ON"))
                                                                    {
                                                                        check.setText("녹화를 종료하고 시도해주세요.");
                                                                        check.setTextColor(Color.parseColor("#810000"));
                                                                        new Handler().postDelayed(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                check.setText("녹화를 종료하려면 한번 더 눌러주세요.");
                                                                                check.setTextColor(Color.parseColor("#cccccc"));
                                                                            }
                                                                        },2000);
                                                                        // Toast.makeText(Video.this, "사진 촬영 중에는 영상 녹화 불가", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else if(value_1.equals("ON")){
                                                                        check.setText("녹화를 종료하고 시도해주세요.");
                                                                        check.setTextColor(Color.parseColor("#810000"));
                                                                        new Handler().postDelayed(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                check.setText("녹화를 종료하려면 한번 더 눌러주세요.");
                                                                                check.setTextColor(Color.parseColor("#cccccc"));
                                                                            }
                                                                        },2000);
                                                                        // Toast.makeText(Video.this, "일반 녹화 중에는 자동 녹화 불가", Toast.LENGTH_SHORT).show();

                                                                    }
                                                                    else if(value_3.equals("ON")){
                                                                        check.setText("녹화를 종료하고 시도해주세요.");
                                                                        check.setTextColor(Color.parseColor("#810000"));
                                                                        new Handler().postDelayed(new Runnable() {
                                                                            @Override
                                                                            public void run() {
                                                                                check.setText("녹화를 종료하려면 한번 더 눌러주세요.");
                                                                                check.setTextColor(Color.parseColor("#cccccc"));
                                                                            }
                                                                        },2000);
                                                                        // Toast.makeText(Video.this, "동작 감지 모드 중에는 녹화 불가", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else{
                                                                        if(value_2.equals("OFF"))
                                                                        {
                                                                            Toast.makeText(Video.this, "촬영 시작", Toast.LENGTH_SHORT).show();
                                                                            databaseReference_auto.setValue("ON");
                                                                        }
                                                                    }

                                                                    if (value_2.equals("ON"))
                                                                    {
                                                                        Toast.makeText(Video.this, "잠시만 기다려 주세요.", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(Video.this,Gallery.class);
                startActivity(intent);
            }
        });
    }
}