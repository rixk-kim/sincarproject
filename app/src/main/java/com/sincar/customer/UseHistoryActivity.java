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

import com.android.volley.VolleyError;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sincar.customer.adapter.UseContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.UseContent;
import com.sincar.customer.entity.LoginDataEntity;
import com.sincar.customer.entity.UseDataEntity;
import com.sincar.customer.network.DataObject;
import com.sincar.customer.network.JsonParser;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.DataParser;
import com.sincar.customer.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.sincar.customer.HWApplication.voLoginData;
import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.common.Constants.LOGIN_REQUEST;

/**
 * 이용내역
 */
public class UseHistoryActivity extends AppCompatActivity implements View.OnClickListener {
    private MenuItem prevBottomNavigation;
    private Context uContext;
    public static UseDataEntity voUseData;

    public static UseHistoryActivity _useHistoryActivity;

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
                RecyclerView recyclerView = (RecyclerView) view;

                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(new UseContentRecyclerViewAdapter(this, UseContent.ITEMS));
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
        postParams.put("REQUESTT_PAGE", "1");                   // 요청페이지
        postParams.put("REQUEST_NUM", "20");                    // 요청갯수

        //프로그래스바 시작
        Util.showDialog();
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
            serverData = "{login: [{\"REGISTER\":\"1\",\"CAUSE\":\"비밀번호 오류\",\"MEMBER_NO\":\"12345\",\"VERSION\":\"1.0.1\",\"APK_URL\":\"http://sincar.co.kr/apk/manager_1.0.1.apk\",\"MEMBER_NAME\":\"신차로\",\"MEMBER_PHONE\":\"01012345678\",\"MEMBER_RECOM_CODE\":\"FCF816\",\"PROFILE_DOWN_URL\":\"http://~~\",\"LICENSE_DOWN_URL\":\"http://~~\",\"AD_NUM\":\"3\",\"MY_POINT\":\"5,430\",\"INVITE_NUM\":\"7\",\"INVITE_FRI_NUM\":\"7\",\"ACCUM_POINT\":\"3,870\"}],\"DATA\":[{\"FRI_NAME\":\"김민정\",\"USE_SERVICE\":\"스팀세차\",\"SAVE_DATE\":\"20.02.26\",\"FRI_POINT\":\"100\"},{\"FRI_NAME\":\"이하영\",\"USE_SERVICE\":\"스팀세차\",\"SAVE_DATE\":\"20.02.28\",\"FRI_POINT\":\"120\"}],\"advertise\":[{\"AD_IMAGE_URL\":\"http://~~\"},{\"AD_IMAGE_URL\":\"http://~~\"},{\"AD_IMAGE_URL\":\"http://~~\"}]}";
            System.out.println("[spirit] it : "  + serverData);

            ArrayList<DataObject> data = JsonParser.getData(serverData);

            // 로그인 정보
            DataObject useListItem = DataParser.getFromParamtoItem(data, "use_list");
            voUseData.setTotalPage(useListItem.getValue("TOTAL"));      //총페이지
            voUseData.setCurrentPage(useListItem.getValue("TOTAL"));    //현재페이지
            voUseData.setCurrentNum(useListItem.getValue("TOTAL"));     //현재갯수

            ArrayList<DataObject> use_data_item = DataParser.getFromParamtoArray(data, "data");

            List<UseContent.UseItem> ITEMS = new ArrayList<UseContent.UseItem>();
            for(int i = 0; i < use_data_item.size(); i++) {
                UseContent.addItem(new UseContent.UseItem(
                        i,
                        use_data_item.get(i).getValue("SEQ"),
                        use_data_item.get(i).getValue("RESERVE_STATUS"),
                        use_data_item.get(i).getValue("RESERVE_TIME"),
                        use_data_item.get(i).getValue("CANCEL_TIME"),
                        use_data_item.get(i).getValue("WASH_ADDRESS"),
                        use_data_item.get(i).getValue("AGENT"),
                        use_data_item.get(i).getValue("USE_PAY"),
                        use_data_item.get(i).getValue("RESERVE_PHONE"),
                        use_data_item.get(i).getValue("RESERVE_NAME"),
                        use_data_item.get(i).getValue("COMMON_PAY"),
                        use_data_item.get(i).getValue("COMMON_PAY"),
                        use_data_item.get(i).getValue("CHARGE_INFO"),
                        use_data_item.get(i).getValue("CAR_MODEL"),
                        use_data_item.get(i).getValue("CAR_NUMBER")
                ));
            }
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
                    recyclerView.setAdapter(new UseContentRecyclerViewAdapter(uContext, UseContent.ITEMS));
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