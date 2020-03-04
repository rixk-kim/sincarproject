package com.sincar.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ReservationCalendarActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_calendar);

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        findViewById(R.id.btnPrev).setOnClickListener(this);
        findViewById(R.id.btnNext).setOnClickListener(this);

        // TODO - Calendar layout 및 이벤트 처리
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.btnPrev:
                finish();
                break;
            case R.id.btnNext:
                startActivity(new Intent(this, ReservationTimeActivity.class));
                break;
        }
    }
}
