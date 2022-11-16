package com.example.cj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class Permission extends AppCompatActivity {

    TextView close, set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.permission);


        //다이얼로그 끄기
        close =(TextView)findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //앱종료
                // ActivityCompat.finishAffinity(Setting_Dialog2.this);
                //다이얼로그 종료
                finish();
            }
        });
        //알 수 없는 앱 설치하기 권한
        set = (TextView)findViewById(R.id.set);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

    }
}