package com.sincar.customer.sy_rentcar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.sincar.customer.CouponeActivity;
import com.sincar.customer.PayApproveActivity;
import com.sincar.customer.PayApproveResult;
import com.sincar.customer.R;
import com.sincar.customer.UseTerms2Activity;
import com.sincar.customer.adapter.ChargeContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.ChargeContent;
import com.sincar.customer.adapter.content.CouponeContent;
import com.sincar.customer.adapter.content.OptionContent;
import com.sincar.customer.item.RentcarReserveResult;
import com.sincar.customer.item.ReserveResult;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.Util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import static com.sincar.customer.HWApplication.rentcarReserveResult;
import static com.sincar.customer.HWApplication.reserveResult;
import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.HWApplication.voRentcarReserveItem;
import static com.sincar.customer.HWApplication.voReserveItem;
import static com.sincar.customer.MapsActivity._mMapsActivity;
import static com.sincar.customer.MapsActivity.homeKeyPressed;
import static com.sincar.customer.common.Constants.PAY_APPROVE_REQUEST;
import static com.sincar.customer.common.Constants.RENTCAR_PAY_REQUEST;
import static com.sincar.customer.util.Util.setAddMoneyDot;

public class Rental_payment extends AppCompatActivity implements View.OnClickListener {
    public final static int RENTAL_PAYMENT_REQ_CODE = 1006;
    private TextView amount_TextView;
    private TextView branch_name, wash_address, reservation_time, my_point;
    private EditText use_point;
    private String coupone_seq = "";
    private CheckBox clause_agree;
    private Context pContext;
    private Button mButton;
    private ConstraintLayout clause_layout;

//    private String reserve_address = ""; //예약주소
//    private String reserve_year = "";    //예약년도
//    private String reserve_month = "";   //예약월
//    private String reserve_day = "";     //예약일
//    private String agent_seq = "";       //예약한 대리점주 SEQ
//    private String agent_company = "";   //대리점 정보
//
//    private String agent_time = "";      //예약한 대리점주 시간
//    private String wash_area = "";       //세차장소
//    private String car_company = "";     //제조사
//    private String car_name = "";        //차량 이름
//    private String car_number = "";      //차번호
//    private String car_wash_pay = "";    //차량 기본 세차 금액

    private int total_amt = 0;          //세차비용
    private int use_coupone_seq = 0;    //사용 쿠폰
    private int use_coupone_pay = 0;    //쿠폰 비용
    private int use_my_point = 0;       //사용 포인트
    private String car_wash_option = ""; //옵션
    private int car_wash_option_pay = 0;    //옵션비용

    private String rent_approve_number = ""; // 예약번호
    private String rent_amount = ""; // 결제금액
    private String rent_delivery_amount = ""; // 딜리버리 금액
    private String rent_insurance_amount = ""; // 보험 금액
    private String rent_reserve_year = ""; //예약연도
    private String rent_reserve_date = ""; //예약날짜(월포함)
    private String rent_reserve_time = ""; //예약시간
    private String rent_return_year = ""; //반납연도
    private String rent_return_date = ""; //반납날짜(월포함)
    private String rent_return_time = ""; //반납시간
    private String rent_rentcar_car = ""; //대여차량
    private String rent_rentcar_num = ""; //대여 차량 번호
    private String rent_rentcar_agent = ""; //지점 정보
    private String rent_rentcar_res_add = null; //배차위치 (없을때 null)
    private String rent_rentcar_ret_add = null; // 반납위치 (없을때 null)
    private String insu_seq = "";   //보험 seq
    private String insu_name = "";  //보험 명
    private int select_delivery; // 딜리버리 선택
    private String current_agent_seq = ""; //지점 seq
    private String current_car_seq = "";    //차량 seq

    //20200630
    private TextView rental_use_amount, rental_use_delivery, rental_use_insurance;
    private int rental_pay      = 0;        //차량대여료
    private int delivery_pay    = 0;        //딜리버리 금액
    private int insurance_pay   = 0;        //보험 금액

    private int product_pay = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_payment);
        pContext = this;
//
        Intent intent = getIntent(); /*데이터 수신*/

        rental_pay     = Integer.parseInt(intent.getExtras().getString("rental_pay"));          /*String형*/
        delivery_pay   = Integer.parseInt(intent.getExtras().getString("delivery_pay"));        /*String형*/
        insurance_pay  = Integer.parseInt(intent.getExtras().getString("insurance_pay"));       /*String형*/
