package com.sincar.customer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.VolleyError;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.Util;

import java.util.Map;

public class PasswordChangeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        hideActionBar();

        findViewById(R.id.btnPrev).setOnClickListener(this);
        findViewById(R.id.btnPassword).setOnClickListener(this);

        Map<String, String> params = null;

        VolleyNetwork.getInstance(this).passwordChangeRequest("url", params, onResponseListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPrev:
                Intent intent = new Intent(this, MyProfileSettingsDetailActivity.class);
                startActivity(intent);
                finish();
                break;

            // 비밀번호 변경
            // 해야할 일
            // 새 비밀번호, 새 비밀번호 확인 입력값 유효성 확인 및 값 비교
            // 서버 통신 후 결과값 확인. 비밀번호 변경 후 로직 변경 필요

            // commonAlert에 listener 필요해서 확인 눌렀을때 해야할 일 처리 필요할꺼 같은데요.
            case R.id.btnPassword:
                Util.commonAlert(this, getString(R.string.password_change_success), false, false);
                break;
        }
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

    VolleyNetwork.OnResponseListener onResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String it) {

        }

        @Override
        public void onResponseFailListener(VolleyError it) {

        }
    };
}
