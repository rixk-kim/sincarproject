package com.sincar.customer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sincar.customer.adapter.OptionServiceRecyclerViewAdapter;
import com.sincar.customer.adapter.PointContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.OptionContent;
import com.sincar.customer.adapter.content.PointContent;

public class ReservationMainActivity extends AppCompatActivity implements View.OnClickListener {
    public final static int CAR_MANAGE_REQ_CODE = 1001;
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
        //예약하기
        findViewById(R.id.reserve_btn).setOnClickListener(this);

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
                intent = new Intent(this, CarManageActivity.class);
                intent.putExtra("path", "reserve");
                startActivityForResult(intent, CAR_MANAGE_REQ_CODE);
                //startActivity(intent);
                break;

            case R.id.reserve_btn:
                //결재 종류 선택 팝업
                reserveSelect();
                break;
        }
    }

    private void reserveSelect()
    {
        final String [] items = {"신용카드", "계좌이체", "휴대폰결재", "가상계좌"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ReservationMainActivity.this);
        builder.setTitle("결재방법 선택");

        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ReservationMainActivity.this, items[which] + " 라이브러리로 이동합니다.", Toast.LENGTH_LONG).show();
                // TODO - 결재 모듈로 이동합니다.

                dialog.dismiss(); // 누르면 바로 닫히는 형태
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAR_MANAGE_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(ReservationMainActivity.this, "Result: " + data.getStringExtra("result"), Toast.LENGTH_SHORT).show();
            } else {   // RESULT_CANCEL
                Toast.makeText(ReservationMainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
