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
    CheckBox rb1[] = new CheckBox[6];
    CheckBox rb2[] = new CheckBox[10];
    String brandStrData = "";
    String[] brandArrayData1 = {"현대", "제네시스", "기아", "쌍용", "르노삼성", "쉐보레"};
    String[] brandArrayData2 = {"닛산", "도요타", "마쯔다", "미쓰비시", "GM", "쉐보레", "닛산", "도요타", "마쯔다", "미쓰비시"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_list_filter_brand);

        imBack = (ImageButton)findViewById(R.id.imBtnBack_rental_list_filter_brand);
        btnBrandCheck = (Button)findViewById(R.id.btnBrandCheck);
        btnBrandClear = (Button)findViewById(R.id.btnBrandClear);
        rb1[0] = (CheckBox)findViewById(R.id.rb1_1);
        rb1[1] = (CheckBox)findViewById(R.id.rb1_2);
        rb1[2] = (CheckBox)findViewById(R.id.rb1_3);
        rb1[3] = (CheckBox)findViewById(R.id.rb1_4);
        rb1[4] = (CheckBox)findViewById(R.id.rb1_5);
        rb1[5] = (CheckBox)findViewById(R.id.rb1_6);
        rb2[0] = (CheckBox)findViewById(R.id.rb2_1);
        rb2[1] = (CheckBox)findViewById(R.id.rb2_2);
        rb2[2] = (CheckBox)findViewById(R.id.rb2_3);
        rb2[3] = (CheckBox)findViewById(R.id.rb2_4);
        rb2[4] = (CheckBox)findViewById(R.id.rb2_5);
        rb2[5] = (CheckBox)findViewById(R.id.rb2_6);
        rb2[6] = (CheckBox)findViewById(R.id.rb2_7);
        rb2[7] = (CheckBox)findViewById(R.id.rb2_8);
        rb2[8] = (CheckBox)findViewById(R.id.rb2_9);
        rb2[9] = (CheckBox)findViewById(R.id.rb2_10);

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
                brandStrData = brandStrData.substring(0, brandStrData.length()-1);
                Intent intent = new Intent();
                intent.putExtra("brand_data", brandStrData);
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }
}
