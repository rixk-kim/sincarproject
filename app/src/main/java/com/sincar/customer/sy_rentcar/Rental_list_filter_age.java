package com.sincar.customer.sy_rentcar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.sincar.customer.R;

public class Rental_list_filter_age extends AppCompatActivity {

    ImageButton imBack;
    Button btAgeCheck;
    RadioGroup rgAge,rgAge2;
    RadioButton rb1, rb2, rb3;

    int rb_age_change;

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

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rgAge.clearCheck();
                rgAge2.clearCheck();
                if(!rb1.isChecked()) {
                    rb1.setChecked(true);
                }
                rb_age_change = 0;
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
                rb_age_change = 1;
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
                rb_age_change = 2;
                if (rb1.isChecked()) rb1.setChecked(false);
                if (rb2.isChecked()) rb2.setChecked(false);
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
