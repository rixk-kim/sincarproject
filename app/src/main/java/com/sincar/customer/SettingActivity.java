package com.sincar.customer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sincar.customer.item.AuthResult;
import com.sincar.customer.item.SettingResult;
import com.sincar.customer.item.WithdrawResult;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.Util;

import java.util.HashMap;

import static com.sincar.customer.HWApplication.settingResult;
import static com.sincar.customer.HWApplication.voWithdrawItem;
import static com.sincar.customer.HWApplication.withdrawResult;
import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.HWApplication.voSettingItem;
import static com.sincar.customer.common.Constants.INFO_CHANGE_REQUEST;
import static com.sincar.customer.common.Constants.LOGIN_REQUEST;
import static com.sincar.customer.common.Constants.MEMBER_WITHRAW_REQUEST;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private Switch mSwitch1, mSwitch2;
    private LinearLayout sLinearLayout; //setting_btnPrev_layout
    private TextView mTextView;

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
        sLinearLayout = (LinearLayout) findViewById(R.id.setting_btnPrev_layout);
        sLinearLayout.setOnClickListener(this);
        //findViewById(R.id.setting_btnPrev).setOnClickListener(this);

        mTextView = (TextView) findViewById(R.id.setting_version_info); //버전정보

        PackageInfo pi = null;
        try{
            pi = getPackageManager().getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(!TextUtils.isEmpty(pi.versionName))
        {
            mTextView.setText("v." + pi.versionName);  //v.1.0.0
        }

        //약관 및 정책
        findViewById(R.id.setting_clause).setOnClickListener(this);
        //로그아웃
        findViewById(R.id.member_logout).setOnClickListener(this);
        //회원탈퇴
        findViewById(R.id.member_withdrawal).setOnClickListener(this);

        // TODO - 활성화 유무에 따라 서버 연동 항목 추가 작업
        //정보성 알림
        mSwitch1 = findViewById(R.id.infoSwitch1);
        mSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
                if (isChecked){
                    requestSetting(1, "Y");
                }else{
                    requestSetting(1, "N");
                }
            }
        });

        //마켓팅 알림
        mSwitch2 = findViewById(R.id.infoSwitch2);
        mSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
                if (isChecked){
                    requestSetting(2, "Y");
                }else{
                    requestSetting(2, "N");
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
        postParams.put("PHONE_NUMBER", voLoginItem.MEMBER_PHONE);   // 폰번호
        postParams.put("MEMBER_NO", voLoginItem.MEMBER_NO);         // 회원번호
        postParams.put("NOTICE_VARI", String.valueOf(various));
        postParams.put("ACTIVE_YN", active);

        //프로그래스바 시작
        Util.showDialog(this);

        //통신중에는 비활성화
        mSwitch1.setEnabled(false);
        mSwitch2.setEnabled(false);

        //로그인 요청
        VolleyNetwork.getInstance(this).serverDataRequest(INFO_CHANGE_REQUEST, postParams, onInfoResponseListener);
    }

    VolleyNetwork.OnResponseListener onInfoResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String it) {
            Gson gSon = new Gson();
            settingResult = gSon.fromJson(it, SettingResult.class);

            voSettingItem.SETTING_RESULT    = settingResult.setting_info.SETTING_RESULT;
            voSettingItem.CAUSE             = settingResult.setting_info.CAUSE;

            //프로그래스바 종료
            Util.dismiss();

            //활성화
            mSwitch1.setEnabled(true);
            mSwitch2.setEnabled(true);

            if("0".equals(voSettingItem.SETTING_RESULT)) {
                Toast.makeText(mContext, "변경되었습니다.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(mContext, "변경에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
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
            case R.id.setting_btnPrev_layout:
                //  내정보
                intent = new Intent(this, MyProfileSettingsActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.setting_clause:
                // 약관 및 정책

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
        builder.setTitle(context.getString(R.string.notice));
        builder.setMessage("한번 탈퇴한 계정은 다시 복구가 불가능합니다.\n" +
                "그래도 탈퇴하시겠습니까?");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 회원탈퇴 (서버 연동)
                requestWithdraw();
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
        postParams.put("PHONE_NUMBER", voLoginItem.MEMBER_PHONE);   // 폰번호
        postParams.put("MEMBER_NO", voLoginItem.MEMBER_NO);         // 회원번호

        //프로그래스바 시작
        Util.showDialog(this);

        //회원탈퇴 요청
        VolleyNetwork.getInstance(this).serverDataRequest(MEMBER_WITHRAW_REQUEST, postParams, onWithdrawResponseListenerWithdraw);
    }

    VolleyNetwork.OnResponseListener onWithdrawResponseListenerWithdraw = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String it) {

            Gson gSon = new Gson();
            withdrawResult = gSon.fromJson(it, WithdrawResult.class);

            voWithdrawItem.WITHRAW_RESULT    = withdrawResult.withraw.WITHRAW_RESULT;
            voWithdrawItem.CAUSE             = withdrawResult.withraw.CAUSE;

            //프로그래스바 종료
            Util.dismiss();

            if("0".equals(voWithdrawItem.WITHRAW_RESULT)) {
                Toast.makeText(mContext, "회원 탈퇴 되었습니다.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext, LoginActivityPre.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(mContext, "회원 탈퇴에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onResponseFailListener(VolleyError it) {
            Toast.makeText(mContext, "회원 탈퇴에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
    };

}


