package com.sincar.customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PayApproveResult extends AppCompatActivity implements View.OnClickListener {

    private Context dContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve);
        dContext = this;

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        findViewById(R.id.reserve_approve_btn).setOnClickListener(this);

        //기존 activity 종료
        MapsActivity mapActivity = (MapsActivity)MapsActivity._mMapsActivity;
        mapActivity.finish();

        if(ReservationCalendarActivity._reservationCalendarActivity != null) {
            ReservationCalendarActivity reservationCalendarActivity = (ReservationCalendarActivity) ReservationCalendarActivity._reservationCalendarActivity;
            reservationCalendarActivity.finish();
        }

        ReservationTimeActivity reservationTimeActivity = (ReservationTimeActivity)ReservationTimeActivity._reservationTimeActivity;
        reservationTimeActivity.finish();

        ReservationMainActivity reservationMainActivity = (ReservationMainActivity)ReservationMainActivity._reservationMainActivity;
        reservationMainActivity.finish();

        PaymentActivity paymentActivity = (PaymentActivity)PaymentActivity._paymentActivity;
        paymentActivity.finish();

        if(PayApproveActivity._payApproveActivity != null) {
            PayApproveActivity payApproveActivity = (PayApproveActivity) PayApproveActivity._payApproveActivity;
            payApproveActivity.finish();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.reserve_approve_btn:
                //  이용내역 상세으로
//                intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
                finish();
                break;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
        finish();
    }
}



