package com.sincar.customer;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sincar.customer.util.Util;

public class UseDeleteSettleActivity extends AppCompatActivity implements View.OnClickListener {
    private String settle_reserve_status;  //예약상태
    private String settle_common_pay;      //일반요금
    private String settle_coupone_pay;     //쿠폰요금
    private String settle_approve_info;    //결재정보
    private String settle_use_pay;         //결재요금
    private String settle_reserve_time;    //예약시간
    private String settle_cancel_time;     //예약취소시간
    private String settle_wash_address;    //세차장소
    private String settle_wash_agent;      //대리점
    private String settle_agent_mobile;    //대리점 전화번호
    private String settle_car_info;        //차량정보
    private String settle_car_number;      //차량번호
    private String settle_point_pay;       //사용포인트

    private TextView textView_settle_common_pay, textView_settle_coupone_pay, textView_settle_approve_info, textView_settle_use_pay;

    private TextView textView_settle_reserve_time, textView_settle_reserve_cancel_time, textView_settle_wash_address;
    private TextView textView_settle_wash_agent, textView_settle_car_info, textView_settle_car_number, textView_settle_point_pay;

    private LinearLayout useLinearLayout, reserve_cancel_area, reserve_cancel_layout;

    private String req_sort;
    private String rent_insurance;  // 면책요금(보험료)
    private String rent_allocate;   // 배차위치
    private String rent_return;     // 반납위치

    private TextView use_delete_title_1, use_delete_title_2, use_delete_title_3;
    private TextView use_del_column1, use_del_column2, use_del_column3;
    private LinearLayout settleAllocateLayout, settleReturnLayout, settleWashLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_delete_settle);

        //이전 Activity 종료.
        UseHistoryActivity useActivity = (UseHistoryActivity)UseHistoryActivity._useHistoryActivity;
        useActivity.finish();

        Intent intent = getIntent(); /*데이터 수신*/
        settle_reserve_status   = intent.getExtras().getString("reserve_status");   /*String형*/
        settle_common_pay       = intent.getExtras().getString("common_pay");       /*String형*/
        settle_coupone_pay      = intent.getExtras().getString("coupone_pay");      /*String형*/
        settle_approve_info     = intent.getExtras().getString("approve_info");     /*String형*/
        settle_use_pay          = intent.getExtras().getString("use_pay");          /*String형*/
        settle_reserve_time     = intent.getExtras().getString("reserve_time");     /*String형*/
        settle_cancel_time      = intent.getExtras().getString("cancel_time");      /*String형*/
        settle_wash_address     = intent.getExtras().getString("wash_address");     /*String형*/
        settle_wash_agent       = intent.getExtras().getString("wash_agent");       /*String형*/
        settle_agent_mobile     = intent.getExtras().getString("agent_mobile");     /*String형*/
        settle_car_info         = intent.getExtras().getString("car_info");         /*String형*/
        settle_car_number       = intent.getExtras().getString("car_number");       /*String형*/
        settle_point_pay        = intent.getExtras().getString("point_pay");        /*String형*/

        req_sort        = intent.getExtras().getString("req_sort");         /*String형*/
        rent_insurance  = intent.getExtras().getString("rent_insurance");   /*String형*/
        rent_allocate   = intent.getExtras().getString("rent_allocate");    /*String형*/
        rent_return     = intent.getExtras().getString("rent_return");      /*String형*/

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        findViewById(R.id.use_delete_settle_btnPrev_layout).setOnClickListener(this);

        //일반요금
        textView_settle_common_pay = (TextView) findViewById(R.id.settle_common_pay);
        textView_settle_common_pay.setText(Util.setAddMoneyDot(settle_common_pay) + "원");

        //쿠폰요금
        textView_settle_coupone_pay = (TextView) findViewById(R.id.settle_coupone_pay);
        textView_settle_coupone_pay.setText(Util.setAddMoneyDot(settle_coupone_pay) + "원");

        //포인트요금
        textView_settle_point_pay = (TextView) findViewById(R.id.settle_point_pay);
        textView_settle_point_pay.setText(Util.setAddMoneyDot(settle_point_pay) + "원");

        //결재정보
        textView_settle_approve_info = (TextView) findViewById(R.id.settle_approve_info);
        textView_settle_approve_info.setText(settle_approve_info);

        //결재요금
        textView_settle_use_pay = (TextView) findViewById(R.id.settle_use_pay);
        textView_settle_use_pay.setText(Util.setAddMoneyDot(settle_use_pay) + "원");

        use_delete_title_1 = (TextView) findViewById(R.id.use_del_title_1);  //기본세차
        use_delete_title_2 = (TextView) findViewById(R.id.use_del_title_2);  //쿠폰
        use_delete_title_3 = (TextView) findViewById(R.id.use_del_title_3);  //포인트

        use_del_column1 = (TextView) findViewById(R.id.use_del_column1);  //예약시간
        use_del_column2 = (TextView) findViewById(R.id.use_del_column2);  //대리점
        use_del_column3 = (TextView) findViewById(R.id.use_del_column3);  //차량정보

        settleAllocateLayout   = (LinearLayout) findViewById(R.id.settle_column10);
        settleReturnLayout     = (LinearLayout) findViewById(R.id.settle_column11);
        settleWashLayout       = (LinearLayout) findViewById(R.id.settle_wash_layout);   //세차장소

        if("1".equals(req_sort))    //렌터카
        {
            use_delete_title_1.setText("차량 대여료");
            use_delete_title_3.setText("보험료");

            use_del_column1.setText("대여시간");
            use_del_column2.setText("지점정보");
            use_del_column3.setText("대여차량");

            settleWashLayout.setVisibility(View.GONE);
            settleAllocateLayout.setVisibility(View.VISIBLE);
            settleReturnLayout.setVisibility(View.VISIBLE);
        }else{
            use_delete_title_1.setText("기본세차");
            use_delete_title_3.setText("포인트");

            use_del_column1.setText("예약시간");
            use_del_column2.setText("대리점");
            use_del_column3.setText("차량정보");

            settleWashLayout.setVisibility(View.VISIBLE);
            settleAllocateLayout.setVisibility(View.GONE);
            settleReturnLayout.setVisibility(View.GONE);
        }


        //예약시간
        textView_settle_reserve_time = (TextView) findViewById(R.id.settle_reserve_time);
        textView_settle_reserve_time.setText(settle_reserve_time);

        //예약취소시간
        textView_settle_reserve_cancel_time = (TextView) findViewById(R.id.settle_reserve_cancel_time);
        textView_settle_reserve_cancel_time.setText(settle_cancel_time);

        //세차장소
        textView_settle_wash_address = (TextView) findViewById(R.id.settle_wash_address);
        textView_settle_wash_address.setText(settle_wash_address);

        //대리점
        textView_settle_wash_agent = (TextView) findViewById(R.id.settle_wash_agent);
        textView_settle_wash_agent.setText(settle_wash_agent);

//        useLinearLayout = (LinearLayout)findViewById(R.id.use_agent_mobile);

        //차량정보
        textView_settle_car_info = (TextView) findViewById(R.id.settle_car_info);
        textView_settle_car_info.setText(settle_car_info);

        //차량번호
        textView_settle_car_number = (TextView) findViewById(R.id.settle_car_number);
        textView_settle_car_number.setText(settle_car_number);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.use_delete_settle_btnPrev_layout:
                //  이용내역으로
                intent = new Intent(this, UseHistoryActivity.class);
                startActivity(intent);
                finish();
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



