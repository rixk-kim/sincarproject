package com.sincar.customer.sy_rentcar;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.sincar.customer.R;
import com.sincar.customer.item.RentCarDetailResult;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.Util;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.w3c.dom.Text;

import java.util.HashMap;

import static com.sincar.customer.HWApplication.rentCarDetailResult;
import static com.sincar.customer.HWApplication.voLoginItem;
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
    String start_date, start_time, return_date, return_time, start_address, end_address, reserve_address;
    String agent_name, rentcar_name, rentcar_seq;
    private TextView rental_car_start_date, rental_car_start_time, rental_car_end_date, rental_car_end_time;
    private TextView tvAgent_name, tvAgent_name_small, tvRental_car_name;
    private TextView btn_rental_allocate, btn_rental_return;
    private View view_touchless;
    private LinearLayout insuranceCheck; private CheckBox deleteCheck;

    //렌탈 리스트 디테일용 커스텀 다이얼로그
    CustomDialogInListDetail customSpinnerDialog;
    Bundle dlgSpinnerBundle;

    //디테일 정보 변수
    TextView tvCarNum, tvCarFuel, tvCarFpy, tvCarCol, tvCarMade, tvCarAge, tvCarMan;
    TextView tvAgentAddress, tvAgentTime, tvAgentDel;
    double lat, lng;
    TextView tvDelPri, tvDelTime, tvDelArea;
    TextView tvInsurance1, tvInsurance2, tvDelPrice, tvCarPrice, tvTotalPrice;
    int insuPrice, deliPrice, totalPrice; // 보험금액, 딜리버리 금액, 총 금액
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

        //배차 및 반납 위치
        rental_allocate_text = (TextView) findViewById(R.id.rental_allocate_text);
        rental_return_text   = (TextView) findViewById(R.id.rental_return_text);

        //배차 및 반납 위치 설정 버튼
        btn_rental_allocate = (TextView)findViewById(R.id.rental_allocate_position);
        btn_rental_return = (TextView)findViewById(R.id.rental_return_position);
        btn_rental_allocate.setOnClickListener(this);
        btn_rental_return.setOnClickListener(this);

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
        tvAgent_name = (TextView)findViewById(R.id.rental_agent);
        tvAgent_name_small = (TextView)findViewById(R.id.rental_agent_name);
        tvRental_car_name = (TextView)findViewById(R.id.rental_car_name);
        insuranceCheck = (LinearLayout)findViewById(R.id.rental_car_insu_check);
        deleteCheck = (CheckBox)findViewById(R.id.delete_checkbox);

        Intent intent = getIntent(); //Rental_list에서 넘어온 데이터 수신

        start_date = intent.getStringExtra("start_date");
        start_time = intent.getStringExtra("start_time");
        return_date = intent.getStringExtra("return_date");
        return_time = intent.getStringExtra("return_time");
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
        tvCarNum = (TextView)findViewById(R.id.rental_car_number);
        tvCarFuel = (TextView)findViewById(R.id.rental_car_fuel);
        tvCarFpy = (TextView)findViewById(R.id.rental_car_mileage);
        tvCarCol = (TextView)findViewById(R.id.rental_car_color);
        tvCarMade = (TextView)findViewById(R.id.rental_car_model);
        tvCarAge = (TextView)findViewById(R.id.rental_car_age);
        tvCarMan = (TextView)findViewById(R.id.rental_car_boarding);

        tvAgentAddress = (TextView)findViewById(R.id.rental_car_address);
        tvAgentTime = (TextView)findViewById(R.id.rental_car_sales_time);
        tvAgentDel = (TextView)findViewById(R.id.rental_car_sales_area);

        tvDelPri = (TextView)findViewById(R.id.rental_use_money);
        tvDelTime = (TextView)findViewById(R.id.rental_use_time);
        tvDelArea = (TextView)findViewById(R.id.rental_car_sales_area);

        tvInsurance1 = (TextView)findViewById(R.id.rental_car_insurance);
        tvInsurance2 = (TextView)findViewById(R.id.rental_use_insurance);
        tvDelPrice = (TextView)findViewById(R.id.rental_use_delivery);
        tvCarPrice = (TextView)findViewById(R.id.rental_use_amount);
        tvTotalPrice = (TextView)findViewById(R.id.rental_use_total);

        //딜리버리 선택에 따른 배차,반납 위치 텍스트와 버튼 활성화 여부 설정
        spinner_Selected(select_delivery);

        detailNetworkTest();

        //리퀘스트 보낼 키값과 밸류 값들
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("MEMBER_NO", voLoginItem.MEMBER_NO);
        postParams.put("RESERVE_ADDRESS", reserve_address);
        postParams.put("RESRERVE_YEAR", "2020");
        postParams.put("RESERVE_DATE", start_date);
        postParams.put("RESERVE_TIME", start_time);
        postParams.put("RETURN_YEAR", "2020");
        postParams.put("RETURN_DATE", return_date);
        postParams.put("RETURN_TIME", return_time);
        postParams.put("REQUEST_AGENT", agent_name);
        postParams.put("REQUEST_RENTCAR", rentcar_name);
        postParams.put("REQUEST_SEQ", rentcar_seq);

        //Util.showDialog(rental_list_context);

        //발리네트워크 연결
