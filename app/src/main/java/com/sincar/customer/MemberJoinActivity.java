package com.sincar.customer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sincar.customer.item.AuthResult;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.Util;

import java.util.HashMap;

import static com.sincar.customer.HWApplication.authResult;
import static com.sincar.customer.HWApplication.voAuthItem;
import static com.sincar.customer.common.Constants.AUTH_NUMBER_REQUEST;
import static com.sincar.customer.common.Constants.LOGIN_REQUEST;

/**
 * 2020.03.24 spirit
 * 회원가입 - 핸드폰 번호 입력 액티비티
 */
public class MemberJoinActivity extends Activity implements View.OnClickListener {
    private static final String TAG = LoginActivityPre.class.getSimpleName();
    //===================== 뷰 =====================
    private Button login_join_btn;
    private EditText join_user_phone;
    //===================== 뷰 =====================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_join_phone);

        findViewById(R.id.btnPrev).setOnClickListener(this);
        login_join_btn = (Button) findViewById(R.id.login_join_btn);
        login_join_btn.setOnClickListener(this);

        join_user_phone = (EditText) findViewById(R.id.join_user_phone);
    }

    /**
     * 에러 팝업
     * 1~ 2 서버 통신 후 에러 메세지
     * 3 ~5 내부 유효성 검사 메세지
     * @param resultCode : 내용 코드
     */
    private void showErrorAlert(String resultCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MemberJoinActivity.this);
        // 메세지
        String errorMsg = "";
        if (resultCode.equals("1")) {
            errorMsg = getString(R.string.not_correct_your_info_msg);
        } else if (resultCode.equals("2")) {
            errorMsg = getString(R.string.not_register_user_msg);
        } else if (resultCode.equals("3")) {
            errorMsg = getString(R.string.input_company_code_msg);
        } else if (resultCode.equals("4")) {
            errorMsg = getString(R.string.input_id_msg);
        } else if (resultCode.equals("5")) {
            errorMsg = getString(R.string.input_pwd_msg);
        }

        builder.setTitle(getString(R.string.notice));
        builder.setMessage(errorMsg);
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        }).show();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

            case R.id.login_join_btn:
                // 서버에 인증번호 발송 요청 이동
                if (join_user_phone != null || join_user_phone.getText().toString().trim().length() != 0) {
                    if (join_user_phone.getText().toString().trim().length() == 0) {
                        showErrorAlert("4");
                        return;
                    }
                }

                requestAuthCode();

                break;

            case R.id.btnPrev:
                //인트로 이동
                intent = new Intent(this, LoginActivityPre.class);
                startActivity(intent);
                finish();
                break;

        }
    }

    private void requestAuthCode() {

        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("PHONE_NUMBER", join_user_phone.getText().toString().trim());     // 사용자 전화번호

        //프로그래스바 시작
        Util.showDialog(this);

        //인증번호 요청
        VolleyNetwork.getInstance(this).serverDataRequest(AUTH_NUMBER_REQUEST, postParams, onAuthResponseListener);
    }

    VolleyNetwork.OnResponseListener onAuthResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String it) {
//            it = " {\"auth_send\": {\"AUTH_RESULT\":\"0\",\"CAUSE\":\"\",\"AUTH_NUMBER\":\"123456\"}}";
            Gson gSon = new Gson();
            authResult = gSon.fromJson(it, AuthResult.class);

            voAuthItem.AUTH_RESULT      = authResult.auth_send.AUTH_RESULT;
            voAuthItem.CAUSE            = authResult.auth_send.CAUSE;
            voAuthItem.AUTH_NUMBER      = authResult.auth_send.AUTH_NUMBER;  //인증번호

            //프로그래스바 종료
            Util.dismiss();

            if("0".equals(voAuthItem.AUTH_RESULT)) {
                Intent intent = new Intent(MemberJoinActivity.this, MemberAuthActivity.class);
                intent.putExtra("phone_number", join_user_phone.getText().toString().trim());
                startActivity(intent);
            }else{
                Toast.makeText(MemberJoinActivity.this, "인증 번호 보내기에 실패하였습니다. 재입력 해주세요.", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onResponseFailListener(VolleyError it) {
            //프로그래스바 종료
            Util.dismiss();

            Toast.makeText(MemberJoinActivity.this, "인증 번호 보내기에 실패하였습니다. 재입력 해주세요.", Toast.LENGTH_LONG).show();
        }
    };

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, LoginActivityPre.class);
        startActivity(intent);
        finish();
    }
}
