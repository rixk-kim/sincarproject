package com.sincar.customer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sincar.customer.item.LoginResult;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.preference.PreferenceManager;
import com.sincar.customer.util.LoadingProgress;
import com.sincar.customer.util.Util;

import java.util.HashMap;

import static com.sincar.customer.HWApplication.voAdvertiseItem;
import static com.sincar.customer.HWApplication.voCompanyListDataItem;
import static com.sincar.customer.HWApplication.voCarListDataItem;
import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.HWApplication.voLoginDataItem;
import static com.sincar.customer.common.Constants.LOGIN_REQUEST;
import static com.sincar.customer.HWApplication.loginResult;


/**
 * 2020.02.17 spirit
 * 로그인 액티비티
 * 1. 점주 검증
 * 2. 로그인 서버 통신 확인. 성공여부. (앱 버전 체크 및 업그레이드 진행)
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText idEt, pwEt;
    private Button login_btn;

    // 업데이트 시 프로그래스 바
    private ProgressDialog updateProgress;
    // 다운로드 APK 파일 명
    private static final String APP_NAME = "sincar.apk";;

    // 앱 업데이트 모드 flag
    private boolean isUpdate = false;
    // 업데이트 URL
    private String updateUrl = "";

    // 자동 로그인 체크 값
    private boolean isLoginCheck;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_ACTION_BAR | Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_layout);

        // 화면 초기화
        init();

        Util.mProgress = new LoadingProgress(this);
    }

    /**
     * 화면 초기화
     */
    private void init() {

        findViewById(R.id.btnPrev).setOnClickListener(this);
        findViewById(R.id.password_search).setOnClickListener(this);

        login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);

        // 전화번호 입력
        idEt = (EditText) findViewById(R.id.user_id_et);
        // 비밀번호 입력
        pwEt = (EditText) findViewById(R.id.user_pwd_et);

        // 자동 로그인 확인
        isLoginCheck = PreferenceManager.getInstance().getCheckLogin();

        if (isLoginCheck) {
            // 아이디 셋팅
            idEt.setText(PreferenceManager.getInstance().getUserId());
            // 패스워드 셋팅
            pwEt.setText(PreferenceManager.getInstance().getUserPwd());

            if(!TextUtils.isEmpty(PreferenceManager.getInstance().getUserId()) && !TextUtils.isEmpty(PreferenceManager.getInstance().getUserPwd()))
            {
                requestLogin();
            }
        }

        // 비밀번호 입력 후 확인 버튼 클릭 시
        pwEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // 로그인 요청
                    requestLogin();
                    //startLogin("sss");
                }
                return false;
            }
        });
    }

    /**
     * 에러 팝업
     * 1~ 2 서버 통신 후 에러 메세지
     * 3 ~5 내부 유효성 검사 메세지
     * @param resultCode : 내용 코드
     */
    private void showErrorAlert(String resultCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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

    /**
     * 앱 종료
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, LoginActivityPre.class);
        startActivity(intent);

        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPrev:
                Intent intent = new Intent(this, LoginActivityPre.class);
                startActivity(intent);

                finish();
                break;

            // 로그인 버튼
            case R.id.login_btn:
                // 전화번호 입력 검증
                // 전화번호를 입력하세요.

                if (idEt != null || idEt.getText().toString().trim().length() != 0) {
                    if (idEt.getText().toString().trim().length() == 0) {
                        showErrorAlert("4");
                        return;
                    }
                }

                if (pwEt != null || pwEt.getText().toString().trim().length() != 0) {
                    if (pwEt.getText().toString().trim().length() == 0) {
                        showErrorAlert("5");
                        return;
                    }
                }

                // 로그인 요청
                requestLogin();
                break;

            case R.id.password_search:
                //비밀번호 찾기
                intent = new Intent(this, MemberJoinActivity.class);
                intent.putExtra("path", "password_search");
                startActivity(intent);
                finish();
                break;
         }
    }

    /**  id=test2018&pw=1234
     * 로그인 요청
     * id : 사용자 계정 명
     * pw : 사용자 계정 비밀번호
     * ip4 : UUID (로컬 생성 및 저장)
     */
    private void requestLogin() {
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("PHONE_NUMBER", idEt.getText().toString().trim());       // 사용자 전화번호
        postParams.put("PASSWORD", pwEt.getText().toString().trim());           // 사용자 패스워드

        //프로그래스바 시작
        Util.showDialog(this);
        //Util.showProgress(this);


        //로그인 요청
        VolleyNetwork.getInstance(this).serverDataRequest(LOGIN_REQUEST, postParams, onResponseListener);
    }

    VolleyNetwork.OnResponseListener onResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String it) {
            startLogin(it);
        }

        @Override
        public void onResponseFailListener(VolleyError it) {
            System.out.println("[spirit]  실패");

            //프로그래스바 종료
            Util.dismiss();
        }
    };

    /**
     * 로그인 성공 후 메인 이동
     */
    private void startLogin(String server_data) {
/*
it =>  {"login": [{"REGISTER":"1","CAUSE":"비밀번호 오류","MEMBER_NO":"12345","VERSION":"1.0.1","APK_URL":"http://sincar.co.kr/apk/manager_1.0.1.apk",
"MEMBER_NAME":"신차로","MEMBER_PHONE":"01012345678","MEMBER_RECOM_CODE":"FCF816","PROFILE_DOWN_URL":"http://~~",
"LICENSE_DOWN_URL":"http://~~","AD_NUM":"3","MY_POINT":"5,430","INVITE_NUM":"7","INVITE_FRI_NUM":"7","ACCUM_POINT":"3,870"}],

"DATA":[{"FRI_NAME":"김민정","USE_SERVICE":"스팀세차","SAVE_DATE":"20.02.26","FRI_POINT":"100"},{"FRI_NAME":"이하영","USE_SERVICE":"스팀세차","SAVE_DATE":"20.02.28","FRI_POINT":"120"}],
"advertise":[{"AD_IMAGE_URL":"http://~~"},{"AD_IMAGE_URL":"http://~~"},{"AD_IMAGE_URL"""http://~~"}]}
*/
//        it = "{login: [{\"REGISTER\":\"1\",\"CAUSE\":\"비밀번호 오류\",\"MEMBER_NO\":\"12345\",\"VERSION\":\"1.0.1\",\"APK_URL\":\"http://sincar.co.kr/apk/manager_1.0.1.apk\",\"MEMBER_NAME\":\"신차로\",\"MEMBER_PHONE\":\"01012345678\",\"MEMBER_RECOM_CODE\":\"FCF816\",\"REGISTER_RECOM_CODE\":\"ABCDEF\",\"PROFILE_DOWN_URL\":\"http://~~\",\"LICENSE_DOWN_URL\":\"http://~~\",\"AD_NUM\":\"3\",\"MY_POINT\":\"35430\",\"INVITE_NUM\":\"7\",\"INVITE_FRI_NUM\":\"7\",\"ACCUM_POINT\":\"123456\"}],\"DATA\":[{\"FRI_NAME\":\"김민정\",\"USE_SERVICE\":\"스팀세차\",\"SAVE_DATE\":\"20.02.26\",\"FRI_POINT\":\"100\"},{\"FRI_NAME\":\"이하영\",\"USE_SERVICE\":\"스팀세차\",\"SAVE_DATE\":\"20.02.28\",\"FRI_POINT\":\"120\"}],\"advertise\":[{\"AD_IMAGE_URL\":\"https://www.sincar.co.kr/design/images/header/sub-m-menu01.jpg\",\"AD_LINK_URL\":\"https://www.sincar.co.kr/design/newcar.asp\"},{\"AD_IMAGE_URL\":\"https://www.sincar.co.kr/design/images/header/sub-m-menu02.jpg\",\"AD_LINK_URL\":\"https://www.sincar.co.kr/design/rentacar.asp\"},{\"AD_IMAGE_URL\":\"https://www.sincar.co.kr/design/images/header/sub-m-menu05.jpg\",\"AD_LINK_URL\":\"https://www.sincar.co.kr/design/repair.asp\"}],\"company_list\":[{\"CAR_COMPANY_CODE\":\"1\",\"CAR_COMPANY\":\"현대\"},{\"CAR_COMPANY_CODE\":\"2\",\"CAR_COMPANY\":\"삼성\"},{\"CAR_COMPANY_CODE\":\"3\",\"CAR_COMPANY\":\"대우\"}],\"car_list\":[{\"CAR_COMPANY_CODE\":\"1\",\"CAR_NAME\":\"아반떼\",\"CAR_CODE\":\"10\",\"CAR_WASH_PAY\":\"50000\"},{\"CAR_COMPANY_CODE\":\"1\",\"CAR_NAME\":\"아반떼\",\"CAR_CODE\":\"10\",\"CAR_WASH_PAY\":\"50000\"},{\"CAR_COMPANY_CODE\":\"1\",\"CAR_NAME\":\"아반떼\",\"CAR_CODE\":\"10\",\"CAR_WASH_PAY\":\"50000\"}]}";
        System.out.println("[spirit] server_data : "  + server_data);

        Gson gSon = new Gson();
        loginResult = gSon.fromJson(server_data, LoginResult.class);

        voLoginItem.REGISTER        = loginResult.login.get(0).REGISTER;
        voLoginItem.CAUSE           = loginResult.login.get(0).CAUSE;

        voLoginItem.MEMBER_NO       = loginResult.login.get(0).MEMBER_NO;
        voLoginItem.VERSION         = loginResult.login.get(0).VERSION;
        voLoginItem.APK_URL         = loginResult.login.get(0).APK_URL;
        voLoginItem.MEMBER_NAME     = loginResult.login.get(0).MEMBER_NAME;
        voLoginItem.MEMBER_PHONE    = loginResult.login.get(0).MEMBER_PHONE;

        voLoginItem.MEMBER_RECOM_CODE       = loginResult.login.get(0).MEMBER_RECOM_CODE;
        voLoginItem.REGISTER_RECOM_CODE     = loginResult.login.get(0).REGISTER_RECOM_CODE;
        voLoginItem.PROFILE_DOWN_URL        = loginResult.login.get(0).PROFILE_DOWN_URL;
        voLoginItem.LICENSE_DOWN_URL        = loginResult.login.get(0).LICENSE_DOWN_URL;
        voLoginItem.AD_NUM                  = loginResult.login.get(0).AD_NUM;
        voLoginItem.MY_POINT                = loginResult.login.get(0).MY_POINT;

        voLoginItem.INVITE_NUM      = loginResult.login.get(0).INVITE_NUM;
        voLoginItem.INVITE_FRI_NUM  = loginResult.login.get(0).INVITE_FRI_NUM;
        voLoginItem.ACCUM_POINT     = loginResult.login.get(0).ACCUM_POINT;

        // LoginDataItem 에 넣어주고.
        voLoginDataItem = loginResult.DATA;

        // 광고 정보
        voAdvertiseItem = loginResult.advertise;
        for(int j = 0; j < loginResult.advertise.size(); j++)
        {
            Log.d("서버전송", "AD_IMAGE_URL = " + loginResult.advertise.get(j).AD_IMAGE_URL );
        }

        //제조사 리스트
        voCompanyListDataItem = loginResult.company_list;
        //차량 리스트
        voCarListDataItem = loginResult.car_list;

        //프로그래스바 종료
        Util.dismiss();
        //Util.hideProgress();
        //버전비교
//        PackageInfo pi = null;
//        try{
//            pi = getPackageManager().getPackageInfo(getPackageName(),0);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        if(!TextUtils.isEmpty(pi.versionName)) {
//            if(pi.versionName.equals(voLoginItem.VERSION)) {
                if ("0".equals(voLoginItem.REGISTER) && !TextUtils.isEmpty(voLoginItem.MEMBER_NO)) {
                    try {
                        PreferenceManager.getInstance().setCheckLogin(true);
                        // 아이디 셋팅
                        PreferenceManager.getInstance().setUserId(idEt.getText().toString().trim());
                        // 패스워드 셋팅
                        PreferenceManager.getInstance().setUserPwd(pwEt.getText().toString().trim());

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        e.toString();
                    }
                } else if ("2".equals(voLoginItem.REGISTER) && !TextUtils.isEmpty(voLoginItem.MEMBER_NO)) {
                    Toast.makeText(this, "회원 가입 후 이용해주세요.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, voLoginItem.CAUSE, Toast.LENGTH_LONG).show();
                }
//            }else{
//                //버전 업데이트 진행.
//                appUpdate();
//            }
//        }else{
//            Toast.makeText(this, "로그인에 실패하였습니다. 재시도 해주세요.", Toast.LENGTH_LONG).show();
//        }
    }

//    private void appUpdate()
//    {
//        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//        builder.setTitle(getString(R.string.notice));
//        builder.setMessage(getString(R.string.new_update_file_install_msg));
//        // 필수
//        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // 전송 받은 다운로드 파일 URL
//  //              updateApp(updateUrl);
//                dialog.dismiss();
//
//                //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.sincar.co.kr/design/newcar.asp"));    //voLoginItem.APK_URL
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(voLoginItem.APK_URL));
//                startActivity(intent);
//
//                finish();
//            }
//        });
//        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // 앱 종료
//                finish();
//            }
//        });
//        builder.show();
//    }
}