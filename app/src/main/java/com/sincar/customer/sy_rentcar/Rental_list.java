package com.sincar.customer.sy_rentcar;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.sincar.customer.R;
import com.sincar.customer.item.AgentResult;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.Util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.sincar.customer.HWApplication.agentResult;
import static com.sincar.customer.HWApplication.voAgentDataItem;
import static com.sincar.customer.HWApplication.voAgentItem;
import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.common.Constants.AGENT_LIST_REQUEST;

public class Rental_list extends AppCompatActivity {
    //8개의 샘플 렌탈카 리스트 생성
    private ConstraintLayout mImagePhoto[] = new ConstraintLayout[8];
//    int con[] = {R.id.rent_list_con1, R.id.rent_list_con2, R.id.rent_list_con3, R.id.rent_list_con4
//            , R.id.rent_list_con5, R.id.rent_list_con6, R.id.rent_list_con7, R.id.rent_list_con8};

    CustomDialog dlg; //렌탈 리스트용 커스텀 다이얼로그
    int dlgCheck = 0; //다이얼로그에 체크된 아이템을 구분 짓기 위한 변수
    String start_date, start_time, return_date, return_time, curAddress; //시간 데이터
    String simpleDate; // yyyyMMdd 타입 날짜
    //    ImageView ivImage;
    Bundle dlgBundle; //다이얼로그용 번들
    Context rental_list_context;
    RecyclerView recyclerView; //리사이클러뷰
    //위경도 확인후 계산시 필요
    private Geocoder gCoder;
    LatLng myLatLng; //현재 위치 위경도
    LatLng rental_shop_latlng; // shop의 위경도
    private final static int RENTAL_CAR_LIST_FILTER = 3333;
    private final int DISTTYPE = 0;
    private final int PRICETYPE = 1;
    private final int POPULARTYPE = 2;


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
        curAddress = intent.getStringExtra("current_Address"); //현재 위치

        gCoder = new Geocoder(this, Locale.getDefault());
//        getAddress();
        myLatLng = ConvertGPS(curAddress);

        //예약 시간을 yyyyMMdd타입으로 바꿔줌
        SimpleDateFormat ymdFormat = new SimpleDateFormat("MMdd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d일 (E)");

