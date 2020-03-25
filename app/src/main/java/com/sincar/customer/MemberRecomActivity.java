package com.sincar.customer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 2020.03.24 spirit
 * 회원가입 - 핸드폰 번호 입력 액티비티
 */
public class MemberRecomActivity extends Activity implements View.OnClickListener {

    private Button login_join_btn;
    private EditText recom_code;
    private String phone_number, password, nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_join_recom);

        Intent intent = getIntent(); /*데이터 수신*/
        phone_number    = intent.getExtras().getString("phone_number");   /*String형*/
        password        = intent.getExtras().getString("password");       /*String형*/
        nickname        = intent.getExtras().getString("nickname");       /*String형*/

        findViewById(R.id.recom_btnPrev).setOnClickListener(this);
        findViewById(R.id.recom_join_btn).setOnClickListener(this);
        findViewById(R.id.recom_nextBtn).setOnClickListener(this);

        recom_code = (EditText) findViewById(R.id.recom_code);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.recom_btnPrev:
                finish();
                break;

            case R.id.recom_join_btn:
                if(!TextUtils.isEmpty(recom_code.getText().toString().trim()))
                {
                    //약관페이지로 이동
                    //추천인 코드로 이동
                    Intent intent = new Intent(MemberRecomActivity.this, com.sincar.customer.MemberJoinTermsActivity.class);
                    intent.putExtra("phone_number", phone_number);
                    intent.putExtra("password", password);
                    intent.putExtra("nickname", nickname);  // 본인 실명
                    intent.putExtra("recommand", recom_code.getText().toString().trim());   // 추천인 코드
                    startActivity(intent);
                }else{
                    Toast.makeText(MemberRecomActivity.this, "추천인 코드를 입력 해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.recom_nextBtn:
                //건너뛰기
                Intent intent = new Intent(MemberRecomActivity.this, com.sincar.customer.MemberJoinTermsActivity.class);
                intent.putExtra("phone_number", phone_number);
                intent.putExtra("password", password);
                intent.putExtra("nickname", nickname);  // 본인 실명
                intent.putExtra("recommand", "");   // 추천인 코드
                startActivity(intent);
                break;
        }
    }

}

