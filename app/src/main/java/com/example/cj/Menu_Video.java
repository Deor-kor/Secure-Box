package com.example.cj;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class Menu_Video extends Fragment {

    View view;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    TextView text;
    Button button;
    RecyclerView list;
    ArrayList<Ob_List> arraylist;
    DatabaseReference databaseReference_video;
    RecyclerView.Adapter adapter;

    DatabaseReference databaseReference_p;

    String value_p;
    TextView check;

    DatabaseReference databaseReference1;
    DatabaseReference databaseReference2;
    String value1,value2;

    Dialog dialog;
    TextView power;

    public Menu_Video() {

    }


    public static Menu_Video newInstance() {
        Menu_Video fragment = new Menu_Video();
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
        view= inflater.inflate(R.layout.menu_video, container, false);

        dialog = new Dialog(getActivity());  //다이어로그 초기화
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 제거
        dialog.setContentView(R.layout.power_dialog);
        dialog.setCanceledOnTouchOutside(false); //다이얼로그 바깥화면 터치 비활성화 코드
        //    dialog.setCancelable(false); //뒤로가기 비활성화
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //투명
        power = dialog.findViewById(R.id.power);

        check = (TextView)view.findViewById(R.id.check);
        check.setVisibility(View.INVISIBLE);
        button = (Button)view.findViewById(R.id.button2);
        text = (TextView)view.findViewById(R.id.text2);

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
                                String value1 = snapshot.getValue().toString();
                                text.setText(value1);

                                if(value1.equals("ON"))
                                {
                                    check.setVisibility(View.VISIBLE);
                                }
                                else if(value1.equals("OFF")){
                                    check.setVisibility(View.INVISIBLE);
                                }

                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (value1.equals(value2))
                                        {
                                            if (value_p.equals("ON"))
                                            {
                                                Toast.makeText(getActivity(), "사진 촬영 중에는 영상 녹화 불가", Toast.LENGTH_SHORT).show();
                                            }
                                            else{

                                                if(value1.equals("OFF"))
                                                {
                                                    Toast.makeText(getActivity(), "촬영 시작", Toast.LENGTH_SHORT).show();
                                                    databaseReference.setValue("ON");
                                                }


                                            }

                                            if (value1.equals("ON"))
                                            {
                                                Toast.makeText(getActivity(), "잠시만 기다려 주세요.", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                        else{
                                            dialog.show();
                                            power.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (value1.equals(value2)){
                                                        dialog.dismiss();
                                                        Toast.makeText(getActivity(), "연결 성공", Toast.LENGTH_SHORT).show();

                                                    }
                                                    else {
                                                        Toast.makeText(getActivity(), "블랙박스 전원이 꺼져있습니다.", Toast.LENGTH_SHORT).show();
                                                    }

                                                }

                                            });
                                            Toast.makeText(getActivity(), "블랙박스를 연결해 주세요.", Toast.LENGTH_SHORT).show();
                                        }







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




        list = (RecyclerView)view.findViewById(R.id.list1);
        list.setLayoutManager(new GridLayoutManager(getActivity(),1));
        list.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
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

        adapter = new CustomAdapter_List2(arraylist,getActivity());
        list.setAdapter(adapter);







        return view;
    }
}