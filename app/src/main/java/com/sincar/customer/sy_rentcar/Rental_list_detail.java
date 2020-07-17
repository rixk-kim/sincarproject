package com.sincar.customer.sy_rentcar;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.sincar.customer.R;
import com.sincar.customer.item.RentCarDetailResult;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.Util;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.HashMap;

import static com.sincar.customer.HWApplication.rentCarDetailResult;
import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.MapsActivity._mMapsActivity;
import static com.sincar.customer.MapsActivity.homeKeyPressed;
import static com.sincar.customer.common.Constants.RENTCAR_LIST_DETAIL_REQUEST;
import static com.sincar.customer.common.Constants.RENTCAR_LIST_REQUEST;

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

    //디테일 정보 변수
    String start_date, start_time, return_date, return_time, start_year, return_year, start_address, end_address, reserve_address;
    String agent_name, rentcar_name, rentcar_num, rentcar_seq;
    String start_date_volley, return_date_volley;
    private TextView rental_car_start_date, rental_car_start_time, rental_car_end_date, rental_car_end_time;
    private TextView tvAgent_name, tvAgent_name_small, tvRental_car_name;
    private TextView btn_rental_allocate, btn_rental_return;
    private View view_touchless;
    private LinearLayout insuranceCheck;
    private CheckBox deleteCheck;

    //렌탈 리스트 디테일용 커스텀 다이얼로그
    CustomDialogInListDetail customSpinnerDialog;
    Bundle dlgSpinnerBundle;

    //디테일 정보 변수
    TextView tvCarNum, tvCarFuel, tvCarFpy, tvCarCol, tvCarMade, tvCarAge, tvCarMan;
    TextView tvAgentAddress, tvAgentTime, tvAgentDel;
    double lat, lng;
    TextView tvDelPri, tvDelTime, tvDelArea;
    TextView tvInsurance1, tvInsurance2, tvDelPrice, tvCarPrice, tvTotalPrice;
    TextView tvDelipossible;
    TextView tvInsuName;
    ImageView ivInsuInfo;
    int insuPrice, deliPrice, rentPrice; // 보험금액, 딜리버리 금액, 총 금액
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

        car_image = (ImageView) findViewById(R.id.rental_car_image);
        //디바이스 화면 크기 구하는 메소드
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x;
//        int height = size.y;

        //배차 및 반납 위치
        rental_allocate_text = (TextView) findViewById(R.id.rental_allocate_text);
        rental_return_text = (TextView) findViewById(R.id.rental_return_text);

        //배차 및 반납 위치 설정 버튼
        btn_rental_allocate = (TextView) findViewById(R.id.rental_allocate_position);
        btn_rental_return = (TextView) findViewById(R.id.rental_return_position);
        btn_rental_allocate.setOnClickListener(this);
        btn_rental_return.setOnClickListener(this);

        //지도
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rMap);
        view_touchless = (View) findViewById(R.id.view_touchless);

        mapView = new MapView(this);
        mapViewContainer = (ViewGroup) findViewById(R.id.rMap);
        mapViewContainer.addView(mapView);
        view_touchless.setClickable(true); //맵을 터치 불가 형식으로 만듬

        //딜리버리 설정 다이얼로그 호출용 텍스트뷰
        tvDelivery_dialog = findViewById(R.id.rental_spinner);

        //sy
        rental_car_start_date = (TextView) findViewById(R.id.rental_car_start_date);
        rental_car_start_time = (TextView) findViewById(R.id.rental_car_start_time);
        rental_car_end_date = (TextView) findViewById(R.id.rental_car_end_date);
        rental_car_end_time = (TextView) findViewById(R.id.rental_car_end_time);
        tvAgent_name = (TextView) findViewById(R.id.rental_agent);
        tvAgent_name_small = (TextView) findViewById(R.id.rental_agent_name);
        tvRental_car_name = (TextView) findViewById(R.id.rental_car_name);
        insuranceCheck = (LinearLayout) findViewById(R.id.rental_car_insu_check);
        deleteCheck = (CheckBox) findViewById(R.id.delete2_checkbox);
        tvDelipossible = (TextView) findViewById(R.id.rental_car_delivery);

        Intent intent = getIntent(); //Rental_list에서 넘어온 데이터 수신

        start_date = intent.getStringExtra("start_date");
        start_time = intent.getStringExtra("start_time");
        return_date = intent.getStringExtra("return_date");
        return_time = intent.getStringExtra("return_time");
        start_year = intent.getStringExtra("start_year");
        return_year = intent.getStringExtra("return_year");
        start_date_volley = intent.getStringExtra("start_date_volley");
        return_date_volley = intent.getStringExtra("return_date_volley");

        reserve_address = intent.getStringExtra("reserve_address");

        agent_name = intent.getStringExtra("REQUEST_AGENT");
        rentcar_name = intent.getStringExtra("REQUEST_RENTCAR");
        rentcar_seq = intent.getStringExtra("REQUEST_SEQ");
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
        tvAgent_name.setText(agent_name);
        tvRental_car_name.setText(rentcar_name);
        tvAgent_name_small.setText(agent_name);

        insuranceCheck.setOnClickListener(this);

        //디테일 정보 바인딩
        tvCarNum = (TextView) findViewById(R.id.rental_car_number);
        tvCarFuel = (TextView) findViewById(R.id.rental_car_fuel);
        tvCarFpy = (TextView) findViewById(R.id.rental_car_mileage);
        tvCarCol = (TextView) findViewById(R.id.rental_car_color);
        tvCarMade = (TextView) findViewById(R.id.rental_car_model);
        tvCarAge = (TextView) findViewById(R.id.rental_car_age);
        tvCarMan = (TextView) findViewById(R.id.rental_car_boarding);

        tvAgentAddress = (TextView) findViewById(R.id.rental_car_address);
        tvAgentTime = (TextView) findViewById(R.id.rental_car_sales_time);
        tvAgentDel = (TextView) findViewById(R.id.rental_car_sales_area);

        tvDelPri = (TextView) findViewById(R.id.rental_use_money);
        tvDelTime = (TextView) findViewById(R.id.rental_use_time);
        tvDelArea = (TextView) findViewById(R.id.rental_car_sales_area);

        tvInsuName = (TextView) findViewById(R.id.rental_insu_name);
        ivInsuInfo = (ImageView)findViewById(R.id.rental_insu_info);
        //가격 관련
        tvInsurance1 = (TextView) findViewById(R.id.rental_car_insurance);
        tvInsurance2 = (TextView) findViewById(R.id.rental_use_insurance);
        tvDelPrice = (TextView) findViewById(R.id.rental_use_delivery);
        tvCarPrice = (TextView) findViewById(R.id.rental_use_amount);
        tvTotalPrice = (TextView) findViewById(R.id.rental_use_total);

        //리퀘스트 보낼 키값과 밸류 값들
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("MEMBER_NO", voLoginItem.MEMBER_NO);
        postParams.put("RESERVE_ADDRESS", reserve_address);
        postParams.put("RESERVE_YEAR", start_year);
        postParams.put("RESERVE_DATE", start_date_volley);
        postParams.put("RESERVE_TIME", start_time);
        postParams.put("RETURN_YEAR", return_year);
        postParams.put("RETURN_DATE", return_date_volley);
        postParams.put("RETURN_TIME", return_time);
        postParams.put("REQUEST_AGENT", agent_name);
        postParams.put("REQUEST_RENTCAR", rentcar_name);
        postParams.put("REQUEST_SEQ", rentcar_seq);

        Util.showDialog(this);

        //발리네트워크 연결
        VolleyNetwork.getInstance(this).serverDataRequest(RENTCAR_LIST_DETAIL_REQUEST, postParams, onRentalListInteractionListener);

