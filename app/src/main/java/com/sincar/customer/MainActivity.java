package com.sincar.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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
        findViewById(R.id.btnCustomerPoint).setOnClickListener(this);
        findViewById(R.id.btnMainMenu1).setOnClickListener(this);
        findViewById(R.id.btnMainMenu2).setOnClickListener(this);
        findViewById(R.id.btnMainMenu3).setOnClickListener(this);
        findViewById(R.id.btnMainMenu4).setOnClickListener(this);
        findViewById(R.id.btnCarRegisterClose).setOnClickListener(this);
        findViewById(R.id.btnCarRegister).setOnClickListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnClickListener(this);
 //       bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.btnCustomerPoint:
                // TODO - 포인트 보기
                intent = new Intent(this, PointHistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.btnMainMenu1:
                // TODO - 스팀세차
                break;
            case R.id.btnMainMenu2:
                // TODO - 대리운전
                break;
            case R.id.btnMainMenu3:
                // TODO - 카페어링
                break;
            case R.id.btnMainMenu4:
                // TODO - 카쉐어
                break;
            case R.id.btnCarRegisterClose:
                // TODO - 배너 차량등록 종료
                break;
            case R.id.btnCarRegister:
                // TODO - 배너 차량등록
                break;

            case R.id.bottom_home:
                // TODO - Home
                break;

            case R.id.bottom_use_history:
                // TODO - 이용내역
                break;

            case R.id.bottom_myinfo:
                // TODO - 내정보
                break;
        }
    }
}
