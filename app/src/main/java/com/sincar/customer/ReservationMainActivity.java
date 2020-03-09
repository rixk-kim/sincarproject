package com.sincar.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sincar.customer.adapter.OptionServiceRecyclerViewAdapter;
import com.sincar.customer.adapter.PointContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.OptionContent;
import com.sincar.customer.adapter.content.PointContent;

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

        // TODO - 서버 연동 후 PointContent.ITEMS에 리스 항목 추가 작업
        // Set the adapter - 포인트 리스트 설정
        View view = findViewById(R.id.optionServiceList);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new OptionServiceRecyclerViewAdapter(OptionContent.ITEMS));
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
