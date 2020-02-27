package com.sincar.customer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class MyProfileSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_settings);

        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        hideActionBar();

        findViewById(R.id.btnBack).setOnClickListener(this);
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
            case R.id.btnBack:
                finish();
                break;

            case R.id.menu_1:   // 프로필 사진 변경
//                intent = new Intent(this, BusinessHourMainActivity.class);
//                startActivity(intent);
                break;
            case R.id.menu_2:   // 비밀번호 변경
                intent = new Intent(this, PasswordChangeActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_3:   // 대리점 정보
//                intent = new Intent(this, BusinessHourMainActivity.class);
//                startActivity(intent);
                break;
            case R.id.menu_4:   // 운전면허증 정보
//                intent = new Intent(this, BusinessHourMainActivity.class);
//                startActivity(intent);
                break;
            case R.id.menu_5:   // 알림 및 버전 정보
                intent = new Intent(this, PushAndVersionActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_6:   // 로그아웃
                showLogoutAlertDialog(this);
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
}
