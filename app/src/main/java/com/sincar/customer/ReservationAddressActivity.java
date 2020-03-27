package com.sincar.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sincar.customer.adapter.AddressContentRecyclerViewAdapter;
import com.sincar.customer.adapter.CardContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.AddressContent;
import com.sincar.customer.adapter.content.CardContent;
import com.sincar.customer.item.AddressResult;
import com.sincar.customer.item.CardResult;
import com.sincar.customer.item.SearchWordResult;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.sincar.customer.HWApplication.addressResult;
import static com.sincar.customer.HWApplication.voAddressDataItem;
import static com.sincar.customer.HWApplication.voAddressItem;
import static com.sincar.customer.HWApplication.searchWordResult;
import static com.sincar.customer.HWApplication.voSearchWordDataItem;
import static com.sincar.customer.HWApplication.voSearchWordItem;
import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.common.Constants.CURRENT_SEARCH_REQUEST;
import static com.sincar.customer.common.Constants.LOGIN_REQUEST;
import static com.sincar.customer.common.Constants.SEARCH_WORD_REQUEST;

public class ReservationAddressActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mSearchAddressKeyword;
    private boolean search_status = false;

    //페이지 처리
    private int request_page = 1;                           // 페이징변수. 초기 값은 0 이다.
    private final int request_offset = 20;                  // 한 페이지마다 로드할 데이터 갯수.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_address);

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        findViewById(R.id.btnPrev).setOnClickListener(this);
        findViewById(R.id.btnSearchCancel).setOnClickListener(this);
        findViewById(R.id.btnSearchAddress).setOnClickListener(this);

        mSearchAddressKeyword = findViewById(R.id.searchAddressKeyword);

//        View view = findViewById(R.id.searchAddressList);
//        view.setVisibility(View.GONE);
//
//        LinearLayout view1 = findViewById(R.id.search_history_empty);
//        view1.setVisibility(View.VISIBLE);

        // TODO - 서버 연동 후 AddressContent.ITEMS에 리스 항목추가 작업
        AddressContent.clearItem(); //초기화
        requestCurrentWord();
        // Set the adapter - 포인트 리스트 설정
