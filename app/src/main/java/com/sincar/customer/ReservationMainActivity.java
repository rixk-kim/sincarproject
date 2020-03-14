package com.sincar.customer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sincar.customer.adapter.CarContentRecyclerViewAdapter;
import com.sincar.customer.adapter.OptionServiceRecyclerViewAdapter;
import com.sincar.customer.adapter.PointContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.CarContent;
import com.sincar.customer.adapter.content.OptionContent;
import com.sincar.customer.adapter.content.PointContent;
import com.sincar.customer.item.CarResult;
import com.sincar.customer.item.OptionResult;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.sincar.customer.HWApplication.optionResult;
import static com.sincar.customer.HWApplication.voOptionDataItem;
import static com.sincar.customer.HWApplication.voOptionItem;
import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.common.Constants.LOGIN_REQUEST;

public class ReservationMainActivity extends AppCompatActivity implements View.OnClickListener {
    public final static int CAR_MANAGE_REQ_CODE = 1001;
    private TextView car_name_str;
    private TextView car_number_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_main);

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        car_name_str = (TextView) findViewById(R.id.car_name_str);
        car_number_str = (TextView) findViewById(R.id.car_number_str);

        findViewById(R.id.btnPrev).setOnClickListener(this);
        //예약하기
        findViewById(R.id.reserve_btn).setOnClickListener(this);

        // TODO - 등록된 차량 정보 확인하여 필요한 레이아웃 visible 및 이벤트 핸들러 추가하기
        boolean isCarRegistered = false;

        if (!isCarRegistered) {
            findViewById(R.id.car_register_layout).setVisibility(View.GONE);
            findViewById(R.id.car_modify_layout).setVisibility(View.VISIBLE);

//            ((TextView)findViewById(R.id.car_name_str)).setText("");
//            ((TextView)findViewById(R.id.car_number_str)).setText("");

            findViewById(R.id.btnCarModify).setOnClickListener(this);
        } else {
            findViewById(R.id.car_register_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.car_modify_layout).setVisibility(View.GONE);

            findViewById(R.id.btnCarRegister).setOnClickListener(this);
        }

        // TODO - 서버 연동 후 PointContent.ITEMS에 리스 항목 추가 작업
        // Set the adapter - 포인트 리스트 설정
        View view = findViewById(R.id.optionServiceList);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new OptionServiceRecyclerViewAdapter(OptionContent.ITEMS));
        }
    }

    /**
     * 부가서비스 리스트 요청
     * MEMBER_NO        : 회원번호
     * REQUESTT_PAGE    : 요청페이지
     * REQUEST_NUM      : 요청갯수
     */
    private void requestNoticeList() {
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("MEMBER_NO", voLoginItem.MEMBER_NO);         // 회원번호
        postParams.put("AGENT_SEQ", "1");                       // 대리점 SEQ

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
            optionResult = gSon.fromJson(serverData, OptionResult.class);

            voOptionItem.TOTAL              = optionResult.add_service.get(0).TOTAL;

            voOptionDataItem     = optionResult.DATA;

            List<CarContent.CarItem> ITEMS = new ArrayList<CarContent.CarItem>();

            for(int i = 0; i < voOptionDataItem.size(); i++) {
                OptionContent.addItem(new OptionContent.OptionItem(
                        i,
                        voOptionDataItem.get(i).SEQ,
                        voOptionDataItem.get(i).SERVICE_NAME,
                        voOptionDataItem.get(i).SERVICE_NAME,
                        voOptionDataItem.get(i).USE_PAY,
                        false
                ));
            }

            //프로그래스바 종료
            Util.dismiss();

            // 서버 연동 후 UseContent.ITEMS에 리스 항목 추가 작업 확인
            // Set the adapter - 이용내역 리스트 설정
            if(OptionContent.ITEMS.size() > 0) {
                View view = findViewById(R.id.optionServiceList);
                if (view instanceof RecyclerView) {
                    Context context = view.getContext();
                    RecyclerView recyclerView = (RecyclerView) view;

                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(new OptionServiceRecyclerViewAdapter(OptionContent.ITEMS));
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
            case R.id.btnPrev:
                finish();
                break;

            case R.id.btnCarRegister:
                // TODO - 차량등록 기능 추가
                break;

            case R.id.btnCarModify:
                // TODO - 차량변경하기 기능 추가
                intent = new Intent(this, CarManageActivity.class);
                intent.putExtra("path", "reserve");
                startActivityForResult(intent, CAR_MANAGE_REQ_CODE);
                //startActivity(intent);
                break;

            case R.id.reserve_btn:
                //결재 종류 선택 팝업
//                reserveSelect();
                startActivity(new Intent(this, PaymentActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAR_MANAGE_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                if(!TextUtils.isEmpty(data.getStringExtra("reserve_carname")))
                {
                    car_name_str.setText(data.getStringExtra("reserve_carname"));
                }

                if(!TextUtils.isEmpty(data.getStringExtra("reserve_carnumber")))
                {
                    car_number_str.setText(data.getStringExtra("reserve_carnumber"));
                }
                Toast.makeText(ReservationMainActivity.this, "차종: " + data.getStringExtra("reserve_carname") + " , 차번호 : " + data.getStringExtra("reserve_carnumber"), Toast.LENGTH_SHORT).show();
            } else {   // RESULT_CANCEL
                Toast.makeText(ReservationMainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
