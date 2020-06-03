package com.sincar.customer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sincar.customer.sy_rentcar.MapApiConst;
import com.sincar.customer.sy_rentcar.Maps_rent_mainfrag;
import com.sincar.customer.sy_rentcar.Maps_rent_time;
import com.sincar.customer.sy_rentcar.OnDateNTimeSetListener;
import com.sincar.customer.sy_rentcar.rCodeCheck;
import com.sincar.customer.util.GPSInfo;
import com.sincar.customer.util.Util;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements
        View.OnClickListener, OnDateNTimeSetListener, MapView.MapViewEventListener,
        MapView.OpenAPIKeyAuthenticationResultListener, MapView.POIItemEventListener, rCodeCheck {

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
    private ConstraintLayout mConstraintLayout, mbtnReserveAddress, next_Layout;

    private String reserve_year, reserve_month, reserve_day;
    public static MapsActivity _mMapsActivity;

    //sy
    FrameLayout framelayout_maps_rentCar;
    Maps_rent_time maps_rent_time_reserve, maps_rent_time_return;
    Maps_rent_mainfrag maps_rent_mainfrag;
    String start_date, start_time, return_date, return_time;
    int now_timeInt, start_timeInt, return_timeInt;
    ImageView btnCurrent;
    Bundle bundle = new Bundle();
    int rCode = 0;
    MapView mapView;
    String appkey = MapApiConst.KAKAO_MAPS_ANDROID_APP_API_KEY;
    ConstraintLayout constraintLayout;
    ConstraintSet constraintSet;
    Point pt;
    RelativeLayout reMap;
    boolean mapCheck = false;
    //MapPOIItem marker;
    ///sy

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        gContext = this;
        _mMapsActivity = MapsActivity.this;

        Intent intent = getIntent(); /*데이터 수신*/
        main_path = intent.getExtras().getString("menu");    /*String형*/

        // GPS 정보
        gps = new GPSInfo(gContext);
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();

        my_latitude = latitude;
        my_longitude = longitude;

        //키해시 확인
        String sigStr = getSigneture(this);

        //화면 크기 구함
        pt = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(pt);

        // 화면 초기화
        init();

    }

    /**
     * 화면 초기화
     */

    private void init() {
        constraintLayout = (ConstraintLayout) findViewById(R.id.maps_constraintlayout);
        constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        // TODO - 지도의 파란동그라미 작업(layout에 추가, 이벤트 작업)
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

        //sy

        btnCurrent = (ImageView) findViewById(R.id.btnCurrent);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d일 (E)");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:00");
        SimpleDateFormat timeIntFormat = new SimpleDateFormat("HH");
        start_date = dateFormat.format(currentTime);
        start_time = timeFormat.format(currentTime);
        return_date = dateFormat.format(currentTime);
        return_time = timeFormat.format(currentTime);
        now_timeInt = Integer.parseInt(timeIntFormat.format(currentTime));
        start_timeInt = return_timeInt = now_timeInt;

        ///sy
        findViewById(R.id.btnMapHome).setOnClickListener(this);
        findViewById(R.id.btnCurrent).setOnClickListener(this);
        mbtnReserveAddress = (ConstraintLayout) findViewById(R.id.btnReserveAddress);
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.btnReserveDate);
        next_Layout = (ConstraintLayout) findViewById(R.id.next_layout);
        framelayout_maps_rentCar = (FrameLayout) findViewById(R.id.framelayout_maps_rentcar);

        reMap = (RelativeLayout)findViewById(R.id.kMap);

        //마커 이용
