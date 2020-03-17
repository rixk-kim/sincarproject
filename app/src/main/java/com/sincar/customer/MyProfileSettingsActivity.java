package com.sincar.customer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MyProfileSettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private MenuItem prevBottomNavigation;
    private Context mContext;
    private BottomNavigationView bottomNavigationView;

    public MyProfileSettingsActivity() {
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_settings);
        mContext = this;
        init();

        //하단메뉴 고정(2:내정보)
        bottomNavigationView = findViewById(R.id.bottomNav_set);
        prevBottomNavigation = bottomNavigationView.getMenu().getItem(2);
        prevBottomNavigation.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.bottom_home: {
                        // HOME
                        intent = new Intent(mContext, MainActivity.class);
                        startActivity(intent);

                        prevBottomNavigation = bottomNavigationView.getMenu().getItem(0);
                        prevBottomNavigation.setChecked(true);

                        finish();
                        break;
                    }
                    case R.id.bottom_use_history: {
                        // 이용내역
                        intent = new Intent(mContext, UseHistoryActivity.class);
                        startActivity(intent);

                        prevBottomNavigation = bottomNavigationView.getMenu().getItem(1);
                        prevBottomNavigation.setChecked(true);

                        finish();
                        break;
                    }
                    case R.id.bottom_myinfo: {
                        // 내정보
                        break;
                    }
                }

                return true;
            }
        });
    }

    /**
     * 화면 초기화
     */
    private void init() {
        hideActionBar();

        findViewById(R.id.btnNext).setOnClickListener(this);
        findViewById(R.id.menu_1).setOnClickListener(this);
        findViewById(R.id.menu_2).setOnClickListener(this);
        findViewById(R.id.menu_3).setOnClickListener(this);
        findViewById(R.id.menu_4).setOnClickListener(this);
        findViewById(R.id.menu_5).setOnClickListener(this);
        findViewById(R.id.menu_6).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.btnNext:   // 내정보 상세 정보
                intent = new Intent(this, MyProfileSettingsDetailActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menu_1:  // 추천인 관리 정보
                intent = new Intent(this, MyProfileSettingsRecomActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_2:   // 공지사항 정보
                intent = new Intent(this, NoticeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_3:   // 결재카드 정보
                intent = new Intent(this,CardActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_4:   // 쿠폰 정보
                intent = new Intent(this, CouponeActivity.class);
                intent.putExtra("path", "profile");
                startActivity(intent);
                finish();
                break;
            case R.id.menu_5:   // 차량관리
                intent = new Intent(this, CarManageActivity.class);
                intent.putExtra("path", "carmanage");
                startActivity(intent);
                finish();
                break;
            case R.id.menu_6:   // 알림 및 버전 정보
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 로그아웃 알럿 다이얼로그
     * @param context
     */
    private void showLogoutAlertDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle(context.getString(R.string.notice));
        builder.setMessage("로그아웃 하시겠습니까?");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }


    //==============================================================================================

    private void hideActionBar() {
        ActionBar ab = getSupportActionBar();
        ab.hide();
    }

    private void actionBarInit() {
        // ActionBar 설정
        // 타이틀 설정
        ActionBar ab = getSupportActionBar();
        ab.setTitle("비밀번호 변경");

        // ActionBar 그림자 제거
        ab.setBackgroundDrawable(null);

        // up icon 설정(show: true, hide: false)
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.filled);

        // 배경 색상 변경 - 배경 흰색으로 할 경우 아이콘 색상 변경 필요
        // colorPrimary color 변경
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);

        prevBottomNavigation = bottomNavigationView.getMenu().getItem(0);
        prevBottomNavigation.setChecked(true);

        finish();
    }
}
