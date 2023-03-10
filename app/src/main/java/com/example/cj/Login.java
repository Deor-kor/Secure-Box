package com.example.cj;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


@RequiresApi(api = Build.VERSION_CODES.O)
public class Login extends AppCompatActivity {
    TextView number;
    LinearLayout login;

    //유저 전화번호 가져오기 권한 및 블루트스 사용 권한 설정
    int PERMISSION_ALL = 100;
    String[] PERMISSIONS = {Manifest.permission.READ_PHONE_NUMBERS,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN};
    //등록된 유저 전화번호 목록 가져오기
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    ArrayList<Ob_User> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //필요한 권한 설정 여부 메소드
        requestPermissions(PERMISSIONS, PERMISSION_ALL);

        //DB에 저장되어 있는 유저 목록 불러오기
        arrayList = new ArrayList<>();
        database = FirebaseDatabase.getInstance("https://cj-2team-default-rtdb.firebaseio.com/");
        databaseReference = database.getReference("user");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    arrayList.clear();//리스트 초기화

                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    {   //불러온 데이터 리스트에 담기
                        arrayList.add(dataSnapshot.getValue(Ob_User.class));
                    }
                }
                catch (NullPointerException nullPointerException){
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        number = (TextView) findViewById(R.id.number);
        login = (LinearLayout) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //필요한 권한 설정 여부 메소드
                requestPermissions(PERMISSIONS, PERMISSION_ALL);
                //DB에 등록되어 있는 유저와 일치하는지 확인
                for(Ob_User ob_user : arrayList)
                {   //일치하면 로그인
                    if(number.getText().toString().equals(ob_user.getNumber()))
                    {
                        Intent intent = new Intent(Login.this,Real_Video.class);
                        startActivity(intent);
                        finish();
                        return;
                    }
                }
                //일치하지 않았을 때
                Toast.makeText(Login.this, "등록된 전호번호가 아닙니다", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            //요청한 권한이 설정되었을 때
            if (hasPermission(Login.this, PERMISSIONS))
            {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                //장치의 전화번호를 읽어오는 권한 설정 여부
                if (ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                //장치의 전화번호를 읽어오는 권한이 허용되면 number EDITTEXT에 전화번호 띄워줌
                String user = telephonyManager.getLine1Number().replace("+82","0");
                number.setText(user);
            }
            //요청한 권한이 거부되었을때 팝업창 띄어줌
            else {
                Intent intent = new Intent(Login.this,Permission.class);
                startActivity(intent);
            }
    }

    private boolean hasPermission(Context context, String... permissions) {
        if(context != null && permissions != null) {
            for(String permission : permissions) {
                if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}