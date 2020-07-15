package com.sincar.customer;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import static com.sincar.customer.MapsActivity._mMapsActivity;
import static com.sincar.customer.MapsActivity.homeKeyPressed;


public class UseTerms2Activity extends AppCompatActivity implements View.OnClickListener {
    private String vari;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_terms_5);  //서비스 이용약관

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        // TODO - 서비스 이용약관 내용 수정
        findViewById(R.id.useterms5_btnPrev).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.useterms5_btnPrev:
                //  내정보
                finish();
                break;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (_mMapsActivity != null) {
            homeKeyPressed = true;
            _mMapsActivity.onPause();
        }
    }
}


