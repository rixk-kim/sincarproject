package com.sincar.customer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sincar.customer.adapter.ChargeContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.ChargeContent;
import com.sincar.customer.adapter.content.CouponeContent;
import com.sincar.customer.adapter.content.OptionContent;
import com.sincar.customer.util.Util;

import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.util.Util.setAddMoneyDot;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {
    public final static int PAYMENT_REQ_CODE = 1004;
    private TextView amount_TextView;
    private TextView branch_name, wash_address, reservation_time, my_point;
    private EditText use_point;
    private String coupone_seq = "";
    private CheckBox clause_agree;
    private Context pContext;
    private Button mButton;

    private String reserve_address = ""; //예약주소
    private String reserve_year = "";    //예약년도
    private String reserve_month = "";   //예약월
    private String reserve_day = "";     //예약일
    private String agent_seq = "";       //예약한 대리점주 SEQ
    private String agent_company = "";   //대리점 정보

    private String agent_time = "";      //예약한 대리점주 시간
    private String wash_area = "";       //세차장소
    private String car_company = "";     //제조사
    private String car_name = "";        //차량 이름
    private String car_number = "";      //차번호
    private String car_wash_pay = "";    //차량 기본 세차 금액

    private int total_amt = 0;      //세차비용
    private int use_coupone_seq = 0;    //사용 쿠폰
    private int use_coupone_pay = 0;    //쿠폰 비용
    private int use_my_point = 0;       //사용 포인트
    private String car_wash_option = ""; //옵션

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        pContext = this;

        Intent intent = getIntent(); /*데이터 수신*/
        reserve_address     = intent.getExtras().getString("reserve_address");  /*String형*/
        reserve_year        = intent.getExtras().getString("reserve_year");     /*String형*/
        reserve_month       = intent.getExtras().getString("reserve_month");    /*String형*/
        reserve_day         = intent.getExtras().getString("reserve_day");      /*String형*/
        agent_seq           = intent.getExtras().getString("agent_seq");        /*String형*/
        agent_company       = intent.getExtras().getString("agent_company");      /*String형*/
        agent_time          = intent.getExtras().getString("agent_time");       /*String형*/
        wash_area           = intent.getExtras().getString("wash_area");        /*String형*/

        car_company         = intent.getExtras().getString("car_company");      /*String형*/
        car_name            = intent.getExtras().getString("car_name");         /*String형*/
        car_number          = intent.getExtras().getString("car_number");       /*String형*/
        car_wash_pay        = intent.getExtras().getString("car_wash_pay");     /*String형*/

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
        findViewById(R.id.btnPrev).setOnClickListener(this);
        findViewById(R.id.btnPointUse).setOnClickListener(this);    //포인트 전액 사용

        findViewById(R.id.btnCancelDesc).setOnClickListener(this);    //취소정책 : 자세히

        ((TextView)findViewById(R.id.title_activity)).setText(R.string.payment_title);
        branch_name = (TextView)findViewById(R.id.branch_name);
        branch_name.setText(agent_company); //지점명

        wash_address = (TextView)findViewById(R.id.wash_address);
        wash_address.setText(reserve_address + "(" + wash_area + ")"); //세차장소

        reservation_time = (TextView)findViewById(R.id.reservation_time);
        if(reserve_month.length() < 2) reserve_month = "0" + reserve_month;
        String dayofday = Util.getDateDay(reserve_year+reserve_month+reserve_day);
        reservation_time.setText(reserve_month + "/" + reserve_day + " (" + dayofday + ") " + agent_time); //예약시간

        findViewById(R.id.btnNext).setVisibility(View.INVISIBLE);
        findViewById(R.id.btnCouponRegister).setOnClickListener(this);
        amount_TextView = (TextView) findViewById(R.id.coupon_amount);
        clause_agree = (CheckBox) findViewById(R.id.clause_agree);
        mButton = (Button) findViewById(R.id.reserve_confirm_btn);
        mButton.setOnClickListener(this);
        mButton.setText("결재하기");

        //포인트
        my_point = (TextView) findViewById(R.id.my_point);
        my_point.setText(Util.setAddMoneyDot(voLoginItem.MY_POINT) + "원 보유");

        use_point = (EditText) findViewById(R.id.use_point);
        use_point.setOnClickListener(this);

        use_point.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // 확인 버튼 눌렀을 때
                    String value = use_point.getText().toString().trim();

                    int mPoint = Integer.parseInt(voLoginItem.MY_POINT);    //내 포인트
                    int input_point = Integer.parseInt(value);

                    if (input_point <= mPoint) {
                        if (input_point >= total_amt) {
                            use_my_point = input_point - total_amt;
                        } else {
                            use_my_point = input_point;
                        }
//                        my_point.setText(setAddMoneyDot(String.valueOf(mPoint-input_point)) + "원 보유");
                        use_point.setText(setAddMoneyDot(String.valueOf(use_my_point)) + "원");
                        use_point.setSelection(use_point.getText().toString().trim().length()); //커서를 끝에 위치!
                        total_amt -= use_my_point;
                        mButton.setText(setAddMoneyDot(String.valueOf(total_amt)) + "원 결재하기");
                    } else {
                        Toast.makeText(PaymentActivity.this, "포인트를 초과 사용하였습니다.", Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }
        });

        // TODO - 서버 연동 후 PointContent.ITEMS에 리스 항목 추가 작업
        ChargeContent.clearItem(); //초기화

       //ChargeItem(0,"스팀세차 내부/외부 (소형)", "50,000원");
        ChargeContent.addItem(new ChargeContent.ChargeItem(0, "스팀세차 내부/외부", car_wash_pay));
        total_amt = Integer.parseInt(car_wash_pay);

        for(int i = 0; i < OptionContent.ITEMS.size(); i++) {
            if(OptionContent.ITEMS.get(i).checked) {
                ChargeContent.addItem(new ChargeContent.ChargeItem(i+1, OptionContent.ITEMS.get(i).option_name, OptionContent.ITEMS.get(i).option_pay));
                total_amt += Integer.parseInt(OptionContent.ITEMS.get(i).option_pay);

                if(i+1 >= OptionContent.ITEMS.size()) {
                    car_wash_option += OptionContent.ITEMS.get(i).option_name;
                }else{
                    car_wash_option += OptionContent.ITEMS.get(i).option_name + "/";
                }
            }
 //           ChargeContent.addItem(new ChargeContent.ChargeItem(1, "가니쉬코팅", "6,000원"));

        }

        // Set the adapter - 포인트 리스트 설정
        View view = findViewById(R.id.paymentItemList);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new ChargeContentRecyclerViewAdapter(ChargeContent.ITEMS));
        }

        mButton.setText(setAddMoneyDot(String.valueOf(total_amt)) + "원 결재하기");
    }

