package com.sincar.customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UseDeleteActivity extends AppCompatActivity implements View.OnClickListener {
    private String reserve_status;  //예약상태
    private String common_pay;      //일반요금
    private String coupone_pay;     //쿠폰요금
    private String approve_info;    //결재정보
    private String use_pay;         //결재요금
    private String reserve_time;    //예약시간
    private String cancel_time;     //예약취소시간
    private String wash_address;    //세차장소
    private String wash_agent;      //대리점
    private String agent_mobile;    //대리점 전화번호
    private String car_info;        //차량정보
    private String car_number;      //차량번호

    private CheckBox delete_checkbox;   //

    private Context dContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_delete);
        dContext = this;

        //이전 Activity 종료.
        UseHistoryActivity useActivity = (UseHistoryActivity)UseHistoryActivity._useHistoryActivity;
        useActivity.finish();

        Intent intent = getIntent(); /*데이터 수신*/
        reserve_status  = intent.getExtras().getString("reserve_status");       /*String형*/
        common_pay      = intent.getExtras().getString("common_pay");       /*String형*/
        coupone_pay     = intent.getExtras().getString("coupone_pay");      /*String형*/
        approve_info    = intent.getExtras().getString("approve_info");     /*String형*/
        use_pay         = intent.getExtras().getString("use_pay");          /*String형*/
        reserve_time    = intent.getExtras().getString("reserve_time");     /*String형*/
        cancel_time     = intent.getExtras().getString("cancel_time");     /*String형*/
        wash_address    = intent.getExtras().getString("wash_address");     /*String형*/
        wash_agent      = intent.getExtras().getString("wash_agent");       /*String형*/
        agent_mobile    = intent.getExtras().getString("agent_mobile");     /*String형*/
        car_info        = intent.getExtras().getString("car_info");         /*String형*/
        car_number  = intent.getExtras().getString("car_number");           /*String형*/

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        findViewById(R.id.use_delete_btnPrev).setOnClickListener(this);
        delete_checkbox = (CheckBox)findViewById(R.id.delete_checkbox);
        findViewById(R.id.reserve_delete_btn).setOnClickListener(this);
//        Button b = (Button)findViewById(R.id.reserve_delete_btn);
//
//        b.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                String result = "";  // 결과를 출력할 문자열  ,  항상 스트링은 빈문자열로 초기화 하는 습관을 가지자
//                if(delete_checkbox.isChecked() == true){
//                    // TODO - 예약취소로 이동
//                    Intent intent = new Intent(dContext, UseDeleteSettleActivity.class);
//                    intent.putExtra("reserve_status", reserve_status);
//                    intent.putExtra("common_pay", common_pay);
//                    intent.putExtra("coupone_pay", coupone_pay);
//                    intent.putExtra("approve_info", approve_info);
//                    intent.putExtra("use_pay", use_pay);
//                    intent.putExtra("reserve_time", reserve_time);
//                    intent.putExtra("cencel_time", cancel_time);
//                    intent.putExtra("wash_address", wash_address);
//                    intent.putExtra("wash_agent", wash_agent);
//                    intent.putExtra("agent_mobile", agent_mobile);
//                    intent.putExtra("car_info", car_info);
//                    intent.putExtra("car_number", car_number);
//                    startActivity(intent);
//                    finish();
//                }else {
//                    Toast.makeText(dContext, "동의를 체크해 주세요.", Toast.LENGTH_SHORT);
//                }
//
//            } // end onClick
//        }); // end setOnClickListener

    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.use_delete_btnPrev:
                //  이용내역 상세으로
                intent = new Intent(this, UseDetailActivity.class);
                intent.putExtra("reserve_status", reserve_status);
                intent.putExtra("common_pay", common_pay);
                intent.putExtra("coupone_pay", coupone_pay);
                intent.putExtra("approve_info", approve_info);
                intent.putExtra("use_pay", use_pay);
                intent.putExtra("reserve_time", reserve_time);
                intent.putExtra("cencel_time", cancel_time);
                intent.putExtra("wash_address", wash_address);
                intent.putExtra("wash_agent", wash_agent);
                intent.putExtra("agent_mobile", agent_mobile);
                intent.putExtra("car_info", car_info);
                intent.putExtra("car_number", car_number);
                startActivity(intent);
                finish();
                break;

            case R.id.reserve_delete_btn:
                if(delete_checkbox.isChecked() == true){
                    // TODO - 예약취소로 이동
                    intent = new Intent(this, UseDeleteSettleActivity.class);
                    intent.putExtra("reserve_status", reserve_status);
                    intent.putExtra("common_pay", common_pay);
                    intent.putExtra("coupone_pay", coupone_pay);
                    intent.putExtra("approve_info", approve_info);
                    intent.putExtra("use_pay", use_pay);
                    intent.putExtra("reserve_time", reserve_time);
                    intent.putExtra("cencel_time", cancel_time);
                    intent.putExtra("wash_address", wash_address);
                    intent.putExtra("wash_agent", wash_agent);
                    intent.putExtra("agent_mobile", agent_mobile);
                    intent.putExtra("car_info", car_info);
                    intent.putExtra("car_number", car_number);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(UseDeleteActivity.this, "동의를 체크해 주세요.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, UseDetailActivity.class);
        intent.putExtra("reserve_status", reserve_status);
        intent.putExtra("common_pay", common_pay);
        intent.putExtra("coupone_pay", coupone_pay);
        intent.putExtra("approve_info", approve_info);
        intent.putExtra("use_pay", use_pay);
        intent.putExtra("reserve_time", reserve_time);
        intent.putExtra("cencel_time", cancel_time);
        intent.putExtra("wash_address", wash_address);
        intent.putExtra("wash_agent", wash_agent);
        intent.putExtra("agent_mobile", agent_mobile);
        intent.putExtra("car_info", car_info);
        intent.putExtra("car_number", car_number);
        startActivity(intent);

        finish();
    }


}


