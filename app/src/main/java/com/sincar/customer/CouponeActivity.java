package com.sincar.customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sincar.customer.adapter.CouponeContentRecyclerViewAdapter;
import com.sincar.customer.adapter.NoticeContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.CouponeContent;
import com.sincar.customer.adapter.content.NoticeContent;
import com.sincar.customer.item.CouponeResult;
import com.sincar.customer.item.NoticeResult;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.sincar.customer.HWApplication.couponeResult;
import static com.sincar.customer.HWApplication.voCouponeItem;
import static com.sincar.customer.HWApplication.voCouponeDataItem;

import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.common.Constants.LOGIN_REQUEST;

public class CouponeActivity extends AppCompatActivity implements View.OnClickListener {
    private Context cContext;
    private String path;
    private LinearLayout couponeLayout;
    private CouponeContentRecyclerViewAdapter mCouponeContentRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupone);
        cContext = this;

        Intent intent = getIntent(); /*데이터 수신*/
        path    = intent.getExtras().getString("path");    /*String형*/

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        findViewById(R.id.coupone_btnPrev).setOnClickListener(this);
        findViewById(R.id.coupone_confirm_btn).setOnClickListener(this);
        couponeLayout = (LinearLayout)findViewById(R.id.coupone_confirm_layout);

        //진입경로에 따라 하단 버튼 활성화
        if("payment".equals(path)){
            couponeLayout.setVisibility(View.VISIBLE);
        }else{
            couponeLayout.setVisibility(View.GONE);
            RecyclerView rV = (RecyclerView)findViewById(R.id.couponeHistoryList);

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)rV.getLayoutParams();
            layoutParams.bottomMargin = 0;
            rV.setLayoutParams(layoutParams);
        }

        // TODO - 서버 연동 후 PointContent.ITEMS에 리스 항목 추가 작업
        // Set the adapter - 포인트 리스트 설정
        View view = findViewById(R.id.couponeHistoryList);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            mCouponeContentRecyclerViewAdapter = new CouponeContentRecyclerViewAdapter(this, CouponeContent.ITEMS);
            recyclerView.setAdapter(mCouponeContentRecyclerViewAdapter);
        }
    }

    /**
     * 쿠폰 리스트 요청
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
               {"coupone_list": [{"TOTAL":"5","CURRENT_PAGE":"1","CURRENT_NUM":"20"}],
               "data":[{"COUPONE_SEQ":"1","COUPONE_TITLE":"2000원 할인 쿠폰","COUPONE_CONTENT":"회원 가입을 축하해요!","COUPONE_DATE":"~2019.12.31","COUPONE_YN":"N"},{},{}…]}
             */

            Gson gSon = new Gson();
            couponeResult = gSon.fromJson(serverData, CouponeResult.class);

            voCouponeItem.TOTAL              = couponeResult.coupone_list.get(0).TOTAL;
            voCouponeItem.CURRENT_PAGE       = couponeResult.coupone_list.get(0).CURRENT_PAGE;
            voCouponeItem.CURRENT_NUM        = couponeResult.coupone_list.get(0).CURRENT_NUM;

            voCouponeDataItem     = couponeResult.DATA;

            List<CouponeContent.CouponeItem> ITEMS = new ArrayList<CouponeContent.CouponeItem>();

            CouponeContent.clearItem(); //초기화

            for(int i = 0; i < voCouponeDataItem.size(); i++) {
                CouponeContent.addItem(new CouponeContent.CouponeItem(
                        i,
                        voCouponeDataItem.get(i).COUPONE_SEQ,
                        voCouponeDataItem.get(i).COUPONE_PAY,
                        voCouponeDataItem.get(i).COUPONE_TITLE,
                        voCouponeDataItem.get(i).COUPONE_DATE,
                        voCouponeDataItem.get(i).COUPONE_CONTENT,
                        voCouponeDataItem.get(i).COUPONE_YN,
                        false
                ));
            }

            //프로그래스바 종료
            Util.dismiss();

            // 서버 연동 후 UseContent.ITEMS에 리스 항목 추가 작업 확인
            // Set the adapter - 이용내역 리스트 설정
            if(CouponeContent.ITEMS.size() > 0) {
                View view = findViewById(R.id.couponeHistoryList);
                if (view instanceof RecyclerView) {
                    Context context = view.getContext();
                    RecyclerView recyclerView = (RecyclerView) view;

                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    mCouponeContentRecyclerViewAdapter = new CouponeContentRecyclerViewAdapter(cContext, CouponeContent.ITEMS);
                    recyclerView.setAdapter(mCouponeContentRecyclerViewAdapter);
                    //recyclerView.setAdapter(new CouponeContentRecyclerViewAdapter(cContext, CouponeContent.ITEMS));
                }
            }else{
                // TODO - 쿠폰 없을 때 화면 UI 추가
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
            case R.id.coupone_btnPrev:
            case R.id.coupone_confirm_btn:
                if("payment".equals(path)){
                    intent = new Intent(this, PaymentActivity.class);
                    intent.putExtra("coupone_seq", mCouponeContentRecyclerViewAdapter.getCouponeSeq());
                    intent.putExtra("coupone_pay", mCouponeContentRecyclerViewAdapter.getCouponePay());
                    setResult(RESULT_OK, intent);
                    finish();

                }else {
                    //  내정보
                    intent = new Intent(this, MyProfileSettingsActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent;
        if("payment".equals(path)){
            intent = new Intent(this, PaymentActivity.class);
            intent.putExtra("coupone_seq", mCouponeContentRecyclerViewAdapter.getCouponeSeq());
            setResult(RESULT_OK, intent);
            finish();
        }else{
            intent = new Intent(this, MyProfileSettingsActivity.class);
            startActivity(intent);

            finish();
        }
    }
}


