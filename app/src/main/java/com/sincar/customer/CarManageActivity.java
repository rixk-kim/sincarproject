package com.sincar.customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sincar.customer.adapter.CarContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.CarContent;
import com.sincar.customer.item.CarDeleteResult;
import com.sincar.customer.item.CarResult;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.sincar.customer.HWApplication.carResult;
import static com.sincar.customer.HWApplication.voCarDataItem;
import static com.sincar.customer.HWApplication.voCarItem;

import static com.sincar.customer.HWApplication.carDeleteResult;
import static com.sincar.customer.HWApplication.voCarDeleteItem;

import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.common.Constants.CAR_DELETE_REQUEST;
import static com.sincar.customer.common.Constants.CAR_LIST_REQUEST;

/**
 * 202.04.09 spirit
 * 등록 차량 리스트 class
 */
public class CarManageActivity extends AppCompatActivity implements View.OnClickListener {
    public static Context carContext;
    private LinearLayout carLayout;
    private CarContentRecyclerViewAdapter mCarContentRecyclerViewAdapter;
    private String path;

    //페이지 처리
    private int request_page = 1;                           // 페이징변수. 초기 값은 0 이다.
    private final int request_offset = 20;                  // 한 페이지마다 로드할 데이터 갯수.

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
        findViewById(R.id.car_manage_btnPrev_layout).setOnClickListener(this);
        findViewById(R.id.car_manage_btnNext_layout).setOnClickListener(this);
        findViewById(R.id.car_manage_reg_btn).setOnClickListener(this); //확인

        carLayout = (LinearLayout)findViewById(R.id.car_confirm_layout);

