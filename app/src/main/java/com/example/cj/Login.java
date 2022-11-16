package com.example.cj;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


@RequiresApi(api = Build.VERSION_CODES.O)
public class Login extends AppCompatActivity {

    TextView number, login;

    private final int PERMISSION_ALL = 100;

    private final String[] PERMISSIONS = {
            Manifest.permission.READ_PHONE_NUMBERS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        login = (TextView) findViewById(R.id.login);
        number = (TextView) findViewById(R.id.number);

        requestPermissions(PERMISSIONS, PERMISSION_ALL);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                requestPermissions(PERMISSIONS, PERMISSION_ALL);




            }
        });



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            if (hasPermission(Login.this, PERMISSIONS)) {

                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
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