//    @Override
//    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//        // TODO Auto-generated method stub
//
//        //오버라이드한 onEditorAction() 메소드
//
//        if(v.getId()==R.id.use_point && actionId==EditorInfo.IME_ACTION_DONE){ // 뷰의 id를 식별, 키보드의 완료 키 입력 검출
//
//            //이 부분에 원하는 이벤트를 작성합니다
//            //이 부분에 원하는 이벤트를 작성합니다
//            //이 부분에 원하는 이벤트를 작성합니다
//// 입력이 끝났을 때
//            int input_point = Integer.parseInt(use_point.getText().toString().trim());
//            int mPoint = Integer.parseInt(voLoginItem.MY_POINT);
//            if(input_point > mPoint)
//            {
//                Toast.makeText(PaymentActivity.this, "보유 포인트를 초과하였습니다.", Toast.LENGTH_SHORT).show();
//            }else {
//                if (mPoint >= total_amt) {
//                    use_my_point = mPoint - total_amt;
//                } else {
//                    use_my_point = mPoint;
//                }
//                my_point.setText("0원 보유");
//                use_point.setText(String.valueOf(use_my_point) + "원");
//                total_amt -= use_my_point;
//                mButton.setText(setAddMoneyDot(String.valueOf(total_amt)) + "원 결재하기");
//            }
//        }
//
//        return false;
//    }


    @Override
    public void onClick(View v) {
        Intent intent;

        switch(v.getId())
        {
            case R.id.btnPrev:
                finish();
                break;
            case R.id.btnCouponRegister:
                //기존 쿠폰 적용 금액이 있다면 원복
                if(!TextUtils.isEmpty(coupone_seq) && use_coupone_pay > 0) {
                    total_amt += use_coupone_pay;

                    use_coupone_pay = 0;
                    coupone_seq = "";

                    amount_TextView.setText(String.valueOf(use_coupone_pay) + "원");
                    mButton.setText(setAddMoneyDot(String.valueOf(total_amt)) + "원 결재하기");
                    Toast.makeText(PaymentActivity.this, "쿠폰 금액 조정", Toast.LENGTH_SHORT).show();
                }

                intent = new Intent(this, CouponeActivity.class);
                intent.putExtra("path", "payment");
                startActivityForResult(intent, PAYMENT_REQ_CODE);
                break;

            case R.id.reserve_confirm_btn:
                if(clause_agree.isChecked() == true){
                    // 예약으로 이동
                    //reserveSelect();
                    intent = new Intent(pContext, PayApproveActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("reserve_address", reserve_address);   //주소
                    bundle.putString("reserve_year", reserve_year);         //년
                    bundle.putString("reserve_month", reserve_month);       //월
                    bundle.putString("reserve_day", reserve_day);           //일
                    bundle.putString("agent_seq", agent_seq);               //예약 대리점주 seq
                    bundle.putString("agent_company", agent_company);       //예약 대리점주
                    bundle.putString("agent_time", agent_time);             //예약시간
                    bundle.putString("wash_area", wash_area);               //세차장소(실내/실외)
                    bundle.putString("car_wash_option", car_wash_option);   //옵션(가니쉬코팅/에머랄드 코팅)
                    bundle.putString("car_company", car_company);           //제조사
                    bundle.putString("car_name", car_name);                 //차량 이름
                    bundle.putString("car_number", car_number);             //차번호
//                    bundle.putString("car_wash_pay", car_wash_pay);         //차량 기본 세차 금액
                    bundle.putString("use_my_point", String.valueOf(use_my_point));         //사용 포인트
                    bundle.putString("use_coupone_seq", coupone_seq);                       //사용 쿠폰 seq
                    bundle.putString("total_amt", String.valueOf(total_amt));               //총 결제 금액

                    //부가서비스
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else {
                    Toast.makeText(PaymentActivity.this, "약관 동의를 체크해 주세요.", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.btnPointUse:
                //포인트 전액 사용하기
                //my_point.setText(Util.setAddMoneyDot(voLoginItem.MY_POINT) + "원 보유");
                //total_amt -= Integer.parseInt(data.getStringExtra("coupone_pay"));
                int mPoint = Integer.parseInt(voLoginItem.MY_POINT);
                if(use_my_point < mPoint) {
                    if (mPoint >= total_amt) {
                        use_my_point = mPoint - total_amt;
                    } else {
                        use_my_point = mPoint;
                    }
                    my_point.setText("0원 보유");
                    use_point.setText(String.valueOf(use_my_point) + "원");
                    total_amt -= use_my_point;
                    mButton.setText(setAddMoneyDot(String.valueOf(total_amt)) + "원 결재하기");
                }else{
                    Toast.makeText(PaymentActivity.this, "포인트 전액 사용하였습니다.", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.btnCancelDesc:
                //취소 정책 자세히
                intent = new Intent(this, UseTerms2Activity.class);
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
                                if (input_point >= total_amt) {
                                    use_my_point = input_point - total_amt;
                                } else {
                                    use_my_point = input_point;
                                }
                                my_point.setText(String.valueOf(mPoint-input_point) + "원 보유");
                                use_point.setText(String.valueOf(use_my_point) + "원");
                                total_amt -= use_my_point;
                                mButton.setText(setAddMoneyDot(String.valueOf(total_amt)) + "원 결재하기");
                            } else {
                                Toast.makeText(PaymentActivity.this, "포인트를 초과 사용하였습니다.", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(PaymentActivity.this, "다시 입력해주세요", Toast.LENGTH_LONG).show();
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

    private void reserveSelect()
    {
        final String [] items = {"신용카드", "계좌이체", "휴대폰결재", "가상계좌"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PaymentActivity.this);
        builder.setTitle("결재방법 선택");

        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PaymentActivity.this, items[which] + " 라이브러리로 이동합니다.", Toast.LENGTH_LONG).show();
                // TODO - 결재 모듈로 이동합니다.

                dialog.dismiss(); // 누르면 바로 닫히는 형태

                Intent intent = new Intent(pContext, PayApproveActivity.class);
                startActivity(intent);

            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYMENT_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                if(!TextUtils.isEmpty(data.getStringExtra("coupone_pay"))) {
                    use_coupone_pay = Integer.parseInt(data.getStringExtra("coupone_pay"));
                    amount_TextView.setText(String.valueOf(use_coupone_pay) + "원");
                    coupone_seq = data.getStringExtra("coupone_seq");

                    total_amt -= Integer.parseInt(data.getStringExtra("coupone_pay"));
                    mButton.setText(setAddMoneyDot(String.valueOf(total_amt)) + "원 결재하기");
//                    Toast.makeText(PaymentActivity.this, "Result: " + data.getStringExtra("coupone_seq"), Toast.LENGTH_SHORT).show();
                }
            } else {   // RESULT_CANCEL
                Toast.makeText(PaymentActivity.this, "쿠폰 정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
