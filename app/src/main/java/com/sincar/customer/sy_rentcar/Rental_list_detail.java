package com.sincar.customer.sy_rentcar;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.sincar.customer.R;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class Rental_list_detail extends FragmentActivity implements
        View.OnClickListener {
    private ImageView car_image;
    private RelativeLayout mRelativeLayout;
    private MapView mapView;
    private ViewGroup mapViewContainer;
    private TextView tvDelivery_dialog;

    //private boolean spinner_select = true;
    private int select_delivery = 0;
    //sy

    public final static int CAR_START_REQ_CODE = 1030;
    public final static int CAR_END_REQ_CODE = 1031;

    //
    String start_date, start_time, return_date, return_time, start_address, end_address;
    private TextView rental_car_start_date, rental_car_start_time, rental_car_end_date, rental_car_end_time;
    private TextView rental_car_address;
    private TextView btn_rental_allocate, btn_rental_return;
    private View view_touchless;

    //렌탈 리스트 디테일용 커스텀 다이얼로그
    CustomDialogInListDetail customSpinnerDialog;
    Bundle dlgSpinnerBundle;
    ///sy

    private TextView rental_allocate_text, rental_return_text; //대여 딜리버리, 반납 딜리버리 텍스트뷰

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
        //spinner_select = true;
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {

        findViewById(R.id.rental_detail_btnPrev_layout).setOnClickListener(this);
        findViewById(R.id.rental_confirm_btn).setOnClickListener(this);

        car_image  = (ImageView) findViewById(R.id.rental_car_image);
        //디바이스 화면 크기 구하는 메소드
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x;
//        int height = size.y;

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
        view_touchless.setClickable(true); //맵을 터치 불가 형식으로 만듬

        //딜리버리 설정 다이얼로그 호출용 텍스트뷰
        tvDelivery_dialog = findViewById(R.id.rental_spinner);

        tvDelivery_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customSpinnerDialog = CustomDialogInListDetail.getInstance();
                dlgSpinnerBundle = new Bundle();
                //체크된 딜리버리 아이템 구분 변수 전달 0: 지점방문 1: 왕복 딜리버리 2: 배차시 딜리버리 3: 반납시 딜리버리
                dlgSpinnerBundle.putInt("dlgSpinnerCheck", select_delivery);
                customSpinnerDialog.setArguments(dlgSpinnerBundle);
                customSpinnerDialog.show(getSupportFragmentManager(), "spinnerDialog_event");
                customSpinnerDialog.setDialogResult(new CustomDialogInListDetail.OnMySpinnerDialogResult() {
                    @Override
                    public void finish(int result) {
                        //다이얼로그 종료후 선택된 딜리버리 아이템에 따라 딜리버리 시스템 설정 변경
                        select_delivery = result;
                        spinner_Selected(select_delivery);
                        switch (select_delivery) {
                            case 0:
                                tvDelivery_dialog.setText("지점 방문");
                                break;
                            case 1:
                                tvDelivery_dialog.setText("왕복 딜리버리");
                                break;
                            case 2:
                                tvDelivery_dialog.setText("배차시 딜리버리");
                                break;
                            case 3:
                                tvDelivery_dialog.setText("반납시 딜리버리");
                                break;
                            default:
                                break;
                        }
                        Toast.makeText(getApplicationContext(), tvDelivery_dialog.getText().toString() + " 선택되었습니다.",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //sy
        rental_car_start_date = (TextView)findViewById(R.id.rental_car_start_date);
        rental_car_start_time = (TextView)findViewById(R.id.rental_car_start_time);
        rental_car_end_date = (TextView)findViewById(R.id.rental_car_end_date);
        rental_car_end_time = (TextView)findViewById(R.id.rental_car_end_time);
//        rental_car_address = (TextView)findViewById(R.id.rental_car_address);

        Intent intent = getIntent(); //Rental_list에서 넘어온 데이터 수신

        start_date = intent.getStringExtra("start_date");
        start_time = intent.getStringExtra("start_time");
        return_date = intent.getStringExtra("return_date");
        return_time = intent.getStringExtra("return_time");
//        curAddress = intent.getStringExtra("current_Address");
        //20-06-23 잠시 보류
        //대리점 위치정보를 맵븁에 보여줌
//        LatLng shop_latlng = new LatLng(
//                intent.getDoubleExtra("shop_lng", 0),
//                intent.getDoubleExtra("shop_lon", 0));
//        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(shop_latlng.latitude, shop_latlng.longitude), true);


        //넘어온 시간 데이터를 시간 텍스트뷰에 적용
        rental_car_start_date.setText(start_date);
        rental_car_start_time.setText(start_time);
        rental_car_end_date.setText(return_date);
        rental_car_end_time.setText(return_time);
//        rental_car_address.setText(curAddress);

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
                switch (select_delivery) {
                    //딜리버리 선택에 따른 확인 버튼 활성화
                    // case 0은 지점 방문이기에 default 값으로 대체
                    case 1:
                        if(rental_allocate_text.getText() == "배차 위치를 정해주세요" && rental_return_text.getText() == "반납 위치를 정해주세요") {
                            Toast.makeText(getApplicationContext(), "먼저 배차 및 위치를 정해주세요",Toast.LENGTH_LONG).show();
                        } else if(rental_allocate_text.getText() == "배차 위치를 정해주세요") {
                            Toast.makeText(getApplicationContext(), "먼저 배차 위치를 정해주세요",Toast.LENGTH_LONG).show();
                        } else if (rental_return_text.getText() == "반납 위치를 정해주세요") {
                            Toast.makeText(getApplicationContext(), "먼저 반납 위치를 정해주세요",Toast.LENGTH_LONG).show();
                        } else {
                            intent = new Intent(this, Rental_payment.class);
                            startActivity(intent);
                        }
                        break;
                    case 2:
                        if(rental_allocate_text.getText() == "배차 위치를 정해주세요") {
                            Toast.makeText(getApplicationContext(), "먼저 배차 위치를 정해주세요",Toast.LENGTH_LONG).show();
                        } else {
                            intent = new Intent(this, Rental_payment.class);
                            startActivity(intent);
                        }
                        break;
                    case 3:
                        if (rental_return_text.getText() == "반납 위치를 정해주세요") {
                            Toast.makeText(getApplicationContext(), "먼저 배차 위치를 정해주세요",Toast.LENGTH_LONG).show();
                        } else {
                            intent = new Intent(this, Rental_payment.class);
                            startActivity(intent);
                        }
                        break;
                    default:
                        intent = new Intent(this, Rental_payment.class);
                        startActivity(intent);
                        break;
                }
                //지도 타일 이미지 캐쉬 데이터 삭제 (메모리 확보용)
                mapView.releaseUnusedMapTileImageResources();
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

    /**
     * 딜리버리 맵뷰 액티비티 전환후 돌아올시 ActivityResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //주소 정보를 주소정보텍스트뷰에 대입
        if (requestCode == CAR_START_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                start_address = data.getStringExtra("address");
                //딜리버리 시스템 선택된 정보로 유지
                select_delivery = data.getIntExtra("delivery_type", 0);
                //spinner_select = true;
            }
        } else if (requestCode == CAR_END_REQ_CODE) {
            if(resultCode == RESULT_OK) {
                end_address = data.getStringExtra("address");
                select_delivery = data.getIntExtra("delivery_type", 0);
               // spinner_select = true;
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
        //액티비티 전환시 맵뷰 컨테이너에서 맵뷰 삭제 ( 맵뷰 다중 지원 오류 방지)
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

    //선택된 딜리버리 시스템에 따라 딜리버리 관련 텍스트뷰 설정
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




