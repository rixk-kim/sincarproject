package com.sincar.customer.sy_rentcar;

import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.sincar.customer.R;
import com.sincar.customer.item.RentCarAgentResult;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;

import static com.sincar.customer.HWApplication.rentCarAgentResult;
import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.HWApplication.voRentCarAgentDataItem;
import static com.sincar.customer.HWApplication.voRentCarAgentItem;
import static com.sincar.customer.common.Constants.RENTCAR_LIST_REQUEST;

import com.sincar.customer.sy_rentcar.Rental_list_filter.age_filter;
import com.sincar.customer.sy_rentcar.Rental_list_filter.price_filter;
import com.sincar.customer.sy_rentcar.Rental_list_filter.type_filter;
import com.sincar.customer.sy_rentcar.Rental_list_filter.kuk_brand_filter;
import com.sincar.customer.sy_rentcar.Rental_list_filter.su_brand_filter;

public class Rental_list extends AppCompatActivity {
    //8개의 샘플 렌탈카 리스트 생성
//    private ConstraintLayout mImagePhoto[] = new ConstraintLayout[8];
//    int con[] = {R.id.rent_list_con1, R.id.rent_list_con2, R.id.rent_list_con3, R.id.rent_list_con4
//            , R.id.rent_list_con5, R.id.rent_list_con6, R.id.rent_list_con7, R.id.rent_list_con8};

    CustomDialog dlg; //렌탈 리스트용 커스텀 다이얼로그
    String start_date, start_time, return_date, return_time, start_year, return_year, reserve_address; //시간 데이터
    String start_date_volley, return_date_volley;
    String simpleDate; // yyyyMMdd 타입 날짜
    ImageView ivImage;
    Bundle dlgBundle; //다이얼로그용 번들
    Context rental_list_context;
    RecyclerView recyclerview; //리사이클러뷰
    NestedScrollView nestedScrollView;
    //위경도 확인후 계산시 필요
    private Geocoder gCoder;
    LatLng reserve_LatLng; //예약주소 위경도
    private final static int RENTAL_CAR_LIST_FILTER = 3333;
//    private final int DISTTYPE = 0;     //거리순 정렬
//    private final int PRICETYPE = 1;    //가격순 정렬
//    private final int POPULARTYPE = 2;  //인기순 정렬

    int rentcarPage_chk = 0, rentcarItem_chk = 0, list1id = 0, list2id = 0;

    final HashMap<String, String> postParams = new HashMap<String, String>();

    //필터 파라미터 나이,가격,외형,브랜드 전체 선택 설정
    age_filter age = age_filter.all;
    price_filter price = price_filter.all;
    ArrayList<type_filter> tfArrayList = new ArrayList<type_filter>(EnumSet.allOf(type_filter.class));
    type_filter[] type = tfArrayList.toArray(new type_filter[tfArrayList.size()]);
    ArrayList<kuk_brand_filter> kbfArrayList = new ArrayList<kuk_brand_filter>(EnumSet.allOf(kuk_brand_filter.class));
    ArrayList<su_brand_filter> sbfArrayList = new ArrayList<su_brand_filter>(EnumSet.allOf(su_brand_filter.class));
    kuk_brand_filter[] kukBrand = kbfArrayList.toArray(new kuk_brand_filter[kbfArrayList.size()]);
    su_brand_filter[] suBrand = sbfArrayList.toArray(new su_brand_filter[sbfArrayList.size()]);

    //정렬 방식 enum
    public enum sortType {
        distType("거리순"), priceType("가격순"), popularType("인기순");
        private final String sort_str;

        sortType(String sort_str) {
            this.sort_str = sort_str;
        }

        public String getValue() {
            return sort_str;
        }
    }

    sortType sort = sortType.distType;

