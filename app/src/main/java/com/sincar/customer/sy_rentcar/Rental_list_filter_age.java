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
import com.sincar.customer.sy_rentcar.Rental_list_filter.age_filter;

public class Rental_list_filter_age extends AppCompatActivity {

    ImageButton imBack;
    Button btAgeCheck;
    RadioGroup rgAge,rgAge2; //버튼 위치 배치 디스플레이를 위해 라디오 그룹 2개 설정
    RadioButton rb1, rb2, rb3; //라디오 버튼3개 설정(전체,만 21세이상,만 26세 이상)

    age_filter rb_age_change = age_filter.all; // 설정된 나이 조건을 구분 짓기 위한 변수 0:전체 1:만 21세 2: 만 26세

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_list_filter_age);

        imBack = (ImageButton)findViewById(R.id.imBtnBack_rental_list_filter_age);
        btAgeCheck = (Button)findViewById(R.id.btnAgeCheck);
        rgAge = findViewById(R.id.rg_rental_list_filter_age);
        rgAge2 = findViewById(R.id.rg_rental_list_filter_age2);
        rb1 = findViewById(R.id.rbAgeAll);
        rb2 = findViewById(R.id.rbAge21);
        rb3 = findViewById(R.id.rbAge26);

        //화면 종료
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //버튼이 클릭 되어지면 체크된 라디오버튼만 활성화 하고 나머지 버튼은 비활성화
        //나이조건 구분 변수에 구분된 데이터 대입 (0 ~ 2)
        rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgAge.clearCheck();
                rgAge2.clearCheck();
                if(!rb1.isChecked()) {
                    rb1.setChecked(true);
                }

                rb_age_change = age_filter.all;
                if (rb2.isChecked()) rb2.setChecked(false);
                if (rb3.isChecked()) rb3.setChecked(false);
            }
        });

        rb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgAge.clearCheck();
                rgAge2.clearCheck();
                if(!rb2.isChecked()) {
                    rb2.setChecked(true);
                }
                rb_age_change = age_filter._21over;
                if (rb1.isChecked()) rb1.setChecked(false);
                if (rb3.isChecked()) rb3.setChecked(false);
            }
        });

        rb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgAge.clearCheck();
                rgAge2.clearCheck();
                if(!rb3.isChecked()) {
                    rb3.setChecked(true);
                }
                rb_age_change = age_filter._26over;
                if (rb1.isChecked()) rb1.setChecked(false);
                if (rb2.isChecked()) rb2.setChecked(false);
            }
        });

        //현재 적용된 조건을 필터에 적용 및 액티비티 종료
        btAgeCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //조건정보 적용된 변수 번들로 데이터 전달
                intent.putExtra("age_data", rb_age_change.getValue());
                intent.putExtra("age", rb_age_change);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
