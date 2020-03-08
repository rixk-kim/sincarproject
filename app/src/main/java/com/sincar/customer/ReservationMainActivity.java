package com.sincar.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ReservationMainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_main);


        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        findViewById(R.id.btnPrev).setOnClickListener(this);

        // TODO - 등록된 차량 정보 확인하여 필요한 레이아웃 visible 및 이벤트 핸들러 추가하기
        boolean isCarRegistered = false;

        if (!isCarRegistered) {
            findViewById(R.id.car_register_layout).setVisibility(View.GONE);
            findViewById(R.id.car_modify_layout).setVisibility(View.VISIBLE);

//            ((TextView)findViewById(R.id.car_name_str)).setText("");
//            ((TextView)findViewById(R.id.car_number_str)).setText("");

            findViewById(R.id.btnCarModify).setOnClickListener(this);
        } else {
            findViewById(R.id.car_register_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.car_modify_layout).setVisibility(View.GONE);

            findViewById(R.id.btnCarRegister).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.btnPrev:
                finish();
                break;

            case R.id.btnCarRegister:
                // TODO - 차량등록 기능 추가
                break;

            case R.id.btnCarModify:
                // TODO - 차량변경하기 기능 추가
                break;
        }
    }
}
