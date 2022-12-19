package com.example.cj;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class Real_Video extends AppCompatActivity {

    String url;
    WebView webview;
    WebSettings webSettings;
    FirebaseDatabase database;
    DatabaseReference databaseReference_p;
    DatabaseReference databaseReference_v;
    DatabaseReference databaseReference_auto;
    DatabaseReference databaseReference_motion;
    DatabaseReference databaseReference1;
    DatabaseReference databaseReference2;
    DatabaseReference databaseReference_ip;

    RecyclerView recyclerview ;
    RecyclerView.Adapter adapter;
    DatabaseReference databaseReference_log;
    ArrayList<Ob_Log> arrayList;

    String check_power;
    String value;

    Dialog dialog;
    LinearLayout power;

    LinearLayout photo,video,option,gallery,drive;
    TextView log_delete,real_video_text;

    BackPressClose  backPressClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.real_video);
        //뒤로가기 두번 눌렀을 때 앱 종료 메소드
        backPressClose = new BackPressClose(this);

        //블랙박스와 연결 확인용 다이얼로그
        dialog = new Dialog(this);  //다이어로그 초기화
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 제거
        dialog.setContentView(R.layout.power_dialog);
        dialog.setCanceledOnTouchOutside(false); //다이얼로그 바깥화면 터치 비활성화 코드
      //dialog.setCancelable(false); //뒤로가기 비활성화
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //투명
        power = dialog.findViewById(R.id.power);

        //실시간 영상을 보여줄 웹뷰
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

        //블랙박스의 연결된 와이파이 IP주소 읽어오기
        database = FirebaseDatabase.getInstance("https://cj-2team-default-rtdb.firebaseio.com/");
        databaseReference_ip = database.getReference("system").child("option").child("ip");
        databaseReference_ip.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String value = snapshot.getValue().toString();
                    url= value+"/stream";
                    webview.loadUrl(url); //IP주소를 읽어오면 해당 블랙박스의 실시간 영상 웹뷰에 띄워줌

                    //녹화 주행 모드
                    drive = (LinearLayout)findViewById(R.id.drive);
                    drive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Real_Video.this, Arduino_Activity.class);
                            intent.putExtra("url",url);
                            startActivity(intent);
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

        //모든 카메라의 초기값을 OFF로 세팅
        databaseReference_v = database.getReference("video").child("video").child("power");
        databaseReference_p = database.getReference("photo").child("photo").child("power");
        databaseReference_auto = database.getReference("video_auto").child("video_auto").child("power");
        databaseReference_motion = database.getReference("motion").child("motion").child("power");
        databaseReference_p.setValue("OFF");
        databaseReference_v.setValue("OFF");
        databaseReference_auto.setValue("OFF");
        databaseReference_motion.setValue("OFF");

        //블랙박스와 앱이 서로 신호를 주고받아 일치하면 앱을 이용 가능하게 하는 코드
        Random random = new Random();
        check_power = String.valueOf(random.nextInt(99999));
        databaseReference1 =database.getReference("system").child("stop").child("power");
        databaseReference2 =database.getReference("system").child("stop").child("raspi");
        databaseReference1.setValue(check_power); //블랙박스에 신호를 보냄
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ClickableViewAccessibility")
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

                                try {
                                    dialog.show();
                                }
                                catch (WindowManager.BadTokenException e){

                                }

                                power.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(Real_Video.this, "블랙박스 전원이 꺼져있습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                        }
                    }, 3000);


                    real_video_text = (TextView)findViewById(R.id.real_video_text);
                    real_video_text.setText("Load image..");

                    //웹뷰 화면 클릭시 화면 새로고침
                    webview.setOnTouchListener(new View.OnTouchListener() {
                        @SuppressLint("ClickableViewAccessibility")
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            if (check_power.equals(value)){//앱에서 보낸신호가 블랙박스랑 일치하면 블랙박스 연결
                                webview.loadUrl(url);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        real_video_text.setText("새로고침을 하려면 화면을 클릭해 주세요.");
                                    }
                                },2000);

                                // Toast.makeText(Real_Video.this, "새로고침", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                try {
                                    dialog.show();
                                    Toast.makeText(Real_Video.this, "블랙박스 전원이 꺼져있습니다.", Toast.LENGTH_SHORT).show();
                                }
                                catch (WindowManager.BadTokenException e){

                                }


                            }

                            return false;
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

        //사진 캡쳐 엑티비티로 이동
        photo = (LinearLayout)findViewById(R.id.photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Real_Video.this,Photo.class);
                intent.putExtra("url",url);
                startActivity(intent);
            }
        });
        //영상 녹화 엑티비티로 이동
        video = (LinearLayout)findViewById(R.id.video);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Real_Video.this,Video.class);
                intent.putExtra("url",url);
                startActivity(intent);
            }
        });
        //각종 옵션 설정 엑티비티로 이동
        option =(LinearLayout)findViewById(R.id.option);
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Real_Video.this,Option.class);
                startActivity(intent);
            }
        });
        //모션 감지 로그 목록 불러오기
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

        //로그 삭제
        log_delete = (TextView)findViewById(R.id.log_delete);
        log_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference_log.removeValue();
            }
        });

        //사진,영상 파일 갤러리 이동
        gallery = (LinearLayout)findViewById(R.id.gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Real_Video.this, Gallery.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        backPressClose.onBackPressed();
    }
}