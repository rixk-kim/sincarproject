package com.sincar.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class ReservationAddressActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_address);

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        findViewById(R.id.btnPrev).setOnClickListener(this);
        findViewById(R.id.btnSearchCancel).setOnClickListener(this);
        findViewById(R.id.btnSearchAddress).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPrev:
                finish();
                break;
            case R.id.btnSearchCancel:
                // TODO - 검색 주소 삭제 및 리스트 Clear
                break;
            case R.id.btnSearchAddress:
                // TODO - 주소 검색 리스트 생성
                break;
        }
    }
}
