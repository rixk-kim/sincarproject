package com.sincar.customer.sy_rentcar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.sincar.customer.R;
import com.sincar.customer.sy_rentcar.Rental_list_filter.type_filter;

public class Rental_list_filter_type extends AppCompatActivity {

    ImageButton imBack;
    Button btnTypeCheck,btnTypeClear;
    CheckBox rb[] = new CheckBox[10];
    String typeStrData = "";
//    String[] typeArrayData = {"경차", "세단", "스포츠", "R/V", "SUV", "승합", "밴", "컨버터블", "쿠페", "트럭"};
    int[] idList = {R.id.rb0, R.id.rb1, R.id.rb2, R.id.rb3, R.id.rb4, R.id.rb5, R.id.rb6, R.id.rb7, R.id.rb8, R.id.rb9};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_list_filter_type);
        imBack = (ImageButton)findViewById(R.id.imBtnBack_rental_list_filter_type);
        btnTypeCheck = (Button)findViewById(R.id.btnTypeCheck);
        btnTypeClear = (Button)findViewById(R.id.btntypeClear);
        //10개의 외형 타입 체크 박스 초기화
        for(int i = 0; i < 10; i++) {
            rb[i] = (CheckBox)findViewById(idList[i]);
        }

        //취소 버튼
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //적용된 체크 초기화
        btnTypeClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < 10; i ++) {
                    rb[i].setChecked(false);
                }
            }
        });

        btnTypeCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //체크된 조건을 String으로 변환
//                for(int i = 0; i < 10; i ++) {
//                    if(rb[i].isChecked()) {
//                        typeStrData += typeArrayData[i];
//                        typeStrData += ",";
//                    }
//                }
                type_filter[] typeArr = type_filter.values();
                for(type_filter d: typeArr) {
                    if(rb[d.ordinal()].isChecked()) {
                        typeStrData += d.getValue();
                        typeStrData += ",";
                    }
                }
                //변환된 String을 번들에 적용후 액티비티 종료
                typeStrData = typeStrData.substring(0, typeStrData.length()-1);
                Intent intent = new Intent();
                intent.putExtra("type_data", typeStrData);
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }
}
