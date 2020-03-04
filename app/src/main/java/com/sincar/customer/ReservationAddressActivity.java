package com.sincar.customer;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sincar.customer.adapter.AddressContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.AddressContent;

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

        // TODO - 서버 연동 후 AddressContent.ITEMS에 리스 항목 추가 작업
        // Set the adapter - 포인트 리스트 설정
        View view = findViewById(R.id.searchAddressList);
        Log.d("test", "test1");
        if (view instanceof RecyclerView) {
            Log.d("test", "test2");
            Context context = view.getContext();
            Log.d("test", "test3");
            RecyclerView recyclerView = (RecyclerView) view;
            Log.d("test", "test4");

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            Log.d("test", "test5");
            recyclerView.setAdapter(new AddressContentRecyclerViewAdapter(AddressContent.ITEMS));
            Log.d("test", "test6");
        }
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
