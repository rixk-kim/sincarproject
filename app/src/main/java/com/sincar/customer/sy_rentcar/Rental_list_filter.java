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

    //필터 종류 4가지
    public enum filter {age, price, type, brand}
    //나이 필터
    public enum age_filter {
        all("전체"), _21over("만 21세 이상"), _26over("만 26세 이상");
        private final String age_str;
        age_filter (String age_str) {
            this.age_str = age_str;
        }
        public String getValue() {
            return age_str;
        }
    }
    //가격 필터
    public enum price_filter {
        all("전체"), _10under("10만원 이하"), _20under("20만원 이하"), _30under("30만원 이하"),
        _40under("40만원 이하"), _50under("50만원 이하"), _50over("50만원 이상");
        private final String price_str;
        price_filter (String price_str) {
            this.price_str = price_str;
        }
        public String getValue() {
            return price_str;
        }
    }
    //외형 필터
    public enum type_filter {
        light("경차"), saedan("세단"), sports("스포츠"), rv("R/V"), suv("SUV"), suenghap("승합"),
        van("밴"), convertouble("컨버터블"), coupeh("쿠페"), truck("트럭");
        private final String type_str;
        type_filter (String type_str) {
            this.type_str = type_str;
        }
        public String getValue() {
            return type_str;
        }
    }
    //브랜드 필터(수입, 국산)
    public enum kuk_brand_filter {
        hyundai("현대"), genesis("제네시스"), kia("기아"), ssangyong("쌍용"),
        renaultsamsung("르노삼성"), chevrolet("쉐보레");
        private  final String kuk_brand_str;
        kuk_brand_filter(String kuk_brand_str) {
            this.kuk_brand_str = kuk_brand_str;
        }
        public String getValue() { return kuk_brand_str; }
    }
    public enum su_brand_filter {
        nissan("닛산"), toyota("토요타"), mastuda("마츠다"), mitsubishi("미쓰비시"),
        gm("GM"), chevrolet("쉐보레"), nissan2("닛산"), toyota2("도요타"),
        mastuda2("마츠다"), mitsubishi2("미쓰비시");
        private final String su_brand_str;
        su_brand_filter (String su_brand_str) {
            this.su_brand_str = su_brand_str;
        }
        public String getValue() { return su_brand_str; }
    }

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
                startActivityForResult(intent, filter.age.ordinal());

            }
        });
        //가격조건 필터 액티비티로 전환
        btnPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Rental_list_filter_price.class);
                startActivityForResult(intent, filter.price.ordinal());
            }
        });
        //외형조건 필터 액티비티로 전환
        btnType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Rental_list_filter_type.class);
                startActivityForResult(intent, filter.type.ordinal());
            }
        });
        //브랜드조건 필터 액티비티로 전환
        btnBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Rental_list_filter_brand.class);
                startActivityForResult(intent, filter.brand.ordinal());
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
        if(requestCode == filter.age.ordinal() && resultCode == RESULT_OK) {
            String age_data = data.getStringExtra("age_data");
            rent_car_list_age = age_data;
            tvAge.setText(age_data);
            //rental_list_filter_ageSelect(i);
        }
        //가격 조건
        else if(requestCode == filter.price.ordinal() && resultCode == RESULT_OK) {
            String price_data = data.getStringExtra("price_data");
            rent_car_list_price = price_data;
            tvPrice.setText(price_data);
            //rental_list_filter_priceSelect(i);
        }
        //외형 조건
        else if(requestCode == filter.type.ordinal() && resultCode == RESULT_OK) {
            String type_data = data.getStringExtra("type_data");
            rent_car_list_type = type_data;
            tvType.setText(type_data);
        }
        //브랜드 조건
        else if(requestCode == filter.brand.ordinal() && resultCode == RESULT_OK) {
            String type_data = data.getStringExtra("brand_data");
            rent_car_list_brand= type_data;
            if(type_data.length() > 25){
                type_data = type_data.substring(0, 25);
                type_data += "...";
            }
            tvBrand.setText(type_data);
        }

    }

//    //적용된 조건에 따라 조건 텍스트뷰에 설정(나이)
//    public void rental_list_filter_ageSelect(int i) {
//        switch (i) {
//            case 0:
//                tvAge.setText("전체");
//                rent_car_list_age = "전체";
//                break;
//            case 1:
//                tvAge.setText("만 21세 이상");
//                rent_car_list_age = "만 21세 이상";
//                break;
//            case 2:
//                tvAge.setText("만 26세 이상");
//                rent_car_list_age = "만 26세 이상";
//                break;
//            default:
//                break;
//        }
//    }
//
//    //적용된 조건에 따라 조건 텍스트뷰에 설정(가격)
//    public void rental_list_filter_priceSelect(int i) {
//        switch (i) {
//            case 0:
//                tvPrice.setText("전체");
//                rent_car_list_price = "전체";
//                break;
//            case 1:
//                tvPrice.setText("10만원 이하");
//                rent_car_list_price = "10만원 이하";
//                break;
//            case 2:
//                tvPrice.setText("20만원 이하");
//                rent_car_list_price = "20만원 이하";
//                break;
//            case 3:
//                tvPrice.setText("30만원 이하");
//                rent_car_list_price = "30만원 이하";
//                break;
//            case 4:
//                tvPrice.setText("40만원 이하");
//                rent_car_list_price = "40만원 이하";
//                break;
//            case 5:
//                tvPrice.setText("50만원 이하");
//                rent_car_list_price = "50만원 이하";
//                break;
//            case 6:
//                tvPrice.setText("50만원 이상");
//                rent_car_list_price = "50만원 이상";
//                break;
//            default:
//                break;
//        }
//    }

}
