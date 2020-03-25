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
public class MemberNickNameActivity extends Activity implements View.OnClickListener {

    private Button login_join_btn;
    private EditText nick_user_name;
    private String phone_number, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_join_nickname);

        Intent intent = getIntent(); /*데이터 수신*/
        phone_number    = intent.getExtras().getString("phone_number");       /*String형*/
        password        = intent.getExtras().getString("password");       /*String형*/

        findViewById(R.id.nick_btnPrev).setOnClickListener(this);
        findViewById(R.id.login_join_btn).setOnClickListener(this);

        nick_user_name = (EditText) findViewById(R.id.nick_user_name);
    }

    /**
     * 에러 팝업
     * 1~ 2 서버 통신 후 에러 메세지
     * 3 ~5 내부 유효성 검사 메세지
     * @param resultCode : 내용 코드
     */
    private void showErrorAlert(String resultCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MemberNickNameActivity.this);
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
        switch (v.getId()) {

            case R.id.nick_btnPrev:
                finish();
                break;

            case R.id.login_join_btn:
                if(!TextUtils.isEmpty(nick_user_name.getText().toString().trim()))
                {
                    //추천인 코드로 이동
                    Intent intent = new Intent(MemberNickNameActivity.this, com.sincar.customer.MemberRecomActivity.class);
                    intent.putExtra("phone_number", phone_number);
                    intent.putExtra("password", password);
                    intent.putExtra("nickname", nick_user_name.getText().toString().trim());
                    startActivity(intent);
                    // 최초 생성 후 이동 시 제거
                    //finish();
                }else{
                    Toast.makeText(MemberNickNameActivity.this, "본인 실명을 입력 해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

