package com.example.cj;

import android.app.Dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.Random;



public class Menu_Main extends Fragment {

    View view;
    WebView webview;
    WebSettings webSettings;
    TextView refresh;
    FirebaseDatabase database;
    DatabaseReference databaseReference_p;
    DatabaseReference databaseReference_v;

    String value_p,value_v;


    DatabaseReference databaseReference1;
    DatabaseReference databaseReference2;
    String check_power;
    String value;


    Dialog dialog;
    TextView power;
    public Menu_Main() {

    }

    public static Menu_Main newInstance() {
        Menu_Main fragment = new Menu_Main();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.menu_main, container, false);

        dialog = new Dialog(getActivity());  //다이어로그 초기화
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 제거
        dialog.setContentView(R.layout.power_dialog);
        dialog.setCanceledOnTouchOutside(false); //다이얼로그 바깥화면 터치 비활성화 코드
    //    dialog.setCancelable(false); //뒤로가기 비활성화
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //투명
        power = dialog.findViewById(R.id.power);


        refresh = (TextView)view.findViewById(R.id.refresh);
        refresh.setVisibility(View.GONE);


        webview = (WebView)view.findViewById(R.id.webview);
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
        webview.loadUrl("192.168.123.112:8080/stream");



        Random random = new Random();
        check_power = String.valueOf(random.nextInt(99999));
        database = FirebaseDatabase.getInstance("https://cj-2team-default-rtdb.firebaseio.com/");
        databaseReference1 =database.getReference("system").child("stop").child("power");
        databaseReference2 =database.getReference("system").child("stop").child("raspi");
        databaseReference_v = database.getReference("video").child("write").child("power");
        databaseReference_p = database.getReference("photo").child("write").child("power");
        databaseReference_p.setValue("OFF");
        databaseReference_v.setValue("OFF");
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
                                            Toast.makeText(getActivity(), "연결 성공", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                            }
                            else{

                                dialog.show();
                                power.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(getActivity(), "블랙박스 전원이 꺼져있습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                        }
                    }, 3000);



                    refresh.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            if (check_power.equals(value)){//앱에서 보낸신호가 블랙박스랑 일치하면 블랙박스 연결
                                webview.loadUrl("192.168.123.112:8080/stream");
                                Toast.makeText(getActivity(), "새로고침", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                dialog.show();
                                Toast.makeText(getActivity(), "블랙박스 전원이 꺼져있습니다.", Toast.LENGTH_SHORT).show();

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
        return view;
    }


}