    @RequiresApi(api = Build.VERSION_CODES.M)
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

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d일 (E)");
        SimpleDateFormat volleyformat = new SimpleDateFormat("MM-dd");
        try {
            start_date_volley = volleyformat.format(dateFormat.parse(start_date));
            return_date_volley = volleyformat.format(dateFormat.parse(return_date));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //리퀘스트 파라미터 세팅
        requestSetting();

//        Util.showDialog(rental_list_context);
//
//        //발리네트워크 연결
//        VolleyNetwork.getInstance(this).serverDataRequest(RENTCAR_LIST_REQUEST, postParams, onRentalListInteractionListener);

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
                dlgBundle.putInt("dlgCheck", sort.ordinal()); //체크된 아이템 확인용 변수 0:거리순 1:가격순 2:인기순
                dlg.setArguments(dlgBundle);
                dlg.show(getSupportFragmentManager(), "dialog_event");
                dlg.setDialogResult(new CustomDialog.OnMyDialogResult() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void finish(sortType st) { //커스텀 다이얼로그가 종료시 호출되는 메소드
                        btSort.setText(st.getValue());
                        sort = st;

                        requestSetting();

                        //Util.showDialog(rental_list_context);

                        //발리네트워크 연결
                        //VolleyNetwork.getInstance(this).serverDataRequest(RENTCAR_LIST_REQUEST, postParams, onRentalListInteractionListener);

                        Toast.makeText(getApplicationContext(), st.getValue() + "을 선택하셨습니다", Toast.LENGTH_SHORT).show();

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

    private void requestSetting() {

        //리퀘스트 보낼 키값과 밸류 값들
        postParams.put("MEMBER_NO", voLoginItem.MEMBER_NO);
        postParams.put("RESERVE_ADDRESS", reserve_address);
        postParams.put("RESERVE_ADD_PO_LAT", String.valueOf(reserve_LatLng.latitude));
        postParams.put("RESERVE_ADD_PO_LON", String.valueOf(reserve_LatLng.longitude));
        postParams.put("RESRERVE_YEAR", start_year);
        postParams.put("RESERVE_DATE", start_date_volley);
        postParams.put("RESERVE_TIME", start_time);
        postParams.put("RETURN_YEAR", return_year);
        postParams.put("RETURN_DATE", return_date_volley);
        postParams.put("RETURN_TIME", return_time);
        postParams.put("REQUEST_PAGE", "5");
        postParams.put("REQUEST_NUM", "50");
        postParams.put("REQUEST_SORT", String.valueOf(sort.ordinal()));

        postParams.put("REQUEST_FIL_AGE", String.valueOf(age.ordinal()));
        postParams.put("REQUEST_FIL_PRICE", String.valueOf(price.ordinal()));
        String typeList = "";
        for (type_filter d : type) {
            typeList += String.valueOf(d.ordinal());
            typeList += '/';
        }
        typeList = typeList.substring(0, typeList.length() - 1);
        postParams.put("REQUEST_FIL_TYPE", typeList);
        String brandList = "";
        for (kuk_brand_filter d : kukBrand) {
            brandList += String.valueOf(d.ordinal());
            brandList += '/';
        }
        for (su_brand_filter d : suBrand) {
            brandList += String.valueOf(d.ordinal() + 6); //국산 브랜드 6개의 순서 더함
            brandList += '/';
        }
        brandList = brandList.substring(0, brandList.length() - 1);
        postParams.put("REQUEST_FIL_BRAND", brandList);

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

                age = (age_filter) data.getSerializableExtra("age");
                price = (price_filter) data.getSerializableExtra("price");
                type = (type_filter[]) data.getSerializableExtra("type");
                kukBrand = (kuk_brand_filter[]) data.getSerializableExtra("kukBrand");
                suBrand = (su_brand_filter[]) data.getSerializableExtra("suBrand");

                requestSetting();

                //Util.showDialog(rental_list_context);

                //발리네트워크 연결
                //VolleyNetwork.getInstance(this).serverDataRequest(RENTCAR_LIST_REQUEST, postParams, onRentalListInteractionListener);

            }
        }
    }

    //발리 네트워크 연결후 Response 성공,실패 메소드
    VolleyNetwork.OnResponseListener onRentalListInteractionListener = new VolleyNetwork.OnResponseListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onResponseSuccessListener(String it) {
            Gson gson = new Gson();

            try {
                rentCarAgentResult = gson.fromJson(it, RentCarAgentResult.class);

                //Toast.makeText(getApplicationContext(), rentCarAgentResult.agent_list.get(0).toString(), Toast.LENGTH_LONG).show();
                voRentCarAgentItem.TOTAL = rentCarAgentResult.rentcar_list.get(0).TOTAL;
                voRentCarAgentItem.CURRENT_PAGE = rentCarAgentResult.rentcar_list.get(0).CURRENT_PAGE;
                voRentCarAgentItem.CURRENT_NUM = rentCarAgentResult.rentcar_list.get(0).CURRENT_NUM;

                voRentCarAgentDataItem = rentCarAgentResult.data;

                putItemToList();
                callRentalListRecyclerViewAdapter();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "서버가 정상적이지 않습니다.", Toast.LENGTH_LONG).show();
            }
            //프로그래스바 종료
            Util.dismiss();
            Log.i("Volley", it);
        }