//        rental_pay = 160000;
//        delivery_pay = 0;
//        insurance_pay = 10000;

        product_pay = rental_pay + delivery_pay + insurance_pay;
                                                                                       //Rental_list_detail에서 넘어오느 데이터
        rent_amount = intent.getStringExtra("rental_pay");                      //결제 금액(보험,딜리버리 금액 제외)
        rent_delivery_amount = intent.getStringExtra("delivery_pay");           //딜리버리 금액
        rent_insurance_amount = intent.getStringExtra("insurance_pay");         //보험 금액
//        rent_coupon_amount = intent.getStringExtra("");
//        rent_point_amount = intent.getStringExtra("");
        rent_reserve_year = intent.getStringExtra("RESERVE_YEAR");              //예약 연도
        rent_reserve_date = intent.getStringExtra("RESERVE_DATE");              //예약 날짜
        rent_reserve_time = intent.getStringExtra("RESERVE_TIME");              //예약 시간
        rent_return_year = intent.getStringExtra("RETURN_YEAR");                //반납 연도
        rent_return_date = intent.getStringExtra("RETURN_DATE");                //반납 날짜
        rent_return_time = intent.getStringExtra("RETURN_TIME");                //반납 시간
        rent_rentcar_car = intent.getStringExtra("RENTCAR_CAR");                //대여 차종
        rent_rentcar_num = intent.getStringExtra("RENTCAR_NUM");                 //대여 차량 번호
        rent_rentcar_agent = intent.getStringExtra("RENTCAR_AGENT");            //지점 이름
        rent_rentcar_res_add = intent.getStringExtra("RENTCAR_RES_ADD");        //딜리버리 배차 위치(없으면 Null)
        rent_rentcar_ret_add = intent.getStringExtra("RENTCAR_RET_ADD");        //딜리버리 반납 위치(없으면 Null)
        insu_seq = intent.getStringExtra("CURRENT_INSU_SEQ");
        insu_name = intent.getStringExtra("CURRENT_INSU_NAME");
        select_delivery = intent.getIntExtra("select_delivery",0);
        current_agent_seq = intent.getStringExtra("CURRENT_AGENT_SEQ");
        current_car_seq = intent.getStringExtra("CURRENT_CAR_SEQ");

//        reserve_address     = intent.getExtras().getString("reserve_address");  /*String형*/
//        reserve_year        = intent.getExtras().getString("reserve_year");     /*String형*/
//        reserve_month       = intent.getExtras().getString("reserve_month");    /*String형*/
//        reserve_day         = intent.getExtras().getString("reserve_day");      /*String형*/
//        agent_seq           = intent.getExtras().getString("agent_seq");        /*String형*/
//        agent_company       = intent.getExtras().getString("agent_company");      /*String형*/
//        agent_time          = intent.getExtras().getString("agent_time");       /*String형*/
//        wash_area           = intent.getExtras().getString("wash_area");        /*String형*/
//
//        car_company         = intent.getExtras().getString("car_company");      /*String형*/
//        car_name            = intent.getExtras().getString("car_name");         /*String형*/
//        car_number          = intent.getExtras().getString("car_number");       /*String형*/
//        car_wash_pay        = intent.getExtras().getString("car_wash_pay");     /*String형*/

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
        findViewById(R.id.rental_btnPrev_layout).setOnClickListener(this);
        findViewById(R.id.rental_btnPointUse).setOnClickListener(this);    //포인트 전액 사용

        findViewById(R.id.rental_btnCancelDesc).setOnClickListener(this);    //취소정책 : 자세히


        rental_use_amount = (TextView) findViewById(R.id.rental_use_amount);
        rental_use_amount.setText(Util.setAddMoneyDot(String.valueOf(rental_pay)) + "원");

        rental_use_delivery = (TextView) findViewById(R.id.rental_use_delivery);
        rental_use_delivery.setText(Util.setAddMoneyDot(String.valueOf(delivery_pay)) + "원");

        rental_use_insurance = (TextView) findViewById(R.id.rental_use_insurance);
        rental_use_insurance.setText(Util.setAddMoneyDot(String.valueOf(insurance_pay)) + "원");

