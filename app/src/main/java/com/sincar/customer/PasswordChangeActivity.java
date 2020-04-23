package com.sincar.customer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sincar.customer.item.AuthResult;
import com.sincar.customer.item.PasswordResult;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.preference.PreferenceManager;
import com.sincar.customer.util.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.sincar.customer.HWApplication.passwordResult;
import static com.sincar.customer.HWApplication.voAuthItem;
import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.HWApplication.voPasswordItem;
import static com.sincar.customer.MemberAuthActivity._memberAuthActivity;
import static com.sincar.customer.MemberJoinActivity._memberJoinActivity;
import static com.sincar.customer.MyProfileSettingsDetailActivity._myProfileSettingsDetailActivity;
import static com.sincar.customer.common.Constants.AUTH_NUMBER_REQUEST;
import static com.sincar.customer.common.Constants.CHANGE_PASSWORD;

public class PasswordChangeActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText new_password1, new_password2;
    private TextView description_activity;
    private Context pContext;
    private String path;
    private String phone_number;
    public static PasswordChangeActivity _passwordChangeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        pContext = this;
        _passwordChangeActivity = PasswordChangeActivity.this;

        Intent intent = getIntent(); /*데이터 수신*/
        path  = intent.getExtras().getString("path");       /*String형*/
        //if("member_join".equals(path)) {
            phone_number = intent.getExtras().getString("phone_number");       /*String형*/
        //}

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

        if("join".equals(path)) {
            description_activity = (TextView) findViewById(R.id.description_activity);
            description_activity.setText(R.string.desc_activity_password);

            new_password1.setHint(R.string.hint_password1);
            new_password2.setHint(R.string.hint_password2);
        }

        // 비밀번호 입력 후 확인 버튼 클릭 시
        new_password2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // 유효성 체크 요청
                    validCheck();
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.change_btnPrev:
                if(!"member_join".equals(path) && !"password_search".equals(path)) {
                    intent = new Intent(this, MyProfileSettingsDetailActivity.class);
                    startActivity(intent);
                }
                finish();
                break;

            case R.id.btnPassword:
                validCheck();
                break;
        }
    }

    private void validCheck()
    {
        String tmp_password1 = new_password1.getText().toString().trim();
        String tmp_password2 = new_password2.getText().toString().trim();

        //비밀번호 유효성
        if(TextUtils.isEmpty(tmp_password1))
        {
            Toast.makeText(PasswordChangeActivity.this,"비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Pattern.matches("^(?=.*[0-9]+)[a-zA-Z][a-zA-Z0-9]{7,29}$", tmp_password1))
        {
            Toast.makeText(PasswordChangeActivity.this,"비밀번호 형식을 지켜주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(tmp_password1.length() < 8)
        {
            Toast.makeText(PasswordChangeActivity.this,"비밀번호 길이를 지켜주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(tmp_password2))
        {
            Toast.makeText(PasswordChangeActivity.this,"비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Pattern.matches("^(?=.*[0-9]+)[a-zA-Z][a-zA-Z0-9]{7,29}$", tmp_password2))
        {
            Toast.makeText(PasswordChangeActivity.this,"비밀번호 형식을 지켜주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(tmp_password2.length() < 8)
        {
            Toast.makeText(PasswordChangeActivity.this,"비밀번호 길이를 지켜주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!tmp_password1.equals(tmp_password2))
        {
            Toast.makeText(PasswordChangeActivity.this,"비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!"member_join".equals(path)) {
            //비밀번호 재설정
            changePassword(tmp_password1);
        }else{
            //본인 실명 입력 페이지 이동
            Intent intent = new Intent(this, MemberNickNameActivity.class);
            intent.putExtra("phone_number", phone_number);
            intent.putExtra("password", tmp_password1);
            startActivity(intent);
        }
    }

    private void changePassword(String password) {

        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("NEW_PASSWORD", password);               // 새 비밀번호
        postParams.put("PHONE_NUMBER", phone_number);     // 폰번호

        //프로그래스바 시작
        Util.showDialog(this);

        //인증번호 요청
        VolleyNetwork.getInstance(this).serverDataRequest(CHANGE_PASSWORD, postParams, onChangeResponseListener);
    }

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

    VolleyNetwork.OnResponseListener onChangeResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String it) {
            Gson gSon = new Gson();
            //LoginResult loginResult = gSon.fromJson(it, LoginResult.class);
            passwordResult = gSon.fromJson(it, PasswordResult.class);

            voPasswordItem.PASSWORD_RESULT      = passwordResult.password_change.PASSWORD_RESULT;
            voPasswordItem.CAUSE                = passwordResult.password_change.CAUSE;

            //프로그래스바 종료
            Util.dismiss();

            if("0".equals(voPasswordItem.PASSWORD_RESULT)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(pContext);
                // 메세지
                builder.setTitle(getString(R.string.notice));
                builder.setMessage(R.string.password_change_success);
                builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();

                        PreferenceManager.getInstance().setCheckLogin(false);
                        // 아이디 셋팅
                        PreferenceManager.getInstance().setUserId("");
                        // 패스워드 셋팅
                        PreferenceManager.getInstance().setUserPwd("");





                        if(!"change".equals(path))
                        {
                            //이전 Activity 종료.
                            if(_memberJoinActivity != null) {
                                MemberJoinActivity memberJoinActivity = (MemberJoinActivity) _memberJoinActivity;
                                memberJoinActivity.finish();
                            }

                            if(_memberAuthActivity != null) {
                                MemberAuthActivity memberAuthActivity = (MemberAuthActivity) _memberAuthActivity;
                                memberAuthActivity.finish();
                            }

                            if(_myProfileSettingsDetailActivity != null) {
                                MyProfileSettingsDetailActivity myProfileSettingsDetailActivity = (MyProfileSettingsDetailActivity) _myProfileSettingsDetailActivity;
                                myProfileSettingsDetailActivity.finish();
                            }

                            Intent intent1 = new Intent(pContext, LoginActivity.class);
                            startActivity(intent1);
                        }

                        finish();
                    }
                }).show();
            }else{
                Toast.makeText(PasswordChangeActivity.this, "비밀번호 변경 실패하였습니다. 재시도 해주세요.", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onResponseFailListener(VolleyError it) {
            //프로그래스바 종료
            Util.dismiss();

            Toast.makeText(PasswordChangeActivity.this, "비밀번호 변경 실패하였습니다. 재시도 해주세요.", Toast.LENGTH_LONG).show();
        }
    };
}
