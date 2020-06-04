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

public class Rental_list_filter_type extends AppCompatActivity {

    ImageButton imBack;
    Button btnTypeCheck,btnTypeClear;
    CheckBox rb[] = new CheckBox[10];
    String typeStrData = "";
    String[] typeArrayData = {"경차", "세단", "스포츠", "R/V", "SUV", "승합", "밴", "컨버터블", "쿠페", "트럭"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_list_filter_type);
        imBack = (ImageButton)findViewById(R.id.imBtnBack_rental_list_filter_type);
        btnTypeCheck = (Button)findViewById(R.id.btnTypeCheck);
        btnTypeClear = (Button)findViewById(R.id.btntypeClear);
        rb[0] = (CheckBox)findViewById(R.id.rb0);
        rb[1] = (CheckBox)findViewById(R.id.rb1);
        rb[2] = (CheckBox)findViewById(R.id.rb2);
        rb[3] = (CheckBox)findViewById(R.id.rb3);
        rb[4] = (CheckBox)findViewById(R.id.rb4);
        rb[5] = (CheckBox)findViewById(R.id.rb5);
        rb[6] = (CheckBox)findViewById(R.id.rb6);
        rb[7] = (CheckBox)findViewById(R.id.rb7);
        rb[8] = (CheckBox)findViewById(R.id.rb8);
        rb[9] = (CheckBox)findViewById(R.id.rb9);

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

                for(int i = 0; i < 10; i ++) {
                    if(rb[i].isChecked()) {
                        typeStrData += typeArrayData[i];
                        typeStrData += ",";
                    }
                }
                typeStrData = typeStrData.substring(0, typeStrData.length()-1);
                Intent intent = new Intent();
                intent.putExtra("type_data", typeStrData);
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }
}