//        VolleyNetwork.getInstance(this).serverDataRequest(RENTCAR_LIST_REQUEST, postParams, onRentalListInteractionListener);

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
                            //TODO - 파라메터 추가
                            intent.putExtra("rental_pay", "160000");    // 차량대여료
                            intent.putExtra("delivery_pay", "0");       // 딜리버리금액
                            intent.putExtra("insurance_pay", "10000");  // 보험 금액
                            startActivity(intent);
                        }
                        break;
                    case 2:
                        if(rental_allocate_text.getText() == "배차 위치를 정해주세요") {
                            Toast.makeText(getApplicationContext(), "먼저 배차 위치를 정해주세요",Toast.LENGTH_LONG).show();
                        } else {
                            intent = new Intent(this, Rental_payment.class);
                            //TODO - 파라메터 추가
                            intent.putExtra("rental_pay", "160000");    // 차량대여료
                            intent.putExtra("delivery_pay", "0");       // 딜리버리금액
                            intent.putExtra("insurance_pay", "10000");  // 보험 금액
                            startActivity(intent);
                        }
                        break;
                    case 3:
                        if (rental_return_text.getText() == "반납 위치를 정해주세요") {
                            Toast.makeText(getApplicationContext(), "먼저 배차 위치를 정해주세요",Toast.LENGTH_LONG).show();
                        } else {
                            intent = new Intent(this, Rental_payment.class);
                            //TODO - 파라메터 추가
                            intent.putExtra("rental_pay", "160000");    // 차량대여료
                            intent.putExtra("delivery_pay", "0");       // 딜리버리금액
                            intent.putExtra("insurance_pay", "10000");  // 보험 금액
                            startActivity(intent);
                        }
                        break;
                    default:
                        intent = new Intent(this, Rental_payment.class);
                        //TODO - 파라메터 추가
                        intent.putExtra("rental_pay", "160000");    // 차량대여료
                        intent.putExtra("delivery_pay", "0");       // 딜리버리금액
                        intent.putExtra("insurance_pay", "10000");  // 보험 금액
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

            case R.id.rental_car_insu_check:
                // TODO : 보험 체크
                deleteCheck.setChecked(!deleteCheck.isChecked());
                int total;
                if(deleteCheck.isChecked()){
                    insuPrice = Integer.parseInt(rentCarDetailResult.rentcar_detail.get(0).CURRENT_INSURANCE);
                    total = insuPrice + deliPrice + totalPrice;
                    tvInsurance2.setText(rentCarDetailResult.rentcar_detail.get(0).CURRENT_INSURANCE + "원");
                    tvTotalPrice.setText(String.valueOf(total) + "원");
                } else {
                    insuPrice = 0;
                    total = insuPrice + deliPrice + totalPrice;
                    tvInsurance2.setText("0원");
                    tvTotalPrice.setText(String.valueOf(total) + "원");
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
        int total;
        switch(spinner_type) {
            case 0:
                btn_rental_allocate.setVisibility(View.INVISIBLE);
                btn_rental_return.setVisibility(View.INVISIBLE);
                rental_allocate_text.setText("사용하지 않음");
                rental_return_text.setText("사용하지 않음");
                deliPrice = 0;
                total = deliPrice + insuPrice + totalPrice;
                tvDelPrice.setText("0원");
                tvTotalPrice.setText(String.valueOf(total) + "원");
                break;
            case 1:
                btn_rental_allocate.setVisibility(View.VISIBLE);
                btn_rental_return.setVisibility(View.VISIBLE);
                text_Selected(start_address, end_address);
                deliPrice = Integer.parseInt(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_DEL_PRI);
                total = deliPrice + insuPrice + totalPrice;
                tvTotalPrice.setText(String.valueOf(total) + "원");
                tvDelPrice.setText(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_DEL_PRI + "원");
                break;
            case 2:
                btn_rental_allocate.setVisibility(View.VISIBLE);
                btn_rental_return.setVisibility(View.INVISIBLE);
                text_Selected(start_address, "사용하지 않음");
                deliPrice = Integer.parseInt(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_DEL_PRI);
                total = deliPrice + insuPrice + totalPrice;
                tvTotalPrice.setText(String.valueOf(total) + "원");
                tvDelPrice.setText(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_DEL_PRI + "원");
                break;
            case 3:
                btn_rental_allocate.setVisibility(View.INVISIBLE);
                btn_rental_return.setVisibility(View.VISIBLE);
                text_Selected("사용하지 않음", end_address);
                deliPrice = Integer.parseInt(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_DEL_PRI);
                total = deliPrice + insuPrice + totalPrice;
                tvTotalPrice.setText(String.valueOf(total) + "원");
                tvDelPrice.setText(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_DEL_PRI + "원");
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

    private void setDetailText() {

        //데이터 적용
        tvCarNum.setText(rentCarDetailResult.rentcar_detail.get(0).CURRENT_CAR_NUM);
        tvCarFuel.setText(rentCarDetailResult.rentcar_detail.get(0).CURRENT_CAR_FUEL);
        tvCarFpy.setText(rentCarDetailResult.rentcar_detail.get(0).CURRENT_CAR_FPY);
        tvCarCol.setText(rentCarDetailResult.rentcar_detail.get(0).CURRENT_CAR_COLOR);
        tvCarMade.setText(rentCarDetailResult.rentcar_detail.get(0).CURRENT_CAR_MADE);
        tvCarMan.setText(rentCarDetailResult.rentcar_detail.get(0).CURRENT_CAR_MAN + "인승");
        int age = Integer.parseInt(rentCarDetailResult.rentcar_detail.get(0).CURRENT_CAR_AGE);
        switch(age) {
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

        tvAgentAddress.setText(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_ADDRESS);
        tvAgentTime.setText(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_TIME);
        tvAgentDel.setText(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_DELI);

        tvDelPri.setText(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_DEL_PRI);
        tvDelTime.setText(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_DEL_TIM);
        tvDelArea.setText(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_DEL_POS);

        tvInsurance1.setText("1일 " + rentCarDetailResult.rentcar_detail.get(0).CURRENT_INSURANCE + "원");
        tvInsurance2.setText("0원");
        tvDelPrice.setText("0원");
        tvCarPrice.setText(rentCarDetailResult.rentcar_detail.get(0).CURRENT_PRICE + "원");

        lat = Double.parseDouble(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_ADD_LAT);
        lng = Double.parseDouble(rentCarDetailResult.rentcar_detail.get(0).CURRENT_AGENT_ADD_LON);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lat, lng), true);

        //자동차 이미지
        Glide.with(this).load(rentCarDetailResult.rentcar_detail.get(0).CURRENT_CAR_URL).into(car_image);

        insuPrice = 0; deliPrice = 0;
        totalPrice = Integer.parseInt(rentCarDetailResult.rentcar_detail.get(0).CURRENT_PRICE);
        tvTotalPrice.setText(rentCarDetailResult.rentcar_detail.get(0).CURRENT_PRICE + "원");
    }

    VolleyNetwork.OnResponseListener onRentalListInteractionListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String it) {
            Gson gson = new Gson();
            rentCarDetailResult = gson.fromJson(it, RentCarDetailResult.class);
            //받아온 데이터 텍스트뷰에 정리
            setDetailText();
            //프로그래스바 종료
            Util.dismiss();
        }

        @Override
        public void onResponseFailListener(VolleyError it) {
            //프로그래스바 종료
            Util.dismiss();
        }
    };

    private void detailNetworkTest() {
        String it = null;
        switch(rentcar_seq) {
            case "1":
            case "6":
                it =  "{\"rentcar_detail\": [{\"CURRENT_CAR_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201902271113552281.jpg\"," +
                        "\"CURRENT_CAR_NUM\":\"20하9182\",\"CURRENT_CAR_FUEL\":\"가솔린(G),디젤(D)\",\"CURRENT_CAR_FPY\":\"10.6-18.4 KM/L\",\"CURRENT_CAR_COLOR\":\"와인레드\"," +
                        "\"CURRENT_CAR_MADE\":\"2018\",\"CURRENT_CAR_AGE\":\"0\",\"CURRENT_CAR_MAN\":\"4\",\"CURRENT_AGENT_ADDRESS\":\"제주 지점\",\"CURRENT_AGENT_ADD_LAT\":\"33.49997329711914\"," +
                        "\"CURRENT_AGENT_ADD_LON\":\"126.53034973144531\",\"CURRENT_AGENT_TIME\":\"09:00-19:00\",\"CURRENT_AGENT_DELI\":\"0\"," +
                        "\"CURRENT_AGENT_DEL_PRI\":\"10000\",\"CURRENT_AGENT_DEL_TIM\":\"10:00-19:00\"," +
                        "\"CURRENT_AGENT_DEL_POS\":\"제주시, 서귀포시\",\"CURRENT_PRICE\":\"60000\", \"CURRENT_INSURANCE\": \"10000\"}]}";
                break;
            case "2":
            case "7":
                it =  "{\"rentcar_detail\": [{\"CURRENT_CAR_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201805281053589087.jpg\"," +
                        "\"CURRENT_CAR_NUM\":\"19하1234\",\"CURRENT_CAR_FUEL\":\"가솔린(G)\",\"CURRENT_CAR_FPY\":\"8.0-13.8 KM/L\",\"CURRENT_CAR_COLOR\":\"브라운\"," +
                        "\"CURRENT_CAR_MADE\":\"2019\",\"CURRENT_CAR_AGE\":\"2\",\"CURRENT_CAR_MAN\":\"4\",\"CURRENT_AGENT_ADDRESS\":\"제주 지점\",\"CURRENT_AGENT_ADD_LAT\":\"33.49997329711914\"," +
                        "\"CURRENT_AGENT_ADD_LON\":\"126.53034973144531\",\"CURRENT_AGENT_TIME\":\"08:00-19:00\",\"CURRENT_AGENT_DELI\":\"0\"," +
                        "\"CURRENT_AGENT_DEL_PRI\":\"20000\",\"CURRENT_AGENT_DEL_TIM\":\"09:00-18:00\"," +
                        "\"CURRENT_AGENT_DEL_POS\":\"제주시, 서귀포시\",\"CURRENT_PRICE\":\"200000\", \"CURRENT_INSURANCE\": \"20000\"}]}";
                break;
            case "3":
            case "8":
                it =  "{\"rentcar_detail\": [{\"CURRENT_CAR_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201902271111488598.jpg\"," +
                        "\"CURRENT_CAR_NUM\":\"18하5678\",\"CURRENT_CAR_FUEL\":\"가솔린(G),디젤(D)\",\"CURRENT_CAR_FPY\":\"8.7-13.8 KM/L\",\"CURRENT_CAR_COLOR\":\"화이트그레이\"," +
                        "\"CURRENT_CAR_MADE\":\"2018\",\"CURRENT_CAR_AGE\":\"2\",\"CURRENT_CAR_MAN\":\"6\",\"CURRENT_AGENT_ADDRESS\":\"제주 지점\",\"CURRENT_AGENT_ADD_LAT\":\"33.49997329711914\"," +
                        "\"CURRENT_AGENT_ADD_LON\":\"126.53034973144531\",\"CURRENT_AGENT_TIME\":\"09:00-19:00\",\"CURRENT_AGENT_DELI\":\"0\"," +
                        "\"CURRENT_AGENT_DEL_PRI\":\"20000\",\"CURRENT_AGENT_DEL_TIM\":\"10:00-19:00\"," +
                        "\"CURRENT_AGENT_DEL_POS\":\"제주시, 서귀포시\",\"CURRENT_PRICE\":\"120000\", \"CURRENT_INSURANCE\": \"30000\"}]}";
                break;
            case "4":
            case "9":
                it =  "{\"rentcar_detail\": [{\"CURRENT_CAR_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/202001091104489284.jpg\"," +
                        "\"CURRENT_CAR_NUM\":\"17하9012\",\"CURRENT_CAR_FUEL\":\"가솔린(G),디젤(D),LPG(L)\",\"CURRENT_CAR_FPY\":\"7.4-14.8 KM/L\",\"CURRENT_CAR_COLOR\":\"라이트블랙\"," +
                        "\"CURRENT_CAR_MADE\":\"2017\",\"CURRENT_CAR_AGE\":\"2\",\"CURRENT_CAR_MAN\":\"4\",\"CURRENT_AGENT_ADDRESS\":\"제주 지점\",\"CURRENT_AGENT_ADD_LAT\":\"33.49997329711914\"," +
                        "\"CURRENT_AGENT_ADD_LON\":\"126.53034973144531\",\"CURRENT_AGENT_TIME\":\"09:00-19:00\",\"CURRENT_AGENT_DELI\":\"0\"," +
                        "\"CURRENT_AGENT_DEL_PRI\":\"10000\",\"CURRENT_AGENT_DEL_TIM\":\"10:00-19:00\"," +
                        "\"CURRENT_AGENT_DEL_POS\":\"제주시, 서귀포시\",\"CURRENT_PRICE\":\"115000\", \"CURRENT_INSURANCE\": \"15000\"}]}";
                break;
            case "5":
            case "10":
                it =  "{\"rentcar_detail\": [{\"CURRENT_CAR_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201908131116037666.jpg\"," +
                        "\"CURRENT_CAR_NUM\":\"16하3456\",\"CURRENT_CAR_FUEL\":\"가솔린(G),디젤(D)\",\"CURRENT_CAR_FPY\":\"10.7-16.1 KM/L\",\"CURRENT_CAR_COLOR\":\"골드\"," +
                        "\"CURRENT_CAR_MADE\":\"2016\",\"CURRENT_CAR_AGE\":\"2\",\"CURRENT_CAR_MAN\":\"4\",\"CURRENT_AGENT_ADDRESS\":\"제주 지점\",\"CURRENT_AGENT_ADD_LAT\":\"33.49997329711914\"," +
                        "\"CURRENT_AGENT_ADD_LON\":\"126.53034973144531\",\"CURRENT_AGENT_TIME\":\"09:00-19:00\",\"CURRENT_AGENT_DELI\":\"0\"," +
                        "\"CURRENT_AGENT_DEL_PRI\":\"10000\",\"CURRENT_AGENT_DEL_TIM\":\"10:00-19:00\"," +
                        "\"CURRENT_AGENT_DEL_POS\":\"제주시, 서귀포시\",\"CURRENT_PRICE\":\"65000\", \"CURRENT_INSURANCE\": \"5000\"}]}";
                break;
            default:
                break;
        }

        Gson gson = new Gson();
        rentCarDetailResult = gson.fromJson(it, RentCarDetailResult.class);

        setDetailText();
    }
}




