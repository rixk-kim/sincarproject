package com.sincar.customer.sy_rentcar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.sincar.customer.R;

public class Rental_list_filter_age extends AppCompatActivity {

    ImageButton imBack;
    Button btAgeCheck;
    RadioGroup rgAge;

    int rb_age_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_list_filter_age);

        imBack = (ImageButton)findViewById(R.id.imBtnBack_rental_list_filter_age);
        btAgeCheck = (Button)findViewById(R.id.btnAgeCheck);
        rgAge = (RadioGroup)findViewById(R.id.rg_rental_list_filter_age);

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rgAge.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.rbAgeAll:
                        rb_age_change = 0;
                        break;
                    case R.id.rbAge21:
                        rb_age_change = 1;
                        break;
                    case R.id.rbAge26:
                        rb_age_change = 2;
                        break;
                    default:
                        break;
                }
            }
        });

        btAgeCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("age_data", rb_age_change);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
