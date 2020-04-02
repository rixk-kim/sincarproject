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
import android.widget.RadioGroup;
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
import static com.sincar.customer.common.Constants.ADDSERVICE_LIST_REQUEST;
import static com.sincar.customer.common.Constants.LOGIN_REQUEST;

public class ReservationMainActivity extends AppCompatActivity implements View.OnClickListener {
    public final static int CAR_MANAGE_REQ_CODE = 1001;
    public final static int CAR_REGISTER_REQ_CODE = 1005;
    private TextView car_name_str, car_number_str;
    private TextView agent_branch, agent_reserve_date;
    private TextView emptyView;
    private String reserve_companyname;
    private String reserve_carname;
    private String reserve_carnumber;

    private String reserve_address; //예약주소
    private String reserve_year;    //예약년도
    private String reserve_month;   //예약월
    private String reserve_day;     //예약일
    private String agent_seq;       //예약한 대리점주 SEQ
    private String agent_company;   //대리점명
    private String agent_time;      //예약한 대리점주 시간
    private String wash_area;       //세차장소
    private String car_wash_pay;    //기본세차비용

    private String use_coupone_seq; //사용쿠폰 SEQ

    private RadioGroup rRadioGroup;
    private Context rContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_main);
        rContext = this;

        Intent intent = getIntent(); /*데이터 수신*/
        reserve_address     = intent.getExtras().getString("reserve_address");  /*String형*/
        reserve_year        = intent.getExtras().getString("reserve_year");     /*String형*/
        reserve_month       = intent.getExtras().getString("reserve_month");    /*String형*/
        reserve_day         = intent.getExtras().getString("reserve_day");      /*String형*/
        agent_seq           = intent.getExtras().getString("agent_seq");        /*String형*/
        agent_company       = intent.getExtras().getString("agent_company");    /*String형*/
        agent_time          = intent.getExtras().getString("agent_time");       /*String형*/

        // 화면 초기화
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 화면 초기화
     */
    private void init() throws Exception {
        car_name_str = (TextView) findViewById(R.id.car_name_str);
        car_number_str = (TextView) findViewById(R.id.car_number_str);
        agent_branch   = (TextView) findViewById(R.id.branch_name);
        agent_branch.setText(agent_company);    //지점명

        if(reserve_month.length() < 2) reserve_month = "0" + reserve_month;

        String dayofday = Util.getDateDay(reserve_year+reserve_month+reserve_day);

        agent_reserve_date = (TextView) findViewById(R.id.reserve_date);
        agent_reserve_date.setText(reserve_month + "/" + reserve_day + "(" + dayofday + ")" + " " + agent_time);   //예약 날짜

        findViewById(R.id.btnPrev_layout1).setOnClickListener(this);
        //예약하기
        findViewById(R.id.reserve_btn).setOnClickListener(this);

        //리스트 없을 때
        emptyView = (TextView) findViewById(R.id.option_empty_view);

        rRadioGroup = (RadioGroup)findViewById(R.id.wash_place_group);

        rRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String result;
                if(checkedId == R.id.wash_place_button1){
                    wash_area = "실내";
                }else{
                    wash_area = "실외";
                }
            }
        });


        findViewById(R.id.btnCarModify).setOnClickListener(this);
        findViewById(R.id.btnCarRegister).setOnClickListener(this);

        // 서버 연동 후 PointContent.ITEMS에 리스 항목 추가 작업
        requestNoticeList();
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
        postParams.put("AGENT_SEQ", agent_seq);                     // 대리점 SEQ

        //프로그래스바 시작
        Util.showDialog(this);
        //사용내역 요청
        VolleyNetwork.getInstance(this).serverDataRequest(ADDSERVICE_LIST_REQUEST, postParams, onAddServiceResponseListener);
    }

    VolleyNetwork.OnResponseListener onAddServiceResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String serverData) {
            /*
                {"add_service": [{"CAR_SEQ":"3","CAR_COMPANY":"현대","CAR_MODEL":"산타페(대형)","CAR_NUMBER":"35나 8733","CAR_PAY":"50000"}],
                "DATA":[{"SERVICE_NAME":"가니쉬 코팅","SERVICE_DETAIL":"가니쉬란 어쩌구 저쩌구","USE_PAY":"6,000"},{"SERVICE_NAME":"에머랄드 코팅","SERVICE_DETAIL":"가니쉬란 어쩌구 저쩌구","USE_PAY":"5,000"},{"SERVICE_NAME":"엔진룸 세척","SERVICE_DETAIL":"가니쉬란 어쩌구 저쩌구","USE_PAY":"6,000"}]}
             */

            Gson gSon = new Gson();
            optionResult = gSon.fromJson(serverData, OptionResult.class);

            voOptionItem.CAR_SEQ              = optionResult.add_service.get(0).CAR_SEQ;
            voOptionItem.CAR_COMPANY          = optionResult.add_service.get(0).CAR_COMPANY;
            voOptionItem.CAR_MODEL            = optionResult.add_service.get(0).CAR_MODEL;
            voOptionItem.CAR_NUMBER           = optionResult.add_service.get(0).CAR_NUMBER;
            voOptionItem.CAR_PAY              = optionResult.add_service.get(0).CAR_PAY;

            car_name_str.setText(voOptionItem.CAR_COMPANY + " " + voOptionItem.CAR_MODEL);
            car_number_str.setText(voOptionItem.CAR_NUMBER);

            if(!TextUtils.isEmpty(voOptionItem.CAR_COMPANY)) reserve_companyname = voOptionItem.CAR_COMPANY;   //제조사
            if(!TextUtils.isEmpty(voOptionItem.CAR_MODEL)) reserve_carname     = voOptionItem.CAR_MODEL;     //차량 이름
            if(!TextUtils.isEmpty(voOptionItem.CAR_NUMBER)) reserve_carnumber   = voOptionItem.CAR_NUMBER;    //차번호
            if(!TextUtils.isEmpty(voOptionItem.CAR_PAY)) car_wash_pay        = voOptionItem.CAR_PAY;       //기본세차비용

            // 등록된 차량 정보 확인하여 필요한 레이아웃 visible 및 이벤트 핸들러 추가하기
            boolean isCarRegistered = false;

            if(TextUtils.isEmpty(voOptionItem.CAR_COMPANY) || TextUtils.isEmpty(voOptionItem.CAR_MODEL))
            {
                isCarRegistered = true;
            }

            if (!isCarRegistered) {
                findViewById(R.id.car_register_layout).setVisibility(View.GONE);
                findViewById(R.id.car_modify_layout).setVisibility(View.VISIBLE);
//                findViewById(R.id.btnCarModify).setOnClickListener(this);
            } else {
                findViewById(R.id.car_register_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.car_modify_layout).setVisibility(View.GONE);
//                findViewById(R.id.btnCarRegister).setOnClickListener(rContext);
            }


            voOptionDataItem     = optionResult.DATA;

            List<OptionContent.OptionItem> ITEMS = new ArrayList<OptionContent.OptionItem>();

            for(int i = 0; i < voOptionDataItem.size(); i++) {
                OptionContent.addItem(new OptionContent.OptionItem(
                        i,
                        voOptionDataItem.get(i).SEQ,
                        voOptionDataItem.get(i).SERVICE_NAME,
                        voOptionDataItem.get(i).SERVICE_DETAIL,
                        voOptionDataItem.get(i).USE_PAY,
                        false
                ));
            }

            //프로그래스바 종료
            Util.dismiss();

            // 서버 연동 후 UseContent.ITEMS에 리스 항목 추가 작업 확인
            // Set the adapter - 이용내역 리스트 설정
            View view = findViewById(R.id.optionServiceList);
            if(OptionContent.ITEMS.size() > 0) {
                view.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);

                if (view instanceof RecyclerView) {
                    Context context = view.getContext();
                    RecyclerView recyclerView = (RecyclerView) view;

                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(new OptionServiceRecyclerViewAdapter(OptionContent.ITEMS));
                }
            }else{
                // 데이타 없을 때 화면 UI 추가
//                LinearLayout view = findViewById(R.id.use_history_empty);
//                view.setVisibility(View.VISIBLE);
                view.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
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
            case R.id.btnPrev_layout1:
                finish();
                break;

            case R.id.btnCarRegister:
                // TODO - 차량등록 기능 추가
                intent = new Intent(this, CarRegisterActivity.class);
                intent.putExtra("path", "reserveMain");
                startActivityForResult(intent, CAR_REGISTER_REQ_CODE);
                break;

            case R.id.btnCarModify:
                // TODO - 차량변경하기 기능 추가
                intent = new Intent(this, CarManageActivity.class);
                intent.putExtra("path", "reserve");
                startActivityForResult(intent, CAR_MANAGE_REQ_CODE);
                //startActivity(intent);
                break;

            case R.id.reserve_btn:

                if(TextUtils.isEmpty(car_name_str.getText().toString().trim()) || TextUtils.isEmpty(car_number_str.getText().toString().trim()))
                {
                    Toast.makeText(ReservationMainActivity.this, "등록된 차량이 없습니다.", Toast.LENGTH_SHORT).show();
                }else {

                    intent = new Intent(this, PaymentActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("reserve_address", reserve_address);   //주소
                    bundle.putString("reserve_year", reserve_year);         //년
                    bundle.putString("reserve_month", reserve_month);       //월
                    bundle.putString("reserve_day", reserve_day);           //일
                    bundle.putString("agent_seq", agent_seq);               //예약 대리점주 seq
                    bundle.putString("agent_company", agent_company);       //예약 대리점주
                    bundle.putString("agent_time", agent_time);             //예약시간
                    if(TextUtils.isEmpty(wash_area)) wash_area = "실내";
                    bundle.putString("wash_area", wash_area);               //세차장소
                    bundle.putString("car_company", reserve_companyname);   //제조사
                    bundle.putString("car_name", reserve_carname);          //차량 이름
                    bundle.putString("car_number", reserve_carnumber);      //차번호
                    bundle.putString("car_wash_pay", car_wash_pay);         //차량 기본 세차 금액
                    //부가서비스
                    intent.putExtras(bundle);

                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAR_MANAGE_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                findViewById(R.id.car_register_layout).setVisibility(View.GONE);
                findViewById(R.id.car_modify_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.btnCarModify).setOnClickListener(this);

                if(!TextUtils.isEmpty(data.getStringExtra("reserve_companyname")))
                {
                    reserve_companyname = data.getStringExtra("reserve_companyname");
                }

                if(!TextUtils.isEmpty(data.getStringExtra("reserve_carname")))
                {
                    reserve_carname = data.getStringExtra("reserve_carname");
                }
                car_name_str.setText(reserve_companyname + " " + reserve_carname);

                if(!TextUtils.isEmpty(data.getStringExtra("reserve_carnumber")))
                {
                    reserve_carnumber = data.getStringExtra("reserve_carnumber");
                    car_number_str.setText(reserve_carnumber);
                }

                if(!TextUtils.isEmpty(data.getStringExtra("car_wash_pay")))
                {
                    car_wash_pay = data.getStringExtra("car_wash_pay");
                }
 //               Toast.makeText(ReservationMainActivity.this, "차종: " + data.getStringExtra("reserve_carname") + " , 차번호 : " + data.getStringExtra("reserve_carnumber"), Toast.LENGTH_SHORT).show();
            } else {   // RESULT_CANCEL
                Toast.makeText(ReservationMainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }

        }else{
            //차량 등록하고 리턴
            if (resultCode == RESULT_OK) {

                findViewById(R.id.car_register_layout).setVisibility(View.GONE);
                findViewById(R.id.car_modify_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.btnCarModify).setOnClickListener(this);

                if(!TextUtils.isEmpty(data.getStringExtra("reserve_companyname")))
                {
                    reserve_companyname = data.getStringExtra("reserve_companyname");
                }

                if(!TextUtils.isEmpty(data.getStringExtra("reserve_carname")))
                {
                    reserve_carname = data.getStringExtra("reserve_carname");
                }
                car_name_str.setText(reserve_companyname + " " + reserve_carname);

                if(!TextUtils.isEmpty(data.getStringExtra("reserve_carnumber")))
                {
                    reserve_carnumber = data.getStringExtra("reserve_carnumber");
                    car_number_str.setText(reserve_carnumber);
                }

                if(!TextUtils.isEmpty(data.getStringExtra("car_wash_pay")))
                {
                    car_wash_pay = data.getStringExtra("car_wash_pay");
                }

//                Toast.makeText(ReservationMainActivity.this, "차종: " + data.getStringExtra("reserve_carname") + " , 차번호 : " + data.getStringExtra("reserve_carnumber"), Toast.LENGTH_SHORT).show();
            } else {   // RESULT_CANCEL
//                Toast.makeText(ReservationMainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
