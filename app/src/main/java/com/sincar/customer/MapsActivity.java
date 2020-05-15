package com.sincar.customer;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sincar.customer.util.GPSInfo;
import com.sincar.customer.util.Util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    private LatLng myPos;
    private LatLng seoul;
    private MarkerOptions markerOptions;

    /*
     * GPS, 일출,일몰, 위도, 경도, 현재 주소
     */
    private GPSInfo gps;        // GPS class
    private double latitude;    //위도
    private double longitude;   //경도
    private String cAddress;    //현재 주소

    private Context gContext;
    private Geocoder gCoder;

    private double my_latitude;    //내위치 위도
    private double my_longitude;   //내위치 경도

    public final static int MAP_REQ_CODE = 1003;
    public final static int CALENDA_REQ_CODE = 2003;

    private TextView currentTextView, reserve_date;
    private String main_path;
    private String search_keyword = "";

    //시연용 코드
    private ConstraintLayout mConstraintLayout, mbtnReserveAddress;

    private String reserve_year, reserve_month, reserve_day;
    public static MapsActivity _mMapsActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        gContext = this;
        _mMapsActivity = MapsActivity.this;

        Intent intent = getIntent(); /*데이터 수신*/
        main_path    = intent.getExtras().getString("menu");    /*String형*/

        // GPS 정보
        gps         = new GPSInfo(gContext);
        latitude    = gps.getLatitude();
        longitude   = gps.getLongitude();

        my_latitude     = latitude;
        my_longitude    = longitude;

        markerOptions = new MarkerOptions();

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

        // 현재위치로 이동
        // 마커 이동 후 위치 값 가져오기
        // Add a marker in Sydney and move the camera
        myPos = new LatLng(my_latitude, my_longitude);

        markerOptions.position(myPos);
        markerOptions.title("내 위치");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.group_7));
        markerOptions.draggable(false);
        mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos,16f));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

        mMap.setOnMarkerDragListener((GoogleMap.OnMarkerDragListener) gContext);
    }

    /**
     * 화면 초기화
     */
    private void init() {
        // TODO - 지도의 파란동그라미 작업(layout에 추가, 이벤트 작업)
        findViewById(R.id.btnMapHome).setOnClickListener(this);
        findViewById(R.id.btnCurrent).setOnClickListener(this);
        mbtnReserveAddress = (ConstraintLayout) findViewById(R.id.btnReserveAddress);

        mConstraintLayout = (ConstraintLayout)findViewById(R.id.btnReserveDate);
        if("steam".equals(main_path))
        {
            mbtnReserveAddress.setOnClickListener(this);
            mConstraintLayout.setOnClickListener(this);
        }else{
            mConstraintLayout.setVisibility(View.GONE);
        }

        findViewById(R.id.btnNext).setOnClickListener(this);


        currentTextView = findViewById(R.id.current_address);
        reserve_date = findViewById(R.id.reserve_date);

        //현재 날짜 요일 구하기
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat weekdayFormat = new SimpleDateFormat("EE", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

        String weekDay = weekdayFormat.format(currentTime);
        reserve_year = yearFormat.format(currentTime);
        reserve_month = monthFormat.format(currentTime);
        reserve_day = dayFormat.format(currentTime);

        reserve_date.setText(reserve_month + "/ " + reserve_day + " (" + weekDay + ")"); //9/15 (화)

        getAddress();

//        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
//            @Override
//            public void onCameraChange(CameraPosition arg0) {
//                LatLng location_center = arg0.target;
//
//                mMap.getCameraPosition();
//                latitude    = arg0.target.latitude;
//                longitude   = arg0.target.longitude;
//
//                getAddress();   //주소 갱신해 줌 ( cAddress )
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!gps.isGetLocation()) {
            gps.showSettingsAlert();
        }
    }


    /**
     * 현 위치 호출시 주소 갱신
     */
    private void getAddress()
    {
        if (gps.isGetLocation()) {
            //Geocoder
            gCoder = new Geocoder(this, Locale.getDefault());
            List<Address> addr = null;
            try{
                addr = gCoder.getFromLocation(latitude, longitude,10);   //위도, 경도, 얻어올 값의 개수
                Address a = addr.get(0);

                String address = a.getAddressLine(0).substring(a.getAddressLine(0).indexOf("\"") + 1, a.getAddressLine(0).length()); // 주소
                address = address.replace("대한민국 ","");
                currentTextView.setText(address);
                Log.d("MapActivity","address ==>" + address);

                if (address != null && address.length() > 0) {
                    String[] splitStr = address.split(" ");
                    cAddress = address;

                    //해당 지역을 넣어준다.
                    //return_address = "위치 : " + splitStr[1] + " " + splitStr[2];

                } else {
                    //주소를 가져오지 못했을 때 처리 추가.
                    //Toast.makeText(this,"주소 정보가 없습니다.", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this,"주소 정보가 없습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.btnMapHome:
                // 지도 home button -> 메인이동
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.btnCurrent:
                // 지도 current button => 하단 버튼과 기능 중복됨
                mMap.clear();

                // GPS 정보
                gps = new GPSInfo(gContext);
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

                my_latitude = latitude;
                my_longitude = longitude;

                if (gps.isGetLocation()) {

                    myPos = new LatLng(my_latitude, my_longitude);

                    markerOptions.position(myPos);
                    markerOptions.title("내 위치");
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.group_7));
                    markerOptions.draggable(false);
                    mMap.addMarker(markerOptions);

                    seoul = new LatLng(my_latitude, my_longitude);

                    markerOptions.position(seoul);
                    markerOptions.title("위치 변경");
                    markerOptions.snippet("검색 위치");
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.current_icon));
                    markerOptions.draggable(true);
                    mMap.addMarker(markerOptions);

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

                    //주소 갱신
                    getAddress();
                    currentTextView.setText(cAddress);

                } else {
                    // GPS 를 사용할수 없으므로
                    gps.showSettingsAlert();
                }
                break;

            case R.id.btnReserveAddress:
                // "주소 검색" 설정
                //startActivity(new Intent(this, ReservationAddressActivity.class));
                intent = new Intent(this, ReservationAddressActivity.class);
                startActivityForResult(intent, MAP_REQ_CODE);
                break;

            case R.id.btnReserveDate:
                // "예약일자" 설정