        try {
            simpleDate = ymdFormat.format(dateFormat.parse(start_date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        simpleDate = "2020" + simpleDate;

        Rental_list_adapterItem.clearItem(); // 저장되있는 리스트 아이템 클리어

        //스팀세차 데이터를 임시로 가져옴 테스트용
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("MEMBER_NO", voLoginItem.MEMBER_NO);
        postParams.put("ADDRESS", curAddress);
        postParams.put("REQUEST_DATE", simpleDate);
        postParams.put("REQUEST_PAGE", "1");
        postParams.put("REQUEST_NUM", "20");
        postParams.put("SEARCH_WORD", curAddress);

        Util.showDialog(rental_list_context);

        //테스트용 스팀세차 리스트로 호출 추후에 조정 필요
        VolleyNetwork.getInstance(this).serverDataRequest(AGENT_LIST_REQUEST, postParams, onRentalListInteractionListener);

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
                                listRefresh(DISTTYPE);
                                Toast.makeText(getApplicationContext(), "거리순을 선택하셨습니다", Toast.LENGTH_SHORT).show();
                                dlgCheck = 0;
                                break;
                            case 1:
                                btSort.setText("가격순");
                                listRefresh(PRICETYPE);
                                Toast.makeText(getApplicationContext(), "가격순을 선택하셨습니다", Toast.LENGTH_SHORT).show();
                                dlgCheck = 1;
                                break;
                            case 2:
                                btSort.setText("인기순");
                                listRefresh(POPULARTYPE);
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

    //정렬 방식 설정 거리순, 가격순, 인기순순
    //거리순
    class distCompare implements Comparator<Rental_list_adapterItem.Rental_List_Item> {
        @Override
        public int compare(Rental_list_adapterItem.Rental_List_Item o1, Rental_list_adapterItem.Rental_List_Item o2) {
            return o1.distance > o2.distance ? 1 : o1.distance < o2.distance ? -1 : 0; //4항 연산자
        }
    }

    //가격순
    class priceCompare implements Comparator<Rental_list_adapterItem.Rental_List_Item> {
        @Override
        public int compare(Rental_list_adapterItem.Rental_List_Item o1, Rental_list_adapterItem.Rental_List_Item o2) {
            return Integer.parseInt(o1.rental_Price) > Integer.parseInt(o2.rental_Price)
                    ? 1 : Integer.parseInt(o1.rental_Price) < Integer.parseInt(o2.rental_Price) ? -1 : 0;
        }
    }

    //인기순
    class populCompare {

    }

    //발리 네트워크 연결후 Response 성공,실패 메소드
    VolleyNetwork.OnResponseListener onRentalListInteractionListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String it) {
            Gson gson = new Gson();
            agentResult = gson.fromJson(it, AgentResult.class);

            voAgentItem.TOTAL = agentResult.agent_list.get(0).TOTAL;
            voAgentItem.CURRENT_PAGE = agentResult.agent_list.get(0).CURRENT_PAGE;
            voAgentItem.CURRENT_NUM = agentResult.agent_list.get(0).CURRENT_NUM;

            voAgentDataItem = agentResult.DATA;

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
                    //예약시간,반납시간, 현재주소의 데이터를 이전 액티비티에서 받아서 넘김
                    Intent intent = new Intent(getApplicationContext(), Rental_list_detail.class);
                    intent.putExtra("start_date", start_date);
                    intent.putExtra("start_time", start_time);
                    intent.putExtra("return_date", return_date);
                    intent.putExtra("return_time", return_time);
                    intent.putExtra("current_Address", curAddress);
                    rental_shop_latlng = ConvertGPS(rental_Item.rental_posi);
                    intent.putExtra("shop_lng", rental_shop_latlng.latitude);
                    intent.putExtra("shop_lon", rental_shop_latlng.longitude);
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

    //클래스통에 아이템을 순차적으로 넣음
    //리싸이클러뷰 아이템 하나에 두개의 속성 아이템을 넣을수 있으므로
    //짝수번째는 왼쪽 홀수번째는 오른쪽에 추가하는 방식으로 함
    private void putItemToList() {

        for (int i = 0; i < voAgentDataItem.size(); i++) {

           rental_shop_latlng = ConvertGPS(voAgentDataItem.get(i).WASH_AREA);
            double dist = distance(myLatLng.latitude, myLatLng.longitude, rental_shop_latlng.latitude, rental_shop_latlng.longitude);
            if (i % 2 == 0) {
                Rental_list_adapterItem.addItem1(new Rental_list_adapterItem.Rental_List_Item(
                        voAgentDataItem.get(i).AGENT_IMG_URL,
                        voAgentDataItem.get(i).AGENT_NAME,
                        voAgentDataItem.get(i).NAME,
                        voAgentDataItem.get(i).AGENT_STAUS,
                        voAgentDataItem.get(i).AGENT_NUMBER,
                        voAgentDataItem.get(i).WASH_AREA,
                        dist
                ));
            } else {
                Rental_list_adapterItem.addItem2(new Rental_list_adapterItem.Rental_List_Item(
                        voAgentDataItem.get(i).AGENT_IMG_URL,
                        voAgentDataItem.get(i).AGENT_NAME,
                        voAgentDataItem.get(i).NAME,
                        voAgentDataItem.get(i).AGENT_STAUS,
                        voAgentDataItem.get(i).AGENT_NUMBER,
                        voAgentDataItem.get(i).WASH_AREA,
                        dist
                ));
            }
            Rental_list_adapterItem.sumList(); //두개의 List그룹을 합침
        }
    }

    //정렬 다이얼로그에서 정렬시 실행되는 메소드
    public void listRefresh(int sortType) {
        recyclerView.removeAllViewsInLayout(); //recyclerview 삭제
        switch (sortType) {
            case DISTTYPE:
                Collections.sort(Rental_list_adapterItem.RENTAL_LIST_ITEM_BOTH, new distCompare()); //가격순으로 정렬
                break;
            case PRICETYPE:
                Collections.sort(Rental_list_adapterItem.RENTAL_LIST_ITEM_BOTH, new priceCompare()); //가격순으로 정렬
                break;
            case POPULARTYPE:
                break;
            default:
                break;
        }

        Rental_list_adapterItem.divList();  //다시 합쳐진 List그룹을 정렬한 상태에서 나눔
        callRentalListRecyclerViewAdapter();
    }

    /**
     * 현 위치 호출시 주소 갱신
     */
//    private void getAddress() {
//        if (gps.isGetLocation()) {
//            //Geocoder
//            List<Address> addr = null;
//            try {
//                addr = gCoder.getFromLocation(latitude, longitude, 10);   //위도, 경도, 얻어올 값의 개수
//                Address a = addr.get(0);
//
//                String address = a.getAddressLine(0).substring(a.getAddressLine(0).indexOf("\"") + 1, a.getAddressLine(0).length()); // 주소
//                address = address.replace("대한민국 ", "");
//                if (address != null && address.length() > 0) {
//                    String[] splitStr = address.split(" ");
//                    curAddress = address;
//
//                    //해당 지역을 넣어준다.
//                    //return_address = "위치 : " + splitStr[1] + " " + splitStr[2];
//
//                } else {
//                    //주소를 가져오지 못했을 때 처리 추가.
//                    //Toast.makeText(this,"주소 정보가 없습니다.", Toast.LENGTH_LONG).show();
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                Toast.makeText(this, "주소 정보가 없습니다.", Toast.LENGTH_LONG).show();
//            }
//        }
//    }

    /**
     * 주소 -> 위경도로 변환
     */
    private LatLng ConvertGPS(String cAddress) {
        List<Address> list = null;
        gCoder = new Geocoder(this, Locale.getDefault());
        LatLng latlng;

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
                latlng = new LatLng(addr.getLatitude(), addr.getLongitude());
                return latlng;
//                Toast.makeText(this, "위도 : " + lat + ", 경도 : " + lon, Toast.LENGTH_LONG).show();

            }
        }
        return null;
    }

    /*
     * TODO 거리계산 메소드(계산이 시간이 걸림, 추후 서버에 역활을 넘길 예정)
     */

    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

}


