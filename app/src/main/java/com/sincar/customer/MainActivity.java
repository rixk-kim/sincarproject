package com.sincar.customer;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.sincar.customer.HWApplication.voLoginData;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //LoginEntity vo = new LoginEntity();
        //System.out.println("[spirit] 회원번호 : " + voLoginData.getMemberNo());
        //System.out.println("[spirit] App Version : " + voLoginData.getAppVersion());

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        TextView branch_title = findViewById(R.id.branch_title);
        TextView branch_owner = findViewById(R.id.branch_owner);

        Button btn1 = findViewById(R.id.button1);
        Button btn2 = findViewById(R.id.button2);
        Button btn3 = findViewById(R.id.button3);
        Button btn4 = findViewById(R.id.button4);
        Button btn5 = findViewById(R.id.button5);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.button1:  // 영업시간 설정
                intent = new Intent(this, BusinessHourMainActivity.class);
                startActivity(intent);
                break;
            case R.id.button2:  // 예약 접수
                intent = new Intent(this, ReservationListActivity.class);
                startActivity(intent);
                break;
            case R.id.button3:  // 활동 지역 설정
                break;
            case R.id.button4:  // 당월 지표
                // 테스트코드 : 지도보기
                intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                break;
            case R.id.button5:  // 내 정보 관리
                intent = new Intent(this, MyProfileSettingsActivity.class);
                startActivity(intent);
                break;
        }
    }
}
