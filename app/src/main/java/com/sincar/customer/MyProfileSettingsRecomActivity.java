package com.sincar.customer;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;



public class MyProfileSettingsRecomActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo_recom);

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        findViewById(R.id.myinfo_btnPrev).setOnClickListener(this);
 //       findViewById(R.id.myinfo_btnNext).setOnClickListener(this);


        // myinfo_user_name => 이름
        // user_mobile_number => 휴대폰 번호

        // TODO - 서버 연동하여 이름, 휴대폰 번호 값 가지고 와서 설정해주기
//        TextView myinfo_user_name = findViewById(R.id.myinfo_user_name);
//        myinfo_user_name.setText("홍길동");
//
//        TextView user_mobile_number = findViewById(R.id.user_mobile_number);
//        user_mobile_number.setText("010-1234-5678");
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
//            case R.id.myinfo_btnPrev:
//                //  TODO - 내정보
//                intent = new Intent(this, MyProfileSettingsActivity.class);
//                startActivity(intent);
//                finish();
//                break;
//            case R.id.myinfo_btnNext:
//                //  TODO - 비밀번호변경
//                intent = new Intent(this, PasswordChangeActivity.class);
//                startActivity(intent);
//                finish();
//                break;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, MyProfileSettingsActivity.class);
        startActivity(intent);

        finish();
    }
}

