package com.sincar.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sincar.customer.adapter.PointContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.PointContent;
import com.sincar.customer.item.LoginDataItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.sincar.customer.HWApplication.voLoginDataItem;
import static com.sincar.customer.HWApplication.voLoginItem;

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

        // 서버 연동하여 1촌, 2촌, 누적 포인트 값 가지고 와서 설정해주기
        TextView count1 = findViewById(R.id.friend_type_1_count);
        if(TextUtils.isEmpty(voLoginItem.INVITE_NUM))
        {
            count1.setText(String.format(getString(R.string.friend_count_str), 0));
        }else{
            count1.setText(String.format(getString(R.string.friend_count_str), Integer.parseInt(voLoginItem.INVITE_NUM)));
        }

        TextView count2 = findViewById(R.id.friend_type_2_count);
        if(TextUtils.isEmpty(voLoginItem.INVITE_NUM))
        {
            count2.setText(String.format(getString(R.string.friend_count_str), 0));
        }else {
            count2.setText(String.format(getString(R.string.friend_count_str), Integer.parseInt(voLoginItem.INVITE_FRI_NUM)));
        }

        TextView point = findViewById(R.id.total_point);
        point.setText("32,870");

        // 서버 연동 후 PointContent.ITEMS에 리스 항목 추가 작업
        // Set the adapter - 포인트 리스트 설정
        List<PointContent.PointItem> ITEMS = new ArrayList<PointContent.PointItem>();

        for(int i = 0; i < voLoginDataItem.size(); i++) {
            PointContent.addItem(new PointContent.PointItem(
                    i,
                    voLoginDataItem.get(i).FRI_NAME,
                    voLoginDataItem.get(i).USE_SERVICE,
                    voLoginDataItem.get(i).SAVE_DATE,
                    voLoginDataItem.get(i).FRI_POINT
            ));
        }

        View view = findViewById(R.id.pointHistoryList);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new PointContentRecyclerViewAdapter(PointContent.ITEMS));
        }
    }

    /**
     * 포인트 리스트 요청
     * PHONE_NUMBER     : 폰번호
     * MEMBER_NO        : 회원번호
     * REQUESTT_PAGE    : 요청페이지
     * REQUEST_NUM      : 요청갯수
     */
    private void requestPointList() {

//        HashMap<String, String> postParams = new HashMap<String, String>();
//        postParams.put("PHONE_NUMBER", loginResult.login.get(0).MEMBER_NO);
//        postParams.put("MEMBER_NO", pwEt.getText().toString().trim());
//        postParams.put("REQUESTT_PAGE", "1");
//        postParams.put("REQUEST_NUM", "20");
//
//        //로그인 요청
//        VolleyNetwork.getInstance(this).passwordChangeRequest(LOGIN_REQUEST, postParams, onResponseListener);
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
