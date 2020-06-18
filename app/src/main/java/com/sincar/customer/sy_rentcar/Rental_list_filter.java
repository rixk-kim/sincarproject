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

    ConstraintLayout btnAge, btnBrand, btnPrice, btnType; //4개의 레이아웃을 버튼으로 활성화
    ImageButton imBack;
    Intent intent;
    Button btnClear, btnAccept;
    TextView tvAge, tvPrice, tvType, tvBrand; //필터로부터 적용될 텍스트뷰
    public static String rent_car_list_age, rent_car_list_price, rent_car_list_type, rent_car_list_brand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_list_filter);

        rent_car_list_age = "전체";
        rent_car_list_price = "전체";
        rent_car_list_type = "전체";
        rent_car_list_brand= "전체";

        btnAge = (ConstraintLayout) findViewById(R.id.btnAge);
        btnBrand = (ConstraintLayout) findViewById(R.id.btnBrand);
        btnPrice = (ConstraintLayout) findViewById(R.id.btnPrice);
        btnType = (ConstraintLayout) findViewById(R.id.btnType);
        btnClear = (Button)findViewById(R.id.btn_rentalCar_filter_clear);
        btnAccept = (Button)findViewById(R.id.btnAccept);
        tvAge = (TextView)findViewById(R.id.tvAge);
        tvPrice = (TextView)findViewById(R.id.tvPrice);
        tvType = (TextView)findViewById(R.id.tvType);
        tvBrand = (TextView)findViewById(R.id.tvBrand);

        imBack = (ImageButton)findViewById(R.id.imBtn_rental_list_filter);

        //나이조건 필터 액티비티로 전환
        btnAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Rental_list_filter_age.class);
                startActivityForResult(intent, 0);
            }
        });
        //가격조건 필터 액티비티로 전환
        btnPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Rental_list_filter_price.class);
                startActivityForResult(intent, 1);
            }
        });
        //외형조건 필터 액티비티로 전환
        btnType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Rental_list_filter_type.class);
                startActivityForResult(intent, 2);
            }
        });
        //브랜드조건 필터 액티비티로 전환
        btnBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Rental_list_filter_brand.class);
                startActivityForResult(intent, 3);
            }
        });
        //적용된 필터들을 초기화
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAge.setText("전체");
                tvPrice.setText("전체");
                tvType.setText("전체");
                tvBrand.setText("전체");
                rent_car_list_age = "전체";
                rent_car_list_price = "전체";
                rent_car_list_type = "전체";
                rent_car_list_brand= "전체";
            }
        });

        //필터를 렌탈카 리스트 적용 취소
        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //필터를 렌탈카 리스트 적용
        //현재는 샘플 페이지임으로 추후 대대적 수정 필요
        btnAccept.setOnClickListener(new View.OnClickListener() {
            Intent backToList = new Intent();

            @Override
            public void onClick(View v) {
                backToList.putExtra("age_data", rent_car_list_age);
                backToList.putExtra("price_data", rent_car_list_price);
                backToList.putExtra("type_data", rent_car_list_type);
                backToList.putExtra("brand_data", rent_car_list_brand);
                setResult(RESULT_OK, backToList);
                finish();
            }
        });
    }

    //필터 조건을 설정후 돌아올 경우 호출되는 메소드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //나이 조건
        if(requestCode == 0 && resultCode == RESULT_OK) {
            int i = data.getIntExtra("age_data", 0);
            rental_list_filter_ageSelect(i);
        }
        //가격 조건
        else if(requestCode == 1 && resultCode == RESULT_OK) {
            int i = data.getIntExtra("price_data", 0);
            rental_list_filter_priceSelect(i);
        }
        //외형 조건
        else if(requestCode == 2 && resultCode == RESULT_OK) {
            String type_data = data.getStringExtra("type_data");
            rent_car_list_type = type_data;
            tvType.setText(type_data);
        }
        //브랜드 조건
        else if(requestCode == 3 && resultCode == RESULT_OK) {
            String type_data = data.getStringExtra("brand_data");
            rent_car_list_brand= type_data;
            tvBrand.setText(type_data);
        }

    }

    //적용된 조건에 따라 조건 텍스트뷰에 설정(나이)
    public void rental_list_filter_ageSelect(int i) {
        switch (i) {
            case 0:
                tvAge.setText("전체");
                rent_car_list_age = "전체";
                break;
            case 1:
                tvAge.setText("만 21세 이상");
                rent_car_list_age = "만 21세 이상";
                break;
            case 2:
                tvAge.setText("만 26세 이상");
                rent_car_list_age = "만 26세 이상";
                break;
            default:
                break;
        }
    }

    //적용된 조건에 따라 조건 텍스트뷰에 설정(가격)
    public void rental_list_filter_priceSelect(int i) {
        switch (i) {
            case 0:
                tvPrice.setText("전체");
                rent_car_list_price = "전체";
                break;
            case 1:
                tvPrice.setText("10만원 이하");
                rent_car_list_price = "10만원 이하";
                break;
            case 2:
                tvPrice.setText("20만원 이하");
                rent_car_list_price = "20만원 이하";
                break;
            case 3:
                tvPrice.setText("30만원 이하");
                rent_car_list_price = "30만원 이하";
                break;
            case 4:
                tvPrice.setText("40만원 이하");
                rent_car_list_price = "40만원 이하";
                break;
            case 5:
                tvPrice.setText("50만원 이하");
                rent_car_list_price = "50만원 이하";
                break;
            case 6:
                tvPrice.setText("50만원 이상");
                rent_car_list_price = "50만원 이상";
                break;
            default:
                break;
        }
    }

}
