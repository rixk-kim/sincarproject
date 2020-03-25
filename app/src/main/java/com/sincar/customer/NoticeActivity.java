package com.sincar.customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sincar.customer.adapter.NoticeContentRecyclerViewAdapter;
import com.sincar.customer.adapter.UseContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.NoticeContent;
import com.sincar.customer.adapter.content.UseContent;
import com.sincar.customer.item.LoginResult;
import com.sincar.customer.item.NoticeDataItem;
import com.sincar.customer.item.NoticeResult;
import com.sincar.customer.network.DataObject;
import com.sincar.customer.network.JsonParser;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.DataParser;
import com.sincar.customer.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.sincar.customer.HWApplication.loginResult;
import static com.sincar.customer.HWApplication.voLoginData;
import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.HWApplication.voNoticeItem;
import static com.sincar.customer.common.Constants.LOGIN_REQUEST;
import static com.sincar.customer.HWApplication.noticeResult;

import static com.sincar.customer.HWApplication.voNoticeDataItem;

public class NoticeActivity extends AppCompatActivity implements View.OnClickListener {
    public static NoticeActivity _noticeActivity;
    private Context nContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        _noticeActivity = NoticeActivity.this;
        nContext = this;

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        findViewById(R.id.notice_btnPrev).setOnClickListener(this);


        // TODO - 서버 연동 후 PointContent.ITEMS에 리스 항목 추가 작업
        // TODO - 서버 연동 되면 requestNoticeList() 호출. 이후 NoticeContent 의 Dummy 아이템 추가 코드 삭제
        // Set the adapter - 포인트 리스트 설정
        View view = findViewById(R.id.noticeHistoryList);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new NoticeContentRecyclerViewAdapter(this, NoticeContent.ITEMS));
        }


    }

    /**
     * 공지 리스트 요청
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
        Util.showDialog(this);
        //사용내역 요청
        VolleyNetwork.getInstance(this).passwordChangeRequest(LOGIN_REQUEST, postParams, onResponseListener);
    }

    VolleyNetwork.OnResponseListener onResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String serverData) {
            /*
             {"notice": [{"TOTAL":"20","CURRENT_PAGE":"1","CURRENT_NUM":"20"}],
             "data":[{"SEQ":"1","TITLE":"1","REG_DATE":"2020","CONTENTS":"SIN..."},{},{}...]}
             */

            Gson gSon = new Gson();
            noticeResult = gSon.fromJson(serverData, NoticeResult.class);

            voNoticeItem.TOTAL              = noticeResult.notice.get(0).TOTAL;
            voNoticeItem.CURRENT_PAGE       = noticeResult.notice.get(0).CURRENT_PAGE;
            voNoticeItem.CURRENT_NUM        = noticeResult.notice.get(0).CURRENT_NUM;

            voNoticeDataItem     = noticeResult.DATA;

            List<NoticeContent.NoticeItem> ITEMS = new ArrayList<NoticeContent.NoticeItem>();

            for(int i = 0; i < voNoticeDataItem.size(); i++) {
                NoticeContent.addItem(new NoticeContent.NoticeItem(
                        i,
                        voNoticeDataItem.get(i).SEQ,
                        voNoticeDataItem.get(i).TITLE,
                        voNoticeDataItem.get(i).REG_DATE,
                        voNoticeDataItem.get(i).CONTENTS
                ));
            }

            //프로그래스바 종료
            Util.dismiss();

            // 서버 연동 후 UseContent.ITEMS에 리스 항목 추가 작업 확인
            // Set the adapter - 이용내역 리스트 설정
            if(NoticeContent.ITEMS.size() > 0) {
                View view = findViewById(R.id.noticeHistoryList);
                if (view instanceof RecyclerView) {
                    Context context = view.getContext();
                    RecyclerView recyclerView = (RecyclerView) view;

                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(new NoticeContentRecyclerViewAdapter(nContext, NoticeContent.ITEMS));
                }
            }else{
                // TODO - 공지사항 없을 때 화면 UI 추가
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


