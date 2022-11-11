package com.example.cj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity2 extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    TextView text;
    Button button;
    TextView text2;
    Button button2;


    RecyclerView list,list1;
    ArrayList<Ob_List> arraylist,arraylist1;
    DatabaseReference databaseReference_photo;
    DatabaseReference databaseReference_video;
    RecyclerView.Adapter adapter,adapter1;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //사진
        button = (Button)findViewById(R.id.button);
        text = (TextView)findViewById(R.id.text);
        database = FirebaseDatabase.getInstance("https://cj-2team-default-rtdb.firebaseio.com/");
        databaseReference = database.getReference("photo").child("write").child("power");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String value = snapshot.getValue().toString();
                    text.setText(value);


                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        if(value.equals("off"))
                        {
                                databaseReference.setValue("on");


                        }
                        else if(value.equals("on"))
                        {
                                databaseReference.setValue("off");


                        }


                        }
                    });

                }
                catch (NullPointerException nullPointerException){
                    databaseReference.setValue("off");
                }
                catch (RuntimeException runtimeException){
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        //동영상
        button2 = (Button)findViewById(R.id.button2);
        text2 = (TextView)findViewById(R.id.text2);
        database = FirebaseDatabase.getInstance("https://cj-2team-default-rtdb.firebaseio.com/");
        databaseReference2 = database.getReference("video").child("write").child("power");
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String value1 = snapshot.getValue().toString();
                    text2.setText(value1);

                    button2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(value1.equals("off"))
                            {
                                databaseReference2.setValue("on");


                            }
                            else if(value1.equals("on"))
                            {
                                databaseReference2.setValue("off");


                            }

                        }
                    });


                }
                catch (NullPointerException nullPointerException){
                    databaseReference2.setValue("off");
                }
                catch (RuntimeException runtimeException){
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        list = (RecyclerView)findViewById(R.id.list);
        list.setLayoutManager(new GridLayoutManager(this,1));
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

        adapter = new CustomAdapter_List(arraylist,this);
        list.setAdapter(adapter);



        list1 = (RecyclerView)findViewById(R.id.list1);
        list1.setLayoutManager(new GridLayoutManager(this,1));
        list1.setHasFixedSize(true);
        arraylist1 = new ArrayList<>();
        databaseReference_video = database.getReference("video").child("list");
        databaseReference_video.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    arraylist1.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        arraylist1.add(dataSnapshot.getValue(Ob_List.class));
                    }

                    adapter1.notifyDataSetChanged();

                }
                catch (NullPointerException nullPointerException){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter1 = new CustomAdapter_List2(arraylist1,this);
        list1.setAdapter(adapter1);



    }
}