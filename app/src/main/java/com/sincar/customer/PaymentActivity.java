package com.sincar.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.sincar.customer.adapter.ChargeContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.ChargeContent;
import com.sincar.customer.adapter.content.CouponeContent;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {
    public final static int PAYMENT_REQ_CODE = 1003;
    private TextView amount_TextView;
    private String coupone_seq;
    private CheckBox clause_agree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        findViewById(R.id.btnPrev).setOnClickListener(this);
        ((TextView)findViewById(R.id.title_activity)).setText(R.string.payment_title);
        findViewById(R.id.btnNext).setVisibility(View.INVISIBLE);
        findViewById(R.id.btnCouponRegister).setOnClickListener(this);
        amount_TextView = (TextView) findViewById(R.id.coupon_amount);
        clause_agree = (CheckBox) findViewById(R.id.clause_agree);
        findViewById(R.id.reserve_confirm_btn).setOnClickListener(this);

        // TODO - 서버 연동 후 PointContent.ITEMS에 리스 항목 추가 작업
        //ChargeItem(position,"스팀세차 내부/외부 (소형)", "50,000원");
        ChargeContent.clearItem(); //초기화

        ChargeContent.addItem(new ChargeContent.ChargeItem(0,"스팀세차 내부/외부 (소형)", "50,000원"));
        ChargeContent.addItem(new ChargeContent.ChargeItem(1,"가니쉬코팅", "6,000원"));

        // Set the adapter - 포인트 리스트 설정
        View view = findViewById(R.id.paymentItemList);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new ChargeContentRecyclerViewAdapter(ChargeContent.ITEMS));
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch(v.getId())
        {
            case R.id.btnPrev:
                finish();
                break;
            case R.id.btnCouponRegister:
                intent = new Intent(this, CouponeActivity.class);
                intent.putExtra("path", "payment");
                startActivityForResult(intent, PAYMENT_REQ_CODE);
                break;

            case R.id.reserve_confirm_btn:
                if(clause_agree.isChecked() == true){
                    // 예약으로 이동
                    reserveSelect();
                }else {
                    Toast.makeText(PaymentActivity.this, "약관 동의를 체크해 주세요.", Toast.LENGTH_LONG).show();
                }

                break;
        }

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
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYMENT_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                amount_TextView.setText(data.getStringExtra("coupone_pay"));
                coupone_seq = data.getStringExtra("coupone_seq");
                Toast.makeText(PaymentActivity.this, "Result: " + data.getStringExtra("coupone_seq"), Toast.LENGTH_SHORT).show();
            } else {   // RESULT_CANCEL
                Toast.makeText(PaymentActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
