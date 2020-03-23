package com.sincar.customer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.Util;

import java.util.Map;
import java.util.regex.Pattern;

public class PasswordChangeActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText new_password1, new_password2;
    private Context pContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        pContext = this;

        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        hideActionBar();

        findViewById(R.id.change_btnPrev).setOnClickListener(this);
        findViewById(R.id.btnPassword).setOnClickListener(this);

        new_password1 = (EditText) findViewById(R.id.new_password1);
        new_password2 = (EditText) findViewById(R.id.new_password2);

        Map<String, String> params = null;

        VolleyNetwork.getInstance(this).passwordChangeRequest("url", params, onResponseListener);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.change_btnPrev:
                intent = new Intent(this, MyProfileSettingsDetailActivity.class);
                startActivity(intent);
                finish();
                break;

            // 비밀번호 변경
            // TODO - 해야할 일
            // 새 비밀번호, 새 비밀번호 확인 입력값 유효성 확인 및 값 비교
            // 서버 통신 후 결과값 확인. 비밀번호 변경 후 로직 변경 필요

            // commonAlert에 listener 필요해서 확인 눌렀을때 해야할 일 처리 필요할꺼 같은데요.
            case R.id.btnPassword:


                //비밀번호 유효성
                if(TextUtils.isEmpty(new_password1.getText().toString().trim()))
                {
                    Toast.makeText(PasswordChangeActivity.this,"비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!Pattern.matches("^[a-zA-Z0-9]*$", new_password1.getText().toString().trim()))
                {
                    Toast.makeText(PasswordChangeActivity.this,"비밀번호 형식을 지켜주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(new_password1.getText().toString().trim().length() < 6)
                {
                    Toast.makeText(PasswordChangeActivity.this,"비밀번호 길이를 지켜주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(new_password2.getText().toString().trim()))
                {
                    Toast.makeText(PasswordChangeActivity.this,"비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!Pattern.matches("^[a-zA-Z0-9]*$", new_password2.getText().toString().trim()))
                {
                    Toast.makeText(PasswordChangeActivity.this,"비밀번호 형식을 지켜주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(new_password2.getText().toString().trim().length() < 6)
                {
                    Toast.makeText(PasswordChangeActivity.this,"비밀번호 길이를 지켜주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!new_password1.equals(new_password2))
                {
                    Toast.makeText(PasswordChangeActivity.this,"비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

 //               Util.commonAlert(this, getString(R.string.password_change_success), false, false);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                // 메세지
                builder.setTitle(getString(R.string.notice));
                builder.setMessage(R.string.password_change_success);
                builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();

                        Intent intent1 = new Intent(pContext, LoginActivity.class);
                        startActivity(intent1);
                        finish();
                    }
                }).show();
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
