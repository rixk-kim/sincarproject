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

public class Rental_list_filter_brand extends AppCompatActivity {

    ImageButton imBack;
    Button btnBrandCheck, btnBrandClear;
    //복수 체크 가능
    CheckBox rb1[] = new CheckBox[6];
    CheckBox rb2[] = new CheckBox[10];
    String brandStrData = "";
    String[] brandArrayData1 = {"현대", "제네시스", "기아", "쌍용", "르노삼성", "쉐보레"};
    String[] brandArrayData2 = {"닛산", "도요타", "마쯔다", "미쓰비시", "GM", "쉐보레", "닛산", "도요타", "마쯔다", "미쓰비시"};
    int[] idList1 = {R.id.rb1_1, R.id.rb1_2, R.id.rb1_3, R.id.rb1_4, R.id.rb1_5, R.id.rb1_6};
    int[] idList2 = {R.id.rb2_1, R.id.rb2_2, R.id.rb2_3, R.id.rb2_4, R.id.rb2_5, R.id.rb2_6, R.id.rb2_7, R.id.rb2_8, R.id.rb2_9, R.id.rb2_10};


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_list_filter_brand);

        imBack = (ImageButton)findViewById(R.id.imBtnBack_rental_list_filter_brand);
        btnBrandCheck = (Button)findViewById(R.id.btnBrandCheck);
        btnBrandClear = (Button)findViewById(R.id.btnBrandClear);
        //6개의 국내 브랜드와 10개의 수입브랜드 체크박스 초기화
        for(int i = 0; i < 6; i++) {
            rb1[i] = (CheckBox)findViewById(idList1[i]);
        }
        for(int i = 0; i < 10; i++) {
            rb2[i] = (CheckBox)findViewById(idList2[i]);
        }

        //취소 버튼
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //체크 버튼 초기화
        btnBrandClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < 6; i ++) {
                    rb1[i].setChecked(false);
                }
                for(int i = 0; i < 10; i ++) {
                    rb2[i].setChecked(false);
                }
            }
        });

        //체크된 버튼을 확인하여 String으로 변환
        btnBrandCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < 6; i ++) {
                    if(rb1[i].isChecked()) {
                        brandStrData += brandArrayData1[i];
                        brandStrData += ",";
                    }
                }
                for(int i = 0; i < 10; i ++) {
                    if(rb2[i].isChecked()) {
                        brandStrData += brandArrayData2[i];
                        brandStrData += ",";
                    }
                }
                //변환된 String을 번들에 적용후 액티비티 종료
                brandStrData = brandStrData.substring(0, brandStrData.length()-1);
                Intent intent = new Intent();
                intent.putExtra("brand_data", brandStrData);
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }
}
