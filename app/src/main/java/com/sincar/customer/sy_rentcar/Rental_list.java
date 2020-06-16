package com.sincar.customer.sy_rentcar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.sincar.customer.MainActivity;
import com.sincar.customer.R;
import com.sincar.customer.item.AgentResult;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {

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

        Button btFilter = (Button) findViewById(R.id.btFilter);

        //리스트 필터 보기 액티비티로 화면 전환
        btFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Rental_list_filter.class);
                startActivity(intent);
            }
        });

        //리스트 sort방식을 결정하기 위한 버튼
        final Button btSort = (Button) findViewById(R.id.btn_rentalCar_Sort);

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
                                Toast.makeText(getApplicationContext(), "거리순을 선택하셨습니다", Toast.LENGTH_SHORT).show();
                                dlgCheck = 0;
                                break;
                            case 1:
                                btSort.setText("가격순");
                                Toast.makeText(getApplicationContext(), "가격순을 선택하셨습니다", Toast.LENGTH_SHORT).show();
                                dlgCheck = 1;
                                break;
                            case 2:
                                btSort.setText("인기순");
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

    VolleyNetwork.OnResponseListener onRentalListInteractionListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String it) {
            Gson gson = new Gson();
            agentResult = gson.fromJson(it, AgentResult.class);

            voAgentItem.TOTAL = agentResult.agent_list.get(0).TOTAL;
            voAgentItem.CURRENT_PAGE = agentResult.agent_list.get(0).CURRENT_PAGE;
            voAgentItem.CURRENT_NUM = agentResult.agent_list.get(0).CURRENT_NUM;

            voAgentDataItem = agentResult.DATA;

            for (int i = 0; i < voAgentDataItem.size(); i++) {
                if (i % 2 == 0) {
                    Rental_list_adapterItem.addItem1(new Rental_list_adapterItem.Rental_List_Item(
                            voAgentDataItem.get(i).AGENT_IMG_URL,
                            voAgentDataItem.get(i).AGENT_NAME,
                            voAgentDataItem.get(i).NAME,
                            voAgentDataItem.get(i).AGENT_STAUS,
                            voAgentDataItem.get(i).AGENT_NUMBER
                    ));
                } else {
                    Rental_list_adapterItem.addItem2(new Rental_list_adapterItem.Rental_List_Item(
                            voAgentDataItem.get(i).AGENT_IMG_URL,
                            voAgentDataItem.get(i).AGENT_NAME,
                            voAgentDataItem.get(i).NAME,
                            voAgentDataItem.get(i).AGENT_STAUS,
                            voAgentDataItem.get(i).AGENT_NUMBER
                    ));
                }


            }
            callRentalListRecyclerViewAdapter();
        }

        @Override
        public void onResponseFailListener(VolleyError it) {

        }
    };

    private void callRentalListRecyclerViewAdapter() {
        RecyclerView recyclerView = findViewById(R.id.rental_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Rental_list_adapter adapter = new Rental_list_adapter(Rental_list_adapterItem.RENTAL_LIST_ITEM1,Rental_list_adapterItem.RENTAL_LIST_ITEM2, rental_list_context);
        recyclerView.setAdapter(adapter);

        //Recyclerview 에서 아이템을 클릭했을때 이벤트(상세 페이지 이동)
        adapter.setOnItemClickListener(new Rental_list_adapter.OnRentalListInteractionListener() {
            @Override
            public void onRentalListInteractionListener(Rental_list_adapterItem rental_Item) {
                //상세페이지 이동
                //예약시간,반납시간, 현재주소의 데이터를 이전 액티비티에서 받아서 넘김
                Intent intent = new Intent(getApplicationContext(), Rental_list_detail.class);
                intent.putExtra("start_date", start_date);
                intent.putExtra("start_time", start_time);
                intent.putExtra("return_date", return_date);
                intent.putExtra("return_time", return_time);
                intent.putExtra("current_Address", curAddress);
                startActivity(intent);
            }
        });
    }

}


