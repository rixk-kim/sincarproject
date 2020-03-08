package com.sincar.customer;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UseDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private String common_pay;      //일반요금
    private String coupone_pay;     //쿠폰요금
    private String approve_info;    //결재정보
    private String use_pay;         //결재요금
    private String reserve_time;    //예약시간
    private String wash_address;    //세차장소
    private String wash_agent;      //대리점
    private String agent_mobile;    //대리점 전화번호
    private String car_info;        //차량정보
    private String car_number;      //차량번호

    private TextView textView_common_pay;
    private TextView textView_coupone_pay;
    private TextView textView_approve_info;
    private TextView textView_use_pay;

    private TextView textView_reserve_time;
    private TextView textView_wash_address;
    private TextView textView_wash_agent;
    private TextView textView_car_info;
    private TextView textView_car_number;

    private LinearLayout useLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_detail);

        //이전 Activity 종료.
        UseHistoryActivity useActivity = (UseHistoryActivity)UseHistoryActivity._useHistoryActivity;
        useActivity.finish();

        Intent intent = getIntent(); /*데이터 수신*/
        common_pay      = intent.getExtras().getString("common_pay");       /*String형*/
        coupone_pay     = intent.getExtras().getString("coupone_pay");      /*String형*/
        approve_info    = intent.getExtras().getString("approve_info");     /*String형*/
        use_pay         = intent.getExtras().getString("use_pay");          /*String형*/
        reserve_time    = intent.getExtras().getString("reserve_time");     /*String형*/
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
        findViewById(R.id.use_detail_btnPrev).setOnClickListener(this);
        findViewById(R.id.reserve_cancel_btn).setOnClickListener(this);

        //일반요금
        textView_common_pay = (TextView) findViewById(R.id.use_common_pay);
        textView_common_pay.setText(common_pay);

        //쿠폰요금
        textView_coupone_pay = (TextView) findViewById(R.id.use_coupone_pay);
        textView_coupone_pay.setText(coupone_pay);

        //결재정보
        textView_approve_info = (TextView) findViewById(R.id.use_approve_info);
        textView_approve_info.setText(approve_info);

        //결재요금
        textView_use_pay = (TextView) findViewById(R.id.use_use_pay);
        textView_use_pay.setText(use_pay);

        //예약시간
        textView_reserve_time = (TextView) findViewById(R.id.use_reserve_time);
        textView_reserve_time.setText(reserve_time);

        //세차장소
        textView_wash_address = (TextView) findViewById(R.id.use_wash_address);
        textView_wash_address.setText(wash_address);

        //대리점
        textView_wash_agent = (TextView) findViewById(R.id.use_wash_agent);
        textView_wash_agent.setText(wash_agent);

        useLinearLayout = (LinearLayout)findViewById(R.id.use_agent_mobile);

        //차량정보
        textView_car_info = (TextView) findViewById(R.id.use_car_info);
        textView_car_info.setText(car_info);

        //차량번호
        textView_car_number = (TextView) findViewById(R.id.use_car_number);
        textView_car_number.setText(car_number);

        //전화걸기
        useLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String call_number = "tel:" + agent_mobile;
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(call_number));
                startActivity(callIntent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.use_detail_btnPrev:
                //  이용내역으로
                intent = new Intent(this, UseHistoryActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.reserve_cancel_btn:
                // TODO - 예약 취소
                break;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, UseHistoryActivity.class);
        startActivity(intent);

        finish();
    }


}