//        mapView.setPOIItemEventListener(this);
//
//        marker = new MapPOIItem();
//        marker.setItemName("Default Marker");
//        marker.setTag(0);
//        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);
//        marker.setMapPoint(mapView.getMapCenterPoint());
//        mapView.addPOIItem(marker);

        if ("steam".equals(main_path)) {
            mbtnReserveAddress.setOnClickListener(this);
            mConstraintLayout.setOnClickListener(this);
            findViewById(R.id.btnNext).setOnClickListener(this);
            currentTextView = findViewById(R.id.current_address);
            currentTextView.setText("");
            reserve_date = findViewById(R.id.reserve_date);
            reserve_date.setText(reserve_month + "/ " + reserve_day + " (" + weekDay + ")"); //9/15 (화)
            constraintSet.connect(btnCurrent.getId(), constraintSet.BOTTOM, mbtnReserveAddress.getId(), ConstraintSet.TOP);
            constraintSet.applyTo(constraintLayout);

        } else { //sy
            constraintSet.connect(btnCurrent.getId(), constraintSet.BOTTOM, framelayout_maps_rentCar.getId(), ConstraintSet.TOP);
            constraintSet.applyTo(constraintLayout);
            mConstraintLayout.setVisibility(View.GONE);
            mbtnReserveAddress.setVisibility(View.GONE);
            next_Layout.setVisibility(View.GONE);
            //framelayout_maps_rentCar.setVisibility(View.GONE);

            maps_rent_time_reserve = new Maps_rent_time();
            maps_rent_time_return = new Maps_rent_time();
            maps_rent_mainfrag = new Maps_rent_mainfrag();
            replaceFragment(1);
        }

        mapView = new MapView(this);
        final ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.kMap);
        mapViewContainer.addView(mapView);
        mapView.setMapViewEventListener(this);

        getAddress();

        //터치 액션이 맵뷰의 영역 밖으로 나가면 액션 중지
        mapView.setOnTouchListener(new View.OnTouchListener() {
            boolean isAction_cancel = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        float x, y;
                        x = event.getX();
                        y = event.getY();
                        if (!(x > 0 && x < v.getWidth() && y > 0 && y < v.getHeight())) {
                            event.setAction(MotionEvent.ACTION_CANCEL);
                            isAction_cancel = true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isAction_cancel)
                            event.setAction(MotionEvent.ACTION_CANCEL);
                        isAction_cancel = false;
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        ///sy
    }

    //sy 렌터카 메인 화면 및 예약시간 프래그먼트를 표현하기 위한 메소드
    public void replaceFragment(int i) {

        FragmentManager fragmentManager;
        FragmentTransaction transaction;
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        if (start_date != null && start_time != null && return_date != null && return_time != null) {
            bundle.putString("start_date", start_date);
            bundle.putString("start_time", start_time);
            bundle.putString("return_date", return_date);
            bundle.putString("return_time", return_time);
            bundle.putInt("start_timeInt", start_timeInt);
            bundle.putInt("return_timeInt", return_timeInt);
            bundle.putString("current_Address", cAddress);
        }
        switch (i) {
            case 1:
                maps_rent_mainfrag.setArguments(bundle);
                transaction.replace(R.id.framelayout_maps_rentcar, maps_rent_mainfrag).commitNow();
                rCode = 0;
                break;
            case 2:
                bundle.putInt("reOrRe", 1);
                maps_rent_time_reserve.setArguments(bundle);
                transaction.replace(R.id.framelayout_maps_rentcar, maps_rent_time_reserve).commit();
                rCode = 1;
                break;
            case 3:
                bundle.putInt("reOrRe", 2);
                maps_rent_time_return.setArguments(bundle);
                transaction.replace(R.id.framelayout_maps_rentcar, maps_rent_time_return).commit();
                rCode = 2;
                break;
            default:
                break;
        }
    }

    //화면의 뷰가 그려질때 불러오는 메소드
    //(화면이 그려지기 전에 getHeight가 0이 리턴 되기 때문에 mapview의 정확한 크기를 정할수 없었음)
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if ("steam".equals(main_path)) {
            constraintSet.constrainHeight(R.id.kMap, pt.y - mbtnReserveAddress.getHeight()
                    - mConstraintLayout.getHeight() - next_Layout.getHeight() + 20);
            constraintSet.applyTo(constraintLayout);
            framelayout_maps_rentCar.setVisibility(View.GONE);
        } else {
            constraintSet.constrainHeight(R.id.kMap, pt.y - framelayout_maps_rentCar.getHeight()+ 20);
            constraintSet.applyTo(constraintLayout);
        }
    }

    ///sy

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

                if ("steam".equals(main_path))
                    currentTextView.setText(cAddress);
                    //sy
                else
                    bundle.putString("current_Address", cAddress);
                ///sy
                Log.d("MapActivity", "address ==>" + address);


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "주소 정보가 없습니다.", Toast.LENGTH_LONG).show();
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
                mapView.clearFocus();

                // GPS 정보
                gps = new GPSInfo(gContext);
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

                my_latitude = latitude;
                my_longitude = longitude;

                if (gps.isGetLocation()) {

                    //주소 갱신
                    getAddress();
                    if ("steam".equals(main_path)) {
                        currentTextView.setText(cAddress);
                        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
                        //sy
                    } else {
                        bundle.putString("current_address", cAddress);
                        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
                        if(rCode == 0) {
                            if (((Maps_rent_mainfrag) getSupportFragmentManager().findFragmentById(R.id.framelayout_maps_rentcar)) != null)
                                ((Maps_rent_mainfrag) getSupportFragmentManager().findFragmentById(R.id.framelayout_maps_rentcar)).AddressChange();
                        }
                    }
                    ///sy

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
                intent = new Intent(this, ReservationCalendarActivity.class);
                intent.putExtra("reserve_address", cAddress);
                intent.putExtra("search_keyword", search_keyword);
//                startActivity(intent);

                // "예약일자" 설정
                intent = new Intent(this, ReservationCalendarActivity.class);
                startActivityForResult(intent, CALENDA_REQ_CODE);
                break;
            case R.id.btnNext:
                // "이 위치로 부름" 작업(시나리오 확인) => 현 위치로 이동
                if (gps.isGetLocation()) {

                    if ("steam".equals(main_path) || "driver".equals(main_path)) {
                        //시간선택으로 바로가기
                        intent = new Intent(this, ReservationTimeActivity.class);
                        intent.putExtra("reserve_address", cAddress);
                        if (TextUtils.isEmpty(search_keyword)) search_keyword = cAddress;
                        intent.putExtra("search_keyword", search_keyword);
                        intent.putExtra("reserve_year", reserve_year);
                        intent.putExtra("reserve_month", reserve_month);
                        intent.putExtra("reserve_day", reserve_day);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "서비스 준비중입니다.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    gps.showSettingsAlert();
                }
                //ConvertGPS("서울 송파구 석촌호수로 274");
                //Toast.makeText(this, cAddress + "로 부르셨습니다. 정보 갱신중..", Toast.LENGTH_LONG).show();
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
                if (!TextUtils.isEmpty(data.getStringExtra("search_result"))) {
                    cAddress = data.getStringExtra("search_result");
                    search_keyword = data.getStringExtra("search_keyword");
//                    if ("steam".equals(main_path))
//                        currentTextView.setText(cAddress);
//                        //sy
//                    else
//                        bundle.putString("current_address", cAddress);
//                    ///sy
                    ConvertGPS(cAddress);
                } else {
                    Toast.makeText(MapsActivity.this, "주소 검색을 다시 해주세요", Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(MapsActivity.this, "Result: " + data.getStringExtra("search_result"), Toast.LENGTH_SHORT).show();
            } else {   // RESULT_CANCEL
//                Toast.makeText(MapsActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == CALENDA_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                if (!TextUtils.isEmpty(data.getStringExtra("reserve_year"))) {
                    String dayofday = "";
                    reserve_year = data.getStringExtra("reserve_year");
                    reserve_month = data.getStringExtra("reserve_month");
                    reserve_day = data.getStringExtra("reserve_day");
                    try {
                        dayofday = Util.getDateDay(reserve_year + reserve_month + reserve_day);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    reserve_date.setText(reserve_month + "/ " + reserve_day + " (" + dayofday + ")"); //9/15 (화)
                } else {
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
    private void ConvertGPS(String cAddress) {
        List<Address> list = null;

//        String str = et3.getText().toString();
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

                //Toast.makeText(this, "위도 : " + lat + ", 경도 : " + lon, Toast.LENGTH_LONG).show();

                //sy
                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lat, lon), true);

                if ("steam".equals(main_path))
                    currentTextView.setText(cAddress);
                    //sy
                else
                    bundle.putString("current_address", cAddress);
                ///sy
                ///sy

//                String sss = String.format("geo:%f,%f", lat, lon);
//
//                Intent intent = new Intent(
//                        Intent.ACTION_VIEW,
//                        Uri.parse(sss));
//                startActivity(intent);
            }
        }
    }

    //sy
    public void start_reserveDate() {
        replaceFragment(2);
    }

    public void start_returnDate() {
        replaceFragment(3);
    }

    @Override
    public void onDateNTimePickerSet(String date, String time, int timeCheck) {
        if (rCode == 1) {
            start_date = date;
            start_time = time;
            start_timeInt = timeCheck + now_timeInt;
        } else if (rCode == 2){
            return_date = date;
            return_time = time;
            return_timeInt = timeCheck + now_timeInt;
        }
    }

    @Override
    public void rCodechk(int rCodecheck) {
        rCode = rCodecheck;
    }

    ///sy

    // 앱 해시키 얻는 메소드
    public static String getSigneture(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);

            for (int i = 0; i < packageInfo.signatures.length; i++) {
                Signature signature = packageInfo.signatures[i];
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    //카카오맵 맵뷰이벤트리스너 오버라이드

    @Override
    public void onMapViewInitialized(@NotNull MapView mapView) {
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(my_latitude, my_longitude), true);
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
        if (mapCheck) {
            getAddress();
            if(rCode == 0) {
                if((Maps_rent_mainfrag)getSupportFragmentManager().findFragmentById(R.id.framelayout_maps_rentcar) != null) {
                    ((Maps_rent_mainfrag) getSupportFragmentManager().findFragmentById(R.id.framelayout_maps_rentcar)).AddressChange();
                }
            }

        }
        mapCheck = true;
    }
    //marker.setMapPoint(mapView.getMapCenterPoint());

    //키 인증 결과 이벤트 오버라이드
    @Override
    public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int i, String s) {

    }

    //마커 오버라이드

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
