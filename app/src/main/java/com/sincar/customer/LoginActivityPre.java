package com.sincar.customer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sincar.customer.database.DBAdapter;
import com.sincar.customer.item.MemberResult;
import com.sincar.customer.item.ReserveResult;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.preference.PreferenceManager;
import com.sincar.customer.util.BackPressedUtil;
import com.sincar.customer.util.Util;

import java.util.HashMap;

import static com.sincar.customer.HWApplication.reserveResult;
import static com.sincar.customer.HWApplication.memberResult;
import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.HWApplication.voMemberItem;
import static com.sincar.customer.HWApplication.voReserveItem;
import static com.sincar.customer.common.Constants.APPVERSION_REQUEST;
import static com.sincar.customer.common.Constants.PAY_APPROVE_REQUEST;

/**
 * 2020.02.26 spirit
 * 로그인 , 회원가입 선택 액티비티
 */
public class LoginActivityPre extends Activity implements View.OnClickListener {
    private static final String TAG = LoginActivityPre.class.getSimpleName();
    //===================== 뷰 =====================
    private Button login_btn;
    private Button memberjoin_btn;
    //===================== 뷰 =====================

    private BackPressedUtil mBackPressedUtil;
    private boolean serverStatus = false;
    private String[][] send_data;
    private Context cContext;
    private DBAdapter cInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout_pre);
        cContext = this;

        mBackPressedUtil = new BackPressedUtil(this);

        login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);

        memberjoin_btn = (Button) findViewById(R.id.memberjoin_btn);
        memberjoin_btn.setOnClickListener(this);

        //app 버전 체크
        boolean versioCheck =  PreferenceManager.getInstance().getVersionCheck();
        if(!versioCheck) requestAuthCode();

