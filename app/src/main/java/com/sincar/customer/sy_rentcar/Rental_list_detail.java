package com.sincar.customer.sy_rentcar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sincar.customer.CouponeActivity;
import com.sincar.customer.R;
import com.sincar.customer.adapter.AdapterSpinner;
import com.sincar.customer.util.Util;

import net.daum.mf.map.api.MapView;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;


import java.util.ArrayList;

import static com.sincar.customer.util.Util.getYear;

public class Rental_list_detail extends FragmentActivity implements
        View.OnClickListener {
    private ImageView car_image;
    private RelativeLayout mRelativeLayout;
    private MapView mapView;
    private ViewGroup mapViewContainer;
    private Spinner spinner;
    AdapterSpinner adapterSpinner;
    private boolean spinner_select = true;
    private int select_delivery = 0;
    //sy

    public final static int CAR_START_REQ_CODE = 1030;
    public final static int CAR_END_REQ_CODE = 1031;


    String start_date, start_time, return_date, return_time, curAddress, start_address, end_address;
    private TextView rental_car_start_date, rental_car_start_time, rental_car_end_date, rental_car_end_time;
    private TextView rental_car_address;
    private TextView btn_rental_allocate, btn_rental_return;
    private View view_touchless;
    ///sy


    private TextView rental_allocate_text, rental_return_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rental_list_detail);
    }

    //sy 현재 액티비티와 rental_car_delivery의 양쪽 맵뷰 사용을 위해 필요함
    @Override
    protected void onResume() {
        super.onResume();
        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {

        findViewById(R.id.rental_detail_btnPrev_layout).setOnClickListener(this);
        findViewById(R.id.rental_confirm_btn).setOnClickListener(this);

        car_image  = (ImageView) findViewById(R.id.rental_car_image);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        //자동차 이미지
        Glide.with(this).load("https://www.sincar.co.kr/upload/program/goods/list/201912241503214320.jpg")
                .into(car_image);

        //배차 및 반납 위치
        rental_allocate_text = (TextView) findViewById(R.id.rental_allocate_text);
        rental_return_text   = (TextView) findViewById(R.id.rental_return_text);

        //배차 및 반납 위치 설정 버튼
        btn_rental_allocate = (TextView)findViewById(R.id.rental_allocate_position);
        btn_rental_return = (TextView)findViewById(R.id.rental_return_position);
        btn_rental_allocate.setOnClickListener(this);
        btn_rental_return.setOnClickListener(this);

        //딜리버리 선택에 따른 배차,반납 위치 텍스트와 버튼 활성화 여부 설정
        spinner_Selected(select_delivery);

        //지도
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rMap);
        view_touchless = (View)findViewById(R.id.view_touchless);

        mapView = new MapView(this);
        mapViewContainer = (ViewGroup) findViewById(R.id.rMap);
        mapViewContainer.addView(mapView);
        view_touchless.setClickable(true);


        ArrayList<String> dlivery_title = new ArrayList<>();
        dlivery_title.add("지점방문"); //ArrayList에 내가 스피너에 보여주고싶은 값 셋팅
        dlivery_title.add("왕복 딜리버리");
        dlivery_title.add("배차시 딜리버리");
        dlivery_title.add("반납시 딜리버리");

        spinner = findViewById(R.id.rental_spinner);
        spinner.setPrompt("딜리버리");
        adapterSpinner = new AdapterSpinner(this,dlivery_title); //그 값을 넣어줌

        spinner.setAdapter(adapterSpinner); //어댑터연결
        spinner.setSelection(select_delivery);   //선택값 지정

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner_select) {
                    spinner_select = false;
                } else { // 로직
                    Toast.makeText(getApplicationContext(),(String)spinner.getItemAtPosition(position)+" 선택되었습니다.",Toast.LENGTH_SHORT).show();
                    select_delivery = position;
                    spinner_Selected(select_delivery);
 //                   select_jisa_title = (String)spinner.getItemAtPosition(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        //sy
        rental_car_start_date = (TextView)findViewById(R.id.rental_car_start_date);
        rental_car_start_time = (TextView)findViewById(R.id.rental_car_start_time);
        rental_car_end_date = (TextView)findViewById(R.id.rental_car_end_date);
        rental_car_end_time = (TextView)findViewById(R.id.rental_car_end_time);
        rental_car_address = (TextView)findViewById(R.id.rental_car_address);

        Intent intent = getIntent(); //Rental_list에서 넘어온 데이터 수신

        start_date = intent.getStringExtra("start_date");
        start_time = intent.getStringExtra("start_time");
        return_date = intent.getStringExtra("return_date");
        return_time = intent.getStringExtra("return_time");
        curAddress = intent.getStringExtra("current_Address");

        rental_car_start_date.setText(start_date);
        rental_car_start_time.setText(start_time);
        rental_car_end_date.setText(return_date);
        rental_car_end_time.setText(return_time);
        rental_car_address.setText(curAddress);

        ///sy
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.rental_detail_btnPrev_layout:
                finish();
                break;

            case R.id.rental_confirm_btn:
                // TODO : 예약하기
                intent = new Intent(this, Rental_payment.class);
                startActivity(intent);
                break;

            case R.id.rental_allocate_position:
                // TODO : 배차위치 변경하러 가기
                //sy
                intent = new Intent(this, Rental_car_delivery_map.class);
                intent.putExtra("delivery_type", select_delivery);
                startActivityForResult(intent,CAR_START_REQ_CODE);
                //Toast.makeText(getApplicationContext(),"배차위치 변경하러 가기",Toast.LENGTH_SHORT).show();
                break;

            case R.id.rental_return_position:
                // TODO : 반납위치 변경하러 가기
                intent = new Intent(this, Rental_car_delivery_map.class);
                intent.putExtra("delivery_type", select_delivery);
                startActivityForResult(intent,CAR_END_REQ_CODE);
                //Toast.makeText(getApplicationContext(),"반납위치 변경하러 가기",Toast.LENGTH_SHORT).show();
                ///sy
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAR_START_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                start_address = data.getStringExtra("address");
                select_delivery = data.getIntExtra("delivery_type", 0);
                spinner_select = true;
            }
        } else if (requestCode == CAR_END_REQ_CODE) {
            if(resultCode == RESULT_OK) {
                end_address = data.getStringExtra("address");
                select_delivery = data.getIntExtra("delivery_type", 0);
                spinner_select = true;
            }
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapViewContainer.removeView(mapView);
    }

    //딜리버리 선택에 따라 배차 및 반납 버튼 활성화여부 설정
    private void spinner_Selected(int spinner_type) {
        switch(spinner_type) {
            case 0:
                btn_rental_allocate.setVisibility(View.INVISIBLE);
                btn_rental_return.setVisibility(View.INVISIBLE);
                rental_allocate_text.setText("사용하지 않음");
                rental_return_text.setText("사용하지 않음");
                break;
            case 1:
                btn_rental_allocate.setVisibility(View.VISIBLE);
                btn_rental_return.setVisibility(View.VISIBLE);
                text_Selected(start_address, end_address);
                break;
            case 2:
                btn_rental_allocate.setVisibility(View.VISIBLE);
                btn_rental_return.setVisibility(View.INVISIBLE);
                text_Selected(start_address, "사용하지 않음");
                break;
            case 3:
                btn_rental_allocate.setVisibility(View.INVISIBLE);
                btn_rental_return.setVisibility(View.VISIBLE);
                text_Selected("사용하지 않음", end_address);
                break;
            default:
                break;
        }
    }

    void text_Selected(String start, String end) {
        if(start == null) {
            rental_allocate_text.setText("배차 위치를 정해주세요");
        } else if(start == "사용하지 않음") {
            rental_allocate_text.setText("사용하지 않음");
        } else {
            rental_allocate_text.setText(start_address);
        }
        if(end == null) {
            rental_return_text.setText("반납 위치를 정해주세요");
        } else if (end == "사용하지 않음") {
            rental_return_text.setText("사용하지 않음");
        } else {
            rental_return_text.setText(end_address);
        }
    }
}




