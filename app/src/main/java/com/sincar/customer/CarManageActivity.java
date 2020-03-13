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
import com.sincar.customer.adapter.CouponeContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.CarContent;
import com.sincar.customer.adapter.content.CouponeContent;
import com.sincar.customer.adapter.content.NoticeContent;
import com.sincar.customer.item.CarResult;
import com.sincar.customer.item.CouponeResult;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.sincar.customer.HWApplication.carResult;
import static com.sincar.customer.HWApplication.voCarDataItem;
import static com.sincar.customer.HWApplication.voCarItem;
import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.common.Constants.LOGIN_REQUEST;

public class CarManageActivity extends AppCompatActivity implements View.OnClickListener {
    private String path;
    private Context carContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_manage);
        carContext = this;

        Intent intent = getIntent(); /*데이터 수신*/
        path    = intent.getExtras().getString("path");    /*String형*/

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    @SuppressLint("ResourceAsColor")
    private void init() {
        findViewById(R.id.car_manage_btnPrev).setOnClickListener(this);
        findViewById(R.id.car_manage_btnNext).setOnClickListener(this);
        findViewById(R.id.car_manage_reg_btn).setOnClickListener(this); //확인

        // TODO - 서버 연동 후 CardContent.ITEMS에 리스 항목 추가 작업
        // Set the adapter - 포인트 리스트 설정
        View view = findViewById(R.id.carManageList);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new CarContentRecyclerViewAdapter(this, CarContent.ITEMS));
        }
    }

    /**
     * 등록차량 리스트 요청
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
               {"car_list": [{"TOTAL":"5","CURRENT_PAGE":"1","CURRENT_NUM":"20"}],
               "data":[{"CAR_SEQ":"1","CAR_COMPANY":"현대","CAR_MODEL":"싼타페","CAR_NUMBER":"12가1234","CAR_SELECTED":"false"},{},{}…]}
             */

            Gson gSon = new Gson();
            carResult = gSon.fromJson(serverData, CarResult.class);

            voCarItem.TOTAL              = carResult.car_list.get(0).TOTAL;
            voCarItem.CURRENT_PAGE       = carResult.car_list.get(0).CURRENT_PAGE;
            voCarItem.CURRENT_NUM        = carResult.car_list.get(0).CURRENT_NUM;

            voCarDataItem     = carResult.DATA;

            List<CarContent.CarItem> ITEMS = new ArrayList<CarContent.CarItem>();

            for(int i = 0; i < voCarDataItem.size(); i++) {
                CarContent.addItem(new CarContent.CarItem(
                        i,
                        voCarDataItem.get(i).CAR_SEQ,
                        voCarDataItem.get(i).CAR_COMPANY,
                        voCarDataItem.get(i).CAR_MODEL,
                        voCarDataItem.get(i).CAR_NUMBER,
                        false
                ));
            }

            //프로그래스바 종료
            Util.dismiss();

            // 서버 연동 후 UseContent.ITEMS에 리스 항목 추가 작업 확인
            // Set the adapter - 이용내역 리스트 설정
            if(CarContent.ITEMS.size() > 0) {
                View view = findViewById(R.id.carManageList);
                if (view instanceof RecyclerView) {
                    Context context = view.getContext();
                    RecyclerView recyclerView = (RecyclerView) view;

                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(new CarContentRecyclerViewAdapter(carContext, CarContent.ITEMS));
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
            case R.id.car_manage_btnPrev:
            case R.id.car_manage_reg_btn:
                //  TODO - 내정보
                if("main".equals(path)) {
                    intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else if("reserve".equals(path)){
                    intent = new Intent(this, ReservationMainActivity.class);
                    intent.putExtra("result", "some value");
                    setResult(RESULT_OK, intent);
                    finish();

                }else {
                    intent = new Intent(this, MyProfileSettingsActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.car_manage_btnNext:
                //  TODO - 차량등록
                intent = new Intent(this, CarRegisterActivity.class);
                intent.putExtra("path", path);
                startActivity(intent);
                finish();
                break;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent;
        if("main".equals(path))
        {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else if("reserve".equals(path)){
            intent = new Intent(this, ReservationMainActivity.class);
            intent.putExtra("result", "some value");
            setResult(RESULT_OK, intent);
            finish();
        }else {
            intent = new Intent(this, MyProfileSettingsActivity.class);
            startActivity(intent);
        }
        finish();
    }


}