//        View view = findViewById(R.id.searchAddressList);
//        if (view instanceof RecyclerView) {
//            Context context = view.getContext();
//            RecyclerView recyclerView = (RecyclerView) view;
//
//            recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            recyclerView.setAdapter(new AddressContentRecyclerViewAdapter(AddressContent.ITEMS));
//        }
    }

    /**
     * 최근 검색 내역 요청
     */
    private void requestCurrentWord() {
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("PHONE_NUMBER", voLoginItem.MEMBER_NO);  // 폰번호
        postParams.put("MEMBER_NO", voLoginItem.MEMBER_NO);     // 회원번호
        postParams.put("REQUEST_PAGE", "1");                    // 요청페이지
        postParams.put("REQUEST_NUM", "10");                    // 요청갯수
        search_status = true;
        //프로그래스바 시작
        Util.showDialog(this);

        //검색 요청
        VolleyNetwork.getInstance(this).serverDataRequest(CURRENT_SEARCH_REQUEST, postParams, onCurrentSearchResponseListener);
    }

    VolleyNetwork.OnResponseListener onCurrentSearchResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String serverData) {
            /*
                 {"search_list": [{"TOTAL_PAGE":"5","CURRENT_PAGE":"1","CURRENT_NUM":"2"}],
                 "data":[{"SEARCH_SEQ":"1","SEARCH_WORD":"송파구청","SEARCH_ADDRESS":"석촌호수로 911"},{},{}…]}
             */

            Gson gSon = new Gson();
            addressResult = gSon.fromJson(serverData, AddressResult.class);

            voAddressItem.TOTAL              = addressResult.search_list.get(0).TOTAL;
            voAddressItem.CURRENT_PAGE       = addressResult.search_list.get(0).CURRENT_PAGE;
            voAddressItem.CURRENT_NUM        = addressResult.search_list.get(0).CURRENT_NUM;

            voAddressDataItem     = addressResult.data;

            List<AddressContent.AddressItem> ITEMS = new ArrayList<AddressContent.AddressItem>();

            for(int i = 0; i < voAddressDataItem.size(); i++) {
                AddressContent.addItem(new AddressContent.AddressItem(
                        i,
                        voAddressDataItem.get(i).SEARCH_SEQ ,
                        voAddressDataItem.get(i).SEARCH_WORD,
                        voAddressDataItem.get(i).SEARCH_ADDRESS
                ));
            }

            //프로그래스바 종료
            Util.dismiss();

            // 서버 연동 후 AddressContent.ITEMS에 리스 항목 추가 작업 확인
            // Set the adapter - 이용내역 리스트 설정
            if(AddressContent.ITEMS.size() > 0) {
                View view = findViewById(R.id.searchAddressList);
                LinearLayout view1 = findViewById(R.id.search_history_empty);

                view.setVisibility(View.VISIBLE);
                view1.setVisibility(View.GONE);

                if (view instanceof RecyclerView) {
                    Context context = view.getContext();
                    RecyclerView recyclerView = (RecyclerView) view;

                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(new AddressContentRecyclerViewAdapter(AddressContent.ITEMS));
                }
            }else{
                // 검색단어 없을 때 화면 UI 추가
                View view = findViewById(R.id.searchAddressList);
                view.setVisibility(View.GONE);

                LinearLayout view1 = findViewById(R.id.search_history_empty);
                view1.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onResponseFailListener(VolleyError it) {

        }
    };

    /**
     * 주소 검색 요청
     * MEMBER_NO    : 회원번호
     * SEARCH_WORD  : 검색단어
     */
    private void requestSearchWord() {
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("MEMBER_NO", voLoginItem.MEMBER_NO);                                 // 회원번호
        postParams.put("SEARCH_WORD", mSearchAddressKeyword.getText().toString().trim());   // 검색단어
        postParams.put("REQUEST_PAGE", String.valueOf(request_page));                       // 요청페이지
        postParams.put("REQUEST_NUM", String.valueOf(request_offset));                      // 요청갯수

        //프로그래스바 시작
        Util.showDialog(this);

        //검색 요청
        VolleyNetwork.getInstance(this).serverDataRequest(SEARCH_WORD_REQUEST, postParams, onSearchWordResponseListener);
    }



    VolleyNetwork.OnResponseListener onSearchWordResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String serverData) {
            /*
                 {"search_list": [{"TOTAL_PAGE":"5","CURRENT_PAGE":"1","CURRENT_NUM":"2"}],
                 "data":[{"SEARCH_SEQ":"1","SEARCH_WORD":"송파구청","SEARCH_ADDRESS":"석촌호수로 911"},{},{}…]}
             */

            Gson gSon = new Gson();
            searchWordResult = gSon.fromJson(serverData, SearchWordResult.class);

            voSearchWordItem.TOTAL              = searchWordResult.address_search.get(0).TOTAL;
//            voAddressItem.CURRENT_PAGE       = addressResult.search_list.get(0).CURRENT_PAGE;
//            voAddressItem.CURRENT_NUM        = addressResult.search_list.get(0).CURRENT_NUM;

            voSearchWordDataItem     = searchWordResult.DATA;

            List<AddressContent.AddressItem> ITEMS = new ArrayList<AddressContent.AddressItem>();

            for(int i = 0; i < voSearchWordDataItem.size(); i++) {
                AddressContent.addItem(new AddressContent.AddressItem(
                        i,
                        voSearchWordDataItem.get(i).SEQ ,
                        voSearchWordDataItem.get(i).TITLE,
                        voSearchWordDataItem.get(i).ADDRESS
                ));
            }

            //프로그래스바 종료
            Util.dismiss();

            // 서버 연동 후 AddressContent.ITEMS에 리스 항목 추가 작업 확인
            // Set the adapter - 이용내역 리스트 설정
            if(AddressContent.ITEMS.size() > 0) {
                View view = findViewById(R.id.searchAddressList);
                LinearLayout view1 = findViewById(R.id.search_history_empty);

                view.setVisibility(View.VISIBLE);
                view1.setVisibility(View.GONE);

                if (view instanceof RecyclerView) {
                    Context context = view.getContext();
                    RecyclerView recyclerView = (RecyclerView) view;

                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(new AddressContentRecyclerViewAdapter(AddressContent.ITEMS));

                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                        }

                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                            super.onScrolled(recyclerView, dx, dy);

                            LinearLayoutManager layoutManager = LinearLayoutManager.class.cast(recyclerView.getLayoutManager());
                            int totalItemCount = layoutManager.getItemCount();
                            int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition();

                            if (lastVisible >= totalItemCount - 1) {
                                int lastPageNum;
                                if( Integer.parseInt(voSearchWordItem.TOTAL) % request_offset == 0 ) {
                                    lastPageNum = (int)Math.floor(Integer.parseInt(voSearchWordItem.TOTAL)/request_offset);
                                }
                                else {
                                    lastPageNum = (int)Math.floor(Integer.parseInt(voSearchWordItem.TOTAL)/request_offset) + 1;
                                }

                                if(lastPageNum > request_page)
                                {
                                    //다음 페이지 요청
                                    request_page+=1;
                                    requestSearchWord();
                                }
                            }

                        }
                    });
                }
            }else{
                // 검색단어 없을 때 화면 UI 추가
                View view = findViewById(R.id.searchAddressList);
                view.setVisibility(View.GONE);

                LinearLayout view1 = findViewById(R.id.search_history_empty);
                view1.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onResponseFailListener(VolleyError it) {

        }
    };

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.btnPrev:
                finish();
                break;
            case R.id.btnSearchCancel:
                // 검색 주소 Clear
                mSearchAddressKeyword.setText("");
                break;
            case R.id.btnSearchAddress:
                // TODO - 주소 검색 리스트 생성
                // 서버연동
                //Toast.makeText(ReservationAddressActivity.this, "서버 연동 준비중입니다.", Toast.LENGTH_SHORT).show();
                if(mSearchAddressKeyword.getText().toString().trim().length() > 1) {
                    AddressContent.clearItem(); //초기화
                    requestSearchWord();
                }else{
                    Toast.makeText(ReservationAddressActivity.this, "검색어 입력시 최소 2자이상 해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


}
