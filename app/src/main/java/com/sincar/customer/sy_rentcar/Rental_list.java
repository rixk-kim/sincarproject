package com.sincar.customer.sy_rentcar;

import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.sincar.customer.R;
import com.sincar.customer.item.RentCarAgentResult;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.Util;

import java.util.HashMap;

import static com.sincar.customer.HWApplication.rentCarAgentResult;
import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.HWApplication.voRentCarAgentDataItem;
import static com.sincar.customer.HWApplication.voRentCarAgentItem;
import static com.sincar.customer.common.Constants.RENTCAR_LIST_REQUEST;

public class Rental_list extends AppCompatActivity {
    //8개의 샘플 렌탈카 리스트 생성
//    private ConstraintLayout mImagePhoto[] = new ConstraintLayout[8];
//    int con[] = {R.id.rent_list_con1, R.id.rent_list_con2, R.id.rent_list_con3, R.id.rent_list_con4
//            , R.id.rent_list_con5, R.id.rent_list_con6, R.id.rent_list_con7, R.id.rent_list_con8};

    CustomDialog dlg; //렌탈 리스트용 커스텀 다이얼로그
    int dlgCheck = 0; //다이얼로그에 체크된 아이템을 구분 짓기 위한 변수
    String start_date, start_time, return_date, return_time, start_year, return_year, reserve_address; //시간 데이터
    String simpleDate; // yyyyMMdd 타입 날짜
    ImageView ivImage;
    Bundle dlgBundle; //다이얼로그용 번들
    Context rental_list_context;
    RecyclerView recyclerView; //리사이클러뷰
    //위경도 확인후 계산시 필요
    private Geocoder gCoder;
    LatLng reserve_LatLng; //예약주소 위경도
    private final static int RENTAL_CAR_LIST_FILTER = 3333;
    private final int DISTTYPE = 0;     //거리순 정렬
    private final int PRICETYPE = 1;    //가격순 정렬
    private final int POPULARTYPE = 2;  //인기순 정렬


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final Button btFilter, btSort;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_list);
        rental_list_context = this;

//        ivImage = (ImageView) findViewById(R.id.rent_list_iv1);
//        Glide.with(this).load("https://www.sincar.co.kr/upload/program/goods/list/201912241503214320.jpg")
//                .into(ivImage);

        Intent intent = getIntent(); //Maps_rent_mainfrag에서 넘어온 데이터 수신
        start_date = intent.getStringExtra("start_date");   //예약 날짜
        start_time = intent.getStringExtra("start_time");   //예약 시간
        return_date = intent.getStringExtra("return_date"); //반납 날짜
        return_time = intent.getStringExtra("return_time"); //반납 시간
        reserve_address = intent.getStringExtra("reserve_address"); //현재 위치
        reserve_LatLng = new LatLng(intent.getDoubleExtra("reserve_lat", 0),
        intent.getDoubleExtra("reserve_lon", 0));
        start_year = intent.getStringExtra("start_year");
        return_year = intent.getStringExtra("return_year");


//        gCoder = new Geocoder(this, Locale.getDefault());
//        getAddress();
        //20-06-23 잠시 보류
//        myLatLng = ConvertGPS(curAddress);

        //예약 시간을 yyyy타입으로 바꿔줌
