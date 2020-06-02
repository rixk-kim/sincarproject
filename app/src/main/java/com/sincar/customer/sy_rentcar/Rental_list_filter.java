package com.sincar.customer.sy_rentcar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.sincar.customer.R;

public class Rental_list_filter extends AppCompatActivity {

    ConstraintLayout btnAge, btnBrand, btnPrice, btnType;
    ImageButton imBack;
    Intent intent;
    Button btnClear;
    TextView tvAge, tvPrice, tvType, tvBrand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_list_filter);

        btnAge = (ConstraintLayout) findViewById(R.id.btnAge);
        btnBrand = (ConstraintLayout) findViewById(R.id.btnBrand);
        btnPrice = (ConstraintLayout) findViewById(R.id.btnPrice);
        btnType = (ConstraintLayout) findViewById(R.id.btnType);
        btnClear = (Button)findViewById(R.id.btn_rentalCar_filter_clear);
        tvAge = (TextView)findViewById(R.id.tvAge);
        tvPrice = (TextView)findViewById(R.id.tvPrice);
        tvType = (TextView)findViewById(R.id.tvType);
        tvBrand = (TextView)findViewById(R.id.tvBrand);

        imBack = (ImageButton)findViewById(R.id.imBtn_rental_list_filter);

        btnAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Rental_list_filter_age.class);
                startActivityForResult(intent, 0);
            }
        });

        btnPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Rental_list_filter_price.class);
                startActivityForResult(intent, 1);
            }
        });


        btnType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Rental_list_filter_type.class);
                startActivityForResult(intent, 2);
            }
        });

        btnBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Rental_list_filter_brand.class);
                startActivityForResult(intent, 3);
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAge.setText("전체");
                tvPrice.setText("전체");
                tvType.setText("전체");
                tvBrand.setText("전체");
            }
        });

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0 && resultCode == RESULT_OK) {
            int i = data.getIntExtra("age_data", 0);
            rental_list_filter_ageSelect(i);
        }
        else if(requestCode == 1 && resultCode == RESULT_OK) {
            int i = data.getIntExtra("price_data", 0);
            rental_list_filter_priceSelect(i);
        }
        else if(requestCode == 2 && resultCode == RESULT_OK) {
            String type_data = data.getStringExtra("type_data");
            tvType.setText(type_data);
        }
        else if(requestCode == 3 && resultCode == RESULT_OK) {
            String type_data = data.getStringExtra("brand_data");
            tvBrand.setText(type_data);
        }

    }

    public void rental_list_filter_ageSelect(int i) {
        switch (i) {
            case 0:
                tvAge.setText("전체");
                break;
            case 1:
                tvAge.setText("만 21세 이상");
                break;
            case 2:
                tvAge.setText("만 26세 이상");
                break;
            default:
                break;
        }
    }

    public void rental_list_filter_priceSelect(int i) {
        switch (i) {
            case 0:
                tvPrice.setText("전체");
                break;
            case 1:
                tvPrice.setText("10만원 이하");
                break;
            case 2:
                tvPrice.setText("20만원 이하");
                break;
            case 3:
                tvPrice.setText("30만원 이하");
                break;
            case 4:
                tvPrice.setText("40만원 이하");
                break;
            case 5:
                tvPrice.setText("50만원 이하");
                break;
            case 6:
                tvPrice.setText("50만원 이상");
                break;
            default:
                break;
        }
    }

}
