package com.sincar.customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.sincar.customer.adapter.NoticeContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.NoticeContent;

public class NoticeActivity extends AppCompatActivity implements View.OnClickListener {
    public static NoticeActivity _noticeActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        _noticeActivity = NoticeActivity.this;

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        findViewById(R.id.notice_btnPrev).setOnClickListener(this);
        //       findViewById(R.id.myinfo_btnNext).setOnClickListener(this);



        // myinfo_user_name => 이름
        // user_mobile_number => 휴대폰 번호

        // TODO - 서버 연동하여 이름, 휴대폰 번호 값 가지고 와서 설정해주기
//        TextView myinfo_user_name = findViewById(R.id.myinfo_user_name);
//        myinfo_user_name.setText("홍길동");
//
//        TextView user_mobile_number = findViewById(R.id.user_mobile_number);
//        user_mobile_number.setText("010-1234-5678");


        // TODO - 서버 연동 후 PointContent.ITEMS에 리스 항목 추가 작업
        // Set the adapter - 포인트 리스트 설정
        View view = findViewById(R.id.noticeHistoryList);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new NoticeContentRecyclerViewAdapter(this, NoticeContent.ITEMS));
        }


    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.notice_btnPrev:
                //  TODO - 내정보
                intent = new Intent(this, MyProfileSettingsActivity.class);
                startActivity(intent);
                finish();
                break;
//            case R.id.myinfo_btnNext:
//                //  TODO - 비밀번호변경
//                intent = new Intent(this, PasswordChangeActivity.class);
//                startActivity(intent);
//                finish();
//                break;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, MyProfileSettingsActivity.class);
        startActivity(intent);

        finish();
    }


}


