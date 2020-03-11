package com.sincar.customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.sincar.customer.adapter.CarContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.CarContent;

public class CarManageActivity extends AppCompatActivity implements View.OnClickListener {
    private String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_manage);

        Intent intent = getIntent(); /*데이터 수신*/
        path    = intent.getExtras().getString("path");    /*String형*/

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    @SuppressLint("ResourceAsColor")
    private void init() {
        findViewById(R.id.car_manage_btnPrev).setOnClickListener(this);
        findViewById(R.id.car_manage_btnNext).setOnClickListener(this);
        findViewById(R.id.car_manage_reg_btn).setOnClickListener(this); //확인




        // myinfo_user_name => 이름
        // user_mobile_number => 휴대폰 번호

        // TODO - 서버 연동하여 이름, 휴대폰 번호 값 가지고 와서 설정해주기
//        TextView myinfo_user_name = findViewById(R.id.myinfo_user_name);
//        myinfo_user_name.setText("홍길동");
//
//        TextView user_mobile_number = findViewById(R.id.user_mobile_number);
//        user_mobile_number.setText("010-1234-5678");


        // TODO - 서버 연동 후 CardContent.ITEMS에 리스 항목 추가 작업
        // Set the adapter - 포인트 리스트 설정
        View view = findViewById(R.id.carManageList);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new CarContentRecyclerViewAdapter(this, CarContent.ITEMS));
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.car_manage_btnPrev:
            case R.id.car_manage_reg_btn:
                //  TODO - 내정보
                if("main".equals(path)) {
                    intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else if("reserve".equals(path)){
                    intent = new Intent(this, ReservationMainActivity.class);
                    intent.putExtra("result", "some value");
                    setResult(RESULT_OK, intent);
                    finish();

                }else {
                    intent = new Intent(this, MyProfileSettingsActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.car_manage_btnNext:
                //  TODO - 차량등록
                intent = new Intent(this, CarRegisterActivity.class);
                intent.putExtra("path", path);
                startActivity(intent);
                finish();
                break;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent;
        if("main".equals(path))
        {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else if("reserve".equals(path)){
            intent = new Intent(this, ReservationMainActivity.class);
            intent.putExtra("result", "some value");
            setResult(RESULT_OK, intent);
            finish();
        }else {
            intent = new Intent(this, MyProfileSettingsActivity.class);
            startActivity(intent);
        }
        finish();
    }


}



