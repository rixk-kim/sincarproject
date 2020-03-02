package com.sincar.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sincar.customer.adapter.PointContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.PointContent;

public class PointHistoryActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_history);

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        findViewById(R.id.btnPrev).setOnClickListener(this);
        findViewById(R.id.btnNext).setOnClickListener(this);

        // TODO - 서버 연동하여 1촌, 2촌, 누적 포인트 값 가지고 와서 설정해주기
        TextView count1 = findViewById(R.id.friend_type_1_count);
        count1.setText(String.format(getString(R.string.friend_count_str), 89));

        TextView count2 = findViewById(R.id.friend_type_2_count);
        count2.setText(String.format(getString(R.string.friend_count_str), 322));

        TextView point = findViewById(R.id.total_point);
        point.setText("32,870");

        // TODO - 서버 연동 후 PointContent.ITEMS에 리스 항목 추가 작업
        // Set the adapter - 포인트 리스트 설정
        View view = findViewById(R.id.pointHistoryList);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new PointContentRecyclerViewAdapter(PointContent.ITEMS));
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.btnPrev:
                finish();
                break;
            case R.id.btnNext:
                // TODO - 포인트 보기
                intent = new Intent(this, PointInfoActivity.class);
                startActivity(intent);
                break;
        }
    }
}
