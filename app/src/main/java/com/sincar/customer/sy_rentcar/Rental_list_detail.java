package com.sincar.customer.sy_rentcar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
        View.OnClickListener, OnDateNTimeSetListener, MapView.MapViewEventListener,
        MapView.OpenAPIKeyAuthenticationResultListener, MapView.POIItemEventListener, rCodeCheck {
    private ImageView car_image;
    private RelativeLayout mRelativeLayout;
    private MapView mapView;
    private Spinner spinner;
    AdapterSpinner adapterSpinner;
    private boolean spinner_select = true;
    private int select_delivery = 0;

    private TextView rental_allocate_text, rental_return_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rental_list_detail);

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

        findViewById(R.id.rental_allocate_position).setOnClickListener(this);
        findViewById(R.id.rental_return_position).setOnClickListener(this);

        if(select_delivery == 0)
        {
            rental_allocate_text.setText("사용하지 않음");
            rental_return_text.setText("사용하지 않음");
        }

        //지도
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rMap);

        mapView = new MapView(this);
        final ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.rMap);
        mapViewContainer.addView(mapView);
        mapView.setMapViewEventListener(this);

        ArrayList<String> dlivery_title = new ArrayList<>();
        dlivery_title.add("지점방문"); //ArrayList에 내가 스피너에 보여주고싶은 값 셋팅
        dlivery_title.add("왕복 딜리버리");
        dlivery_title.add("배차시 딜리버리");
        dlivery_title.add("반납시 딜리버리");

//
////        spinner.setSelection(-1);   //초기 선택값 지정

        spinner = findViewById(R.id.rental_spinner);
        spinner.setPrompt("딜리버리");
        adapterSpinner = new AdapterSpinner(this,dlivery_title); //그 값을 넣어줌

        spinner.setAdapter(adapterSpinner); //어댑터연결


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner_select) {
                    spinner_select = false;
                } else { // 로직
                    Toast.makeText(getApplicationContext(),(String)spinner.getItemAtPosition(position)+" 선택되었습니다.",Toast.LENGTH_SHORT).show();
                    select_delivery = position;
 //                   select_jisa_title = (String)spinner.getItemAtPosition(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


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
                Toast.makeText(getApplicationContext(),"배차위치 변경하러 가기",Toast.LENGTH_SHORT).show();
                break;

            case R.id.rental_return_position:
                // TODO : 반납위치 변경하러 가기
                Toast.makeText(getApplicationContext(),"반납위치 변경하러 가기",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }


    @Override
    public void onDateNTimePickerSet(String date, String time, int timeCheck) {

    }

    @Override
    public void rCodechk(int rCodecheck) {

    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int i, String s) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}



