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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sincar.customer.item.AuthResult;
import com.sincar.customer.item.JoinResult;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.Util;

import java.util.HashMap;

import static com.sincar.customer.HWApplication.joinResult;
import static com.sincar.customer.HWApplication.voJoinItem;
import static com.sincar.customer.common.Constants.LOGIN_REQUEST;
import static com.sincar.customer.common.Constants.MEMBER_JOIN_REQUEST;

/**
 * 2020.03.24 spirit
 * 회원가입 - 약관동의 액티비티
 */
public class MemberJoinTermsActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Button terms_join_btn;
    private EditText recom_code;
    private String phone_number, password, nickname, recommand;
    private CheckBox terms_checkbox1;
    private CheckBox checkBoxTerms_1, checkBoxTerms_2, checkBoxTerms_3, checkBoxTerms_4;
    private boolean confirm_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_join_terms);

        Intent intent = getIntent(); /*데이터 수신*/
        phone_number    = intent.getExtras().getString("phone_number");   /*String형*/
        password        = intent.getExtras().getString("password");       /*String형*/
        nickname        = intent.getExtras().getString("nickname");       /*String형*/
        recommand       = intent.getExtras().getString("recommand");      /*String형*/

        findViewById(R.id.terms_btnPrev).setOnClickListener(this);
        terms_join_btn = (Button) findViewById(R.id.terms_join_btn);
        terms_join_btn.setOnClickListener(this);

