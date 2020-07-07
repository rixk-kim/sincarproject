package com.sincar.customer.sy_rentcar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.sincar.customer.R;
import com.sincar.customer.sy_rentcar.Rental_list_filter.price_filter;

public class Rental_list_filter_price extends AppCompatActivity {

    ImageButton imBack;
    Button btnPriCheck;
    RadioGroup rgPri, rgPri2; //버튼 위치 배치 디스플레이를 위해 라디오 그룹 2개 설정
    RadioButton rb1, rb2, rb3, rb4, rb5, rb6, rb7; // 7개의 라디오버튼 변수 선언

    price_filter rbPriceChange; // 설정된 가격 조건을 구분 짓기 위한 변수 0: 전체 1: 10만원이하 2: 20만원 이하 ~~

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_list_filter_price);
        imBack = (ImageButton)findViewById(R.id.imBtnBack_rental_list_filter_price);
        btnPriCheck = (Button)findViewById(R.id.btnPriceCheck);
        rgPri = (RadioGroup)findViewById(R.id.rg_rental_list_filter_price);
        rgPri2 = (RadioGroup)findViewById(R.id.rg_rental_list_filter_price2);
        rb1 = (RadioButton)findViewById(R.id.rbPriall);
        rb2 = (RadioButton)findViewById(R.id.rbPri10u);
        rb3 = (RadioButton)findViewById(R.id.rbPri20u);
        rb4 = (RadioButton)findViewById(R.id.rbPri30u);
        rb5 = (RadioButton)findViewById(R.id.rbPri40u);
        rb6 = (RadioButton)findViewById(R.id.rbPri50u);
        rb7 = (RadioButton)findViewById(R.id.rbPri50o);

        //라디오 버튼 6개 중 1개 클릭시 나머지 버튼은 비활성화
        //가격조건 구분 변수에 구분된 데이터 대입 (0 ~ 6)
        rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgPri.clearCheck();
                rgPri2.clearCheck();
                if(!rb1.isChecked()) {
                    rb1.setChecked(true);
                }
                rbPriceChange = price_filter.all;
                if (rb2.isChecked()) rb2.setChecked(false);
                if (rb3.isChecked()) rb3.setChecked(false);
                if (rb4.isChecked()) rb4.setChecked(false);
                if (rb5.isChecked()) rb5.setChecked(false);
                if (rb6.isChecked()) rb6.setChecked(false);
                if (rb7.isChecked()) rb7.setChecked(false);
            }
        });

        rb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgPri.clearCheck();
                rgPri2.clearCheck();
                if(!rb2.isChecked()) {
                    rb2.setChecked(true);
                }
                rbPriceChange = price_filter._10under;
                if (rb1.isChecked()) rb1.setChecked(false);
                if (rb3.isChecked()) rb3.setChecked(false);
                if (rb4.isChecked()) rb4.setChecked(false);
                if (rb5.isChecked()) rb5.setChecked(false);
                if (rb6.isChecked()) rb6.setChecked(false);
                if (rb7.isChecked()) rb7.setChecked(false);
            }
        });

        rb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgPri.clearCheck();
                rgPri2.clearCheck();
                if(!rb3.isChecked()) {
                    rb3.setChecked(true);
                }
                rbPriceChange = price_filter._20under;
                if (rb1.isChecked()) rb1.setChecked(false);
                if (rb2.isChecked()) rb2.setChecked(false);
                if (rb4.isChecked()) rb4.setChecked(false);
                if (rb5.isChecked()) rb5.setChecked(false);
                if (rb6.isChecked()) rb6.setChecked(false);
                if (rb7.isChecked()) rb7.setChecked(false);
            }
        });

        rb4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgPri.clearCheck();
                rgPri2.clearCheck();
                if(!rb4.isChecked()) {
                    rb4.setChecked(true);
                }
                rbPriceChange = price_filter._30under;
                if (rb1.isChecked()) rb1.setChecked(false);
                if (rb2.isChecked()) rb2.setChecked(false);
                if (rb3.isChecked()) rb3.setChecked(false);
                if (rb5.isChecked()) rb5.setChecked(false);
                if (rb6.isChecked()) rb6.setChecked(false);
                if (rb7.isChecked()) rb7.setChecked(false);
            }
        });

        rb5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgPri.clearCheck();
                rgPri2.clearCheck();
                if(!rb5.isChecked()) {
                    rb5.setChecked(true);
                }
                rbPriceChange = price_filter._40under;
                if (rb1.isChecked()) rb1.setChecked(false);
                if (rb2.isChecked()) rb2.setChecked(false);
                if (rb3.isChecked()) rb3.setChecked(false);
                if (rb4.isChecked()) rb4.setChecked(false);
                if (rb6.isChecked()) rb6.setChecked(false);
                if (rb7.isChecked()) rb7.setChecked(false);
            }
        });

        rb6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgPri.clearCheck();
                rgPri2.clearCheck();
                if(!rb6.isChecked()) {
                    rb6.setChecked(true);
                }
                rbPriceChange = price_filter._50under;
                if (rb1.isChecked()) rb1.setChecked(false);
                if (rb2.isChecked()) rb2.setChecked(false);
                if (rb3.isChecked()) rb3.setChecked(false);
                if (rb4.isChecked()) rb4.setChecked(false);
                if (rb5.isChecked()) rb5.setChecked(false);
                if (rb7.isChecked()) rb7.setChecked(false);
            }
        });

        rb7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgPri.clearCheck();
                rgPri2.clearCheck();
                if(!rb7.isChecked()) {
                    rb7.setChecked(true);
                }
                rbPriceChange = price_filter._50over;
                if (rb1.isChecked()) rb1.setChecked(false);
                if (rb2.isChecked()) rb2.setChecked(false);
                if (rb3.isChecked()) rb3.setChecked(false);
                if (rb4.isChecked()) rb4.setChecked(false);
                if (rb5.isChecked()) rb5.setChecked(false);
                if (rb6.isChecked()) rb6.setChecked(false);
            }
        });

        //취소 버튼
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //조건 적용 버튼
        btnPriCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("price_data", rbPriceChange.getValue());
                intent.putExtra("price", rbPriceChange);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
