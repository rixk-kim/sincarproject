package com.sincar.customer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sincar.customer.item.LoginDataItem;
import com.sincar.customer.item.LoginResult;
import com.sincar.customer.network.DataObject;
import com.sincar.customer.network.JsonParser;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.DataParser;

import java.util.ArrayList;
import java.util.HashMap;

import static com.sincar.customer.HWApplication.voAdvertiseItem;
import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.HWApplication.voLoginDataItem;
import static com.sincar.customer.HWApplication.voLoginAdvertiseItem;
import static com.sincar.customer.common.Constants.LOGIN_REQUEST;
import static com.sincar.customer.HWApplication.loginResult;

//import static com.sincar.customer.item.LoginDataItem;

/**
 * 2020.02.17 spirit
 * 로그인 액티비티
 * 1. 점주 검증
 * 2. 로그인 서버 통신 확인. 성공여부. (앱 버전 체크 및 업그레이드 진행)
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
    //===================== 뷰 =====================
    private EditText idEt, pwEt;
    private Button login_btn;
//    private Button call_btn;
    //===================== 뷰 =====================

    // 업데이트 시 프로그래스 바
    private ProgressDialog updateProgress;
    // 다운로드 APK 파일 명
    private static final String APP_NAME = "sincar.apk";;

    // 앱 업데이트 모드 flag
    private boolean isUpdate = false;
    // 업데이트 URL
    private String updateUrl = "";

    //public static Activity loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR | Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_layout);

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);

        // 전화번호 입력
        idEt = (EditText) findViewById(R.id.user_id_et);
        // 비밀번호 입력
        pwEt = (EditText) findViewById(R.id.user_pwd_et);

        // 비밀번호 입력 후 확인 버튼 클릭 시
        pwEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // 로그인 요청
                //requestLogin();
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

                startLogin("sss");

                // 로그인 요청
//                requestLogin();
/*
                // 메인 이동
                Intent intent1 = new Intent(LoginActivity.this, com.sincar.customer.MainActivity.class);
                startActivity(intent1);
                // 최초 생성 후 이동 시 제거
                finish();
*/
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
        // 사용자 전화번호
        postParams.put("phoneNumber", idEt.getText().toString().trim());
        // 사용자 패스워드
        postParams.put("pw", pwEt.getText().toString().trim());

        //로그인 요청
        VolleyNetwork.getInstance(this).passwordChangeRequest(LOGIN_REQUEST, postParams, onResponseListener);
    }

    VolleyNetwork.OnResponseListener onResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String it) {
            startLogin(it);
        }

        @Override
        public void onResponseFailListener(VolleyError it) {

        }
    };

    /**
     * 로그인 성공 후 메인 이동
     */
    private void startLogin(String it) {
/*
it =>  {"login": [{"REGISTER":"1","CAUSE":"비밀번호 오류","MEMBER_NO":"12345","VERSION":"1.0.1","APK_URL":"http://sincar.co.kr/apk/manager_1.0.1.apk",
"MEMBER_NAME":"신차로","MEMBER_PHONE":"01012345678","MEMBER_RECOM_CODE":"FCF816","PROFILE_DOWN_URL":"http://~~",
"LICENSE_DOWN_URL":"http://~~","AD_NUM":"3","MY_POINT":"5,430","INVITE_NUM":"7","INVITE_FRI_NUM":"7","ACCUM_POINT":"3,870"}],

"DATA":[{"FRI_NAME":"김민정","USE_SERVICE":"스팀세차","SAVE_DATE":"20.02.26","FRI_POINT":"100"},{"FRI_NAME":"이하영","USE_SERVICE":"스팀세차","SAVE_DATE":"20.02.28","FRI_POINT":"120"}],
"advertise":[{"AD_IMAGE_URL":"http://~~"},{"AD_IMAGE_URL":"http://~~"},{"AD_IMAGE_URL"""http://~~"}]}
*/
        it = "{login: [{\"REGISTER\":\"1\",\"CAUSE\":\"비밀번호 오류\",\"MEMBER_NO\":\"12345\",\"VERSION\":\"1.0.1\",\"APK_URL\":\"http://sincar.co.kr/apk/manager_1.0.1.apk\",\"MEMBER_NAME\":\"신차로\",\"MEMBER_PHONE\":\"01012345678\",\"MEMBER_RECOM_CODE\":\"FCF816\",\"REGISTER_RECOM_CODE\":\"ABCDEF\",\"PROFILE_DOWN_URL\":\"http://~~\",\"LICENSE_DOWN_URL\":\"http://~~\",\"AD_NUM\":\"3\",\"MY_POINT\":\"35430\",\"INVITE_NUM\":\"7\",\"INVITE_FRI_NUM\":\"7\",\"ACCUM_POINT\":\"123456\"}],\"DATA\":[{\"FRI_NAME\":\"김민정\",\"USE_SERVICE\":\"스팀세차\",\"SAVE_DATE\":\"20.02.26\",\"FRI_POINT\":\"100\"},{\"FRI_NAME\":\"이하영\",\"USE_SERVICE\":\"스팀세차\",\"SAVE_DATE\":\"20.02.28\",\"FRI_POINT\":\"120\"}],\"advertise\":[{\"AD_IMAGE_URL\":\"https://assets.materialup.com/uploads/dcc07ea4-845a-463b-b5f0-4696574da5ed/preview.jpg\",\"AD_LINK_URL\":\"http://www.naver.com\"},{\"AD_IMAGE_URL\":\"https://assets.materialup.com/uploads/20ded50d-cc85-4e72-9ce3-452671cf7a6d/preview.jpg\",\"AD_LINK_URL\":\"http://www.daum.net\"},{\"AD_IMAGE_URL\":\"https://assets.materialup.com/uploads/76d63bbc-54a1-450a-a462-d90056be881b/preview.png\",\"AD_LINK_URL\":\"http://www.nate.com\"}]}";
        System.out.println("[spirit] it : "  + it);
        // {login: [
        //      {"REGISTER":"1",
        //      "CAUSE":"비밀번호 오류",
        //      "MEMBER_NO":"12345",
        //      "VERSION":"1.0.1",
        //      "APK_URL":"http://sincar.co.kr/apk/manager_1.0.1.apk",
        //      "MEMBER_NAME":"신차로",
        //      "MEMBER_PHONE":"01012345678",
        //      "MEMBER_RECOM_CODE":"FCF816",
        //      "PROFILE_DOWN_URL":"http://~~",
        //      "LICENSE_DOWN_URL":"http://~~",
        //      "AD_NUM":"3",
        //      "MY_POINT":"5,430",
        //      "INVITE_NUM":"7",
        //      "INVITE_FRI_NUM":"7",
        //      "ACCUM_POINT":"3,870"}
        //  ],
        // "DATA":[
        //      {"FRI_NAME":"김민정","USE_SERVICE":"스팀세차","SAVE_DATE":"20.02.26","FRI_POINT":"100"},
        //      {"FRI_NAME":"이하영","USE_SERVICE":"스팀세차","SAVE_DATE":"20.02.28","FRI_POINT":"120"}],
        // "advertise":[
        //      {"AD_IMAGE_URL":"http://~~"},
        //      {"AD_IMAGE_URL":"http://~~"},
        //      {"AD_IMAGE_URL":"http://~~"}
        // ]}

        Gson gSon = new Gson();
        //LoginResult loginResult = gSon.fromJson(it, LoginResult.class);
        loginResult = gSon.fromJson(it, LoginResult.class);

        Log.d("서버전송", "loginResult = " + loginResult.toString());
        Log.d("서버전송", "loginResult = " + loginResult.login.size());
        Log.d("서버전송", "loginResult = " + loginResult.DATA.size());
        Log.d("서버전송", "loginResult = " + loginResult.advertise.size());

        Log.d("서버전송", "회원번호 = " + loginResult.login.get(0).MEMBER_NO );
        Log.d("서버전송", "App Version = " + loginResult.login.get(0).VERSION );
        Log.d("서버전송", "APK_URL = " + loginResult.login.get(0).APK_URL );
        Log.d("서버전송", "App MEMBER_NAME = " + loginResult.login.get(0).MEMBER_NAME );
        Log.d("서버전송", "App MEMBER_PHONE = " + loginResult.login.get(0).MEMBER_PHONE );

        voLoginItem.MEMBER_NO       = loginResult.login.get(0).MEMBER_NO;
        voLoginItem.VERSION         = loginResult.login.get(0).VERSION;
        voLoginItem.APK_URL         = loginResult.login.get(0).APK_URL;
        voLoginItem.MEMBER_NAME     = loginResult.login.get(0).MEMBER_NAME;
        voLoginItem.MEMBER_PHONE    = loginResult.login.get(0).MEMBER_PHONE;

//        ArrayList<DataObject> data = JsonParser.getData(it);
//
//        // 로그인 정보
//        final DataObject loginItem = DataParser.getFromParamtoItem(data, "login");
//
//        System.out.println("[spirit] 회원번호 : " + loginItem.getValue("MEMBER_NO"));
//        System.out.println("[spirit] App Version : " + loginItem.getValue("VERSION"));
//        System.out.println("[spirit] APK_URL : " + loginItem.getValue("APK_URL"));
//        System.out.println("[spirit] MEMBER_NAME : " + loginItem.getValue("MEMBER_NAME"));
//        System.out.println("[spirit] MEMBER_PHONE : " + loginItem.getValue("MEMBER_PHONE"));

//        voLoginData.setMemberNo(loginItem.getValue("MEMBER_NO"));
//        voLoginData.setAppVersion(loginItem.getValue("VERSION"));
//        voLoginData.setApkDownloadUrl(loginItem.getValue("APK_URL"));
//        voLoginData.setMemberName(loginItem.getValue("MEMBER_NAME"));
//        voLoginData.setMemberPhone(loginItem.getValue("MEMBER_PHONE"));

        Log.d("서버전송", "MEMBER_RECOM_CODE = " + loginResult.login.get(0).MEMBER_RECOM_CODE );
        Log.d("서버전송", "REGISTER_RECOM_CODE = " + loginResult.login.get(0).REGISTER_RECOM_CODE );
        Log.d("서버전송", "PROFILE_DOWN_URL = " + loginResult.login.get(0).PROFILE_DOWN_URL );
        Log.d("서버전송", "LICENSE_DOWN_URL = " + loginResult.login.get(0).LICENSE_DOWN_URL );
        Log.d("서버전송", "App AD_NUM = " + loginResult.login.get(0).AD_NUM );
        Log.d("서버전송", "App MY_POINT = " + loginResult.login.get(0).MY_POINT );

        voLoginItem.MEMBER_RECOM_CODE       = loginResult.login.get(0).MEMBER_RECOM_CODE;
        voLoginItem.REGISTER_RECOM_CODE     = loginResult.login.get(0).REGISTER_RECOM_CODE;
        voLoginItem.PROFILE_DOWN_URL        = loginResult.login.get(0).PROFILE_DOWN_URL;
        voLoginItem.LICENSE_DOWN_URL        = loginResult.login.get(0).LICENSE_DOWN_URL;
        voLoginItem.AD_NUM                  = loginResult.login.get(0).AD_NUM;
        voLoginItem.MY_POINT                = loginResult.login.get(0).MY_POINT;



//        System.out.println("[spirit] MEMBER_RECOM_CODE : " + loginItem.getValue("MEMBER_RECOM_CODE"));
//        System.out.println("[spirit] PROFILE_DOWN_URL : " + loginItem.getValue("PROFILE_DOWN_URL"));
//        System.out.println("[spirit] LICENSE_DOWN_URL : " + loginItem.getValue("LICENSE_DOWN_URL"));
//        System.out.println("[spirit] AD_NUM : " + loginItem.getValue("AD_NUM"));
//        System.out.println("[spirit] MY_POINT : " + loginItem.getValue("MY_POINT"));

//        voLoginData.setMemberRecomCode(loginItem.getValue("MEMBER_RECOM_CODE"));
//        voLoginData.setProfileDownloadUrl(loginItem.getValue("PROFILE_DOWN_URL"));
//        voLoginData.setLicenseDownloadUrl(loginItem.getValue("LICENSE_DOWN_URL"));
//        voLoginData.setAdNum(loginItem.getValue("AD_NUM"));
//        voLoginData.setMyPoint(loginItem.getValue("MY_POINT"));

//        System.out.println("[spirit] INVITE_NUM : " + loginItem.getValue("INVITE_NUM"));
//        System.out.println("[spirit] INVITE_FRI_NUM : " + loginItem.getValue("INVITE_FRI_NUM"));
//        System.out.println("[spirit] ACCUM_POINT : " + loginItem.getValue("ACCUM_POINT"));

        Log.d("서버전송", "INVITE_NUM = " + loginResult.login.get(0).INVITE_NUM );
        Log.d("서버전송", "INVITE_FRI_NUM = " + loginResult.login.get(0).INVITE_FRI_NUM );
        Log.d("서버전송", "ACCUM_POINT = " + loginResult.login.get(0).ACCUM_POINT );

        voLoginItem.INVITE_NUM      = loginResult.login.get(0).INVITE_NUM;
        voLoginItem.INVITE_FRI_NUM  = loginResult.login.get(0).INVITE_FRI_NUM;
        voLoginItem.ACCUM_POINT     = loginResult.login.get(0).ACCUM_POINT;
//
//        voLoginData.setInviteNum(loginItem.getValue("INVITE_NUM"));
//        voLoginData.setInviteFriNumNum(loginItem.getValue("INVITE_FRI_NUM"));
//        voLoginData.setAccumPoint(loginItem.getValue("ACCUM_POINT"));

//        for(int i = 0; i < loginResult.DATA.size(); i++)
//        {
//            Log.d("서버전송", "FRI_NAME = " + loginResult.DATA.get(i).FRI_NAME );
//            Log.d("서버전송", "USE_SERVICE = " + loginResult.DATA.get(i).USE_SERVICE );
//            Log.d("서버전송", "SAVE_DATE = " + loginResult.DATA.get(i).SAVE_DATE );
//            Log.d("서버전송", "FRI_POINT = " + loginResult.DATA.get(i).FRI_POINT );
//
//            // TODO : 여기서 LoginDataItem 에 넣어주고 싶어요
//            //voLoginDataItem.     = loginResult.DATA.get(i).FRI_NAME;
//        }

        // LoginDataItem 에 넣어주고.
        voLoginDataItem = loginResult.DATA;

//        ArrayList<DataObject> userItem = DataParser.getFromParamtoArray(data, "DATA");
//        if(userItem.size() > 0)
//        {
//            String[] friend_name = new String[userItem.size()];
//            String[] use_service = new String[userItem.size()];
//            String[] save_date = new String[userItem.size()];
//            String[] fri_point = new String[userItem.size()];
//
//            for (int i = 0; i < userItem.size(); i++) {
//                System.out.println("[spirit] FRI_NAME : " + userItem.get(i).getValue("FRI_NAME"));   // .getDataList().size()); // .toArray().toString()); // .get(0).getValue());
//                System.out.println("[spirit] USE_SERVICE : " + userItem.get(i).getValue("USE_SERVICE"));
//                System.out.println("[spirit] SAVE_DATE : " + userItem.get(i).getValue("SAVE_DATE"));
//                System.out.println("[spirit] FRI_POINT : " + userItem.get(i).getValue("FRI_POINT"));
//
//                friend_name[i] = userItem.get(i).getValue("FRI_NAME");
//                use_service[i] = userItem.get(i).getValue("USE_SERVICE");
//                save_date[i] = userItem.get(i).getValue("SAVE_DATE");
//                fri_point[i] = userItem.get(i).getValue("FRI_POINT");
//
//            }
//            voLoginData.setFriName(friend_name);
//            voLoginData.setUseService(use_service);
//            voLoginData.setSaveDate(save_date);
//            voLoginData.setFriPoint(fri_point);
//        }

        //"advertise":[{"AD_IMAGE_URL":"http://~~"},{"AD_IMAGE_URL":"http://~~"},{"AD_IMAGE_URL"""http://~~"}]
        // 광고 정보
        voAdvertiseItem = loginResult.advertise;
        for(int j = 0; j < loginResult.advertise.size(); j++)
        {
            Log.d("서버전송", "AD_IMAGE_URL = " + loginResult.advertise.get(j).AD_IMAGE_URL );
        }
//        final ArrayList<DataObject> adItem = DataParser.getFromParamtoArray(data, "advertise");
//
//        if(adItem.size() > 0) {
//            String[] ad_image_url = new String[adItem.size()];
//            for (int j = 0; j < adItem.size(); j++) {
//                System.out.println("[spirit] AD_IMAGE_URL : " + adItem.get(j).getValue("AD_IMAGE_URL"));
//
//                ad_image_url[j] = adItem.get(j).getValue("AD_IMAGE_URL");
//            }
//
////            voLoginData.setAdDownloadImageUrl(ad_image_url);
//        }

        try {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.toString();
        }
    }
}
