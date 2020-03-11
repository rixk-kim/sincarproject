package com.sincar.customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import com.sincar.customer.adapter.CouponeContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.CouponeContent;

public class CouponeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupone);

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        findViewById(R.id.coupone_btnPrev).setOnClickListener(this);
        //       findViewById(R.id.myinfo_btnNext).setOnClickListener(this);


        // TODO - 서버 연동 후 PointContent.ITEMS에 리스 항목 추가 작업
        // Set the adapter - 포인트 리스트 설정
        View view = findViewById(R.id.couponeHistoryList);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new CouponeContentRecyclerViewAdapter(this, CouponeContent.ITEMS));
        }


    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.coupone_btnPrev:
                //  TODO - 내정보
                intent = new Intent(this, MyProfileSettingsActivity.class);
                startActivity(intent);
                finish();
                break;
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


