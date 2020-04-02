package com.sincar.customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sincar.customer.database.DBAdapter;
import com.sincar.customer.item.ReserveResult;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.BackPressedUtil;
import com.sincar.customer.util.Util;

import java.util.HashMap;

import static com.sincar.customer.HWApplication.reserveResult;
import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.HWApplication.voReserveItem;
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
//        this.requestWindowFeature(Window.FEATURE_ACTION_BAR | Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_layout_pre);
        cContext = this;

        mBackPressedUtil = new BackPressedUtil(this);

        login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);

        memberjoin_btn = (Button) findViewById(R.id.memberjoin_btn);
        memberjoin_btn.setOnClickListener(this);

        //미전송 데이타 조회
        DBAdapter.getInstance(this);
        cInstance = DBAdapter.getInstance(this);
        cInstance.open();
        send_data = null;
        send_data = cInstance.getYetSendInfo();
        cInstance.close();
        System.out.println("[spirit] count : " + send_data.length);

        if(send_data.length > 0)
        {
            login_btn.setEnabled(false);
            memberjoin_btn.setEnabled(false);

            requestReserveInfo();
        }
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
        postParams.put("MEMBER_NO", send_data[0][1]);         // 회원번호
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