        @Override
        public void onResponseFailListener(VolleyError it) {
            //프로그래스바 종료
            Util.dismiss();
            Log.i("Volley", "Response Fail");
        }
    };

    //아답터 연결후 레이아웃 표시 메소드
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void callRentalListRecyclerViewAdapter() {

        if (Rental_list_adapterItem.RENTAL_LIST_ITEM1.size() > 0) {
            recyclerview = findViewById(R.id.rental_list_recyclerview);
            nestedScrollView = findViewById(R.id.scr_rent_List);
            final LinearLayoutManager mLayoutManager;
            mLayoutManager = new LinearLayoutManager(this);
            recyclerview.setLayoutManager(mLayoutManager);

            final Rental_list_adapter adapter = new Rental_list_adapter(Rental_list_adapterItem.RENTAL_LIST_ITEM1, Rental_list_adapterItem.RENTAL_LIST_ITEM2, rental_list_context);
            recyclerview.setAdapter(adapter);

            nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if(!nestedScrollView.canScrollVertically(1) && rentcarItem_chk < Integer.parseInt(voRentCarAgentItem.TOTAL)) {
                        nestedScrollView.smoothScrollBy(0,0); // 스크롤 멈춤
                        Util.showDialogRentList(rental_list_context);
                        putItemToList();
                        adapter.notifyDataSetChanged();
                    }
                }
            });

            //Recyclerview 에서 아이템을 클릭했을때 이벤트(상세 페이지 이동)
            adapter.setOnItemClickListener(new Rental_list_adapter.OnRentalListInteractionListener() {
                @Override
                public void onRentalListInteractionListener(Rental_list_adapterItem.Rental_List_Item rental_Item) {
                    //상세페이지 이동
                    //예약시간,반납시간, 현재위치의 데이터를 이전 액티비티에서 받아서 넘김
                    Intent intent = new Intent(getApplicationContext(), Rental_list_detail.class);
                    intent.putExtra("start_date", start_date);
                    intent.putExtra("start_time", start_time);
                    intent.putExtra("start_date_volley", start_date_volley);
                    intent.putExtra("return_date", return_date);
                    intent.putExtra("return_time", return_time);
                    intent.putExtra("return_date_volley", return_date_volley);
                    intent.putExtra("reserve_address", reserve_address);
                    intent.putExtra("start_year", start_year);
                    intent.putExtra("return_year", return_year);
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

        if(rentcarPage_chk <Integer.parseInt(voRentCarAgentItem.CURRENT_PAGE)) {

            for (int i = 0; i < Integer.parseInt(voRentCarAgentItem.CURRENT_NUM); i++) {

//           rental_shop_latlng = ConvertGPS(voRentCarAgentDataItem.get(i).WASH_AREA);
//            double dist = distance(myLatLng.latitude, myLatLng.longitude, rental_shop_latlng.latitude, rental_shop_latlng.longitude);
                if (rentcarItem_chk < Integer.parseInt(voRentCarAgentItem.TOTAL)) {
                    if (i % 2 == 0) {
                        Rental_list_adapterItem.addItem1(new Rental_list_adapterItem.Rental_List_Item(
                                list1id,
                                voRentCarAgentDataItem.get(rentcarItem_chk).RENTCAR_SEQ,
                                voRentCarAgentDataItem.get(rentcarItem_chk).RENTCAR_NAME,
                                voRentCarAgentDataItem.get(rentcarItem_chk).RENTCAR_IMG_URL,
                                voRentCarAgentDataItem.get(rentcarItem_chk).RENTCAR_AGENT,
                                voRentCarAgentDataItem.get(rentcarItem_chk).RENTCAR_AGENT_ADD,
                                voRentCarAgentDataItem.get(rentcarItem_chk).RENTCAR_DISCOUNT,
                                voRentCarAgentDataItem.get(rentcarItem_chk).RENTCAR_PRICE,
                                voRentCarAgentDataItem.get(rentcarItem_chk).RENTCAR_FIL_AGE,
                                voRentCarAgentDataItem.get(rentcarItem_chk).RENTCAR_FIL_TYPE,
                                voRentCarAgentDataItem.get(rentcarItem_chk).RENTCAR_FIL_BRAND,
                                voRentCarAgentDataItem.get(rentcarItem_chk).RENTCAR_SORT_DIST,
                                voRentCarAgentDataItem.get(rentcarItem_chk).RENTCAR_SORT_POPU
                        ));
                        list1id++;
                    } else {
                        Rental_list_adapterItem.addItem2(new Rental_list_adapterItem.Rental_List_Item(
                                list2id,
                                voRentCarAgentDataItem.get(rentcarItem_chk).RENTCAR_SEQ,
                                voRentCarAgentDataItem.get(rentcarItem_chk).RENTCAR_NAME,
                                voRentCarAgentDataItem.get(rentcarItem_chk).RENTCAR_IMG_URL,
                                voRentCarAgentDataItem.get(rentcarItem_chk).RENTCAR_AGENT,
                                voRentCarAgentDataItem.get(rentcarItem_chk).RENTCAR_AGENT_ADD,
                                voRentCarAgentDataItem.get(rentcarItem_chk).RENTCAR_DISCOUNT,
                                voRentCarAgentDataItem.get(rentcarItem_chk).RENTCAR_PRICE,
                                voRentCarAgentDataItem.get(rentcarItem_chk).RENTCAR_FIL_AGE,
                                voRentCarAgentDataItem.get(rentcarItem_chk).RENTCAR_FIL_TYPE,
                                voRentCarAgentDataItem.get(rentcarItem_chk).RENTCAR_FIL_BRAND,
                                voRentCarAgentDataItem.get(rentcarItem_chk).RENTCAR_SORT_DIST,
                                voRentCarAgentDataItem.get(rentcarItem_chk).RENTCAR_SORT_POPU
                        ));
                        list2id++;
                    }
                    rentcarItem_chk++;
                }
            }
        }
        rentcarPage_chk++;

//        if(Util.mProgress != null) {
//            Util.dismiss();
//        }
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

    //네트워크 연결 전 테스트용 메소드
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void listNetworkTest() {
        String it;
        //테스트용 더미데이터
        it = "{\"rentcar_list\": [{\"TOTAL\":\"50\",\"CURRENT_PAGE\":\"3\",\"CURRENT_NUM\":\"20\"}]," +
                "\"data\": [{" +
                "\"RENTCAR_SEQ\":\"1\",\"RENTCAR_NAME\":\"아반떼\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201902271113552281.jpg\"," +
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
                "\"RENTCAR_FIL_TYPE\":\"중형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +

                "{\"RENTCAR_SEQ\":\"11\",\"RENTCAR_NAME\":\"아반떼\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201902271113552281.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"65000\",\"RENTCAR_FIL_AGE\":\"1\"," +
                "\"RENTCAR_FIL_TYPE\":\"중형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"12\",\"RENTCAR_NAME\":\"제네시스 G80\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201805281053589087.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"200000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"제네시스\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"13\",\"RENTCAR_NAME\":\"카니발\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201902271111488598.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"120000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"기아\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"14\",\"RENTCAR_NAME\":\"그랜저\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/202001091104489284.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"115000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"15\",\"RENTCAR_NAME\":\"쏘나타\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201908131116037666.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"65000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"중형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"16\",\"RENTCAR_NAME\":\"아반떼\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201902271113552281.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"65000\",\"RENTCAR_FIL_AGE\":\"1\"," +
                "\"RENTCAR_FIL_TYPE\":\"중형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"17\",\"RENTCAR_NAME\":\"제네시스 G80\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201805281053589087.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"200000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"제네시스\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"18\",\"RENTCAR_NAME\":\"카니발\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201902271111488598.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"120000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"기아\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"19\",\"RENTCAR_NAME\":\"그랜저\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/202001091104489284.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"115000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"20\",\"RENTCAR_NAME\":\"쏘나타\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201908131116037666.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"65000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"중형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +

                "{\"RENTCAR_SEQ\":\"21\",\"RENTCAR_NAME\":\"아반떼\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201902271113552281.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"65000\",\"RENTCAR_FIL_AGE\":\"1\"," +
                "\"RENTCAR_FIL_TYPE\":\"중형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"22\",\"RENTCAR_NAME\":\"제네시스 G80\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201805281053589087.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"200000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"제네시스\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"23\",\"RENTCAR_NAME\":\"카니발\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201902271111488598.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"120000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"기아\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"24\",\"RENTCAR_NAME\":\"그랜저\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/202001091104489284.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"115000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"25\",\"RENTCAR_NAME\":\"쏘나타\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201908131116037666.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"65000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"중형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"26\",\"RENTCAR_NAME\":\"아반떼\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201902271113552281.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"65000\",\"RENTCAR_FIL_AGE\":\"1\"," +
                "\"RENTCAR_FIL_TYPE\":\"중형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"27\",\"RENTCAR_NAME\":\"제네시스 G80\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201805281053589087.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"200000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"제네시스\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"28\",\"RENTCAR_NAME\":\"카니발\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201902271111488598.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"120000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"기아\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"29\",\"RENTCAR_NAME\":\"그랜저\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/202001091104489284.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"115000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"30\",\"RENTCAR_NAME\":\"쏘나타\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201908131116037666.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"65000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"중형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +

                "{\"RENTCAR_SEQ\":\"31\",\"RENTCAR_NAME\":\"아반떼\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201902271113552281.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"65000\",\"RENTCAR_FIL_AGE\":\"1\"," +
                "\"RENTCAR_FIL_TYPE\":\"중형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"32\",\"RENTCAR_NAME\":\"제네시스 G80\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201805281053589087.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"200000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"제네시스\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"33\",\"RENTCAR_NAME\":\"카니발\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201902271111488598.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"120000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"기아\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"34\",\"RENTCAR_NAME\":\"그랜저\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/202001091104489284.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"115000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"35\",\"RENTCAR_NAME\":\"쏘나타\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201908131116037666.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"65000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"중형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"36\",\"RENTCAR_NAME\":\"아반떼\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201902271113552281.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"65000\",\"RENTCAR_FIL_AGE\":\"1\"," +
                "\"RENTCAR_FIL_TYPE\":\"중형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"37\",\"RENTCAR_NAME\":\"제네시스 G80\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201805281053589087.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"200000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"제네시스\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"38\",\"RENTCAR_NAME\":\"카니발\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201902271111488598.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"120000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"기아\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"39\",\"RENTCAR_NAME\":\"그랜저\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/202001091104489284.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"115000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"40\",\"RENTCAR_NAME\":\"쏘나타\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201908131116037666.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"65000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"중형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +

                "{\"RENTCAR_SEQ\":\"41\",\"RENTCAR_NAME\":\"아반떼\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201902271113552281.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"65000\",\"RENTCAR_FIL_AGE\":\"1\"," +
                "\"RENTCAR_FIL_TYPE\":\"중형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"42\",\"RENTCAR_NAME\":\"제네시스 G80\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201805281053589087.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"200000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"제네시스\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"43\",\"RENTCAR_NAME\":\"카니발\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201902271111488598.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"120000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"기아\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"44\",\"RENTCAR_NAME\":\"그랜저\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/202001091104489284.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"115000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"45\",\"RENTCAR_NAME\":\"쏘나타\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201908131116037666.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"65000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"중형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"46\",\"RENTCAR_NAME\":\"아반떼\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201902271113552281.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"65000\",\"RENTCAR_FIL_AGE\":\"1\"," +
                "\"RENTCAR_FIL_TYPE\":\"중형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"47\",\"RENTCAR_NAME\":\"제네시스 G80\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201805281053589087.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"200000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"제네시스\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"48\",\"RENTCAR_NAME\":\"카니발\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201902271111488598.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"120000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"기아\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"49\",\"RENTCAR_NAME\":\"그랜저\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/202001091104489284.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"115000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"대형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"}," +
                "{\"RENTCAR_SEQ\":\"50\",\"RENTCAR_NAME\":\"쏘나타\",\"RENTCAR_IMG_URL\":\"https://www.sincar.co.kr/upload/program/goods/list/201908131116037666.jpg\"," +
                "\"RENTCAR_AGENT\":\"제주 지점\",\"RENTCAR_AGENT_ADD\":\"제주특별자치도\",\"RENTCAR_DISCOUNT\":\"75% 할인\",\"RENTCAR_PRICE\":\"65000\",\"RENTCAR_FIL_AGE\":\"2\"," +
                "\"RENTCAR_FIL_TYPE\":\"중형\",\"RENTCAR_FIL_BRAND\":\"현대\",\"RENTCAR_SORT_DIST\":\"20.12345678\",\"RENTCAR_SORT_POPU\":\"113\"" +
                "}]}";

        Gson gson = new Gson();
        rentCarAgentResult = gson.fromJson(it, RentCarAgentResult.class);

        //Toast.makeText(getApplicationContext(), rentCarAgentResult.agent_list.get(0).toString(), Toast.LENGTH_LONG).show();
        voRentCarAgentItem.TOTAL = rentCarAgentResult.rentcar_list.get(0).TOTAL;
        voRentCarAgentItem.CURRENT_PAGE = rentCarAgentResult.rentcar_list.get(0).CURRENT_PAGE;
        voRentCarAgentItem.CURRENT_NUM = rentCarAgentResult.rentcar_list.get(0).CURRENT_NUM;

        voRentCarAgentDataItem = rentCarAgentResult.data;

        Rental_list_adapterItem.clearItem(); //아이템 초기화(비워줌)
        putItemToList();
        callRentalListRecyclerViewAdapter();

        //프로그래스바 종료
        //Util.dismiss();
    }
}