        if("reserve".equals(path)){
            carLayout.setVisibility(View.VISIBLE);
        }else{
            carLayout.setVisibility(View.GONE);
            RecyclerView rV = (RecyclerView)findViewById(R.id.carManageList);

            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)rV.getLayoutParams();
            layoutParams.bottomMargin = 0;
            rV.setLayoutParams(layoutParams);
        }

        // 서버 연동 후 CarContent.ITEMS에 리스 항목 추가 작업
        CarContent.clearItem(); //초기화

        requestCarList();
    }

    /**
     * 등록차량 리스트 요청
     * PHONE_NEMBER     : 폰번호
     * MEMBER_NO        : 회원번호
     * REQUESTT_PAGE    : 요청페이지
     * REQUEST_NUM      : 요청갯수
     */
    private void requestCarList() {
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("PHONE_NUMBER", voLoginItem.MEMBER_PHONE);       // 폰번호
        postParams.put("MEMBER_NO", voLoginItem.MEMBER_NO);             // 회원번호
        postParams.put("REQUEST_PAGE", String.valueOf(request_page));   // 요청페이지
        postParams.put("REQUEST_NUM", String.valueOf(request_offset));  // 요청갯수

        //프로그래스바 시작
        Util.showDialog(this);
        //사용내역 요청
        VolleyNetwork.getInstance(this).serverDataRequest(CAR_LIST_REQUEST, postParams, onCarListResponseListener);
    }

    VolleyNetwork.OnResponseListener onCarListResponseListener = new VolleyNetwork.OnResponseListener() {
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

            voCarDataItem     = carResult.data;

            List<CarContent.CarItem> ITEMS = new ArrayList<CarContent.CarItem>();

            for(int i = 0; i < voCarDataItem.size(); i++) {
                CarContent.addItem(new CarContent.CarItem(
                        i,
                        voCarDataItem.get(i).CAR_SEQ,
                        voCarDataItem.get(i).CAR_COMPANY,
                        voCarDataItem.get(i).CAR_MODEL,
                        voCarDataItem.get(i).CAR_NUMBER,
                        false,
                        voCarDataItem.get(i).CAR_PAY
                ));
            }

            //프로그래스바 종료
            Util.dismiss();

            // 서버 연동 후 UseContent.ITEMS에 리스 항목 추가 작업 확인
            // Set the adapter - 이용내역 리스트 설정
            if(CarContent.ITEMS.size() > 0) {
                View view = findViewById(R.id.carManageList);
                LinearLayout view1 = findViewById(R.id.carManage_empty);

                view.setVisibility(View.VISIBLE);
                view1.setVisibility(View.GONE);

                if (view instanceof RecyclerView) {
                    Context context = view.getContext();
                    RecyclerView recyclerView = (RecyclerView) view;

                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    //recyclerView.setAdapter(new CarContentRecyclerViewAdapter(carContext, CarContent.ITEMS));
                    mCarContentRecyclerViewAdapter = new CarContentRecyclerViewAdapter(carContext, CarContent.ITEMS, path);
                    recyclerView.setAdapter(mCarContentRecyclerViewAdapter);

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
                                if( Integer.parseInt(voCarItem.TOTAL) % request_offset == 0 ) {
                                    lastPageNum = (int)Math.floor(Integer.parseInt(voCarItem.TOTAL)/request_offset);
                                }
                                else {
                                    lastPageNum = (int)Math.floor(Integer.parseInt(voCarItem.TOTAL)/request_offset) + 1;
                                }

                                if(lastPageNum > request_page)
                                {
                                    //다음 페이지 요청
                                    request_page+=1;
                                    requestCarList();
                                }
                            }

                        }
                    });
                }
            }else{
                // 등록차량 없을 때 화면 UI 추가
                View view = findViewById(R.id.carManageList);
                LinearLayout view1 = findViewById(R.id.carManage_empty);

                view.setVisibility(View.GONE);
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

        switch (v.getId()) {
            case R.id.car_manage_btnPrev_layout:
            case R.id.car_manage_reg_btn:
                // 내정보
                if("main".equals(path)) {
                    intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else if("reserve".equals(path)){
                    intent = new Intent(this, ReservationMainActivity.class);
                    intent.putExtra("reserve_companyname", mCarContentRecyclerViewAdapter.getItemCompanyName());
                    intent.putExtra("reserve_carname", mCarContentRecyclerViewAdapter.getItemcarName());
                    intent.putExtra("reserve_carnumber", mCarContentRecyclerViewAdapter.getItemcarNumber());
                    intent.putExtra("car_wash_pay", mCarContentRecyclerViewAdapter.getItemcarPay());
                    setResult(RESULT_OK, intent);
                    finish();

                }else {
                    intent = new Intent(this, MyProfileSettingsActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.car_manage_btnNext_layout:
                //  차량등록
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
            intent.putExtra("reserve_companyname", mCarContentRecyclerViewAdapter.getItemCompanyName());
            intent.putExtra("reserve_carname", mCarContentRecyclerViewAdapter.getItemcarName());
            intent.putExtra("reserve_carnumber", mCarContentRecyclerViewAdapter.getItemcarNumber());
            intent.putExtra("car_wash_pay", mCarContentRecyclerViewAdapter.getItemcarPay());
            setResult(RESULT_OK, intent);
            finish();
        }else {
            intent = new Intent(this, MyProfileSettingsActivity.class);
            startActivity(intent);
        }
        finish();
    }

    /**
     * 차량 삭제 요청
     * @param car_seq
     */
    public void carDelete(String car_seq)
    {
        requestCarDelete(car_seq);
//        Toast.makeText(this, "차량 삭제 요청 : " + car_seq, Toast.LENGTH_SHORT).show();
    }

    /**
     * 등록차량 삭제 요청
     * MEMBER_NO        : 회원번호
     * SEQ              : 등록차량 SEQ
     */
    private void requestCarDelete(String carSeq) {
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("MEMBER_NO", voLoginItem.MEMBER_NO);     // 회원번호
        postParams.put("SEQ", carSeq);                          // 등록차량 SEQ

        //프로그래스바 시작
        Util.showDialog(this);
        //사용내역 요청
        VolleyNetwork.getInstance(this).serverDataRequest(CAR_DELETE_REQUEST, postParams, onCarDeleteResponseListener);
    }

    VolleyNetwork.OnResponseListener onCarDeleteResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String it) {
            // {\"auth_send\": {\"AUTH_RESULT\":\"0\",\"CAUSE\":\"\",\"AUTH_NUMBER\":\"123456\"}}";
            // {"delete_result": {"DELETE_RESULT":"1","CAUSE":"형식에 맞지 않습니다."}}
            Gson gSon = new Gson();
            //LoginResult loginResult = gSon.fromJson(it, LoginResult.class);
            carDeleteResult = gSon.fromJson(it, CarDeleteResult.class);

            voCarDeleteItem.DELETE_RESULT      = carDeleteResult.delete_result.DELETE_RESULT;
            voCarDeleteItem.CAUSE              = carDeleteResult.delete_result.CAUSE;

            //프로그래스바 종료
            Util.dismiss();

            if("0".equals(voCarDeleteItem.DELETE_RESULT))
            {
                Toast.makeText(carContext, "삭제 하였습니다.", Toast.LENGTH_LONG).show();

                // 서버 연동 후 CarContent.ITEMS에 리스 항목 추가 작업
                CarContent.clearItem(); //초기화
                request_page = 1;
                requestCarList();
            }else{
                Toast.makeText(carContext, "삭제에 실패하였습니다.", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onResponseFailListener(VolleyError it) {
            //프로그래스바 종료
            Util.dismiss();

            Toast.makeText(carContext, "삭제에 실패하였습니다.", Toast.LENGTH_LONG).show();
        }
    };
}