//        //미전송 데이타 조회
//        DBAdapter.getInstance(this);
//        cInstance = DBAdapter.getInstance(this);
//        cInstance.open();
//        send_data = null;
//        send_data = cInstance.getYetSendInfo();
//        cInstance.close();
//        System.out.println("[spirit] count : " + send_data.length);
//
//        if(send_data.length > 0)
//        {
//            login_btn.setEnabled(false);
//            memberjoin_btn.setEnabled(false);
//
//            requestReserveInfo();
//        }
    }

    private void requestAuthCode() {

        PreferenceManager.getInstance().setVersionCheck(true);

        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("PHONE_NUMBER", "01076524299");     // 테스트 전화번호

        //프로그래스바 시작
        Util.showDialog(this);

        //인증번호 요청
        VolleyNetwork.getInstance(this).serverDataRequest(APPVERSION_REQUEST, postParams, onMemberResponseListener);
    }

    VolleyNetwork.OnResponseListener onMemberResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String it) {
            //           it = " {\"auth_send\": {\"AUTH_RESULT\":\"0\",\"CAUSE\":\"\",\"AUTH_NUMBER\":\"123456\"}}";
            Gson gSon = new Gson();
            //LoginResult loginResult = gSon.fromJson(it, LoginResult.class);
            memberResult = gSon.fromJson(it, MemberResult.class);

            voMemberItem.MEMBER_RESULT      = memberResult.member_result.MEMBER_RESULT;
            voMemberItem.APP_VERSION        = memberResult.member_result.APP_VERSION;
            voMemberItem.APK_URL            = memberResult.member_result.APK_URL;

            //프로그래스바 종료
            Util.dismiss();

            PackageInfo pi = null;
            try{
                pi = getPackageManager().getPackageInfo(getPackageName(),0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            if(!TextUtils.isEmpty(pi.versionName)) {
                if (pi.versionName.equals(voMemberItem.APP_VERSION)) {
                    //미전송 데이타 조회
                    DBAdapter.getInstance(cContext);
                    cInstance = DBAdapter.getInstance(cContext);
                    cInstance.open();
                    send_data = null;
                    send_data = cInstance.getYetSendInfo();
                    cInstance.close();
                    System.out.println("[spirit] count : " + send_data.length);

                    if(send_data.length > 0)
                    {
//                        login_btn.setEnabled(false);
//                        memberjoin_btn.setEnabled(false);
//
//                        requestReserveInfo();
                    }
                }else{
                    //버전 업데이트 진행.
                    appUpdate();
                }
            }
        }

        @Override
        public void onResponseFailListener(VolleyError it) {
            //프로그래스바 종료
            Util.dismiss();

            //Toast.makeText(MemberAuthActivity.this, "재전송에 실패하였습니다.", Toast.LENGTH_LONG).show();
        }
    };

    private void appUpdate()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivityPre.this);
        builder.setTitle(getString(R.string.notice));
        builder.setMessage(getString(R.string.new_update_file_install_msg));
        // 필수
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 전송 받은 다운로드 파일 URL
                //              updateApp(updateUrl);
                dialog.dismiss();

                //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.sincar.co.kr/design/newcar.asp"));    //voLoginItem.APK_URL
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(voMemberItem.APK_URL));
                startActivity(intent);

                finish();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 앱 종료
                finish();
            }
        });
        builder.show();
    }

    /**
     * 앱 종료
     */
    @Override
    public void onBackPressed()
    {
        try
        {
            mBackPressedUtil.onBackPressed();
        }catch (Exception e1){
            e1.printStackTrace();
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.login_btn:
                // 로그인 이동
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.memberjoin_btn:
                // 회원가입 이동
                intent = new Intent(this, MemberJoinActivity.class);
                intent.putExtra("path", "member_join");
                startActivity(intent);
                finish();
                break;

        }
    }

    /**
     * 예약정보 전송
     * MEMBER_NO        : 회원번호
     * REQUESTT_PAGE    : 요청페이지
     * REQUEST_NUM      : 요청갯수
     */
    private void requestReserveInfo() {
        HashMap<String, String> postParams = new HashMap<String, String>();
        //PHONE_NEMBER
        postParams.put("APPROVE_NUMBER", send_data[0][0]);           // 예약번호
        postParams.put("MEMBER_NO", send_data[0][1]);               // 회원번호
        postParams.put("RESERVE_ADDRESS", send_data[0][2]);         // 예약주소
        postParams.put("RESERVE_YEAR", send_data[0][3]);               // 년
        postParams.put("RESERVE_MONTH", send_data[0][4]);             // 월
        postParams.put("RESERVE_DAY", send_data[0][5]);                 // 일
        postParams.put("AGENT_SEQ", send_data[0][6]);                     // 대리점 SEQ
        postParams.put("AGENT_COMPANY", send_data[0][7]);             // 대리점
        postParams.put("AGENT_TIME", send_data[0][8]);                   // 예약시간
        postParams.put("WASH_PLACE", send_data[0][9]);                    // 세차장소
        postParams.put("ADD_SERVICE", send_data[0][10]);             // 부가 서비스
        postParams.put("CAR_COMPANY", send_data[0][11]);                 // 제조사
        postParams.put("CAR_MODEL", send_data[0][12]);                      // 모델명
        postParams.put("CAR_NUMBER", send_data[0][13]);                   // 차량번호
        postParams.put("POINT_USE", send_data[0][14]);                  // 사용 포인트
        postParams.put("COUPONE_SEQ", send_data[0][15]);             // 사용 쿠폰번호
        postParams.put("CHARGE_PAY", send_data[0][16]);                    // 총 결재 요금

        //프로그래스바 시작
        Util.showDialog(this);
        //사용내역 요청
        VolleyNetwork.getInstance(this).serverDataRequest(PAY_APPROVE_REQUEST, postParams, onReserveResponseListener);
    }

    VolleyNetwork.OnResponseListener onReserveResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String serverData) {
            /*
                  {"reserve": [{"RESERVE_RESULT":"0","CAUSE":"","MY_POINT":"24000"}]}
                  {"reserve": [{"RESERVE_RESULT":"1","CAUSE":"형식에 맞지 않습니다.","MY_POINT":""}]}
             */

            //서버 저장하고 다음 이동
            Gson gSon = new Gson();
            reserveResult = gSon.fromJson(serverData, ReserveResult.class);

            voReserveItem.RESERVE_RESULT     = reserveResult.reserve.get(0).RESERVE_RESULT;
            voReserveItem.CAUSE              = reserveResult.reserve.get(0).CAUSE;
            voReserveItem.MY_POINT           = reserveResult.reserve.get(0).MY_POINT;

            //프로그래스바 종료
            Util.dismiss();

            login_btn.setEnabled(true);
            memberjoin_btn.setEnabled(true);

            if("0".equals(voReserveItem.RESERVE_RESULT)) {
                cInstance.open();
                cInstance.reserve_data_update(send_data[0][0]);
                cInstance.close();
            }else{
                Toast.makeText(cContext, "서버 저장에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onResponseFailListener(VolleyError it) {
            //프로그래스바 종료
            Util.dismiss();

            login_btn.setEnabled(true);
            memberjoin_btn.setEnabled(true);

//            saveFailShowAlertDialog();
        }
    };
}
