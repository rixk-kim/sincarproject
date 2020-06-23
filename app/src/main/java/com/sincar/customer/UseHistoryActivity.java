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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.sincar.customer.adapter.UseContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.UseContent;
import com.sincar.customer.entity.UseDataEntity;
import com.sincar.customer.item.UseResult;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.Util;

import java.util.HashMap;

import static com.sincar.customer.HWApplication.useResult;
import static com.sincar.customer.HWApplication.voUseDataItem;
import static com.sincar.customer.HWApplication.voUseItem;
import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.common.Constants.USE_HISTORY_REQUEST;

/**
 * 이용내역
 */
public class UseHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    private MenuItem prevBottomNavigation;
    private Context uContext;
    public static UseDataEntity voUseData;

    private RecyclerView mRecyclerView;

    //페이지 처리
    private int request_page = 1;                           // 페이징변수. 초기 값은 0 이다.
    private final int request_offset = 20;                  // 한 페이지마다 로드할 데이터 갯수.
    private boolean mLockListView = false;          // 데이터 불러올때 중복안되게 하기위한 변수
//    private ProgressBar progressBar;                // 데이터 로딩중을 표시할 프로그레스바

    public static UseHistoryActivity _useHistoryActivity;
    private UseContentRecyclerViewAdapter mUseContentRecyclerViewAdapter;

    private TextView tvSelecte; //이용 내역 목록 선택 다이얼로그
    //렌탈 리스트 디테일용 커스텀 다이얼로그
    CustomDialoginUseHis customDialogInUseHis;
    Bundle dlgUseHisBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_history);

        _useHistoryActivity = UseHistoryActivity.this;  //이전 activity finish를 위한 변수 선언.
        uContext = this;

        voUseData = new UseDataEntity();

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    @SuppressLint("ResourceAsColor")
    private void init() {
        // TODO - 서버 연동 후 CardContent.ITEMS에 리스 항목 추가 작업
        // Set the adapter - 이용내역 리스트 설정
        // 실서버 연동시
        UseContent.clearItem(); //초기화
        requestUseHistory();

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

        tvSelecte = (TextView)findViewById(R.id.select_dialog_useHis);
        tvSelecte.setOnClickListener(this);
    }

    /**
     * 사용내역 요청
     * MEMBER_NO        : 회원번호
     * REQUESTT_PAGE    : 요청페이지
     * REQUEST_NUM      : 요청갯수
     */
    private void requestUseHistory() {
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("MEMBER_NO", voLoginItem.MEMBER_NO);                 // 회원번호
        postParams.put("REQUESTT_PAGE", String.valueOf(request_page));      // 요청페이지
        postParams.put("REQUEST_NUM", String.valueOf(request_offset));      // 요청갯수

        //프로그래스바 시작
        Util.showDialog(this);
        //사용내역 요청
        VolleyNetwork.getInstance(this).serverDataRequest(USE_HISTORY_REQUEST, postParams, onResponseListener);
    }

    VolleyNetwork.OnResponseListener onResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String serverData) {
            /*
             {"data":
             [{"SEQ":"1","RESERVE_STATUS":"0","RESERVE_TIME":"2020-12-22 14:00",...},
             {"SEQ":"1","RESERVE_STATUS":"0","RESERVE_TIME":"2020-12-22 14:00",...}..]}
             */
//            serverData = "";
            //System.out.println("[spirit] it : "  + serverData);
            /*
             {"use_list":
             [{"TOTAL":"100","CURRENT_PAGE":"1","CURRENT_NUM":"20"}],
             "data": [{"SEQ":"1","RESERVE_STATUS":"0","RESERVE_TIME":"2020-12-22 14:00",...},{"SEQ":"1","RESERVE_STATUS":"0","RESERVE_TIME":"2020-12-22 14:00",...}..]}
             */
 //           serverData = "{\"use_list\":[{\"TOTAL\":\"30\",\"CURRENT_PAGE\":\"1\",\"CURRENT_NUM\":\"20\"}],\"data\":[{\"SEQ\":\"D4DSA3AS6D\",\"RESERVE_STATUS\":\"0\",\"RESERVE_TIME\":\"2020-03-26 10:32\",\"CANCEL_TIME\":\"2020-03-26 10:32\",\"WASH_ADDRESS\":\"서울특별시 송파구 석촌호수로 274 (지상)\",\"AGENT\":\"\",\"USE_PAY\":\"42000\",\"RESERVE_NAME\":\"홍길동\",\"RESERVE_PHONE\":\"01000000000\",\"COMMON_PAY\":\"45000\",\"COUPONE\":\"3000\",\"CHARGE_INFO\":\"케이뱅크 \",\"CAR_MODEL\":\"아반떼((준중형))\",\"CAR_NUMBER\":\"12가1234\"},{\"SEQ\":\"ASDGDFDS12\",\"RESERVE_STATUS\":\"0\",\"RESERVE_TIME\":\"2020-03-26 10:32\",\"CANCEL_TIME\":\"2020-03-26 10:32\",\"WASH_ADDRESS\":\"서울특별시 송파구 석촌호수로 274 (지상)\",\"AGENT\":\"\",\"USE_PAY\":\"42000\",\"RESERVE_NAME\":\"이순신\",\"RESERVE_PHONE\":\"01000000000\",\"COMMON_PAY\":\"45000\",\"COUPONE\":\"3000\",\"CHARGE_INFO\":\"케이뱅크 \",\"CAR_MODEL\":\"아반떼((준중형))\",\"CAR_NUMBER\":\"12가1234\"},{\"SEQ\":\"ASDGDAAS42\",\"RESERVE_STATUS\":\"0\",\"RESERVE_TIME\":\"2020-03-26 10:32\",\"CANCEL_TIME\":\"2020-03-26 10:32\",\"WASH_ADDRESS\":\"서울특별시 송파구 석촌호수로 274 (지상)\",\"AGENT\":\"\",\"USE_PAY\":\"42000\",\"RESERVE_NAME\":\"박철순\",\"RESERVE_PHONE\":\"01000000000\",\"COMMON_PAY\":\"45000\",\"COUPONE\":\"3000\",\"CHARGE_INFO\":\"케이뱅크 \",\"CAR_MODEL\":\"아반떼((준중형))\",\"CAR_NUMBER\":\"12가1234\"},{\"SEQ\":\"BSCF1234AS\",\"RESERVE_STATUS\":\"0\",\"RESERVE_TIME\":\"2020-03-26 10:32\",\"CANCEL_TIME\":\"2020-03-26 10:32\",\"WASH_ADDRESS\":\"서울특별시 송파구 석촌호수로 274 (지상)\",\"AGENT\":\"\",\"USE_PAY\":\"42000\",\"RESERVE_NAME\":\"장보고\",\"RESERVE_PHONE\":\"01000000000\",\"COMMON_PAY\":\"45000\",\"COUPONE\":\"3000\",\"CHARGE_INFO\":\"케이뱅크 \",\"CAR_MODEL\":\"아반떼((준중형))\",\"CAR_NUMBER\":\"12가1234\"},{\"SEQ\":\"ASDVG12345\",\"RESERVE_STATUS\":\"0\",\"RESERVE_TIME\":\"2020-03-26 10:32\",\"CANCEL_TIME\":\"2020-03-26 10:32\",\"WASH_ADDRESS\":\"서울특별시 송파구 석촌호수로 274 (지상)\",\"AGENT\":\"\",\"USE_PAY\":\"42000\",\"RESERVE_NAME\":\"박상원\",\"RESERVE_PHONE\":\"01000000000\",\"COMMON_PAY\":\"45000\",\"COUPONE\":\"3000\",\"CHARGE_INFO\":\"케이뱅크 \",\"CAR_MODEL\":\"아반떼((준중형))\",\"CAR_NUMBER\":\"12가1234\"},{\"SEQ\":\"ASDVG12322\",\"RESERVE_STATUS\":\"0\",\"RESERVE_TIME\":\"2020-03-26 10:32\",\"CANCEL_TIME\":\"2020-03-26 10:32\",\"WASH_ADDRESS\":\"서울특별시 송파구 석촌호수로 274 (지상)\",\"AGENT\":\"\",\"USE_PAY\":\"42000\",\"RESERVE_NAME\":\"김형욱\",\"RESERVE_PHONE\":\"01000000000\",\"COMMON_PAY\":\"45000\",\"COUPONE\":\"3000\",\"CHARGE_INFO\":\"케이뱅크 \",\"CAR_MODEL\":\"아반떼((준중형))\",\"CAR_NUMBER\":\"12가1234\"},{\"SEQ\":\"ASDV232322\",\"RESERVE_STATUS\":\"0\",\"RESERVE_TIME\":\"2020-03-26 10:32\",\"CANCEL_TIME\":\"2020-03-26 10:32\",\"WASH_ADDRESS\":\"서울특별시 송파구 석촌호수로 274 (지상)\",\"AGENT\":\"\",\"USE_PAY\":\"42000\",\"RESERVE_NAME\":\"최창민\",\"RESERVE_PHONE\":\"01000000000\",\"COMMON_PAY\":\"45000\",\"COUPONE\":\"3000\",\"CHARGE_INFO\":\"케이뱅크 \",\"CAR_MODEL\":\"아반떼((준중형))\",\"CAR_NUMBER\":\"12가1234\"},{\"SEQ\":\"ASDVAA2322\",\"RESERVE_STATUS\":\"0\",\"RESERVE_TIME\":\"2020-03-26 10:32\",\"CANCEL_TIME\":\"2020-03-26 10:32\",\"WASH_ADDRESS\":\"서울특별시 송파구 석촌호수로 274 (지상)\",\"AGENT\":\"\",\"USE_PAY\":\"42000\",\"RESERVE_NAME\":\"박상철\",\"RESERVE_PHONE\":\"01000000000\",\"COMMON_PAY\":\"45000\",\"COUPONE\":\"3000\",\"CHARGE_INFO\":\"케이뱅크 \",\"CAR_MODEL\":\"아반떼((준중형))\",\"CAR_NUMBER\":\"12가1234\"},{\"SEQ\":\"ASDVBB2322\",\"RESERVE_STATUS\":\"0\",\"RESERVE_TIME\":\"2020-03-26 10:32\",\"CANCEL_TIME\":\"2020-03-26 10:32\",\"WASH_ADDRESS\":\"서울특별시 송파구 석촌호수로 274 (지상)\",\"AGENT\":\"\",\"USE_PAY\":\"42000\",\"RESERVE_NAME\":\"김인식\",\"RESERVE_PHONE\":\"01000000000\",\"COMMON_PAY\":\"45000\",\"COUPONE\":\"3000\",\"CHARGE_INFO\":\"케이뱅크 \",\"CAR_MODEL\":\"아반떼((준중형))\",\"CAR_NUMBER\":\"12가1234\"},{\"SEQ\":\"ASDVCC2322\",\"RESERVE_STATUS\":\"0\",\"RESERVE_TIME\":\"2020-03-26 10:32\",\"CANCEL_TIME\":\"2020-03-26 10:32\",\"WASH_ADDRESS\":\"서울특별시 송파구 석촌호수로 274 (지상)\",\"AGENT\":\"\",\"USE_PAY\":\"42000\",\"RESERVE_NAME\":\"강남\",\"RESERVE_PHONE\":\"01000000000\",\"COMMON_PAY\":\"45000\",\"COUPONE\":\"3000\",\"CHARGE_INFO\":\"케이뱅크 \",\"CAR_MODEL\":\"아반떼((준중형))\",\"CAR_NUMBER\":\"12가1234\"},{\"SEQ\":\"ASDVDD2322\",\"RESERVE_STATUS\":\"0\",\"RESERVE_TIME\":\"2020-03-26 10:32\",\"CANCEL_TIME\":\"2020-03-26 10:32\",\"WASH_ADDRESS\":\"서울특별시 송파구 석촌호수로 274 (지상)\",\"AGENT\":\"\",\"USE_PAY\":\"42000\",\"RESERVE_NAME\":\"김환희\",\"RESERVE_PHONE\":\"01000000000\",\"COMMON_PAY\":\"45000\",\"COUPONE\":\"3000\",\"CHARGE_INFO\":\"케이뱅크 \",\"CAR_MODEL\":\"아반떼((준중형))\",\"CAR_NUMBER\":\"12가1234\"},{\"SEQ\":\"AADVDD2322\",\"RESERVE_STATUS\":\"0\",\"RESERVE_TIME\":\"2020-03-26 10:32\",\"CANCEL_TIME\":\"2020-03-26 10:32\",\"WASH_ADDRESS\":\"서울특별시 송파구 석촌호수로 274 (지상)\",\"AGENT\":\"\",\"USE_PAY\":\"42000\",\"RESERVE_NAME\":\"강준식\",\"RESERVE_PHONE\":\"01000000000\",\"COMMON_PAY\":\"45000\",\"COUPONE\":\"3000\",\"CHARGE_INFO\":\"케이뱅크 \",\"CAR_MODEL\":\"아반떼((준중형))\",\"CAR_NUMBER\":\"12가1234\"},{\"SEQ\":\"AADVTT2322\",\"RESERVE_STATUS\":\"0\",\"RESERVE_TIME\":\"2020-03-26 10:32\",\"CANCEL_TIME\":\"2020-03-26 10:32\",\"WASH_ADDRESS\":\"서울특별시 송파구 석촌호수로 274 (지상)\",\"AGENT\":\"\",\"USE_PAY\":\"42000\",\"RESERVE_NAME\":\"임형철\",\"RESERVE_PHONE\":\"01000000000\",\"COMMON_PAY\":\"45000\",\"COUPONE\":\"3000\",\"CHARGE_INFO\":\"케이뱅크 \",\"CAR_MODEL\":\"아반떼((준중형))\",\"CAR_NUMBER\":\"12가1234\"},{\"SEQ\":\"AADVRR2322\",\"RESERVE_STATUS\":\"0\",\"RESERVE_TIME\":\"2020-03-26 10:32\",\"CANCEL_TIME\":\"2020-03-26 10:32\",\"WASH_ADDRESS\":\"서울특별시 송파구 석촌호수로 274 (지상)\",\"AGENT\":\"\",\"USE_PAY\":\"42000\",\"RESERVE_NAME\":\"김창식\",\"RESERVE_PHONE\":\"01000000000\",\"COMMON_PAY\":\"45000\",\"COUPONE\":\"3000\",\"CHARGE_INFO\":\"케이뱅크 \",\"CAR_MODEL\":\"아반떼((준중형))\",\"CAR_NUMBER\":\"12가1234\"},{\"SEQ\":\"AACCRR2322\",\"RESERVE_STATUS\":\"0\",\"RESERVE_TIME\":\"2020-03-26 10:32\",\"CANCEL_TIME\":\"2020-03-26 10:32\",\"WASH_ADDRESS\":\"서울특별시 송파구 석촌호수로 274 (지상)\",\"AGENT\":\"\",\"USE_PAY\":\"42000\",\"RESERVE_NAME\":\"박홍신\",\"RESERVE_PHONE\":\"01000000000\",\"COMMON_PAY\":\"45000\",\"COUPONE\":\"3000\",\"CHARGE_INFO\":\"케이뱅크 \",\"CAR_MODEL\":\"아반떼((준중형))\",\"CAR_NUMBER\":\"12가1234\"},{\"SEQ\":\"AACCRR6622\",\"RESERVE_STATUS\":\"0\",\"RESERVE_TIME\":\"2020-03-26 10:32\",\"CANCEL_TIME\":\"2020-03-26 10:32\",\"WASH_ADDRESS\":\"서울특별시 송파구 석촌호수로 274 (지상)\",\"AGENT\":\"\",\"USE_PAY\":\"42000\",\"RESERVE_NAME\":\"김일이\",\"RESERVE_PHONE\":\"01000000000\",\"COMMON_PAY\":\"45000\",\"COUPONE\":\"3000\",\"CHARGE_INFO\":\"케이뱅크 \",\"CAR_MODEL\":\"아반떼((준중형))\",\"CAR_NUMBER\":\"12가1234\"},{\"SEQ\":\"AACCLL6622\",\"RESERVE_STATUS\":\"0\",\"RESERVE_TIME\":\"2020-03-26 10:32\",\"CANCEL_TIME\":\"2020-03-26 10:32\",\"WASH_ADDRESS\":\"서울특별시 송파구 석촌호수로 274 (지상)\",\"AGENT\":\"\",\"USE_PAY\":\"42000\",\"RESERVE_NAME\":\"김삼사\",\"RESERVE_PHONE\":\"01000000000\",\"COMMON_PAY\":\"45000\",\"COUPONE\":\"3000\",\"CHARGE_INFO\":\"케이뱅크 \",\"CAR_MODEL\":\"아반떼((준중형))\",\"CAR_NUMBER\":\"12가1234\"},{\"SEQ\":\"AACCLL6622\",\"RESERVE_STATUS\":\"0\",\"RESERVE_TIME\":\"2020-03-26 10:32\",\"CANCEL_TIME\":\"2020-03-26 10:32\",\"WASH_ADDRESS\":\"서울특별시 송파구 석촌호수로 274 (지상)\",\"AGENT\":\"\",\"USE_PAY\":\"42000\",\"RESERVE_NAME\":\"김오육\",\"RESERVE_PHONE\":\"01000000000\",\"COMMON_PAY\":\"45000\",\"COUPONE\":\"3000\",\"CHARGE_INFO\":\"케이뱅크 \",\"CAR_MODEL\":\"아반떼((준중형))\",\"CAR_NUMBER\":\"12가1234\"},{\"SEQ\":\"AACCII6622\",\"RESERVE_STATUS\":\"0\",\"RESERVE_TIME\":\"2020-03-26 10:32\",\"CANCEL_TIME\":\"2020-03-26 10:32\",\"WASH_ADDRESS\":\"서울특별시 송파구 석촌호수로 274 (지상)\",\"AGENT\":\"\",\"USE_PAY\":\"42000\",\"RESERVE_NAME\":\"김칠팔\",\"RESERVE_PHONE\":\"01000000000\",\"COMMON_PAY\":\"45000\",\"COUPONE\":\"3000\",\"CHARGE_INFO\":\"케이뱅크 \",\"CAR_MODEL\":\"아반떼((준중형))\",\"CAR_NUMBER\":\"12가1234\"},{\"SEQ\":\"AACCPP6622\",\"RESERVE_STATUS\":\"0\",\"RESERVE_TIME\":\"2020-03-26 10:32\",\"CANCEL_TIME\":\"2020-03-26 10:32\",\"WASH_ADDRESS\":\"서울특별시 송파구 석촌호수로 274 (지상)\",\"AGENT\":\"\",\"USE_PAY\":\"42000\",\"RESERVE_NAME\":\"김구십\",\"RESERVE_PHONE\":\"01000000000\",\"COMMON_PAY\":\"45000\",\"COUPONE\":\"3000\",\"CHARGE_INFO\":\"케이뱅크 \",\"CAR_MODEL\":\"아반떼((준중형))\",\"CAR_NUMBER\":\"12가1234\"}]}";

            Gson gSon = new Gson();
            useResult = gSon.fromJson(serverData, UseResult.class);

            voUseItem.TOTAL              = useResult.use_list.get(0).TOTAL;
            voUseItem.CURRENT_PAGE       = useResult.use_list.get(0).CURRENT_PAGE;
            voUseItem.CURRENT_NUM        = useResult.use_list.get(0).CURRENT_NUM;

            voUseDataItem     = useResult.data;

 //           List<UseContent.UseItem> ITEMS = new ArrayList<UseContent.UseItem>();

            for(int i = 0; i < voUseDataItem.size(); i++) {
                UseContent.addItem(new UseContent.UseItem(
                        i,
                        voUseDataItem.get(i).SEQ,
                        voUseDataItem.get(i).RESERVE_STATUS,
                        voUseDataItem.get(i).RESERVE_TIME,
                        voUseDataItem.get(i).CANCEL_TIME,
                        voUseDataItem.get(i).WASH_ADDRESS,
                        voUseDataItem.get(i).AGENT,
                        voUseDataItem.get(i).USE_PAY,
                        voUseDataItem.get(i).RESERVE_PHONE,

                        voUseDataItem.get(i).RESERVE_NAME,
                        voUseDataItem.get(i).COMMON_PAY,
                        voUseDataItem.get(i).COUPONE,
                        voUseDataItem.get(i).CHARGE_INFO,
                        voUseDataItem.get(i).CAR_MODEL,
                        voUseDataItem.get(i).CAR_NUMBER,
                        voUseDataItem.get(i).POINT_PAY,
                        voUseDataItem.get(i).COUPONE_SEQ,
                        voUseDataItem.get(i).AGENT_SEQ,
                        voUseDataItem.get(i).ADD_SERVICE,
                        voUseDataItem.get(i).CAR_COMPANY,
                        voUseDataItem.get(i).WASH_PLACE
                ));
            }

            //프로그래스바 종료
            Util.dismiss();
            // TODO - 서버 연동 후 UseContent.ITEMS에 리스 항목 추가 작업 확인
            // Set the adapter - 이용내역 리스트 설정
            if(UseContent.ITEMS.size() > 0) {
                View view = findViewById(R.id.useHistoryList);
                LinearLayout view1 = findViewById(R.id.use_history_empty);

                view.setVisibility(View.VISIBLE);
                view1.setVisibility(View.GONE);

                if (view instanceof RecyclerView) {
                    Context context = view.getContext();
                    RecyclerView recyclerView = (RecyclerView) view;

                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(new UseContentRecyclerViewAdapter(uContext, UseContent.ITEMS, _useHistoryActivity));

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
                                if( Integer.parseInt(voUseItem.TOTAL) % request_offset == 0 ) {
                                    lastPageNum = (int)Math.floor(Integer.parseInt(voUseItem.TOTAL)/request_offset);
                                }
                                else {
                                    lastPageNum = (int)Math.floor(Integer.parseInt(voUseItem.TOTAL)/request_offset) + 1;
                                }

                                if(lastPageNum > request_page)
                                {
                                    request_page+=1;
                                    requestUseHistory();
//                                    //다음 페이지 요청
//                                    for (int i = 1; i <= 5; i++) {
//                                        UseContent.addItem(UseContent.createDummyItem(i + ((request_page*5) - 1)));
//                                    }
                                }
                            }

                        }
                    });

                }
            }else{
                View view = findViewById(R.id.useHistoryList);
                view.setVisibility(View.GONE);

                LinearLayout view1 = findViewById(R.id.use_history_empty);
                view1.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onResponseFailListener(VolleyError it) {
            //프로그래스바 종료
            Util.dismiss();
        }
    };

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

        if(v.getId() == R.id.select_dialog_useHis) {
            customDialogInUseHis = CustomDialoginUseHis.getInstance();
            dlgUseHisBundle = new Bundle();

            customDialogInUseHis.show(getSupportFragmentManager(), "customDialogUseHis");
        }
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