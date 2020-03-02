package com.sincar.customer;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // 화면 초기화
        init();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // TODO - 현재위치로 이동
        // TODO - 마커 이동 후 위치 값 가져오기
        // Add a marker in Sydney and move the camera
        LatLng seoul = new LatLng(37.56, 126.97);

        mMap.addMarker(new MarkerOptions().position(seoul)
                .title("Marker in Sydney")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.current_icon))
        );
        mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    /**
     * 화면 초기화
     */
    private void init() {
        // TODO - 지도의 파란동그라미 작업(layout에 추가, 이벤트 작업)

        findViewById(R.id.btnMapHome).setOnClickListener(this);
        findViewById(R.id.btnCurrent).setOnClickListener(this);
        findViewById(R.id.btnReserveDate).setOnClickListener(this);
        findViewById(R.id.btnNext).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.btnMapHome:
                // TODO - 지도 home button
                break;

            case R.id.btnCurrent:
                // TODO - 지도 current button
                break;

            case R.id.btnReserveDate:
                // TODO - "예약일자" 설정
                break;
            case R.id.btnNext:
                // TODO - "이 위치로 부름" 작업
                break;
        }
    }
}