//        detailNetworkTest();

        ///sy

        //딜리버리 선택에 따른 배차,반납 위치 텍스트와 버튼 활성화 여부 설정
        spinner_Selected(select_delivery);

        tvDelivery_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_DELI)) {
                    Toast.makeText(getApplicationContext(), "이 차의 대리점은 딜리버리를 지원하지 않습니다.", Toast.LENGTH_LONG).show();
                } else {
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
                            Toast.makeText(getApplicationContext(), tvDelivery_dialog.getText().toString() + " 선택되었습니다.", Toast.LENGTH_SHORT).show();
                        }

                    });
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
            case R.id.rental_detail_btnPrev_layout:
                finish();
                break;

            case R.id.rental_confirm_btn:
                // TODO : 예약하기

                //TODO - 파라메터 추가
                //rental_payment액티비티에 전달할 데이터 입력
                intent = new Intent(this, Rental_payment.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                intent.putExtra("RESERVE_YEAR", start_year);
                intent.putExtra("RESERVE_DATE", start_date_volley);
                intent.putExtra("RESERVE_TIME", start_time);
                intent.putExtra("RETURN_YEAR", return_year);
                intent.putExtra("RETURN_DATE", return_date_volley);
                intent.putExtra("RETURN_TIME", return_time);
                intent.putExtra("RENTCAR_CAR", rentcar_name);
                intent.putExtra("RENTCAR_NUM", rentcar_num);
                intent.putExtra("RENTCAR_AGENT", agent_name);
                intent.putExtra("RENTCAR_RES_ADD", start_address);
                intent.putExtra("RENTCAR_RET_ADD", end_address);
                intent.putExtra("rental_pay", String.valueOf(rentPrice));    // 차량대여료
                intent.putExtra("delivery_pay", String.valueOf(deliPrice));       // 딜리버리금액
                intent.putExtra("insurance_pay", String.valueOf(insuPrice));  // 보험 금액
                intent.putExtra("CURRENT_INSU_SEQ", rentCarDetailResult.rentcar_detail.get(0).CURRENT_INSURANCE.get(0).CURRENT_INSU_SEQ);
                intent.putExtra("CURRENT_INSU_NAME", rentCarDetailResult.rentcar_detail.get(0).CURRENT_INSURANCE.get(0).CURRENT_INSU_NAME);
                intent.putExtra("select_delivery", select_delivery);    //딜리버리 선택 방식

                switch (select_delivery) {
                    //딜리버리 선택에 따른 확인 버튼 활성화
                    // case 0은 지점 방문이기에 default 값으로 대체
                    case 1:
                        if (rental_allocate_text.getText() == "배차 위치를 정해주세요" && rental_return_text.getText() == "반납 위치를 정해주세요") {
                            Toast.makeText(getApplicationContext(), "먼저 배차 및 위치를 정해주세요", Toast.LENGTH_LONG).show();
                        } else if (rental_allocate_text.getText() == "배차 위치를 정해주세요") {
                            Toast.makeText(getApplicationContext(), "먼저 배차 위치를 정해주세요", Toast.LENGTH_LONG).show();
                        } else if (rental_return_text.getText() == "반납 위치를 정해주세요") {
                            Toast.makeText(getApplicationContext(), "먼저 반납 위치를 정해주세요", Toast.LENGTH_LONG).show();
                        } else {
                            startActivity(intent);
                        }
                        break;
                    case 2:
                        if (rental_allocate_text.getText() == "배차 위치를 정해주세요") {
                            Toast.makeText(getApplicationContext(), "먼저 배차 위치를 정해주세요", Toast.LENGTH_LONG).show();
                        } else {
                            startActivity(intent);
                        }
                        break;
                    case 3:
                        if (rental_return_text.getText() == "반납 위치를 정해주세요") {
                            Toast.makeText(getApplicationContext(), "먼저 배차 위치를 정해주세요", Toast.LENGTH_LONG).show();
                        } else {
                            startActivity(intent);
                        }
                        break;
                    default:
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
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                intent.putExtra("delivery_type", select_delivery);
                startActivityForResult(intent, CAR_START_REQ_CODE);
                //Toast.makeText(getApplicationContext(),"배차위치 변경하러 가기",Toast.LENGTH_SHORT).show();
                break;

            case R.id.rental_return_position:
                // TODO : 반납위치 변경하러 가기
                intent = new Intent(this, Rental_car_delivery_map.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                intent.putExtra("delivery_type", select_delivery);
                startActivityForResult(intent, CAR_END_REQ_CODE);
                //Toast.makeText(getApplicationContext(),"반납위치 변경하러 가기",Toast.LENGTH_SHORT).show();
                ///sy
                break;

            case R.id.rental_car_insu_check:
                // TODO : 보험 체크
                if(deleteCheck.isClickable()) {
                    deleteCheck.setChecked(!deleteCheck.isChecked());
                    insuCheck();
                }
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
            if (resultCode == RESULT_OK) {
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
        int total;
        switch (spinner_type) {
            case 0:
                btn_rental_allocate.setVisibility(View.INVISIBLE);
                btn_rental_return.setVisibility(View.INVISIBLE);
                rental_allocate_text.setText("사용하지 않음");
                rental_return_text.setText("사용하지 않음");
                deliPrice = 0;
                total = deliPrice + insuPrice + rentPrice;
                tvDelPrice.setText("0원");
                tvTotalPrice.setText(Util.setAddMoneyDot(String.valueOf(total)) + "원");
                break;
            case 1:
                btn_rental_allocate.setVisibility(View.VISIBLE);
                btn_rental_return.setVisibility(View.VISIBLE);
                text_Selected(start_address, end_address);
                deliPrice = Integer.parseInt(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_DEL_PRI.ROUND_TRIP);
                total = deliPrice + insuPrice + rentPrice;
                tvTotalPrice.setText(Util.setAddMoneyDot(String.valueOf(total)) + "원");
                tvDelPrice.setText(Util.setAddMoneyDot(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_DEL_PRI.ROUND_TRIP) + "원");
                tvDelPri.setText(Util.setAddMoneyDot(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_DEL_PRI.ROUND_TRIP) + "원");
                break;
            case 2:
                btn_rental_allocate.setVisibility(View.VISIBLE);
                btn_rental_return.setVisibility(View.INVISIBLE);
                text_Selected(start_address, "사용하지 않음");
                deliPrice = Integer.parseInt(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_DEL_PRI.DISPATCH);
                total = deliPrice + insuPrice + rentPrice;
                tvTotalPrice.setText(Util.setAddMoneyDot(String.valueOf(total)) + "원");
                tvDelPrice.setText(Util.setAddMoneyDot(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_DEL_PRI.DISPATCH) + "원");
                tvDelPri.setText(Util.setAddMoneyDot(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_DEL_PRI.DISPATCH) + "원");
                break;
            case 3:
                btn_rental_allocate.setVisibility(View.INVISIBLE);
                btn_rental_return.setVisibility(View.VISIBLE);
                text_Selected("사용하지 않음", end_address);
                deliPrice = Integer.parseInt(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_DEL_PRI.RETURN);
                total = deliPrice + insuPrice + rentPrice;
                tvTotalPrice.setText(Util.setAddMoneyDot(String.valueOf(total)) + "원");
                tvDelPrice.setText(Util.setAddMoneyDot(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_DEL_PRI.RETURN) + "원");
                tvDelPri.setText(Util.setAddMoneyDot(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_DEL_PRI.RETURN) + "원");
                break;
            default:
                break;
        }
    }

    //선택된 딜리버리 시스템에 따라 딜리버리 관련 텍스트뷰 설정
    void text_Selected(String start, String end) {
        if (start == null) {
            rental_allocate_text.setText("배차 위치를 정해주세요");
        } else if (start == "사용하지 않음") {
            rental_allocate_text.setText("사용하지 않음");
        } else {
            rental_allocate_text.setText(start_address);
        }
        if (end == null) {
            rental_return_text.setText("반납 위치를 정해주세요");
        } else if (end == "사용하지 않음") {
            rental_return_text.setText("사용하지 않음");
        } else {
            rental_return_text.setText(end_address);
        }
    }

    //발리 네트워크 적용후 리퀘스트 받은 데이터 적용
    private void setDetailText() throws Exception {

        rentcar_num = rentCarDetailResult.rentcar_detail.get(0).CURRENT_CAR_NUM;
        tvCarNum.setText(rentcar_num);
        tvCarFuel.setText(rentCarDetailResult.rentcar_detail.get(0).CURRENT_CAR_FUEL);
        tvCarFpy.setText(rentCarDetailResult.rentcar_detail.get(0).CURRENT_CAR_FPY);
        tvCarCol.setText(rentCarDetailResult.rentcar_detail.get(0).CURRENT_CAR_COLOR);
        tvCarMade.setText(rentCarDetailResult.rentcar_detail.get(0).CURRENT_CAR_MADE);
        tvCarMan.setText(rentCarDetailResult.rentcar_detail.get(0).CURRENT_CAR_MAN + "인승");
        int age = Integer.parseInt(rentCarDetailResult.rentcar_detail.get(0).CURRENT_CAR_AGE);
        switch (age) {
            case 0:
                tvCarAge.setText("전체");
                break;
            case 1:
                tvCarAge.setText("만 21세 이상");
                break;
            case 2:
                tvCarAge.setText("만 26세 이상");
                break;
            default:
                break;
        }

        String cAddress = rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_ADDRESS;
        if (cAddress.length() > 14) {
            String[] strArray = cAddress.split(" ");
            cAddress = "";
            for (int i = 0; i < strArray.length; i++) {
                if (strArray.length / 2 == i)
                    cAddress += "\n";
                cAddress += strArray[i] + " ";
            }
        }
        tvAgentAddress.setText(cAddress);
        tvAgentTime.setText("(주중) : " + rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_TIME + "\n" +
                "(주말) : " + rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_TIME_WKD);
        tvAgentDel.setText(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_DELI);

        tvDelPri.setText("0원");
        tvDelTime.setText(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_DEL_TIM);
        tvDelArea.setText(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_DEL_POS);

        if (!"".equals(rentCarDetailResult.rentcar_detail.get(0).CURRENT_INSURANCE.get(0).CURRENT_INSU_SEQ)) {
            tvInsurance1.setText("1일 " + Util.setAddMoneyDot(rentCarDetailResult.rentcar_detail.get(0).CURRENT_INSURANCE.get(0).CURRENT_INSU_PRI) + "원");
            tvInsuName.setText(rentCarDetailResult.rentcar_detail.get(0).CURRENT_INSURANCE.get(0).CURRENT_INSU_NAME);
            insuCheck();
        } else {
            deleteCheck.setClickable(false);
            tvInsuName.setText("보험이 없습니다.");
            ivInsuInfo.setVisibility(View.GONE);
            tvInsurance1.setText("");
            tvInsurance2.setText("");
        }

        // 딜리버리 체크는 spinner_Selected 메소드로 이미 함
        tvCarPrice.setText(Util.setAddMoneyDot(rentCarDetailResult.rentcar_detail.get(0).CURRENT_PRICE) + "원");

        lat = Double.parseDouble(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_ADD_LAT);
        lng = Double.parseDouble(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_ADD_LON);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lat, lng), true);

        //자동차 이미지
        Glide.with(this).load(rentCarDetailResult.rentcar_detail.get(0).CURRENT_CAR_URL).into(car_image);

        rentPrice = Integer.parseInt(rentCarDetailResult.rentcar_detail.get(0).CURRENT_PRICE);
        tvTotalPrice.setText(Util.setAddMoneyDot(rentCarDetailResult.rentcar_detail.get(0).CURRENT_PRICE) + "원");
        if ("1".equals(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_DELI)) {
            tvDelipossible.setText("불가능");
        } else {
            tvDelipossible.setText("가능");
        }
    }

    //보험 체크박스 체크 여부 확인후 텍스트뷰 설정
    private void insuCheck() {
        int total;
        if (deleteCheck.isChecked()) {
            insuPrice = Integer.parseInt(rentCarDetailResult.rentcar_detail.get(0).CURRENT_INSURANCE.get(0).CURRENT_INSU_PRI);
            total = insuPrice + deliPrice + rentPrice;
            tvInsurance2.setText(Util.setAddMoneyDot(rentCarDetailResult.rentcar_detail.get(0).CURRENT_INSURANCE.get(0).CURRENT_INSU_PRI) + "원");
            tvTotalPrice.setText(Util.setAddMoneyDot(String.valueOf(total)) + "원");
        } else {
            insuPrice = 0;
            total = insuPrice + deliPrice + rentPrice;
            tvInsurance2.setText("0원");
            tvTotalPrice.setText(Util.setAddMoneyDot(String.valueOf(total)) + "원");
        }
    }

    //발리네트워크
    VolleyNetwork.OnResponseListener onRentalListInteractionListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String it) {
            Gson gson = new Gson();
            Log.i("Volley", it);
            rentCarDetailResult = gson.fromJson(it, RentCarDetailResult.class);
            //받아온 데이터 텍스트뷰에 정리
            try {
                setDetailText();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "서버가 정상적이지 않습니다.잠시후에 시도해주세요", Toast.LENGTH_LONG).show();
                finish();
            }
            //프로그래스바 종료
            Util.dismiss();

        }

        @Override
        public void onResponseFailListener(VolleyError it) {
            //프로그래스바 종료
            Util.dismiss();
        }
    };

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        homeKeyPressed = true;
        _mMapsActivity.onPause();
    }

    private void detailNetworkTest() {
        String it = null;
        switch (rentcar_seq) {
            case "3":
                it = "{\"rentcar_detail\":[{\"CURRENT_CAR_URL\":\"\",\"CURRENT_CAR_NUM\":\"\",\"CURRENT_CAR_FUEL\":\"\",\"CURRENT_CAR_FPY\":\"\"," +
                        "\"CURRENT_CAR_COLOR\":\"\",\"CURRENT_CAR_MADE\":\"\",\"CURRENT_CAR_AGE\":\"\",\"CURRENT_CAR_MAN\":\"\",\"CURRENT_AGENT_ADDRESS\":\"\"," +
                        "\"CURRENT_AGENT_ADD_LAT\":\"\",\"CURRENT_AGENT_ADD_LON\":\"\",\"CURRENT_AGENT_TIME\":\"\",\"CURRENT_AGENT_TIME_WKD\":\"\",\"CURRENT_AGENT_DELI\":\"\"," +
                        "\"CURRENT_AGENT_DEL_PRI\":{ \"ROUND_TRIP\" : \"\" , \"DISPATCH\" : \"\" , \"RETURN\" : \"\"},\"CURRENT_AGENT_DEL_TIM\":\"\",\"CURRENT_AGENT_DEL_POS\":\"\",\"CURRENT_PRICE\":\"\"}]}";
                break;
            case "6":
            case "11":
            case "16":
            case "21":
            case "26":
            case "31":
            case "36":
            case "41":
            case "46":
                it = "{\"rentcar_detail\": [{\"CURRENT_CAR_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201902271113552281.jpg\"," +
                        "\"CURRENT_CAR_NUM\":\"20하9182\",\"CURRENT_CAR_FUEL\":\"가솔린(G),디젤(D)\",\"CURRENT_CAR_FPY\":\"10.6-18.4 KM/L\",\"CURRENT_CAR_COLOR\":\"와인레드\"," +
                        "\"CURRENT_CAR_MADE\":\"2018\",\"CURRENT_CAR_AGE\":\"0\",\"CURRENT_CAR_MAN\":\"4\",\"CURRENT_AGENT_ADDRESS\":\"제주 지점\",\"CURRENT_AGENT_ADD_LAT\":\"33.49997329711914\"," +
                        "\"CURRENT_AGENT_ADD_LON\":\"126.53034973144531\",\"CURRENT_AGENT_TIME\":\"09:00-19:00\",\"CURRENT_AGENT_TIME_WKD\":\"09:00-13:00\", \"CURRENT_AGENT_DELI\" : \"1\", " +
                        "\"CURRENT_PRICE\":\"60000\", \"CURRENT_INSURANCE\": \"10000\", \"CURRENT_INSU_SEQ\" : \"0\", \"CURRENT_INSU_NAME\" : \"일일 손해 보험\"}]}";
                break;
            case "2":
            case "7":
            case "12":
            case "17":
            case "22":
            case "27":
            case "32":
            case "37":
            case "42":
            case "47":
                it = "{\"rentcar_detail\": [{\"CURRENT_CAR_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201805281053589087.jpg\"," +
                        "\"CURRENT_CAR_NUM\":\"19하1234\",\"CURRENT_CAR_FUEL\":\"가솔린(G)\",\"CURRENT_CAR_FPY\":\"8.0-13.8 KM/L\",\"CURRENT_CAR_COLOR\":\"브라운\"," +
                        "\"CURRENT_CAR_MADE\":\"2019\",\"CURRENT_CAR_AGE\":\"2\",\"CURRENT_CAR_MAN\":\"4\",\"CURRENT_AGENT_ADDRESS\":\"제주 지점\",\"CURRENT_AGENT_ADD_LAT\":\"33.49997329711914\"," +
                        "\"CURRENT_AGENT_ADD_LON\":\"126.53034973144531\",\"CURRENT_AGENT_TIME\":\"08:00-19:00\",\"CURRENT_AGENT_TIME_WKD\":\"09:00-13:00\",\"CURRENT_AGENT_DELI\":\"0\"," +
                        "\"CURRENT_AGENT_DEL_PRI\": {\"ROUND_TRIP\" : \"20000\",\"DISPATCH\" : \"10000\", \"RETURN\" : \"10000\" },\"CURRENT_AGENT_DEL_TIM\":\"09:00-18:00\"," +
                        "\"CURRENT_AGENT_DEL_POS\":\"제주시, 서귀포시\",\"CURRENT_PRICE\":\"200000\", \"CURRENT_INSURANCE\": \"20000\", \"CURRENT_INSU_SEQ\" : \"0\", \"CURRENT_INSU_NAME\" : \"손해 보험\"}]}";
                break;
            case "1":
            case "8":
            case "13":
            case "18":
            case "23":
            case "28":
            case "33":
            case "38":
            case "43":
            case "48":
                it = "{\"rentcar_detail\": [{\"CURRENT_CAR_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201902271111488598.jpg\"," +
                        "\"CURRENT_CAR_NUM\":\"18하5678\",\"CURRENT_CAR_FUEL\":\"가솔린(G),디젤(D)\",\"CURRENT_CAR_FPY\":\"8.7-13.8 KM/L\",\"CURRENT_CAR_COLOR\":\"화이트그레이\"," +
                        "\"CURRENT_CAR_MADE\":\"2018\",\"CURRENT_CAR_AGE\":\"2\",\"CURRENT_CAR_MAN\":\"6\",\"CURRENT_AGENT_ADDRESS\":\"제주 지점\",\"CURRENT_AGENT_ADD_LAT\":\"33.49997329711914\"," +
                        "\"CURRENT_AGENT_ADD_LON\":\"126.53034973144531\",\"CURRENT_AGENT_TIME\":\"09:00-19:00\",\"CURRENT_AGENT_TIME_WKD\":\"09:00-13:00\",\"CURRENT_AGENT_DELI\":\"0\"," +
                        "\"CURRENT_AGENT_DEL_PRI\": {\"ROUND_TRIP\" : \"30000\",\"DISPATCH\" : \"15000\", \"RETURN\" : \"15000\" },\"CURRENT_AGENT_DEL_TIM\":\"10:00-19:00\"," +
                        "\"CURRENT_AGENT_DEL_POS\":\"제주시, 서귀포시\",\"CURRENT_PRICE\":\"120000\", \"CURRENT_INSURANCE\": \"30000\", \"CURRENT_INSU_SEQ\" : \"0\", \"CURRENT_INSU_NAME\" : \"일일보험\"}]}";
                break;
            case "4":
            case "9":
            case "14":
            case "19":
            case "24":
            case "29":
            case "34":
            case "39":
            case "44":
            case "49":
                it = "{\"rentcar_detail\": [{\"CURRENT_CAR_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/202001091104489284.jpg\"," +
                        "\"CURRENT_CAR_NUM\":\"17하9012\",\"CURRENT_CAR_FUEL\":\"가솔린(G),디젤(D),LPG(L)\",\"CURRENT_CAR_FPY\":\"7.4-14.8 KM/L\",\"CURRENT_CAR_COLOR\":\"라이트블랙\"," +
                        "\"CURRENT_CAR_MADE\":\"2017\",\"CURRENT_CAR_AGE\":\"2\",\"CURRENT_CAR_MAN\":\"4\",\"CURRENT_AGENT_ADDRESS\":\"제주 지점\",\"CURRENT_AGENT_ADD_LAT\":\"33.49997329711914\"," +
                        "\"CURRENT_AGENT_ADD_LON\":\"126.53034973144531\",\"CURRENT_AGENT_TIME\":\"09:00-19:00\",\"CURRENT_AGENT_TIME_WKD\":\"09:00-13:00\",\"CURRENT_AGENT_DELI\":\"0\"," +
                        "\"CURRENT_AGENT_DEL_PRI\": {\"ROUND_TRIP\" : \"10000\",\"DISPATCH\" : \"5000\", \"RETURN\" : \"5000\" },\"CURRENT_AGENT_DEL_TIM\":\"10:00-19:00\"," +
                        "\"CURRENT_AGENT_DEL_POS\":\"제주시, 서귀포시\",\"CURRENT_PRICE\":\"115000\", \"CURRENT_INSURANCE\": \"15000\", \"CURRENT_INSU_SEQ\" : \"0\", \"CURRENT_INSU_NAME\" : \"일일손해 보험\"}]}";
                break;
            case "5":
            case "10":
            case "15":
            case "20":
            case "25":
            case "30":
            case "35":
            case "40":
            case "45":
            case "50":
                it = "{\"rentcar_detail\": [{\"CURRENT_CAR_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201908131116037666.jpg\"," +
                        "\"CURRENT_CAR_NUM\":\"16하3456\",\"CURRENT_CAR_FUEL\":\"가솔린(G),디젤(D)\",\"CURRENT_CAR_FPY\":\"10.7-16.1 KM/L\",\"CURRENT_CAR_COLOR\":\"골드\"," +
                        "\"CURRENT_CAR_MADE\":\"2016\",\"CURRENT_CAR_AGE\":\"2\",\"CURRENT_CAR_MAN\":\"4\",\"CURRENT_AGENT_ADDRESS\":\"제주 지점\",\"CURRENT_AGENT_ADD_LAT\":\"33.49997329711914\"," +
                        "\"CURRENT_AGENT_ADD_LON\":\"126.53034973144531\",\"CURRENT_AGENT_TIME\":\"09:00-19:00\",\"CURRENT_AGENT_TIME_WKD\":\"09:00-13:00\",\"CURRENT_AGENT_DELI\":\"0\"," +
                        "\"CURRENT_AGENT_DEL_PRI\": {\"ROUND_TRIP\" : \"150000\",\"DISPATCH\" : \"10000\", \"RETURN\" : \"10000\" },\"CURRENT_AGENT_DEL_TIM\":\"10:00-19:00\"," +
                        "\"CURRENT_AGENT_DEL_POS\":\"제주시, 서귀포시\",\"CURRENT_PRICE\":\"65000\", \"CURRENT_INSURANCE\": \"5000\", \"CURRENT_INSU_SEQ\" : \"0\", \"CURRENT_INSU_NAME\" : \"일일손해보험\"}]}";
                break;
            default:
                break;
        }

        Gson gson = new Gson();
        rentCarDetailResult = gson.fromJson(it, RentCarDetailResult.class);

//        setDetailText();
    }
}