//        findViewById(R.id.terms_join_btn).setOnClickListener(this);


 //       recom_code = (EditText) findViewById(R.id.recom_code);

        terms_checkbox1 = (CheckBox) findViewById(R.id.terms_checkbox1);
        terms_checkbox1.setOnCheckedChangeListener(this);

        checkBoxTerms_1 = (CheckBox) findViewById(R.id.checkBoxTerms_1);
        checkBoxTerms_1.setOnCheckedChangeListener(this);
        checkBoxTerms_2 = (CheckBox) findViewById(R.id.checkBoxTerms_2);
        checkBoxTerms_2.setOnCheckedChangeListener(this);
        checkBoxTerms_3 = (CheckBox) findViewById(R.id.checkBoxTerms_3);
        checkBoxTerms_3.setOnCheckedChangeListener(this);
        checkBoxTerms_4 = (CheckBox) findViewById(R.id.checkBoxTerms_4);
        checkBoxTerms_4.setOnCheckedChangeListener(this);

        findViewById(R.id.terms_view_1).setOnClickListener(this);
        findViewById(R.id.terms_view_2).setOnClickListener(this);
        findViewById(R.id.terms_view_3).setOnClickListener(this);
        findViewById(R.id.terms_view_4).setOnClickListener(this);

        terms_checkbox1.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) {
                    // CheckBox is checked.
                    checkBoxTerms_1.setChecked(true);
                    checkBoxTerms_2.setChecked(true);
                    checkBoxTerms_3.setChecked(true);
                    checkBoxTerms_4.setChecked(true);

                    confirm_status = true;
                    terms_join_btn.setEnabled(true);
                } else {
                    // CheckBox is unchecked.
                    checkBoxTerms_1.setChecked(false);
                    checkBoxTerms_2.setChecked(false);
                    checkBoxTerms_3.setChecked(false);
                    checkBoxTerms_4.setChecked(false);

                    confirm_status = false;
                    terms_join_btn.setEnabled(false);
                }
            }
        }) ;

        checkBoxTerms_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked )
                {
                    if(checkBoxTerms_1.isChecked() && checkBoxTerms_2.isChecked() && checkBoxTerms_3.isChecked() && checkBoxTerms_4.isChecked())
                    {
                        terms_checkbox1.setChecked(true);
                    }else{
                        terms_checkbox1.setChecked(false);
                    }

                    if(checkBoxTerms_1.isChecked() && checkBoxTerms_2.isChecked() && checkBoxTerms_3.isChecked())
                    {
                        confirm_status = true;
                        terms_join_btn.setEnabled(true);
                    }
                } else {
                    if(terms_checkbox1.isChecked())
                    {
                        terms_checkbox1.setChecked(false);
                    }

                    confirm_status = false;
                    terms_join_btn.setEnabled(false);
                }
            }
        });

        checkBoxTerms_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked )
                {
                    if(checkBoxTerms_1.isChecked() && checkBoxTerms_2.isChecked() && checkBoxTerms_3.isChecked() && checkBoxTerms_4.isChecked())
                    {
                        terms_checkbox1.setChecked(true);
                    }else{
                        terms_checkbox1.setChecked(false);
                    }

                    if(checkBoxTerms_1.isChecked() && checkBoxTerms_2.isChecked() && checkBoxTerms_3.isChecked())
                    {
                        confirm_status = true;
                        terms_join_btn.setEnabled(true);
                    }
                } else {
                    if(terms_checkbox1.isChecked())
                    {
                        terms_checkbox1.setChecked(false);
                    }

                    confirm_status = false;
                    terms_join_btn.setEnabled(false);
                }
            }
        });

        checkBoxTerms_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked )
                {
                    if(checkBoxTerms_1.isChecked() && checkBoxTerms_2.isChecked() && checkBoxTerms_3.isChecked() && checkBoxTerms_4.isChecked())
                    {
                        terms_checkbox1.setChecked(true);
                    }else{
                        terms_checkbox1.setChecked(false);
                    }

                    if(checkBoxTerms_1.isChecked() && checkBoxTerms_2.isChecked() && checkBoxTerms_3.isChecked())
                    {
                        confirm_status = true;
                        terms_join_btn.setEnabled(true);
                    }

                } else {
                    if(terms_checkbox1.isChecked())
                    {
                        terms_checkbox1.setChecked(false);
                    }

                    confirm_status = false;
                    terms_join_btn.setEnabled(false);
                }
            }
        });

        checkBoxTerms_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked )
                {
                    if(checkBoxTerms_1.isChecked() && checkBoxTerms_2.isChecked() && checkBoxTerms_3.isChecked() && checkBoxTerms_4.isChecked())
                    {
                        terms_checkbox1.setChecked(true);
                    }else{
                        terms_checkbox1.setChecked(false);
                    }

                    if(checkBoxTerms_1.isChecked() && checkBoxTerms_2.isChecked() && checkBoxTerms_3.isChecked())
                    {
                        confirm_status = true;
                        terms_join_btn.setEnabled(true);
                    }
                } else {
                    if(terms_checkbox1.isChecked())
                    {
                        terms_checkbox1.setChecked(false);
                    }
                }
            }
        });
    }




    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

            case R.id.terms_btnPrev:
                finish();
                break;

            case R.id.terms_view_1:
                // TODO - 이용약관 동의 페이지로
                intent = new Intent(this, UseTerms1Activity.class);
                intent.putExtra("path", "5");
                startActivity(intent);
                break;

            case R.id.terms_view_2:
                // TODO - 개인정보 보호정책 동의 페이지로
                intent = new Intent(this, UseTerms1Activity.class);
                intent.putExtra("path", "6");
                startActivity(intent);
                break;

            case R.id.terms_view_3:
                // TODO - 위치기반서비스 이용 동의 페이지로
                intent = new Intent(this, UseTerms1Activity.class);
                intent.putExtra("path", "7");
                startActivity(intent);
                break;

            case R.id.terms_view_4:
                // TODO - 이용약관 동의 페이지로
                intent = new Intent(this, UseTerms1Activity.class);
                intent.putExtra("path", "8");
                startActivity(intent);
                break;

            case R.id.terms_join_btn:
                // TODO - 회원 가입하고 성공시 로그인 페이지로 이동
                if(confirm_status)
                {
                    //로그인 페이지로 이동
                    requestMemberJoin();
                    //Toast.makeText(MemberJoinTermsActivity.this, "로그인 페이지로 이동", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MemberJoinTermsActivity.this, "필수 동의를 해주세요", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (terms_checkbox1.isChecked() )
        {
            // perform logic
//            Toast.makeText(MemberJoinTermsActivity.this, "전체동의", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(MemberJoinTermsActivity.this, "전체거부", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 회원가입
     */
    private void requestMemberJoin() {

        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("PHONE_NUMBER", phone_number);       // 사용자 전화번호
        postParams.put("PASSWORD", password);               // 사용자 비밀번호
        postParams.put("NICKNAME", nickname);               // 본인실명
        postParams.put("RECOMMAND", recommand);             // 추천인코드
        postParams.put("TERMS_1", "Y");                     // 이용약관 동의
        postParams.put("TERMS_2", "Y");                     // 개인정보 보호정책 동의
        postParams.put("TERMS_3", "Y");                     // 위치기반서비스 이용 동의
        if(checkBoxTerms_1.isChecked())
        {
            postParams.put("TERMS_4", "Y");                 // 이용약관 동의
        }else {
            postParams.put("TERMS_4", "N");                 // 이용약관 동의
        }

        //프로그래스바 시작
        Util.showDialog(this);

        //인증번호 요청
        VolleyNetwork.getInstance(this).serverDataRequest(MEMBER_JOIN_REQUEST, postParams, onMemberJoinResponseListener);
    }

    VolleyNetwork.OnResponseListener onMemberJoinResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String it) {
            //{"join_result":{"JOIN_RESULT":"0","CAUSE":"회원가입 완료"}}

            Gson gSon = new Gson();
            joinResult = gSon.fromJson(it, JoinResult.class);

            voJoinItem.JOIN_RESULT      = joinResult.join_result.JOIN_RESULT;

            //프로그래스바 종료
            Util.dismiss();

            if("0".equals(voJoinItem.JOIN_RESULT)) {
                Toast.makeText(MemberJoinTermsActivity.this, "회원 가입 되었습니다. 로그인 페이지로 이동합니다.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MemberJoinTermsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(MemberJoinTermsActivity.this, "네트워크 오류입니다. 재시도 해주세요.", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onResponseFailListener(VolleyError it) {
            Toast.makeText(MemberJoinTermsActivity.this, "서버 전송에 실패하였습니다. 재시도 해주세요.", Toast.LENGTH_LONG).show();
        }
    };
}