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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.sincar.customer.adapter.UseContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.UseContent;
import com.sincar.customer.entity.LoginDataEntity;
import com.sincar.customer.entity.UseDataEntity;
import com.sincar.customer.item.UseResult;
import com.sincar.customer.network.DataObject;
import com.sincar.customer.network.JsonParser;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.DataParser;
import com.sincar.customer.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.sincar.customer.HWApplication.useResult;
import static com.sincar.customer.HWApplication.voUseDataItem;
import static com.sincar.customer.HWApplication.voUseItem;
import static com.sincar.customer.HWApplication.voLoginData;
import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.adapter.content.UseContent.clearItem;
import static com.sincar.customer.common.Constants.LOGIN_REQUEST;

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
    private final int request_offset = 5;                  // 한 페이지마다 로드할 데이터 갯수.
    private boolean mLockListView = false;          // 데이터 불러올때 중복안되게 하기위한 변수
//    private ProgressBar progressBar;                // 데이터 로딩중을 표시할 프로그레스바

    public static UseHistoryActivity _useHistoryActivity;
    private UseContentRecyclerViewAdapter mUseContentRecyclerViewAdapter;

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
        // requestUseHistory();

         if(UseContent.ITEMS.size() > 0) {
            View view = findViewById(R.id.useHistoryList);
            view.setVisibility(View.VISIBLE);
            if (view instanceof RecyclerView) {
                Context context = view.getContext();
                mRecyclerView = (RecyclerView) view;

                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                mUseContentRecyclerViewAdapter = new UseContentRecyclerViewAdapter(this, UseContent.ITEMS, _useHistoryActivity);
                mRecyclerView.setAdapter(mUseContentRecyclerViewAdapter);

                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                        System.out.println("[spirit] lastVisibled : " + lastVisible );
                        System.out.println("[spirit] lastVisibled : " + totalItemCount );

                        if (lastVisible >= totalItemCount - 1) {
                            int lastPageNum;
                            if( 102 % request_offset == 0 ) {
                                lastPageNum = (int)Math.floor(102/request_offset);
                            }
                            else {
                                lastPageNum = (int)Math.floor(102/request_offset) + 1;
                            }
//                            Toast.makeText(getApplicationContext(), "lastVisibled " , Toast.LENGTH_SHORT).show();

                            System.out.println("[spirit] lastPageNum : " + lastPageNum );
                            System.out.println("[spirit] request_page : " + request_page );

                            if(lastPageNum > request_page)
                            {
                                //다음 페이지 요청
                                request_page+=1;
                                int start_num = ((request_page - 1) * 5);
                                for (int i = start_num; i <= (start_num + 5); i++) {
                                    UseContent.addItem(UseContent.createDummyItem(i));

                                    System.out.println("[spirit] addItem : [" + i + "]" );
                                }

                                mUseContentRecyclerViewAdapter.viewRenew();
                                //UseContent.addItem(UseContent.createDummyItem((request_page*1) - 1));
                            }
                        }

                    }
                });
            }
        }else{
             LinearLayout view = findViewById(R.id.use_history_empty);
             view.setVisibility(View.VISIBLE);
        }
        //////////////////////////////////////////////////////////////////////////////

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
    }

    /**
     * 사용내역 요청
     * MEMBER_NO        : 회원번호
     * REQUESTT_PAGE    : 요청페이지
     * REQUEST_NUM      : 요청갯수
     */
    private void requestUseHistory() {
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("MEMBER_NO", voLoginItem.MEMBER_NO);     // 회원번호
        postParams.put("REQUESTT_PAGE", String.valueOf(request_page));    // 요청페이지
        postParams.put("REQUEST_NUM", String.valueOf(request_offset));                    // 요청갯수

        //프로그래스바 시작
        Util.showDialog(this);
        //사용내역 요청
        VolleyNetwork.getInstance(this).passwordChangeRequest(LOGIN_REQUEST, postParams, onResponseListener);
    }

    VolleyNetwork.OnResponseListener onResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String serverData) {
            /*
             {"data":
             [{"SEQ":"1","RESERVE_STATUS":"0","RESERVE_TIME":"2020-12-22 14:00",...},
             {"SEQ":"1","RESERVE_STATUS":"0","RESERVE_TIME":"2020-12-22 14:00",...}..]}
             */
            serverData = "";
            //System.out.println("[spirit] it : "  + serverData);

            Gson gSon = new Gson();
            useResult = gSon.fromJson(serverData, UseResult.class);

            voUseItem.TOTAL              = useResult.use_list.get(0).TOTAL;
            voUseItem.CURRENT_PAGE       = useResult.use_list.get(0).CURRENT_PAGE;
            voUseItem.CURRENT_NUM        = useResult.use_list.get(0).CURRENT_NUM;

            voUseDataItem     = useResult.DATA;

            List<UseContent.UseItem> ITEMS = new ArrayList<UseContent.UseItem>();

            for(int i = 0; i < voUseDataItem.size(); i++) {
                UseContent.addItem(new UseContent.UseItem(
                        i,
                        voUseDataItem.get(i).SEQ,
                        voUseDataItem.get(i).RESERVE_TIME,
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
                        voUseDataItem.get(i).CAR_NUMBER
                ));
            }


//            ArrayList<DataObject> data = JsonParser.getData(serverData);
//
//            // 로그인 정보
//            DataObject useListItem = DataParser.getFromParamtoItem(data, "use_list");
//            voUseData.setTotalPage(useListItem.getValue("TOTAL"));      //총페이지
//            voUseData.setCurrentPage(useListItem.getValue("TOTAL"));    //현재페이지
//            voUseData.setCurrentNum(useListItem.getValue("TOTAL"));     //현재갯수
//
//            ArrayList<DataObject> use_data_item = DataParser.getFromParamtoArray(data, "data");
//
//            List<UseContent.UseItem> ITEMS = new ArrayList<UseContent.UseItem>();
//            for(int i = 0; i < use_data_item.size(); i++) {
//                UseContent.addItem(new UseContent.UseItem(
//                        i,
//                        use_data_item.get(i).getValue("SEQ"),
//                        use_data_item.get(i).getValue("RESERVE_STATUS"),
//                        use_data_item.get(i).getValue("RESERVE_TIME"),
//                        use_data_item.get(i).getValue("CANCEL_TIME"),
//                        use_data_item.get(i).getValue("WASH_ADDRESS"),
//                        use_data_item.get(i).getValue("AGENT"),
//                        use_data_item.get(i).getValue("USE_PAY"),
//                        use_data_item.get(i).getValue("RESERVE_PHONE"),
//                        use_data_item.get(i).getValue("RESERVE_NAME"),
//                        use_data_item.get(i).getValue("COMMON_PAY"),
//                        use_data_item.get(i).getValue("COMMON_PAY"),
//                        use_data_item.get(i).getValue("CHARGE_INFO"),
//                        use_data_item.get(i).getValue("CAR_MODEL"),
//                        use_data_item.get(i).getValue("CAR_NUMBER")
//                ));
//            }
            //프로그래스바 종료
            Util.dismiss();
            // TODO - 서버 연동 후 UseContent.ITEMS에 리스 항목 추가 작업 확인
            // Set the adapter - 이용내역 리스트 설정
            if(UseContent.ITEMS.size() > 0) {
                View view = findViewById(R.id.useHistoryList);
                view.setVisibility(View.VISIBLE);
                if (view instanceof RecyclerView) {
                    Context context = view.getContext();
                    RecyclerView recyclerView = (RecyclerView) view;

                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(new UseContentRecyclerViewAdapter(uContext, UseContent.ITEMS, _useHistoryActivity));

                    mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                LinearLayout view = findViewById(R.id.use_history_empty);
                view.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onResponseFailListener(VolleyError it) {

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