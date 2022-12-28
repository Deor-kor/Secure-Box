package com.example.cj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Arduino_Activity extends AppCompatActivity {

    Button front, back, left, right;

    BluetoothAdapter mBluetoothAdapter;
    Set<BluetoothDevice> mDevices;
    BluetoothSocket bSocket;
    OutputStream mOutputStream;
    InputStream mInputStream;
    BluetoothDevice mRemoteDevice;
    boolean onBT = false;
    String sendByte;
    String android_date;
    ProgressDialog asyncDialog;
    static final int REQUEST_ENABLE_BT = 1;
    TextView BTButton;
    LinearLayout auto, manual;
    LinearLayout stop1, stop2;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference_auto;
    DatabaseReference databaseReference_manual;
    String AUTO, MANUAL;
    WebView webview;
    WebSettings webSettings;
    TextView log_back;
    int count1 = 0;
    LinearLayout manual_button,auto_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aduino_activity);
        //블랙박스 RC카 자율주행 및 수동주행 제어 화면

        BTButton = (TextView) findViewById(R.id.btnBTCon);
        front = (Button) findViewById(R.id.front);
        back = (Button) findViewById(R.id.back);
        left = (Button) findViewById(R.id.left);
        right = (Button) findViewById(R.id.right);
        auto = (LinearLayout) findViewById(R.id.auto);
        manual = (LinearLayout) findViewById(R.id.manual);
        manual_button = (LinearLayout)findViewById(R.id.manual_button);
        auto_button = (LinearLayout)findViewById(R.id.auto_button);

        //실시간 영상 출력을 위한 웹뷰
        webview = (WebView) findViewById(R.id.webview);
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
        webview.loadUrl(getIntent().getStringExtra("url"));

        //주행모드 초기값으로 OFF 지정
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("주행모드");
        databaseReference.child("자율주행").child("auto").child("mode").setValue("OFF");
        databaseReference.child("수동주행").child("manual").child("mode").setValue("OFF");

        stop1 = (LinearLayout) findViewById(R.id.stop1);
        stop2 = (LinearLayout) findViewById(R.id.stop2);

        //기본 값은 수동주행이고 자율주행 모드로 제어 가능
        databaseReference_auto = database.getReference("주행모드").child("자율주행").child("auto").child("mode");
        databaseReference_auto.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    AUTO = snapshot.getValue().toString();

                    if (AUTO.equals("ON")) {
                        auto.setVisibility(View.GONE);
                        stop2.setVisibility(View.VISIBLE);
                    } else if (AUTO.equals("OFF")) {
                        auto.setVisibility(View.VISIBLE);
                        stop2.setVisibility(View.GONE);
                    }
                } catch (NullPointerException nullPointerException) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        log_back = (TextView) findViewById(R.id.log_back);
        log_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        try {
            BTSend.interrupt();   // 데이터 송신 쓰레드 종료
            mInputStream.close();
            mOutputStream.close();
            bSocket.close();
            onBT = false;
            BTButton.setText("connect");
        } catch (Exception ignored) {
        }

        //블루투스 연결
        BTButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!onBT) { //Connect
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter == null) { //장치가 블루투스를 지원하지 않는 경우.
                        Toast.makeText(getApplicationContext(), "Bluetooth 지원을 하지 않는 기기입니다.", Toast.LENGTH_SHORT).show();
                    } else { // 장치가 블루투스를 지원하는 경우.
                        if (!mBluetoothAdapter.isEnabled()) {

                            // 블루투스를 지원하지만 비활성 상태인 경우
                            // 블루투스를 활성 상태로 바꾸기 위해 사용자 동의 요청
                            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT); // 버젼문제
                        } else {

                            // 블루투스를 지원하며 활성 상태인 경우
                            // 페어링된 기기 목록을 보여주고 연결할 장치를 선택.
                            if (ActivityCompat.checkSelfPermission(Arduino_Activity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices(); // 버젼문제
                            if (pairedDevices.size() > 0) {
                                // 페어링 된 장치가 있는 경우.
                                selectDevice();
                            } else {
                                // 페어링 된 장치가 없는 경우.
                                Toast.makeText(getApplicationContext(), "먼저 Bluetooth 설정에 들어가 페어링을 진행해 주세요.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else { //DisConnect
                        try {
                            BTSend.interrupt();   // 데이터 송신 쓰레드 종료
                            mInputStream.close();
                            mOutputStream.close();
                            bSocket.close();
                            onBT = false;
                            BTButton.setText("connect");
                        } catch (Exception ignored) {
                    }
                }
            }
        });

        //자율 주행 모드 ON
        auto_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(count1%2==0){
                    databaseReference.child("자율주행").child("auto").child("mode").setValue("ON");
                    databaseReference.child("수동주행").child("manual").child("mode").setValue("OFF");
                    android_date = "a";
                    sendbtData(android_date);
                    count1++;
                }
                else if(count1%2==1){
                    databaseReference.child("자율주행").child("auto").child("mode").setValue("OFF");
                    databaseReference.child("수동주행").child("manual").child("mode").setValue("OFF");
                    android_date = "y";
                    sendbtData(android_date);
                    count1++;
                }

            }
        });


        front.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                  //  Toast.makeText(Arduino_Activity.this, "전진", Toast.LENGTH_SHORT).show();
                    android_date = "g";
                    sendbtData(android_date);
                } else if (action == MotionEvent.ACTION_UP) {
                    android_date = "s";
                   // Toast.makeText(Arduino_Activity.this, "멈춤", Toast.LENGTH_SHORT).show();
                    sendbtData(android_date);
                }
                return false;
            }
        });

        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                 //   Toast.makeText(Arduino_Activity.this, "후진", Toast.LENGTH_SHORT).show();
                    android_date = "b";
                    sendbtData(android_date);
                } else if (action == MotionEvent.ACTION_UP) {
                    android_date = "s";
                 //   Toast.makeText(Arduino_Activity.this, "멈춤", Toast.LENGTH_SHORT).show();
                    sendbtData(android_date);
                }
                return false;
            }
        });

        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                 //   Toast.makeText(Arduino_Activity.this, "좌회전", Toast.LENGTH_SHORT).show();
                    android_date = "l";
                    sendbtData(android_date);
                } else if (action == MotionEvent.ACTION_UP) {
                    android_date = "s";
                 //   Toast.makeText(Arduino_Activity.this, "멈춤", Toast.LENGTH_SHORT).show();
                    sendbtData(android_date);
                }
                return false;
            }
        });

        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                 //   Toast.makeText(Arduino_Activity.this, "우회전", Toast.LENGTH_SHORT).show();
                    android_date = "r";
                    sendbtData(android_date);
                } else if (action == MotionEvent.ACTION_UP) {
                    android_date = "s";
                   // Toast.makeText(Arduino_Activity.this, "멈춤", Toast.LENGTH_SHORT).show();
                    sendbtData(android_date);
                }
                return false;
            }
        });


    }


    public void selectDevice() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mDevices = mBluetoothAdapter.getBondedDevices(); // 버젼문제
        final int mPairedDeviceCount = mDevices.size();

        if (mPairedDeviceCount == 0) {
            //  페어링 된 장치가 없는 경우
            Toast.makeText(getApplicationContext(), "장치를 페어링 해주세요!", Toast.LENGTH_SHORT).show();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("블루투스 장치 선택");
        // 페어링 된 블루투스 장치의 이름 목록 작성
        List<String> listItems = new ArrayList<>();
        for (BluetoothDevice device : mDevices) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            listItems.add(device.getName()); // 버젼문제
        }
        listItems.add("취소");    // 취소 항목 추가

        final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == mPairedDeviceCount) {
                    // 연결할 장치를 선택하지 않고 '취소'를 누른 경우
                    //finish();
                } else {
                    // 연결할 장치를 선택한 경우
                    // 선택한 장치와 연결을 시도함
                    connectToSelectedDevice(items[item].toString());
                }
            }
        });
        builder.setCancelable(false);    // 뒤로 가기 버튼 사용 금지
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void connectToSelectedDevice(final String selectedDeviceName) {
        mRemoteDevice = getDeviceFromBondedList(selectedDeviceName);
        //Progress Dialog
        asyncDialog = new ProgressDialog(Arduino_Activity.this);
        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        asyncDialog.setMessage("블루투스 연결중..");
        asyncDialog.show();
        asyncDialog.setCancelable(false);

        Thread BTConnect = new Thread(new Runnable() {
            public void run() {
                try {
                    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //HC-06 UUID
                    // 소켓 생성
                    if (ActivityCompat.checkSelfPermission(Arduino_Activity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    bSocket = mRemoteDevice.createRfcommSocketToServiceRecord(uuid); // 버젼문제
                    // RFCOMM 채널을 통한 연결
                    bSocket.connect(); // 버젼문제
                    // 데이터 송수신을 위한 스트림 열기
                    mOutputStream = bSocket.getOutputStream();
                    mInputStream = bSocket.getInputStream();

                    runOnUiThread(new Runnable() {
                        @SuppressLint({"ShowToast", "SetTextI18n"})
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), selectedDeviceName + " 연결 완료", Toast.LENGTH_LONG).show();
                            BTButton.setText("disconnect");
                            asyncDialog.dismiss();
                        }
                    });
                    onBT = true;

                } catch (Exception e) {
                    // 블루투스 연결 중 오류 발생
                    runOnUiThread(new Runnable() {
                        @SuppressLint({"ShowToast", "SetTextI18n"})
                        @Override
                        public void run() {
                            //  tvBT.setText("연결 오류 -- BT 상태 확인해주세요.");
                            asyncDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "블루투스 연결 오류", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        BTConnect.start();
    }

    public BluetoothDevice getDeviceFromBondedList(String name) {
        BluetoothDevice selectedDevice = null;

        for (BluetoothDevice device : mDevices) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            }
            if (name.equals(device.getName())) { // 버젼문제
                selectedDevice = device;
                break;
            }
        }
        return selectedDevice;
    }

    Thread BTSend  = new Thread(new Runnable() {
        public void run() {
            try {
             //   mOutputStream.write(sendByte);    // 프로토콜 전송
                String data = sendByte;
                byte[] arr = data.getBytes();

                   mOutputStream.write(arr);
            } catch (Exception e) {
                // 문자열 전송 도중 오류가 발생한 경우.
            }
        }
    });

    //fixme : 데이터 전송
    public void sendbtData(String btLightPercent){

        sendByte = btLightPercent;
        BTSend.run();
    }

}