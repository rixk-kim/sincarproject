package com.sincar.customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sincar.customer.adapter.CardContentRecyclerViewAdapter;
import com.sincar.customer.adapter.UseContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.CardContent;
import com.sincar.customer.adapter.content.UseContent;

public class UseHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    private MenuItem prevBottomNavigation;
    private Context uContext;

    public static UseHistoryActivity _useHistoryActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_history);
        _useHistoryActivity = UseHistoryActivity.this;
        uContext = this;

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    @SuppressLint("ResourceAsColor")
    private void init() {
//        findViewById(R.id.card_btnPrev).setOnClickListener(this);
//        findViewById(R.id.card_btnNext).setOnClickListener(this);



        // myinfo_user_name => 이름
        // user_mobile_number => 휴대폰 번호

        // TODO - 서버 연동하여 이름, 휴대폰 번호 값 가지고 와서 설정해주기
//        TextView myinfo_user_name = findViewById(R.id.myinfo_user_name);
//        myinfo_user_name.setText("홍길동");
//
//        TextView user_mobile_number = findViewById(R.id.user_mobile_number);
//        user_mobile_number.setText("010-1234-5678");


        // TODO - 서버 연동 후 CardContent.ITEMS에 리스 항목 추가 작업
        // Set the adapter - 이용내역 리스트 설정
        View view = findViewById(R.id.useHistoryList);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new UseContentRecyclerViewAdapter(this, UseContent.ITEMS));
        }

        //하단메뉴 고정(0:홈)
        BottomNavigationView bottomNavigationView = findViewById(R.id.useBottomNav);
        prevBottomNavigation = bottomNavigationView.getMenu().getItem(1);
        prevBottomNavigation.setChecked(true);

        // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.bottom_home: {
                        // HOME
                        intent = new Intent(uContext, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                    case R.id.bottom_use_history: {
                        //이용내역
                        break;
                    }
                    case R.id.bottom_myinfo: {
                        intent = new Intent(uContext, MyProfileSettingsActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }

                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;

//        switch (v.getId()) {
//            case R.id.card_btnPrev:
//                //  TODO - 내정보
//                intent = new Intent(this, MyProfileSettingsActivity.class);
//                startActivity(intent);
//                finish();
//                break;
//            case R.id.card_btnNext:
//                //  TODO - 카드등록
//                intent = new Intent(this, CardRegisterActivity.class);
//                startActivity(intent);
//                finish();
//                break;
//        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }


}


