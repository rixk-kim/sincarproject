package com.sincar.customer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.sincar.customer.network.DataObject;
import com.sincar.customer.network.JsonParser;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.DataParser;

import java.util.ArrayList;
import java.util.HashMap;

import static com.sincar.customer.HWApplication.voLoginData;
import static com.sincar.customer.common.Constants.LOGIN_REQUEST;


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
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnPrev:
                // 메인 이동
                //Intent intent = new Intent(LoginActivity.this, com.sincar.customer.LoginActivityPre.class);
                //startActivity(intent);
                // 최초 생성 후 이동 시 제거
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
        it = "{login: [{\"REGISTER\":\"1\",\"CAUSE\":\"비밀번호 오류\",\"MEMBER_NO\":\"12345\",\"VERSION\":\"1.0.1\",\"APK_URL\":\"http://sincar.co.kr/apk/manager_1.0.1.apk\",\"MEMBER_NAME\":\"신차로\",\"MEMBER_PHONE\":\"01012345678\",\"MEMBER_RECOM_CODE\":\"FCF816\",\"PROFILE_DOWN_URL\":\"http://~~\",\"LICENSE_DOWN_URL\":\"http://~~\",\"AD_NUM\":\"3\",\"MY_POINT\":\"5,430\",\"INVITE_NUM\":\"7\",\"INVITE_FRI_NUM\":\"7\",\"ACCUM_POINT\":\"3,870\"}],\"DATA\":[{\"FRI_NAME\":\"김민정\",\"USE_SERVICE\":\"스팀세차\",\"SAVE_DATE\":\"20.02.26\",\"FRI_POINT\":\"100\"},{\"FRI_NAME\":\"이하영\",\"USE_SERVICE\":\"스팀세차\",\"SAVE_DATE\":\"20.02.28\",\"FRI_POINT\":\"120\"}],\"advertise\":[{\"AD_IMAGE_URL\":\"http://~~\"},{\"AD_IMAGE_URL\":\"http://~~\"},{\"AD_IMAGE_URL\":\"http://~~\"}]}";
        System.out.println("[spirit] it : "  + it);

        ArrayList<DataObject> data = JsonParser.getData(it);

        // 로그인 정보
        final DataObject loginItem = DataParser.getFromParamtoItem(data, "login");

        System.out.println("[spirit] 회원번호 : " + loginItem.getValue("MEMBER_NO"));
        System.out.println("[spirit] App Version : " + loginItem.getValue("VERSION"));
        System.out.println("[spirit] APK_URL : " + loginItem.getValue("APK_URL"));
        System.out.println("[spirit] MEMBER_NAME : " + loginItem.getValue("MEMBER_NAME"));
        System.out.println("[spirit] MEMBER_PHONE : " + loginItem.getValue("MEMBER_PHONE"));

        voLoginData.setMemberNo(loginItem.getValue("MEMBER_NO"));
        voLoginData.setAppVersion(loginItem.getValue("VERSION"));
        voLoginData.setApkDownloadUrl(loginItem.getValue("APK_URL"));
        voLoginData.setMemberName(loginItem.getValue("MEMBER_NAME"));
        voLoginData.setMemberPhone(loginItem.getValue("MEMBER_PHONE"));

        System.out.println("[spirit] MEMBER_RECOM_CODE : " + loginItem.getValue("MEMBER_RECOM_CODE"));
        System.out.println("[spirit] PROFILE_DOWN_URL : " + loginItem.getValue("PROFILE_DOWN_URL"));
        System.out.println("[spirit] LICENSE_DOWN_URL : " + loginItem.getValue("LICENSE_DOWN_URL"));
        System.out.println("[spirit] AD_NUM : " + loginItem.getValue("AD_NUM"));
//        System.out.println("[spirit] MY_POINT : " + loginItem.getValue("MY_POINT"));

        voLoginData.setMemberRecomCode(loginItem.getValue("MEMBER_RECOM_CODE"));
        voLoginData.setProfileDownloadUrl(loginItem.getValue("PROFILE_DOWN_URL"));
        voLoginData.setLicenseDownloadUrl(loginItem.getValue("LICENSE_DOWN_URL"));
        voLoginData.setAdNum(loginItem.getValue("AD_NUM"));
//        voLoginData.setMyPoint(loginItem.getValue("MY_POINT"));

//        System.out.println("[spirit] INVITE_NUM : " + loginItem.getValue("INVITE_NUM"));
//        System.out.println("[spirit] INVITE_FRI_NUM : " + loginItem.getValue("INVITE_FRI_NUM"));
//        System.out.println("[spirit] ACCUM_POINT : " + loginItem.getValue("ACCUM_POINT"));
//
//        voLoginData.setInviteNum(loginItem.getValue("INVITE_NUM"));
//        voLoginData.setInviteFriNumNum(loginItem.getValue("INVITE_FRI_NUM"));
//        voLoginData.setAccumPoint(loginItem.getValue("ACCUM_POINT"));

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
        final ArrayList<DataObject> adItem = DataParser.getFromParamtoArray(data, "advertise");

        if(adItem.size() > 0) {
            String[] ad_image_url = new String[adItem.size()];
            for (int j = 0; j < adItem.size(); j++) {
                System.out.println("[spirit] AD_IMAGE_URL : " + adItem.get(j).getValue("AD_IMAGE_URL"));

                ad_image_url[j] = adItem.get(j).getValue("AD_IMAGE_URL");
            }

            voLoginData.setAdDownloadImageUrl(ad_image_url);
        }

        try {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.toString();
        }
    }
}
