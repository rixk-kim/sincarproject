package com.sincar.customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sincar.customer.adapter.CardContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.CardContent;
import com.sincar.customer.network.DataObject;
import com.sincar.customer.network.JsonParser;
import com.sincar.customer.util.DataParser;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

public class CarRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_register);

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    @SuppressLint("ResourceAsColor")
    private void init() {
        findViewById(R.id.car_reg_btnPrev).setOnClickListener(this);    //이전
        findViewById(R.id.btnNext1).setOnClickListener(this);           //제조사 선택
        findViewById(R.id.btnNext2).setOnClickListener(this);           //차량 선택
        findViewById(R.id.car_reg_btn).setOnClickListener(this);        //확인



        // myinfo_user_name => 이름
        // user_mobile_number => 휴대폰 번호

        // TODO - 서버 연동하여 이름, 휴대폰 번호 값 가지고 와서 설정해주기
//        TextView myinfo_user_name = findViewById(R.id.myinfo_user_name);
//        myinfo_user_name.setText("홍길동");
//
//        TextView user_mobile_number = findViewById(R.id.user_mobile_number);
//        user_mobile_number.setText("010-1234-5678");

    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.car_reg_btnPrev:
                //  TODO - 내정보
                intent = new Intent(this, CarManageActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.btnNext1:
                //  TODO - 제조사 선택

                break;

            case R.id.btnNext2:
                //  TODO - 차량 선택

                break;

            case R.id.car_reg_btn:
                //  TODO - 차량 등록
                Toast.makeText(this, "카드가 등록 되었습니다.", Toast.LENGTH_SHORT).show();

                //  TODO - 차량 등록 후 리스트 이동

                break;

        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, CardActivity.class);
        startActivity(intent);

        finish();
    }


}


