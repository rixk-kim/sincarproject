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

import java.util.ArrayList;
import java.util.EnumSet;

import static com.sincar.customer.MapsActivity._mMapsActivity;
import static com.sincar.customer.MapsActivity.homeKeyPressed;

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
        gm("GM"), chevrolet("쉐보레")/*, nissan2("닛산"), toyota2("도요타"),
        mastuda2("마츠다"), mitsubishi2("미쓰비시")*/;
        private final String su_brand_str;
        su_brand_filter (String su_brand_str) {
            this.su_brand_str = su_brand_str;
        }
        public String getValue() { return su_brand_str; }
    }

    //Rental_list액티비티에 넘길 데이터
    age_filter age;
    price_filter price;
    type_filter type[];
    kuk_brand_filter kukBrand[];
    su_brand_filter suBrand[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_list_filter);

        rent_car_list_age = "전체";
        rent_car_list_price = "전체";
        rent_car_list_type = "전체";
        rent_car_list_brand= "전체";

        age = age_filter.all;
        price = price_filter.all;
        ArrayList<type_filter> tfArrayList = new ArrayList<type_filter>(EnumSet.allOf(type_filter.class));
        type = tfArrayList.toArray(new type_filter[tfArrayList.size()]);
        ArrayList<kuk_brand_filter> kbfArrayList = new ArrayList<kuk_brand_filter>(EnumSet.allOf(kuk_brand_filter.class));
        ArrayList<su_brand_filter> sbfArrayList = new ArrayList<su_brand_filter>(EnumSet.allOf(su_brand_filter.class));
        kukBrand = kbfArrayList.toArray(new kuk_brand_filter[kbfArrayList.size()]);
        suBrand = sbfArrayList.toArray(new su_brand_filter[sbfArrayList.size()]);

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
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION); // intent가 호출될때 onUserLeaveHint()가 실행되는것을 차단
                startActivityForResult(intent, filter.age.ordinal());
            }
        });
        //가격조건 필터 액티비티로 전환
        btnPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Rental_list_filter_price.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION); // intent가 호출될때 onUserLeaveHint()가 실행되는것을 차단
                startActivityForResult(intent, filter.price.ordinal());
            }
        });
        //외형조건 필터 액티비티로 전환
        btnType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Rental_list_filter_type.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION); // intent가 호출될때 onUserLeaveHint()가 실행되는것을 차단
                startActivityForResult(intent, filter.type.ordinal());
            }
        });
        //브랜드조건 필터 액티비티로 전환
        btnBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Rental_list_filter_brand.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION); // intent가 호출될때 onUserLeaveHint()가 실행되는것을 차단
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

                age = age_filter.all;
                price = price_filter.all;
                ArrayList<type_filter> tfArrayList = new ArrayList<type_filter>(EnumSet.allOf(type_filter.class));
                type = tfArrayList.toArray(new type_filter[tfArrayList.size()]);
                ArrayList<kuk_brand_filter> kbfArrayList = new ArrayList<kuk_brand_filter>(EnumSet.allOf(kuk_brand_filter.class));
                ArrayList<su_brand_filter> sbfArrayList = new ArrayList<su_brand_filter>(EnumSet.allOf(su_brand_filter.class));
                kukBrand = kbfArrayList.toArray(new kuk_brand_filter[kbfArrayList.size()]);
                suBrand = sbfArrayList.toArray(new su_brand_filter[sbfArrayList.size()]);
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
                backToList.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION); // intent가 호출될때 onUserLeaveHint()가 실행되는것을 차단
                backToList.putExtra("age_data", rent_car_list_age);
                backToList.putExtra("price_data", rent_car_list_price);
                backToList.putExtra("type_data", rent_car_list_type);
                backToList.putExtra("brand_data", rent_car_list_brand);
                backToList.putExtra("age", age);
                backToList.putExtra("price", price);
                backToList.putExtra("type", type);
                backToList.putExtra("kukBrand", kukBrand);
                backToList.putExtra("suBrand", suBrand);
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
            rent_car_list_age = data.getStringExtra("age_data");
            tvAge.setText(rent_car_list_age);
            age = (age_filter)data.getSerializableExtra("age");
        }
        //가격 조건
        else if(requestCode == filter.price.ordinal() && resultCode == RESULT_OK) {
            rent_car_list_price = data.getStringExtra("price_data");
            tvPrice.setText(rent_car_list_price);
            price = (price_filter) data.getSerializableExtra("price");
        }
        //외형 조건
        else if(requestCode == filter.type.ordinal() && resultCode == RESULT_OK) {
            rent_car_list_type = data.getStringExtra("type_data");
            tvType.setText(rent_car_list_type);
            type = (type_filter[]) data.getSerializableExtra("type");
        }
        //브랜드 조건
        else if(requestCode == filter.brand.ordinal() && resultCode == RESULT_OK) {
            rent_car_list_brand = data.getStringExtra("brand_data");
            if(rent_car_list_brand.length() > 25){
                rent_car_list_brand = rent_car_list_brand.substring(0, 25);
                rent_car_list_brand += "...";    //25자 이상 넘어가면 "..."으로 변환
            }
            tvBrand.setText(rent_car_list_brand);
            kukBrand = (kuk_brand_filter[]) data.getSerializableExtra("kukBrand");
            suBrand = (su_brand_filter[]) data.getSerializableExtra("suBrand");
        }
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        homeKeyPressed = true;
        _mMapsActivity.onPause();
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
