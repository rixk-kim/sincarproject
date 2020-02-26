package com.sincar.customer;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.sincar.customer.common.Constants;
import com.sincar.customer.network.DataObject;
import com.sincar.customer.network.JsonParser;
import com.sincar.customer.network.NetWorkController;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.preference.PreferenceManager;
import com.sincar.customer.util.DataParser;
import com.sincar.customer.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * 2020.02.17 spirit
 * 로그인 액티비티
 * 1. 점주 검증
 * 2. 로그인 서버 통신 확인. 성공여부. (앱 버전 체크 및 업그레이드 진행)
 */
public class LoginActivity extends Activity implements View.OnClickListener, NetWorkController.NetworkControllerListener {
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

        // 로그아웃 상태 플래그
       // isLogout = getIntent().getBooleanExtra("isLogout", false);
        // 업데이트 상태 플래그
        //isUpdate = getIntent().getBooleanExtra("isUpdate", false);

        // 파일 다운로드 URL
//        if (isUpdate) {
//            updateUrl = getIntent().getStringExtra("url");
//        }

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        // 전화번호 입력란 구현....
        // 로그인 요청
        //requestLogin();

        login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);
//
//
//
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

            case R.id.btnBack:
                // 메인 이동
                Intent intent = new Intent(LoginActivity.this, com.sincar.customer.LoginActivityPre.class);
                startActivity(intent);
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

                // 로그인 요청
//                requestLogin();


                // 메인 이동
                Intent intent1 = new Intent(LoginActivity.this, com.sincar.customer.MainActivity.class);
                startActivity(intent1);
                // 최초 생성 후 이동 시 제거
                finish();

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

        //volley
        VolleyNetwork.getInstance(this).passwordChangeRequest("url", postParams, onResponseListener);
    }

    VolleyNetwork.OnResponseListener onResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String it) {
            startLogin();
        }

        @Override
        public void onResponseFailListener(VolleyError it) {

        }
    };

    /**
     * 로그인 성공 후 메인 이동
     */
    private void startLogin() {

        try {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.toString();
            uploadError(null, 0, e.toString() + " : ");
        }
    }

    /**
     * 경고 알럿
     * @param context
     */
//    public static void showAlert(final Context context, String message) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle(context.getString(R.string.notice));
//        builder.setMessage(message);
//        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
//                intent.setData(uri);
//                context.startActivity(intent);
//            }
//        });
//        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        }).show();
//    }

    /**
     * 데이터 수신
     * @param url
     * @param result
     */
    @Override
    public void responseComplete(String url, String result, int responseCode) {

        if (responseCode != 200) {
            // 예외처리
            if (responseCode == 0) { // 인터넷 연결 실패 또는 기타 문제
                Util.commonAlert(LoginActivity.this, getString(R.string.error_network_connected), false, false);
            }else{
                Util.commonAlert(LoginActivity.this, getString(R.string.server_accept_failed_connect_msg), false, false);
            }
            return;
        }

        Log.d(TAG,"result : "  + result);  //result : {"login": [{"login_yn":"y","alt":"\ub85c\uadf8\uc778 \uc131\uacf5."}]}

        try {
            ArrayList<DataObject> data = JsonParser.getData(result);

            Log.d(TAG,"data.size() : "  + data.size());

            if (url.equals(Constants.LOGIN_REQUEST)) {

                // 로그인 정보
                final DataObject loginItem = DataParser.getFromParamtoItem(data, "login");

                // 로그인 유무
                final String login_sucuess = loginItem.getValue("login_yn");


                Log.d(TAG,"login_sucuess : " + login_sucuess);  //com.carriage.freight.network.DataObject@2873078

                if ("y".equals(login_sucuess)) {
                    Toast.makeText(LoginActivity.this, getString(R.string.success_login_msg), Toast.LENGTH_SHORT).show();
                    //startLogin(storeList, storeTypeList, item.getValue("strWorkSite"), item.getValue("workSite"));

//                    if (isLoginCheck) {
//                        // 아이디 셋팅
//                        PreferenceManager.getInstance().setUserId(idEt.getText().toString().trim());
//                        // 패스워드 셋팅
//                        PreferenceManager.getInstance().setUserPwd(pwEt.getText().toString().trim());
//                    } else {
//                        // 프리퍼런스 아이디 값 해제
//                        PreferenceManager.getInstance().setUserId("");
//                        // 프리퍼런스 패스워드 값 해제
//                        PreferenceManager.getInstance().setUserPwd("");
//                    }

                    startLogin();
                } else if("n".equals(login_sucuess)){
                    //웹뷰 회원가입으로
                    Toast.makeText(LoginActivity.this, R.string.fail_Login_msg, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }catch (Exception e) {
            e.toString();
            // 에러 서버 전송
            uploadError(null, 0, getString(R.string.server_connect_error));
        }
    }

    /**
     * 서버 통신 및 로컬 에러 전송
     * @param errorInfo    : 서버 오류 시 리턴 받은 오류 정보
     * @param responseCode : 서버 에러 코드 (404, 500...) || 로컬 에러 시 0
     * @param errorMsg     : 로컬 오류 시 전송 용 메세지
     */
    private void uploadError(DataObject errorInfo, int responseCode, String errorMsg) {

        try {
            NetWorkController mNetworkController = new NetWorkController(Constants.ERROR_UPLOAD_URL, LoginActivity.this);
            JSONArray array = new JSONArray();
            JSONObject obj = new JSONObject();
            obj.put("key", 0);
//            obj.put("site", Integer.parseInt(PreferenceManager.getInstance().getStoreCd()));
//            obj.put("regEmp", Integer.parseInt(PreferenceManager.getInstance().getUserNo()));
            // 서버 통신 시 에러 전송 값
            if (errorInfo != null) {
                int errCode = Integer.parseInt(errorInfo.getValue("ErrCode"));
                String errInfoMsg = buildErrorMsg() + errorInfo.getValue("ErrMessage");
                obj.put("code", errCode);
                if (errInfoMsg != null) {
                    obj.put("errMsg", errInfoMsg);
                }
                // 로컬 에러 전송 값
            } else {
                obj.put("code", 0);
                obj.put("errMsg", errorMsg);
            }

            obj.put("code64", responseCode);
            obj.put("className", this.getClass().getName());

            array.put(obj);
            JSONArray array1 = new JSONArray();
            JSONObject totalData = new JSONObject();
            totalData.put("TotalCnt", 1);
            array1.put(totalData);

            JSONObject finalData = new JSONObject();
            finalData.put("InData", array);
            finalData.put("TotalData", array1);

            mNetworkController.setHttpConnectionListner(this);
            // Json 데이터 , Json 담을 key 값
            mNetworkController.sendParamAddJson(finalData, "jsonInData");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 에러 전송 용 로컬 정보 생성
     */
    private String buildErrorMsg() {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[4];
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        // 메소드 명
        sb.append(ste.getMethodName());
        sb.append(" > #");
        // 발생한 라인 넘버
        sb.append(ste.getLineNumber());
        sb.append("] ");
        return sb.toString();
    }
}
