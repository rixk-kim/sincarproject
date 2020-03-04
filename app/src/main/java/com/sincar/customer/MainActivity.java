package com.sincar.customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private MenuItem prevBottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    @SuppressLint("NewApi")
    private void init() {
        findViewById(R.id.btnCustomerPoint).setOnClickListener(this);
        findViewById(R.id.btnMainMenu1).setOnClickListener(this);
        findViewById(R.id.btnMainMenu2).setOnClickListener(this);
        findViewById(R.id.btnMainMenu3).setOnClickListener(this);
        findViewById(R.id.btnMainMenu4).setOnClickListener(this);
        findViewById(R.id.btnCarRegisterClose).setOnClickListener(this);
        findViewById(R.id.btnCarRegister).setOnClickListener(this);

        //하단메뉴 고정(0:홈)
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        prevBottomNavigation = bottomNavigationView.getMenu().getItem(0);
        prevBottomNavigation.setChecked(true);

        // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.bottom_home: {
                        // TODO - HOME
                        break;
                    }
                    case R.id.bottom_use_history: {
                        // TODO - 이용내역
                        break;
                    }
                    case R.id.bottom_myinfo: {
                        intent = new Intent(mContext, MyProfileSettingsActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }

                return true;
            }
        });
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
                intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
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
        }
    }
}
