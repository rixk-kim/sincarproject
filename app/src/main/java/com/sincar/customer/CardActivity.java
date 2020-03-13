package com.sincar.customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sincar.customer.adapter.CarContentRecyclerViewAdapter;
import com.sincar.customer.adapter.CardContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.CarContent;
import com.sincar.customer.adapter.content.CardContent;
import com.sincar.customer.adapter.content.NoticeContent;
import com.sincar.customer.item.CarResult;
import com.sincar.customer.item.CardResult;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.sincar.customer.HWApplication.cardResult;
import static com.sincar.customer.HWApplication.voCardDataItem;
import static com.sincar.customer.HWApplication.voCardItem;
import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.common.Constants.LOGIN_REQUEST;

public class CardActivity extends AppCompatActivity implements View.OnClickListener {
    private Context cardContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        cardContext = this;

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    @SuppressLint("ResourceAsColor")
    private void init() {
        findViewById(R.id.card_btnPrev).setOnClickListener(this);
        findViewById(R.id.card_btnNext).setOnClickListener(this);


        // TODO - 서버 연동 후 CardContent.ITEMS에 리스 항목 추가 작업
        // Set the adapter - 포인트 리스트 설정
        View view = findViewById(R.id.cardHistoryList);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new CardContentRecyclerViewAdapter(this, CardContent.ITEMS));
        }
    }

    /**
     * 등록카드 리스트 요청
     * PHONE_NEMBER     : 폰번호
     * MEMBER_NO        : 회원번호
     * REQUESTT_PAGE    : 요청페이지
     * REQUEST_NUM      : 요청갯수
     */
    private void requestNoticeList() {
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("PHONE_NEMBER", voLoginItem.MEMBER_PHONE);   // 폰번호
        postParams.put("MEMBER_NO", voLoginItem.MEMBER_NO);         // 회원번호
        postParams.put("REQUESTT_PAGE", "1");                       // 요청페이지
        postParams.put("REQUEST_NUM", "20");                        // 요청갯수

        //프로그래스바 시작
        Util.showDialog();
        //사용내역 요청
        VolleyNetwork.getInstance(this).passwordChangeRequest(LOGIN_REQUEST, postParams, onResponseListener);
    }

    VolleyNetwork.OnResponseListener onResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String serverData) {
            /*
                {"card_list": [{"TOTAL":"5","CURRENT_PAGE":"1","CURRENT_NUM":"1"}],
                "data":[{"CARD_SEQ":"1","CARD_NAME":"BC카드","CARD_NUMBER":"1234-5678-1234-4321","CARD_SELECTED":"false"},{},{}…]}
             */

            Gson gSon = new Gson();
            cardResult = gSon.fromJson(serverData, CardResult.class);

            voCardItem.TOTAL              = cardResult.card_list.get(0).TOTAL;
            voCardItem.CURRENT_PAGE       = cardResult.card_list.get(0).CURRENT_PAGE;
            voCardItem.CURRENT_NUM        = cardResult.card_list.get(0).CURRENT_NUM;

            voCardDataItem     = cardResult.DATA;

            List<CardContent.CardItem> ITEMS = new ArrayList<CardContent.CardItem>();

            for(int i = 0; i < voCardDataItem.size(); i++) {
                CardContent.addItem(new CardContent.CardItem(
                        i,
                        voCardDataItem.get(i).CARD_SEQ,
                        voCardDataItem.get(i).CARD_NAME,
                        voCardDataItem.get(i).CARD_NUMBER,
                        false
                ));
            }

            //프로그래스바 종료
            Util.dismiss();

            // 서버 연동 후 UseContent.ITEMS에 리스 항목 추가 작업 확인
            // Set the adapter - 이용내역 리스트 설정
            if(CardContent.ITEMS.size() > 0) {
                View view = findViewById(R.id.cardHistoryList);
                if (view instanceof RecyclerView) {
                    Context context = view.getContext();
                    RecyclerView recyclerView = (RecyclerView) view;

                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(new CardContentRecyclerViewAdapter(cardContext, CardContent.ITEMS));
                }
            }else{
                // TODO - 등록차량 없을 때 화면 UI 추가
//                LinearLayout view = findViewById(R.id.use_history_empty);
//                view.setVisibility(View.VISIBLE);
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
            case R.id.card_btnPrev:
                //  내정보
                intent = new Intent(this, MyProfileSettingsActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.card_btnNext:
                //  카드등록
                intent = new Intent(this, CardRegisterActivity.class);
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


