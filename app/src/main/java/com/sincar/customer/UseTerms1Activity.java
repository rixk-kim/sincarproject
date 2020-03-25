package com.sincar.customer;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class UseTerms1Activity extends AppCompatActivity implements View.OnClickListener {
    private String variable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent(); /*데이터 수신*/
        variable    = intent.getExtras().getString("path");    /*String형*/

        if("1".equals(variable) || "5".equals(variable))
        {
            setContentView(R.layout.activity_use_terms_1);  //서비스 이용약관
        }else if("2".equals(variable) || "6".equals(variable))
        {
            setContentView(R.layout.activity_use_terms_2);  //개인정보 처리방침
        }else if("3".equals(variable) || "7".equals(variable))
        {
            setContentView(R.layout.activity_use_terms_3);  //위치기반서비스 이용약관
        }else
        {
            setContentView(R.layout.activity_use_terms_4);  //마케팅 정보 수신동의
        }
        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        // TODO - 서비스 이용약관 내용 수정
        if("1".equals(variable) || "5".equals(variable))
        {
            findViewById(R.id.useterms1_btnPrev).setOnClickListener(this);
        }else if("2".equals(variable) || "6".equals(variable))
        {
            findViewById(R.id.useterms2_btnPrev).setOnClickListener(this);
        }else if("3".equals(variable) || "7".equals(variable))
        {
            findViewById(R.id.useterms3_btnPrev).setOnClickListener(this);
        }else
        {
            findViewById(R.id.useterms4_btnPrev).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.useterms1_btnPrev:
            case R.id.useterms2_btnPrev:
            case R.id.useterms3_btnPrev:
            case R.id.useterms4_btnPrev:
                //  내정보
                int tmp_variable = Integer.parseInt(variable);
                if(tmp_variable < 5) {
                    intent = new Intent(this, UseTermsActivity.class);
                    startActivity(intent);
                }
                finish();
                break;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        int tmp_variable = Integer.parseInt(variable);
        if(tmp_variable < 5) {
            Intent intent = new Intent(this, UseTermsActivity.class);
            startActivity(intent);
        }
        finish();
    }
}