//        SimpleDateFormat ymdFormat = new SimpleDateFormat("MMdd");
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d일 (E)");
//
//        try {
//            simpleDate = ymdFormat.format(dateFormat.parse(start_date));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        simpleDate = "2020" + simpleDate;
//
//        Rental_list_adapterItem.clearItem(); // 저장되있는 리스트 아이템 클리어
//
        //리퀘스트 보낼 키값과 밸류 값들
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("MEMBER_NO", voLoginItem.MEMBER_NO);
        postParams.put("RESERVE_ADDRESS", reserve_address);
        postParams.put("RESERVE_ADD_PO_LAT", String.valueOf(reserve_LatLng.latitude));
        postParams.put("RESERVE_ADD_PO_LON", String.valueOf(reserve_LatLng.longitude));
        postParams.put("RESRERVE_YEAR", start_year);
        postParams.put("RESERVE_DATE", start_date);
        postParams.put("RESERVE_TIME", start_time);
        postParams.put("RETURN_YEAR", return_year);
        postParams.put("RETURN_DATE", return_date);
        postParams.put("RETURN_TIME", return_time);
        postParams.put("REQUEST_PAGE", "1");
        postParams.put("REQUEST_NUM", "20");
        postParams.put("REQUEST_SORT", "0");
        postParams.put("REQUEST_FIL_AGE", "0");
        postParams.put("REQUEST_FIL_PRICE", "0");
        postParams.put("REQUEST_FIL_TYPE", "0");
        postParams.put("REQUEST_FIL_BRAND", "0");

        Log.v("putItem", "reserve_lat : " + String.valueOf(reserve_LatLng.latitude) + " reserve_lon : "
        + String.valueOf(reserve_LatLng.longitude));

        //Util.showDialog(rental_list_context);

        //발리네트워크 연결
        VolleyNetwork.getInstance(this).serverDataRequest(RENTCAR_LIST_REQUEST, postParams, onRentalListInteractionListener);

        //테스트 메소드
        listNetworkTest();

        // TODO 상단바 아이콘 설정
        ImageButton ibBack = (ImageButton) findViewById(R.id.ibBack1);

        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btFilter = (Button) findViewById(R.id.btn_rentalCar_Fil);

        //리스트 필터 보기 액티비티로 화면 전환
        btFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Rental_list_filter.class);
                startActivityForResult(intent, RENTAL_CAR_LIST_FILTER);
            }
        });

        //리스트 sort방식을 결정하기 위한 버튼
        btSort = (Button) findViewById(R.id.btn_rentalCar_Sort);

//        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;

        //다이얼로그 호출
        btSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg = CustomDialog.getInstance();
                dlgBundle = new Bundle();
                dlgBundle.putInt("dlgCheck", dlgCheck); //체크된 아이템 확인용 변수 0:거리순 1:가격순 2:인기순
                dlg.setArguments(dlgBundle);
                dlg.show(getSupportFragmentManager(), "dialog_event");
                dlg.setDialogResult(new CustomDialog.OnMyDialogResult() {
                    @Override
                    public void finish(int result) { //커스텀 다이얼로그가 종료시 호출되는 메소드
                        //추후 통신 인터페이스 적용후 대대적 수정 필요
                        switch (result) {
                            case 0:
                                btSort.setText("거리순");
                                //20-06-23 잠시 보류
                               //listRefresh(DISTTYPE);
                                Toast.makeText(getApplicationContext(), "거리순을 선택하셨습니다", Toast.LENGTH_SHORT).show();
                                dlgCheck = 0;
                                break;
                            case 1:
                                btSort.setText("가격순");
                                //20-06-23 잠시 보류
//                                listRefresh(PRICETYPE);
                                Toast.makeText(getApplicationContext(), "가격순을 선택하셨습니다", Toast.LENGTH_SHORT).show();
                                dlgCheck = 1;
                                break;
                            case 2:
                                btSort.setText("인기순");
                                //20-06-23 잠시 보류
//                                listRefresh(POPULARTYPE);
                                Toast.makeText(getApplicationContext(), "인기순을 선택하셨습니다", Toast.LENGTH_SHORT).show();
                                dlgCheck = 2;
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        });

        //8개의 임시 렌탈카 리스트 변수 적용
        //추후 통신 인터페이스 적용후 대대적 수정 필요
//        for (int i = 0; i < 8; i++) {
//            mImagePhoto[i] = (ConstraintLayout) findViewById(con[i]);
//
//            mImagePhoto[i].setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //상세페이지 이동
//                    //예약시간,반납시간, 현재주소의 데이터를 이전 액티비티에서 받아서 넘김
//                    Intent intent = new Intent(getApplicationContext(), Rental_list_detail.class);
//                    intent.putExtra("start_date", start_date);
//                    intent.putExtra("start_time", start_time);
//                    intent.putExtra("return_date", return_date);
//                    intent.putExtra("return_time", return_time);
//                    intent.putExtra("current_Address", curAddress);
//                    startActivity(intent);
//                }
//            });
//        }


    }

    //필터 액티비티 전환후 돌아올때 실행되는 메소드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RENTAL_CAR_LIST_FILTER) {
            if (resultCode == RESULT_OK) {
                String filter_data = "나이 제한 : ";
                filter_data += data.getStringExtra("age_data");
                filter_data += "\n가격 설정 :";
                filter_data += " " + data.getStringExtra("price_data");
                filter_data += "\n외형 설정 :";
                filter_data += " " + data.getStringExtra("type_data");
                filter_data += "\n브랜드 설정 :";
                filter_data += " " + data.getStringExtra("brand_data");
                Toast.makeText(getApplicationContext(), filter_data, Toast.LENGTH_LONG).show();
            }
        }
    }

    //20-06-23 잠시 보류