//                intent = new Intent(this, ReservationCalendarActivity.class);
//                intent.putExtra("reserve_address", cAddress);
//                intent.putExtra("search_keyword", search_keyword);
//                startActivity(intent);

                // "예약일자" 설정
                intent = new Intent(this, ReservationCalendarActivity.class);
                startActivityForResult(intent, CALENDA_REQ_CODE);
                break;
            case R.id.btnNext:
                // "이 위치로 부름" 작업(시나리오 확인) => 현 위치로 이동
                if (gps.isGetLocation()) {

                    if ("steam".equals(main_path)) {
                        //시간선택으로 바로가기
                        if(!TextUtils.isEmpty(cAddress)) {
                            intent = new Intent(this, ReservationTimeActivity.class);
                            intent.putExtra("reserve_address", cAddress);
                            if (TextUtils.isEmpty(search_keyword)) search_keyword = cAddress;
                            intent.putExtra("search_keyword", search_keyword);
                            intent.putExtra("reserve_year", reserve_year);
                            intent.putExtra("reserve_month", reserve_month);
                            intent.putExtra("reserve_day", reserve_day);
                            startActivity(intent);
                        }else{
                            Toast.makeText(this, "주소를 가져오지 못했습니다. 다시 시도해주세요.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "서비스 준비중입니다.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    gps.showSettingsAlert();
                }
                //ConvertGPS("서울 송파구 석촌호수로 274");
//                Toast.makeText(this, cAddress + "로 부르셨습니다. 정보 갱신중..", Toast.LENGTH_LONG).show();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MAP_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                if(!TextUtils.isEmpty(data.getStringExtra("search_result"))) {
                    cAddress = data.getStringExtra("search_result");
                    search_keyword = data.getStringExtra("search_keyword");
                    currentTextView.setText(cAddress);
                    ConvertGPS(cAddress);
                }else{
                    Toast.makeText(MapsActivity.this, "주소 검색을 다시 해주세요", Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(MapsActivity.this, "Result: " + data.getStringExtra("search_result"), Toast.LENGTH_SHORT).show();
            } else {   // RESULT_CANCEL
//                Toast.makeText(MapsActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }

        }else if(requestCode == CALENDA_REQ_CODE){
            if (resultCode == RESULT_OK) {
                if(!TextUtils.isEmpty(data.getStringExtra("reserve_year"))) {
                    String dayofday = "";
                    reserve_year = data.getStringExtra("reserve_year");
                    reserve_month = data.getStringExtra("reserve_month");
                    reserve_day = data.getStringExtra("reserve_day");
                    try {
                        dayofday = Util.getDateDay(reserve_year+reserve_month+reserve_day);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    reserve_date.setText(reserve_month + "/ " + reserve_day + " (" + dayofday + ")"); //9/15 (화)
                }else{
                    Toast.makeText(MapsActivity.this, "날짜를 다시 해주세요", Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(MapsActivity.this, "Result: " + data.getStringExtra("search_result"), Toast.LENGTH_SHORT).show();
            } else {   // RESULT_CANCEL
//                Toast.makeText(MapsActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 주소 -> 위경도로 변환
     */
    private void ConvertGPS(String cAddress)
    {
        List<Address> list = null;

//        String str = et3.getText().toString();
        try {
            list = gCoder.getFromLocationName
                    (cAddress, // 지역 이름
                            10); // 읽을 개수
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
        }

        if (list != null) {
            if (list.size() == 0) {
                Log.e("test","해당되는 주소 정보는 없습니다");
            } else {
                // 해당되는 주소로 인텐트 날리기
                Address addr = list.get(0);
                double lat = addr.getLatitude();
                double lon = addr.getLongitude();

                //myPos = new LatLng(my_latitude, my_longitude);
                mMap.clear();

                markerOptions.position(myPos);
                markerOptions.title("내 위치");
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.group_7));
                markerOptions.draggable(false);
                mMap.addMarker(markerOptions);

                seoul = new LatLng(lat, lon);

                markerOptions.position(seoul);
                markerOptions.title("위치 변경");
                markerOptions.snippet("검색 위치");
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.current_icon));
                markerOptions.draggable(true);
                mMap.addMarker(markerOptions);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

//                Toast.makeText(this, "위도 : " + lat + ", 경도 : "+lon , Toast.LENGTH_LONG).show();

                String sss = String.format("geo:%f,%f", lat, lon);

//                Intent intent = new Intent(
//                        Intent.ACTION_VIEW,
//                        Uri.parse(sss));
//                startActivity(intent);
            }
        }

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        mMap.getCameraPosition();
        latitude    = marker.getPosition().latitude;
        longitude   = marker.getPosition().longitude;

        getAddress();   //주소 갱신해 줌 ( cAddress )

//        Toast.makeText(this, cAddress + "로 부르셨습니다. 정보 갱신중..", Toast.LENGTH_LONG).show();

    }
}
