package com.sincar.customer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.sincar.customer.network.VolleyNetwork;

import java.util.HashMap;

import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.common.Constants.LOGIN_REQUEST;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mContext = this;
        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        findViewById(R.id.setting_btnPrev).setOnClickListener(this);
        //       findViewById(R.id.myinfo_btnNext).setOnClickListener(this);

        //약관 및 정책
        findViewById(R.id.setting_clause).setOnClickListener(this);
        //로그아웃
        findViewById(R.id.member_logout).setOnClickListener(this);
        //회원탈퇴
        findViewById(R.id.member_withdrawal).setOnClickListener(this);

        // TODO - 활성화 유무에 따라 서버 연동 항목 추가 작업
        //정보성 알림
        Switch mSwitch1 = findViewById(R.id.infoSwitch1);
        mSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
                if (isChecked){
 //                   requestSetting(1, "Y");
                    Toast.makeText(mContext, "활성화", Toast.LENGTH_SHORT).show();
                }else{
//                    requestSetting(1, "N");
                    Toast.makeText(mContext, "비활성화", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //마켓팅 알림
        Switch mSwitch2 = findViewById(R.id.infoSwitch2);
        mSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
                if (isChecked){
//                    requestSetting(2, "Y");
                    Toast.makeText(mContext, "활성화", Toast.LENGTH_SHORT).show();
                }else{
//                    requestSetting(2, "N");
                    Toast.makeText(mContext, "비활성화", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    /**
     * 정보성 알림 설정 요청
     * PHONE_NEMBER : 폰번호
     * MEMBER_NO    : 회원번호
     * NOTICE_VARI  : 구분(1:정보 알림, 마케팅 알림)
     * ACTIVE_YN    : 활성화 유무(Y/N)
     */
    private void requestSetting(int various, String active) {

        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("PHONE_NEMBER", voLoginItem.MEMBER_PHONE);   // 폰번호
        postParams.put("MEMBER_NO", voLoginItem.MEMBER_NO);         // 회원번호
        postParams.put("NOTICE_VARI", String.valueOf(various));
        postParams.put("ACTIVE_YN", active);

        //로그인 요청
        VolleyNetwork.getInstance(this).passwordChangeRequest(LOGIN_REQUEST, postParams, onResponseListener);
    }

    VolleyNetwork.OnResponseListener onResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String it) {
            Toast.makeText(mContext, "변경되었습니다.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponseFailListener(VolleyError it) {
            Toast.makeText(mContext, "변경에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.setting_btnPrev:
                //  내정보
                intent = new Intent(this, MyProfileSettingsActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.setting_clause:
                // TODO - 약관 및 정책
                Toast.makeText(this, "약관 및 정책으로 이동", Toast.LENGTH_SHORT).show();

                intent = new Intent(this, UseTermsActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.member_logout:
                // 로그아웃
                showLogoutAlertDialog(this);
                break;

            case R.id.member_withdrawal:
                // 회원탈퇴
                showWithdrawAlertDialog(this);
                break;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, MyProfileSettingsActivity.class);
        startActivity(intent);

        finish();
    }

    /**
     * 로그아웃 알럿 다이얼로그
     * @param context
     */
    private void showLogoutAlertDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle(context.getString(R.string.notice));
        builder.setMessage("로그아웃 하시겠습니까?");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(mContext, LoginActivityPre.class);
                startActivity(intent);

                finish();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    /**
     * 회원탈퇴 알럿 다이얼로그
     * @param context
     */
    private void showWithdrawAlertDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle(context.getString(R.string.notice));
        builder.setMessage("한번 탈퇴한 계정은 다시 \n" +
                "복구가 불가능합니다.\n" +
                "그래도 탈퇴하시겠습니까?");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO - 회원탈퇴 (서버 연동)
                // requestWithdraw();
                Toast.makeText(context, "회원 탈퇴 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    /**
     * 정보성 알림 설정 요청
     * PHONE_NEMBER : 폰번호
     * MEMBER_NO    : 회원번호
     * NOTICE_VARI  : 구분(1:정보 알림, 마케팅 알림)
     * ACTIVE_YN    : 활성화 유무(Y/N)
     */
    private void requestWithdraw() {

        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("PHONE_NEMBER", voLoginItem.MEMBER_PHONE);   // 폰번호
        postParams.put("MEMBER_NO", voLoginItem.MEMBER_NO);         // 회원번호

        //회원탈퇴 요청
        VolleyNetwork.getInstance(this).passwordChangeRequest(LOGIN_REQUEST, postParams, onResponseListenerWithdraw);
    }

    VolleyNetwork.OnResponseListener onResponseListenerWithdraw = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String it) {
            Toast.makeText(mContext, "회원 탈퇴 되었습니다.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponseFailListener(VolleyError it) {
            Toast.makeText(mContext, "회원 탈퇴에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
    };

}


