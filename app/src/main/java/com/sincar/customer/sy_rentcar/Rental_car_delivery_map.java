package com.sincar.customer.sy_rentcar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sincar.customer.MapsActivity;
import com.sincar.customer.R;
import com.sincar.customer.ReservationAddressActivity;
import com.sincar.customer.util.GPSInfo;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.sincar.customer.MapsActivity.MAP_REQ_CODE;

public class Rental_car_delivery_map extends AppCompatActivity implements View.OnClickListener, MapView.MapViewEventListener {

    /*
     * GPS, 일출,일몰, 위도, 경도, 현재 주소
     */
    private GPSInfo gps;        // GPS class
    private double latitude;    //위도
    private double longitude;   //경도
    private String cAddress;    //현재 주소

    private Context gContext;
    private Geocoder gCoder;

    private String search_keyword = "";

    private double my_latitude;    //내위치 위도
    private double my_longitude;   //내위치 경도

    MapView mapView;
    ViewGroup mapViewContainer;
    TextView tvAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_car_delivery_map);

        gContext = this;

        // GPS 정보
        gps = new GPSInfo(gContext);
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();

        my_latitude = latitude;
        my_longitude = longitude;

    }

    //화명 초기화
    void init() {
        mapView = new MapView(this);
        mapViewContainer = (ViewGroup) findViewById(R.id.car_del_map);
        mapViewContainer.addView(mapView);
        mapView.setMapViewEventListener(this);

        tvAddress = (TextView) findViewById(R.id.tvCar_del_address);
        findViewById(R.id.car_del_btnBack).setOnClickListener(this);
        findViewById(R.id.car_del_curAdd).setOnClickListener(this);
        findViewById(R.id.con_car_del_address).setOnClickListener(this);
        findViewById(R.id.btn_car_del_accept).setOnClickListener(this);

        getAddress();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

            case R.id.car_del_btnBack:
                finish();
                break;
            case R.id.car_del_curAdd:
                mapView.clearFocus();

                // GPS 정보
                gps = new GPSInfo(gContext);
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

                my_latitude = latitude;
                my_longitude = longitude;

                if (gps.isGetLocation()) {
                    //주소 갱신
                    mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
                    getAddress();
                }
                break;
            case R.id.con_car_del_address:
                intent = new Intent(this, ReservationAddressActivity.class);
                startActivityForResult(intent, MAP_REQ_CODE);
                break;
            case R.id.btn_car_del_accept:
                Intent getIntent = getIntent();
                intent = new Intent();
                int delivery_type = getIntent.getIntExtra("delivery_type", 0);
                intent.putExtra("delivery_type", delivery_type);
                intent.putExtra("address", cAddress);
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 현 위치 호출시 주소 갱신
     */
    private void getAddress() {
        if (gps.isGetLocation()) {
            //Geocoder
            gCoder = new Geocoder(this, Locale.getDefault());
            List<Address> addr = null;
            try {
                addr = gCoder.getFromLocation(latitude, longitude, 10);   //위도, 경도, 얻어올 값의 개수
                Address a = addr.get(0);

                String address = a.getAddressLine(0).substring(a.getAddressLine(0).indexOf("\"") + 1, a.getAddressLine(0).length()); // 주소
                address = address.replace("대한민국 ", "");
                if (address != null && address.length() > 0) {
                    String[] splitStr = address.split(" ");
                    cAddress = address;

                    //해당 지역을 넣어준다.
                    //return_address = "위치 : " + splitStr[1] + " " + splitStr[2];

                } else {
                    //주소를 가져오지 못했을 때 처리 추가.
                    //Toast.makeText(this,"주소 정보가 없습니다.", Toast.LENGTH_LONG).show();
                }

                tvAddress.setText(cAddress);

                Log.d("Rental_car_delivery_map", "address ==>" + address);


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "주소 정보가 없습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAP_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                if (!TextUtils.isEmpty(data.getStringExtra("search_result"))) {
                    cAddress = data.getStringExtra("search_result");
                    search_keyword = data.getStringExtra("search_keyword");
                    ConvertGPS(cAddress);
                } else {
                    Toast.makeText(Rental_car_delivery_map.this, "주소 검색을 다시 해주세요", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


    /**
     * 주소 -> 위경도로 변환
     */
    private void ConvertGPS(String cAddress) {
        List<Address> list = null;

        try {
            list = gCoder.getFromLocationName
                    (cAddress, // 지역 이름
                            10); // 읽을 개수
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
        }

        if (list != null) {
            if (list.size() == 0) {
                Log.e("test", "해당되는 주소 정보는 없습니다");
            } else {
                // 해당되는 주소로 인텐트 날리기
                Address addr = list.get(0);
                double lat = addr.getLatitude();
                double lon = addr.getLongitude();

                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lat, lon), true);
                tvAddress.setText(cAddress);

            }
        }
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
        latitude = mapPoint.getMapPointGeoCoord().latitude;
        longitude = mapPoint.getMapPointGeoCoord().longitude;
        getAddress();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!gps.isGetLocation()) {
            gps.showSettingsAlert();
        }
        init();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapViewContainer.removeView(mapView);
    }
}
