package com.sincar.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sincar.customer.adapter.PointContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.PointContent;
import com.sincar.customer.item.LoginDataItem;
import com.sincar.customer.item.PointResult;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.sincar.customer.HWApplication.pointResult;
import static com.sincar.customer.HWApplication.voPointDataItem;
import static com.sincar.customer.HWApplication.voPointItem;

import static com.sincar.customer.HWApplication.voLoginDataItem;
import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.common.Constants.POINT_LIST_REQUEST;

public class PointHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    //페이지 처리
    private int request_page = 1;                           // 페이징변수. 초기 값은 0 이다.
    private final int request_offset = 20;                  // 한 페이지마다 로드할 데이터 갯수.

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
        findViewById(R.id.btnPrev_layout).setOnClickListener(this);
        findViewById(R.id.btnNext).setOnClickListener(this);

        // 서버 연동하여 1촌, 2촌, 누적 포인트 값 가지고 와서 설정해주기
        TextView count1 = findViewById(R.id.friend_type_1_count);
        if(TextUtils.isEmpty(voLoginItem.INVITE_NUM))
        {
            count1.setText(String.format(getString(R.string.friend_count_str), 0));
        }else{
            count1.setText(String.format(getString(R.string.friend_count_str), Integer.parseInt(voLoginItem.INVITE_NUM)));
        }

        //2촌
        TextView count2 = findViewById(R.id.friend_type_2_count);
        if(TextUtils.isEmpty(voLoginItem.INVITE_NUM))
        {
            count2.setText(String.format(getString(R.string.friend_count_str), 0));
        }else {
            count2.setText(String.format(getString(R.string.friend_count_str), Integer.parseInt(voLoginItem.INVITE_FRI_NUM)));
        }

        //누적 포인트
        TextView point = findViewById(R.id.total_point);
        if(TextUtils.isEmpty(voLoginItem.ACCUM_POINT)) {
            point.setText("0");
        }else{
            point.setText(Util.setAddMoneyDot(voLoginItem.ACCUM_POINT));
        }

        // 서버 연동 후 PointContent.ITEMS에 리스 항목 추가 작업
        PointContent.clearItem();
        requestPointList();
        // Set the adapter - 포인트 리스트 설정
//        List<PointContent.PointItem> ITEMS = new ArrayList<PointContent.PointItem>();
//
//        for(int i = 0; i < voLoginDataItem.size(); i++) {
//            PointContent.addItem(new PointContent.PointItem(
//                    i,
//                    "1",
//                    voLoginDataItem.get(i).FRI_NAME,
//                    voLoginDataItem.get(i).USE_SERVICE,
//                    voLoginDataItem.get(i).SAVE_DATE,
//                    voLoginDataItem.get(i).FRI_POINT
//            ));
//        }
//
//        View view = findViewById(R.id.pointHistoryList);
//        if (view instanceof RecyclerView) {
//            Context context = view.getContext();
//            RecyclerView recyclerView = (RecyclerView) view;
//
//            recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            recyclerView.setAdapter(new PointContentRecyclerViewAdapter(PointContent.ITEMS));
//        }
    }

    /**
     * 포인트 리스트 요청
     * PHONE_NUMBER     : 폰번호
     * MEMBER_NO        : 회원번호
     * REQUESTT_PAGE    : 요청페이지
     * REQUEST_NUM      : 요청갯수
     */
    private void requestPointList() {

        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("PHONE_NUMBER", voLoginItem.MEMBER_PHONE);       // 폰번호
        postParams.put("MEMBER_NO", voLoginItem.MEMBER_NO);             // 회원번호
        postParams.put("REQUEST_PAGE", String.valueOf(request_page));   // 요청페이지
        postParams.put("REQUEST_NUM", String.valueOf(request_offset));  // 요청갯수

        //프로그래스바 시작
        Util.showDialog(this);
        //사용내역 요청
        VolleyNetwork.getInstance(this).serverDataRequest(POINT_LIST_REQUEST, postParams, onPointListResponseListener);
    }

    VolleyNetwork.OnResponseListener onPointListResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String serverData) {
        /*
           {"point_list": [{"TOTAL":"5","CURRENT_PAGE":"1","CURRENT_NUM":"20"}],"data":[{"SEQ":"1","FRI_NAME":"김민정","USE_SERVICE":"스팀세차","SAVE_DATE":"20.02.26","FRI_POINT":"100"},{"SEQ":"2","FRI_NAME":"이하영","USE_SERVICE":"스팀세차","SAVE_DATE":"20.02.28","FRI_POINT":"120"},{}…]}
         */

            Gson gSon = new Gson();
            pointResult = gSon.fromJson(serverData, PointResult.class);

            voPointItem.TOTAL              = pointResult.point_list.get(0).TOTAL;
            voPointItem.CURRENT_PAGE       = pointResult.point_list.get(0).CURRENT_PAGE;
            voPointItem.CURRENT_NUM        = pointResult.point_list.get(0).CURRENT_NUM;

            voPointDataItem     = pointResult.data;

            List<PointContent.PointItem> ITEMS = new ArrayList<PointContent.PointItem>();

            for(int i = 0; i < voPointDataItem.size(); i++) {
                PointContent.addItem(new PointContent.PointItem(
                        i,
                        voPointDataItem.get(i).SEQ,
                        voPointDataItem.get(i).FRI_NAME,
                        voPointDataItem.get(i).USE_SERVICE,
                        voPointDataItem.get(i).SAVE_DATE,
                        voPointDataItem.get(i).FRI_POINT
                ));
            }

            //프로그래스바 종료
            Util.dismiss();

            // 서버 연동 후 UseContent.ITEMS에 리스 항목 추가 작업 확인
            // Set the adapter - 이용내역 리스트 설정
            if(PointContent.ITEMS.size() > 0) {
                View view = findViewById(R.id.pointHistoryList);
                LinearLayout view1 = findViewById(R.id.point_history_empty);

                view.setVisibility(View.VISIBLE);
                view1.setVisibility(View.GONE);

                if (view instanceof RecyclerView) {
                    Context context = view.getContext();
                    RecyclerView recyclerView = (RecyclerView) view;

                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(new PointContentRecyclerViewAdapter(PointContent.ITEMS));

                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                        }

                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                            super.onScrolled(recyclerView, dx, dy);

                            LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                            int totalItemCount = layoutManager.getItemCount();
                            int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition();

                            if (lastVisible >= totalItemCount - 1) {
                                int lastPageNum;
                                if( Integer.parseInt(voPointItem.TOTAL) % request_offset == 0 ) {
                                    lastPageNum = (int)Math.floor(Integer.parseInt(voPointItem.TOTAL)/request_offset);
                                }
                                else {
                                    lastPageNum = (int)Math.floor(Integer.parseInt(voPointItem.TOTAL)/request_offset) + 1;
                                }

                                if(lastPageNum > request_page)
                                {
                                    //다음 페이지 요청
                                    request_page+=1;
                                    requestPointList();
                                }
                            }

                        }
                    });
                }
            }else{
                // TODO - 등록차량 없을 때 화면 UI 추가
                View view = findViewById(R.id.pointHistoryList);
                view.setVisibility(View.GONE);

                LinearLayout view1 = findViewById(R.id.point_history_empty);
                view1.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onResponseFailListener(VolleyError it) {

        }
    };

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.btnPrev_layout:
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