//        findViewById(R.id.btnNext).setVisibility(View.INVISIBLE);
        findViewById(R.id.rental_btnCouponRegister).setOnClickListener(this);
        amount_TextView = (TextView) findViewById(R.id.rental_coupon_amount);

        clause_agree = (CheckBox) findViewById(R.id.rental_clause_agree);
        clause_layout = (ConstraintLayout) findViewById(R.id.rental_clause_layout);
        clause_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clause_agree.isChecked()) {
                    clause_agree.setChecked(false);
                } else {
                    clause_agree.setChecked(true);
                }
            }
        });

        mButton = (Button) findViewById(R.id.rental_reserve_confirm_btn);
        mButton.setOnClickListener(this);
        mButton.setText("결제하기");

        //포인트
        my_point = (TextView) findViewById(R.id.rental_my_point);
        my_point.setText(Util.setAddMoneyDot(voLoginItem.MY_POINT) + "원 보유");

        use_point = (EditText) findViewById(R.id.rental_use_point);
        use_point.setOnClickListener(this);

        use_point.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // 확인 버튼 눌렀을 때
                    String value = use_point.getText().toString().trim();
                    //특정 문자열 제거(, / 원)
                    value = value.replaceAll(",","");
                    value = value.replaceAll("원","");
                    if(!TextUtils.isEmpty(value))
                    {
                        int mPoint = Integer.parseInt(voLoginItem.MY_POINT);    //내 포인트
                        int input_point = Integer.parseInt(value);              //사용 포인트

                        //car_wash_pay  -> 기본 세차비용
                        //use_coupone_pay -> 쿠폰 비용

                        if(input_point > mPoint)
                        {
                            //보유 포인트보다 크게 입력한 상태
//                            Toast.makeText(com.sincar.customer.PaymentActivity.this, "보유 포인트보다 초과 입력하였습니다.", Toast.LENGTH_LONG).show();
                            use_point.setText("0원");
                            mButton.setText(setAddMoneyDot(String.valueOf(product_pay - use_coupone_pay)) + "원 결재하기");
                        }else{
                            if(input_point > (product_pay - use_coupone_pay))
                            {
                                //입력 포인트가 기본 비용보다 클경우
                                int pay_result = input_point - (product_pay - use_coupone_pay);
                                use_my_point = (product_pay - use_coupone_pay);
                                use_point.setText(setAddMoneyDot(String.valueOf(product_pay - use_coupone_pay)) + "원");
                                mButton.setText("0원 결제하기");
                            }else{
                                int pay_result = (product_pay - use_coupone_pay) - input_point;
                                use_my_point = input_point;
                                use_point.setText(setAddMoneyDot(String.valueOf(input_point)) + "원");
                                mButton.setText(setAddMoneyDot(String.valueOf(pay_result)) + "원 결재하기");
                            }
                        }
                    }
                }
                return false;
            }
        });

        total_amt = product_pay;
        mButton.setText(setAddMoneyDot(String.valueOf(total_amt)) + "원 결제하기");
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch(v.getId())
        {
            case R.id.rental_btnPrev_layout:
                finish();
                break;
            case R.id.rental_btnCouponRegister:
                //기존 쿠폰 적용 금액이 있다면 원복
                if(!TextUtils.isEmpty(coupone_seq) && use_coupone_pay > 0) {
                    total_amt += use_coupone_pay;

                    use_coupone_pay = 0;
                    coupone_seq = "";

                    amount_TextView.setText(String.valueOf(use_coupone_pay) + "원");
                    mButton.setText(setAddMoneyDot(String.valueOf(total_amt)) + "원 결제하기");
                    Toast.makeText(com.sincar.customer.sy_rentcar.Rental_payment.this, "쿠폰 금액 조정", Toast.LENGTH_SHORT).show();
                }

                intent = new Intent(this, CouponeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION); // intent가 호출될때 onUserLeaveHint()가 실행되는것을 차단
                intent.putExtra("path", "rental_payment");
                startActivityForResult(intent, RENTAL_PAYMENT_REQ_CODE);
                break;

            case R.id.rental_reserve_confirm_btn:
                if(clause_agree.isChecked() == true){
                    // 예약으로 이동
                    //reserveSelect();
//                    intent = new Intent(pContext, PayApproveActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("reserve_address", reserve_address);   //주소
//                    bundle.putString("reserve_year", reserve_year);         //년
//                    bundle.putString("reserve_month", reserve_month);       //월
//                    bundle.putString("reserve_day", reserve_day);           //일
//                    bundle.putString("agent_seq", agent_seq);               //예약 대리점주 seq
//                    bundle.putString("agent_company", agent_company);       //예약 대리점주
//                    bundle.putString("agent_time", agent_time);             //예약시간
//                    bundle.putString("wash_area", wash_area);               //세차장소(실내/실외)
//                    bundle.putString("car_wash_option", car_wash_option);   //옵션(가니쉬코팅/에머랄드 코팅)
//                    bundle.putString("car_company", car_company);           //제조사
//                    bundle.putString("car_name", car_name);                 //차량 이름
//                    bundle.putString("car_number", car_number);             //차번호
////                    bundle.putString("car_wash_pay", car_wash_pay);         //차량 기본 세차 금액
//                    bundle.putString("use_my_point", String.valueOf(use_my_point));         //사용 포인트
//                    bundle.putString("use_coupone_seq", coupone_seq);                       //사용 쿠폰 seq
//
//                    //car_wash_option_pay => 부가서비스 비용
//                    String tmp_value = use_point.getText().toString().trim();
//                    int input_point = 0;
//                    //특정 문자열 제거(, / 원)
//                    tmp_value = tmp_value.replaceAll(",","");
//                    tmp_value = tmp_value.replaceAll("원","");
//                    if(!TextUtils.isEmpty(tmp_value)) {
//                        input_point = Integer.parseInt(tmp_value);              //사용 포인트
//                    }
//
//                    int reserve_pay = Integer.parseInt(car_wash_pay) + car_wash_option_pay - use_coupone_pay - input_point;
//                    if(reserve_pay < 0) reserve_pay = 0;
//
//                    bundle.putString("total_amt", String.valueOf(reserve_pay));               //총 결제 금액
//
//                    //부가서비스
//                    intent.putExtras(bundle);
//                    startActivity(intent);

                    //인터페이스 적용후 활성화 예정
                    intent = new Intent(pContext, Rental_pay_web.class);
                    //임시 : 결제 웹페이지로 가지 않고 완료페이지 이동
//                    intent = new Intent(pContext, Rental_approve.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION); // intent가 호출될때 onUserLeaveHint()가 실행되는것을 차단

                    intent.putExtra("MEMBER_NO", voLoginItem.MEMBER_NO);
                    intent.putExtra("AMOUNT", rent_amount);
                    intent.putExtra("APPROVE_NUMBER", rent_approve_number);
                    intent.putExtra("RENTCAR_DEL_TYPE", String.valueOf(select_delivery));
                    intent.putExtra("DELIVERY_AMOUNT", rent_delivery_amount);
                    intent.putExtra("INSURANCE_SEQ", insu_seq);
                    intent.putExtra("INSURANCE_NAME", insu_name);
                    intent.putExtra("INSURANCE_AMOUNT", rent_insurance_amount);
                    intent.putExtra("COUPON_SEQ", coupone_seq);
                    intent.putExtra("POINT_AMOUNT", String.valueOf(use_my_point));
                    intent.putExtra("RESERVE_YEAR", rent_reserve_year);
                    intent.putExtra("RESERVE_DATE", rent_reserve_date);
                    intent.putExtra("RESERVE_TIME", rent_reserve_time);
                    intent.putExtra("RETURN_YEAR", rent_return_year);
                    intent.putExtra("RETURN_DATE", rent_return_date);
                    intent.putExtra("RETURN_TIME", rent_return_time);
                    intent.putExtra("RENTCAR_CAR", rent_rentcar_car);
                    intent.putExtra("RENTCAR_NUM", rent_rentcar_num);
                    intent.putExtra("RENTCAR_AGENT", rent_rentcar_agent);
                    intent.putExtra("RENTCAR_RES_ADD", rent_rentcar_res_add);
                    intent.putExtra("RENTCAR_RET_ADD", rent_rentcar_ret_add);
                    intent.putExtra("CURRENT_AGENT_SEQ", current_agent_seq);
                    intent.putExtra("CURRENT_CAR_SEQ", current_car_seq);

                    startActivity(intent);
                }else {
                    Toast.makeText(com.sincar.customer.sy_rentcar.Rental_payment.this, "약관 동의를 체크해 주세요.", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.rental_btnPointUse:  //포인트 전액 사용하기
                int mPoint = Integer.parseInt(voLoginItem.MY_POINT);    //내포인트

                if(mPoint > (product_pay - use_coupone_pay))
                {
                    //내 포인트가 기본 비용보다 클경우
                    int pay_result = mPoint - (product_pay - use_coupone_pay);
                    use_my_point = product_pay - use_coupone_pay;
                    use_point.setText(setAddMoneyDot(String.valueOf(product_pay - use_coupone_pay)) + "원");
                    mButton.setText("0원 결재하기");
                }else{
                    int pay_result = (product_pay - use_coupone_pay) - mPoint;
                    use_my_point = mPoint;
                    use_point.setText(setAddMoneyDot(String.valueOf(mPoint)) + "원");
                    mButton.setText(setAddMoneyDot(String.valueOf(pay_result)) + "원 결제하기");
                }
                break;

            case R.id.rental_btnCancelDesc:
                //취소 정책 자세히
                intent = new Intent(this, UseTerms2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION); // intent가 호출될때 onUserLeaveHint()가 실행되는것을 차단
                startActivity(intent);
                break;
        }
    }

    private void setPointPopup()
    {
        final EditText et = new EditText(pContext);

        final AlertDialog.Builder alt_bld = new AlertDialog.Builder(pContext,R.style.MyAlertDialogStyle);

        alt_bld.setTitle("포인트 입력")
                .setMessage("사용할 포인트를 입력하세요")
                .setIcon(R.drawable.location)
                .setCancelable(false)
                .setView(et)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String value = et.getText().toString();
                        //user_name.setText(value);
                        try {
                            int mPoint = Integer.parseInt(voLoginItem.MY_POINT);    //내 포인트
                            int input_point = Integer.parseInt(value);

                            if (input_point <= mPoint) {
                                if (input_point >= (product_pay - use_coupone_pay)) {
                                    use_my_point = input_point - (product_pay - use_coupone_pay);
                                } else {
                                    use_my_point = input_point;
                                }
                                my_point.setText(String.valueOf(mPoint-input_point) + "원 보유");
                                use_point.setText(String.valueOf(use_my_point) + "원");
                                total_amt -= use_my_point;
                                mButton.setText(setAddMoneyDot(String.valueOf((product_pay - use_coupone_pay))) + "원 결재하기");
                            } else {
                                Toast.makeText(com.sincar.customer.sy_rentcar.Rental_payment.this, "포인트를 초과 사용하였습니다.", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(com.sincar.customer.sy_rentcar.Rental_payment.this, "다시 입력해주세요", Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 아니오 클릭. dialog 닫기.
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RENTAL_PAYMENT_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                if(!TextUtils.isEmpty(data.getStringExtra("coupone_pay"))) {
                    String value = use_point.getText().toString().trim();
                    int input_point = 0;
                    //특정 문자열 제거(, / 원)
                    value = value.replaceAll(",","");
                    value = value.replaceAll("원","");
                    if(!TextUtils.isEmpty(value))
                    {
                        input_point = Integer.parseInt(value);              //사용 포인트
                    }

                    use_coupone_pay = Integer.parseInt(data.getStringExtra("coupone_pay"));

                    if((input_point + use_coupone_pay) > (product_pay))
                    {
                        amount_TextView.setText(String.valueOf(use_coupone_pay) + "원");
                        coupone_seq = data.getStringExtra("coupone_seq");

                        mButton.setText("0원 결제하기");
                    }else{
                        amount_TextView.setText(String.valueOf(use_coupone_pay) + "원");
                        coupone_seq = data.getStringExtra("coupone_seq");
                        int total_amount = (product_pay) - (input_point + use_coupone_pay);
                        mButton.setText(setAddMoneyDot(String.valueOf(total_amount)) + "원 결제하기");

                    }

                }
            } else {   // RESULT_CANCEL
                Toast.makeText(com.sincar.customer.sy_rentcar.Rental_payment.this, "쿠폰 정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }

        }
    }
//
//    //결재 정보 DB입력
//    private void rentCarRequestReserveInfo() {
//        HashMap<String, String> postParams = new HashMap<String, String>();
//
//        rent_approve_number =  voLoginItem.MEMBER_NO + "R" + Util.getYearMonthDay1();
//
//        postParams.put("MEMBER_NO", voLoginItem.MEMBER_NO);         // 회원번호
//        postParams.put("AMOUNT", rent_amount);                      // 결재 금액
//        postParams.put("APPROVE_NUMBER", rent_approve_number);      // 예약번호
//        postParams.put("RENTCAR_DEL_TYPE", String.valueOf(select_delivery)); //딜리버리 선택방식
//        postParams.put("DELIVERY_AMOUNT", rent_delivery_amount);    // 딜리버리 금액
//        postParams.put("INSURANCE_SEQ", insu_seq);                  //보험 seq
//        postParams.put("INSURANCE_NAME", insu_name);                //보험 명
//        postParams.put("INSURANCE_AMOUNT", rent_insurance_amount);  // 보험 금액
//        postParams.put("COUPON_AMOUNT", coupone_seq);        // 쿠폰 사용
//        postParams.put("POINT_AMOUNT", String.valueOf(use_my_point));          // 포인트 사용양
//        postParams.put("RESERVE_YEAR", rent_reserve_year);          // 예약 연도
//        postParams.put("RESERVE_DATE", rent_reserve_date);          // 예약 날짜
//        postParams.put("RESERVE_TIME", rent_reserve_time);          // 예약 시간
//        postParams.put("RETURN_YEAR", rent_return_year);            // 반납 연도
//        postParams.put("RETURN_DATE", rent_return_date);            // 반납 날짜
//        postParams.put("RETURN_TIME", rent_return_time);            // 반납 시간
//        postParams.put("RENTCAR_CAR", rent_rentcar_car);            // 대여 차량
//        postParams.put("RENTCAR_NUM", rent_rentcar_num);            // 대여 차량 번호
//        postParams.put("RENTCAR_AGENT", rent_rentcar_agent);        // 대여 지점 이름
//        postParams.put("RENTCAR_RES_ADD", rent_rentcar_res_add);    // 딜리버리 배차 위치
//        postParams.put("RENTCAR_RET_ADD", rent_rentcar_ret_add);    // 딜리버리 반납 위치
//
//
//
//        //프로그래스바 시작
//        Util.showDialog(this);
//        //사용내역 요청
//        VolleyNetwork.getInstance(this).serverDataRequest(RENTCAR_PAY_REQUEST, postParams, onReserveResponseListener);
//
//        //test
//        //VolleyNetwork.getInstance(this).serverDataRequest(TEST_REQUEST, postParams, onReserveResponseListener);
//
//    }
//
//    VolleyNetwork.OnResponseListener onReserveResponseListener = new VolleyNetwork.OnResponseListener() {
//        @Override
//        public void onResponseSuccessListener(String serverData) {
//            /*
//                  {"reserve": [{"RESERVE_RESULT":"0","CAUSE":"","MY_POINT":"24000"}]}
//                  {"reserve": [{"RESERVE_RESULT":"1","CAUSE":"형식에 맞지 않습니다.","MY_POINT":""}]}
//             */
//
//            //서버 저장하고 다음 이동
//            Gson gSon = new Gson();
//            try {
//                rentcarReserveResult = gSon.fromJson(serverData, RentcarReserveResult.class);
//
//                voRentcarReserveItem.RENTCAR_response = rentcarReserveResult.rentcarReserve.get(0).RENTCAR_response;
//                voRentcarReserveItem.RENTCAR_message = rentcarReserveResult.rentcarReserve.get(0).RENTCAR_message;
//                voRentcarReserveItem.RENTCAR_MYPOINT = rentcarReserveResult.rentcarReserve.get(0).RENTCAR_MYPOINT;
//
//                //프로그래스바 종료
//                Util.dismiss();
//
//                System.out.println("[spirit]RENTCAR_RESERVE_RESULT ====>" + voRentcarReserveItem.RENTCAR_response);
//                if ("success".equals(voRentcarReserveItem.RENTCAR_response)) {
//                    voLoginItem.MY_POINT = voRentcarReserveItem.RENTCAR_MYPOINT;  //포인트 갱신
//
//                    //성공화면 이동
//                    Intent intent = new Intent(pContext, Rental_approve.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    Toast.makeText(pContext, voRentcarReserveItem.RENTCAR_message + "\n다시 예약 부탁 드립니다.", Toast.LENGTH_SHORT).show();
//
//                    finish();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//
//                //프로그래스바 종료
//                Util.dismiss();
//
//                Toast.makeText(pContext, "오류 발생하였습니다. 다시 예약 부탁 드립니다.", Toast.LENGTH_SHORT).show();
//
//                finish();
//            }
//        }
//
//        @Override
//        public void onResponseFailListener(VolleyError it) {
//
//            NetworkResponse response = it.networkResponse;
//            if(it instanceof ServerError && response != null) {
//                try {
//                    String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
//                    Log.e("VolleyError", res);
//                } catch (UnsupportedEncodingException e1) {
//                    e1.printStackTrace();
//                }
//            }
//            Log.e("VolleyError", "onResponseFailListener : " + it);
//
//            //프로그래스바 종료
//            Util.dismiss();
//        }
//    };

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        homeKeyPressed = true;
        _mMapsActivity.onPause();
    }

}