//    //정렬 방식 설정 거리순, 가격순, 인기순순
//    //거리순
//    class distCompare implements Comparator<Rental_list_adapterItem.Rental_List_Item> {
//        @Override
//        public int compare(Rental_list_adapterItem.Rental_List_Item o1, Rental_list_adapterItem.Rental_List_Item o2) {
//            return o1.distance > o2.distance ? 1 : o1.distance < o2.distance ? -1 : 0; //4항 연산자
//        }
//    }
//
//    //가격순
//    class priceCompare implements Comparator<Rental_list_adapterItem.Rental_List_Item> {
//        @Override
//        public int compare(Rental_list_adapterItem.Rental_List_Item o1, Rental_list_adapterItem.Rental_List_Item o2) {
//            return Integer.parseInt(o1.rental_Price) > Integer.parseInt(o2.rental_Price)
//                    ? 1 : Integer.parseInt(o1.rental_Price) < Integer.parseInt(o2.rental_Price) ? -1 : 0;
//        }
//    }
//
//    //인기순
//    class populCompare {
//
//    }
//
    //발리 네트워크 연결후 Response 성공,실패 메소드
    VolleyNetwork.OnResponseListener onRentalListInteractionListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String it) {
            Gson gson = new Gson();
            rentCarAgentResult = gson.fromJson(it, RentCarAgentResult.class);

            //Toast.makeText(getApplicationContext(), rentCarAgentResult.agent_list.get(0).toString(), Toast.LENGTH_LONG).show();
            voRentCarAgentItem.TOTAL = rentCarAgentResult.rentcar_list.get(0).TOTAL;
            voRentCarAgentItem.CURRENT_PAGE = rentCarAgentResult.rentcar_list.get(0).CURRENT_PAGE;
            voRentCarAgentItem.CURRENT_NUM = rentCarAgentResult.rentcar_list.get(0).CURRENT_NUM;

            voRentCarAgentDataItem = rentCarAgentResult.data;

            putItemToList();
            callRentalListRecyclerViewAdapter();

            //프로그래스바 종료
            Util.dismiss();
        }

        @Override
        public void onResponseFailListener(VolleyError it) {
            //프로그래스바 종료
            Util.dismiss();
        }
    };

    //아답터 연결후 레이아웃 표시 메소드
    private void callRentalListRecyclerViewAdapter() {

        if (Rental_list_adapterItem.RENTAL_LIST_ITEM1.size() > 0) {
            recyclerView = findViewById(R.id.rental_list_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            Rental_list_adapter adapter = new Rental_list_adapter(Rental_list_adapterItem.RENTAL_LIST_ITEM1, Rental_list_adapterItem.RENTAL_LIST_ITEM2, rental_list_context);
            recyclerView.setAdapter(adapter);


            //Recyclerview 에서 아이템을 클릭했을때 이벤트(상세 페이지 이동)
            adapter.setOnItemClickListener(new Rental_list_adapter.OnRentalListInteractionListener() {
                @Override
                public void onRentalListInteractionListener(Rental_list_adapterItem.Rental_List_Item rental_Item) {
                    //상세페이지 이동
                    //예약시간,반납시간, 현재위치의 데이터를 이전 액티비티에서 받아서 넘김
                    Intent intent = new Intent(getApplicationContext(), Rental_list_detail.class);
                    intent.putExtra("start_date", start_date);
                    intent.putExtra("start_time", start_time);
                    intent.putExtra("return_date", return_date);
                    intent.putExtra("return_time", return_time);
                    intent.putExtra("reserve_address", reserve_address);
                    intent.putExtra("REQUEST_AGENT", rental_Item.rentcar_agent);
                    intent.putExtra("REQUEST_RENTCAR", rental_Item.rentcar_name);
                    intent.putExtra("REQUEST_SEQ", rental_Item.rentcar_seq);

                    startActivity(intent);
                }
            });
        } else {
            View view = findViewById(R.id.scr_rent_List);
            view.setVisibility(View.GONE);

            LinearLayout view1 = findViewById(R.id.rental_list_empty);
            view1.setVisibility(View.VISIBLE);
            Button btSort, btFil;
            btSort = findViewById(R.id.btn_rentalCar_Sort);
            btSort.setClickable(false);
            btFil = findViewById(R.id.btn_rentalCar_Fil);
            btFil.setClickable(false);
        }
    }

    //클래스에 아이템을 순차적으로 넣음
    //리싸이클러뷰 아이템 하나에 두개의 속성 아이템을 넣을수 있으므로
    //짝수번째는 왼쪽 홀수번째는 오른쪽에 추가하는 방식으로 함
    private void putItemToList() {

        int list1id = 0, list2id = 0;
        for (int i = 0; i < voRentCarAgentDataItem.size(); i++) {

//           rental_shop_latlng = ConvertGPS(voRentCarAgentDataItem.get(i).WASH_AREA);
//            double dist = distance(myLatLng.latitude, myLatLng.longitude, rental_shop_latlng.latitude, rental_shop_latlng.longitude);
            if (i % 2 == 0) {
                Rental_list_adapterItem.addItem1(new Rental_list_adapterItem.Rental_List_Item(
                        list1id,
                        voRentCarAgentDataItem.get(i).RENTCAR_SEQ,
                        voRentCarAgentDataItem.get(i).RENTCAR_NAME,
                        voRentCarAgentDataItem.get(i).RENTCAR_IMG_URL,
                        voRentCarAgentDataItem.get(i).RENTCAR_AGENT,
                        voRentCarAgentDataItem.get(i).RENTCAR_AGENT_ADD,
                        voRentCarAgentDataItem.get(i).RENTCAR_DISCOUNT,
                        voRentCarAgentDataItem.get(i).RENTCAR_PRICE,
                        voRentCarAgentDataItem.get(i).RENTCAR_FIL_AGE,
                        voRentCarAgentDataItem.get(i).RENTCAR_FIL_TYPE,
                        voRentCarAgentDataItem.get(i).RENTCAR_FIL_BRAND,
                        voRentCarAgentDataItem.get(i).RENTCAR_SORT_DIST,
                        voRentCarAgentDataItem.get(i).RENTCAR_SORT_POPU
                ));
                list1id++;
            } else {
                Rental_list_adapterItem.addItem2(new Rental_list_adapterItem.Rental_List_Item(
                        list2id,
                        voRentCarAgentDataItem.get(i).RENTCAR_SEQ,
                        voRentCarAgentDataItem.get(i).RENTCAR_NAME,
                        voRentCarAgentDataItem.get(i).RENTCAR_IMG_URL,
                        voRentCarAgentDataItem.get(i).RENTCAR_AGENT,
                        voRentCarAgentDataItem.get(i).RENTCAR_AGENT_ADD,
                        voRentCarAgentDataItem.get(i).RENTCAR_DISCOUNT,
                        voRentCarAgentDataItem.get(i).RENTCAR_PRICE,
                        voRentCarAgentDataItem.get(i).RENTCAR_FIL_AGE,
                        voRentCarAgentDataItem.get(i).RENTCAR_FIL_TYPE,
                        voRentCarAgentDataItem.get(i).RENTCAR_FIL_BRAND,
                        voRentCarAgentDataItem.get(i).RENTCAR_SORT_DIST,
                        voRentCarAgentDataItem.get(i).RENTCAR_SORT_POPU
                ));
                list2id++;
            }
        }
    }

//    //정렬 다이얼로그에서 정렬시 실행되는 메소드
//    public void listRefresh(int sortType) {
//        recyclerView.removeAllViewsInLayout(); //recyclerview 삭제
//        Rental_list_adapterItem.sumList();
//        switch (sortType) {
//            case DISTTYPE:
//                Collections.sort(Rental_list_adapterItem.RENTAL_LIST_ITEM_BOTH, new distCompare()); //가격순으로 정렬
//                break;
//            case PRICETYPE:
//                Collections.sort(Rental_list_adapterItem.RENTAL_LIST_ITEM_BOTH, new priceCompare()); //가격순으로 정렬
//                break;
//            case POPULARTYPE:
//                break;
//            default:
//                break;
//        }
//
//        Rental_list_adapterItem.divList();  //다시 합쳐진 List그룹을 정렬한 상태에서 나눔
//        callRentalListRecyclerViewAdapter();
//    }
//
//
//    /*
//     * TODO 거리계산 메소드(계산이 시간이 걸림, 추후 서버에 역활을 넘길 예정)
//     */
//
//    private static double distance(double lat1, double lon1, double lat2, double lon2) {
//        double theta = lon1 - lon2;
//        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
//                * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
//
//        dist = Math.acos(dist);
//        dist = rad2deg(dist);
//        dist = dist * 60 * 1.1515;
//
//        return (dist);
//    }
//
//    private static double deg2rad(double deg) {
//        return (deg * Math.PI / 180.0);
//    }
//
//    private static double rad2deg(double rad) {
//        return (rad * 180 / Math.PI);
//    }

    //네트워크 연결 전 테스트용 메소드
    private void listNetworkTest() {
        String it;
        //테스트용 더미데이터
        it = "{\"rentcar_list\": [{\"TOTAL\":\"10\",\"CURRENT_PAGE\":\"2\",\"CURRENT_NUM\":\"5\"}]," +
                "\"data\": [{\"RENTCAR_SEQ\":\"1\",\"RENTCAR_NAME\":\"아반떼\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201902271113552281.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"65000\",\"RENTCAR_FIL_AGE\":\"1\"," +
                "\"RENTCAR_FIL_TYPE\":\"중형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"2\",\"RENTCAR_NAME\":\"제네시스 G80\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201805281053589087.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"200000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"제네시스\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"3\",\"RENTCAR_NAME\":\"카니발\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201902271111488598.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"120000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"기아\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"4\",\"RENTCAR_NAME\":\"그랜저\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/202001091104489284.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"115000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"5\",\"RENTCAR_NAME\":\"쏘나타\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201908131116037666.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"65000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"중형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"6\",\"RENTCAR_NAME\":\"아반떼\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201902271113552281.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"65000\",\"RENTCAR_FIL_AGE\":\"1\"," +
                "\"RENTCAR_FIL_TYPE\":\"중형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"7\",\"RENTCAR_NAME\":\"제네시스 G80\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201805281053589087.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"200000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"제네시스\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"8\",\"RENTCAR_NAME\":\"카니발\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201902271111488598.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"120000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"기아\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"9\",\"RENTCAR_NAME\":\"그랜저\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/202001091104489284.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"115000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"10\",\"RENTCAR_NAME\":\"쏘나타\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201908131116037666.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"65000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"중형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}]}";

        Gson gson = new Gson();
        rentCarAgentResult = gson.fromJson(it, RentCarAgentResult.class);

        //Toast.makeText(getApplicationContext(), rentCarAgentResult.agent_list.get(0).toString(), Toast.LENGTH_LONG).show();
        voRentCarAgentItem.TOTAL = rentCarAgentResult.rentcar_list.get(0).TOTAL;
        voRentCarAgentItem.CURRENT_PAGE = rentCarAgentResult.rentcar_list.get(0).CURRENT_PAGE;
        voRentCarAgentItem.CURRENT_NUM = rentCarAgentResult.rentcar_list.get(0).CURRENT_NUM;

        voRentCarAgentDataItem = rentCarAgentResult.data;

        putItemToList();
        callRentalListRecyclerViewAdapter();

        //프로그래스바 종료
        //Util.dismiss();
    }


}


