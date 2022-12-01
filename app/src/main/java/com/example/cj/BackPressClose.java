package com.example.cj;

import android.app.Activity;
import android.widget.Toast;

public class BackPressClose {

    long backKeyPressedTime = 0; //뒤로가기 버튼을 누른 시간
    Toast toast; //안내문을 띄어 주기 위한 토스트 변수
    Activity activity;  //엑티비티를 입력 받을 변수

    //현재 엑티비티를 입력 받기 위한 생성자
    public  BackPressClose(Activity context){
        this.activity = context;
    }

    //사용자에게 두 번의 뒤로가기 입력을 받은 경우
    public void onBackPressed(){
        //한 번의 뒤로가기 버튼의 눌린 뒤 현재 시간을 변수에 저장 한 후 토스트 룰력
        if(System.currentTimeMillis() > backKeyPressedTime + 2000){
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        //한 번의 뒤로가기 버튼이 눌린 후 0~2초 사이에 한 번더 눌리게 되면 현재 엑티비티를 호출
        if(System.currentTimeMillis() <= backKeyPressedTime + 2000){
            activity.finishAffinity();
            //앱 완전 종료를 api16 이하 버전에서는 이렇게 사용하고 api16 이상에서는 activity.finishAffinity(); 이렇게 사용 함
        }
    }

    public void showGuide(){
        Toast.makeText(activity,"한 번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
    }

}