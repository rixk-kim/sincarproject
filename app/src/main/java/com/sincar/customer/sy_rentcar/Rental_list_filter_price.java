package com.sincar.customer.sy_rentcar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.sincar.customer.R;

public class Rental_list_filter_price extends AppCompatActivity {

    ImageButton imBack;
    Button btnPriCheck;
    RadioGroup rgPri;

    int rbPriceChange;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_list_filter_price);
        imBack = (ImageButton)findViewById(R.id.imBtnBack_rental_list_filter_price);
        btnPriCheck = (Button)findViewById(R.id.btnPriceCheck);
        rgPri = (RadioGroup)findViewById(R.id.rg_rental_list_filter_price);

        rgPri.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.rbPriall:
                        rbPriceChange = 0;
                        break;
                    case R.id.rbPri10u:
                        rbPriceChange = 1;
                        break;
                    case R.id.rbPri20u:
                        rbPriceChange = 2;
                        break;
                    case R.id.rbPri30u:
                        rbPriceChange = 3;
                        break;
                    case R.id.rbPri40u:
                        rbPriceChange = 4;
                        break;
                    case R.id.rbPri50u:
                        rbPriceChange = 5;
                        break;
                    case R.id.rbPri50o:
                        rbPriceChange = 6;
                        break;
                    default:
                        break;
                }
            }
        });

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnPriCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("price_data", rbPriceChange);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
