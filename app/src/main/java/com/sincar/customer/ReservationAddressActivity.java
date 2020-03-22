package com.sincar.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.sincar.customer.HWApplication.addressResult;
import static com.sincar.customer.HWApplication.voAddressDataItem;
import static com.sincar.customer.HWApplication.voAddressItem;
import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.common.Constants.LOGIN_REQUEST;

public class ReservationAddressActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mSearchAddressKeyword;
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

        // TODO - 서버 연동 후 AddressContent.ITEMS에 리스 항목추가 작업
        // Set the adapter - 포인트 리스트 설정
        View view = findViewById(R.id.searchAddressList);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new AddressContentRecyclerViewAdapter(AddressContent.ITEMS));
        }
    }

    /**
     * 주소 검색 요청
     * MEMBER_NO    : 회원번호
     * SEARCH_WORD  : 검색단어
     */
    private void requestSearchWord() {
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("MEMBER_NO", voLoginItem.MEMBER_NO);      // 회원번호
        postParams.put("SEARCH_WORD", mSearchAddressKeyword.getText().toString().trim());    // 검색단어

        //검색 요청
        VolleyNetwork.getInstance(this).passwordChangeRequest(LOGIN_REQUEST, postParams, onSearchResponseListener);
    }

    VolleyNetwork.OnResponseListener onSearchResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String serverData) {
            /*
                 {"search_list": [{"TOTAL_PAGE":"5","CURRENT_PAGE":"1","CURRENT_NUM":"2"}],
                 "data":[{"SEARCH_SEQ":"1","SEARCH_WORD":"석촌호수로 911"},{},{}…]}
             */

            Gson gSon = new Gson();
            addressResult = gSon.fromJson(serverData, AddressResult.class);

            voAddressItem.TOTAL              = addressResult.search_list.get(0).TOTAL;
            voAddressItem.CURRENT_PAGE       = addressResult.search_list.get(0).CURRENT_PAGE;
            voAddressItem.CURRENT_NUM        = addressResult.search_list.get(0).CURRENT_NUM;

            voAddressDataItem     = addressResult.DATA;

            List<AddressContent.AddressItem> ITEMS = new ArrayList<AddressContent.AddressItem>();

            for(int i = 0; i < voAddressDataItem.size(); i++) {
                AddressContent.addItem(new AddressContent.AddressItem(
                        i,
                        voAddressDataItem.get(i).SEARCH_SEQ ,
                        voAddressDataItem.get(i).SEARCH_WORD
                ));
            }

            //프로그래스바 종료
            Util.dismiss();

            // 서버 연동 후 AddressContent.ITEMS에 리스 항목 추가 작업 확인
            // Set the adapter - 이용내역 리스트 설정
            if(AddressContent.ITEMS.size() > 0) {
                View view = findViewById(R.id.searchAddressList);
                if (view instanceof RecyclerView) {
                    Context context = view.getContext();
                    RecyclerView recyclerView = (RecyclerView) view;

                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(new AddressContentRecyclerViewAdapter(AddressContent.ITEMS));
                }
            }else{
                // TODO - 검색단어 없을 때 화면 UI 추가
//                LinearLayout view = findViewById(R.id.use_history_empty);
//                view.setVisibility(View.VISIBLE);
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
                Toast.makeText(ReservationAddressActivity.this, "서버 연동 준비중입니다.", Toast.LENGTH_SHORT).show();
                //requestSearchWord()
                break;
        }
    }


}
