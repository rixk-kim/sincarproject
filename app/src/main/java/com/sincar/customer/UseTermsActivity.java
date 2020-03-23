package com.sincar.customer;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class UseTermsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_terms);

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        findViewById(R.id.useterms_btnPrev).setOnClickListener(this);

        findViewById(R.id.terms_menu_1).setOnClickListener(this);
        findViewById(R.id.terms_menu_2).setOnClickListener(this);
        findViewById(R.id.terms_menu_3).setOnClickListener(this);
        findViewById(R.id.terms_menu_4).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.useterms_btnPrev:
                //  내정보
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.terms_menu_1:

 //               Toast.makeText(this, "서비스 이용약관 정보 준비중입니다.", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, UseTerms1Activity.class);
                intent.putExtra("path", "1");
                startActivity(intent);
                break;

            case R.id.terms_menu_2:
                // TODO - 개인정보 처리방침
//                Toast.makeText(this, "개인정보 처리방침 정보 준비중입니다.", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, UseTerms1Activity.class);
                intent.putExtra("path", "2");
                startActivity(intent);
                break;

            case R.id.terms_menu_3:
                // TODO - 위치기반서비스 이용약관
//                Toast.makeText(this, "위치기반서비스 이용약관 정보 준비중입니다.", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, UseTerms1Activity.class);
                intent.putExtra("path", "3");
                startActivity(intent);
                break;

            case R.id.terms_menu_4:
                // TODO - 마케팅 정보 수신동의
//                Toast.makeText(this, "마케팅 정보 수신동의 정보 준비중입니다.", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, UseTerms1Activity.class);
                intent.putExtra("path", "4");
                startActivity(intent);
                break;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);

        finish();
    }
}


