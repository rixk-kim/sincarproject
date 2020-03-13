package com.sincar.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sincar.customer.adapter.AgentRecyclerViewAdapter;
import com.sincar.customer.adapter.CardContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.AgentContent;
import com.sincar.customer.adapter.content.AgentContent.AgentItem;
import com.sincar.customer.adapter.content.CardContent;
import com.sincar.customer.item.AgentResult;
import com.sincar.customer.item.CardResult;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.sincar.customer.HWApplication.agentResult;
import static com.sincar.customer.HWApplication.voAgentDataItem;
import static com.sincar.customer.HWApplication.voAgentItem;
import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.common.Constants.LOGIN_REQUEST;

public class ReservationTimeActivity extends AppCompatActivity
        implements View.OnClickListener, AgentRecyclerViewAdapter.OnAgentListInteractionListener {
    private Context reContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_time);
        reContext = this;

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        findViewById(R.id.btnPrev).setOnClickListener(this);

        // TODO - 서버 연동 후 AgentTimeContent.ITEMS에 리스 항목 추가 작업
        // Set the adapter - 포인트 리스트 설정
        View view = findViewById(R.id.agentList);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new AgentRecyclerViewAdapter(AgentContent.ITEMS, this));
        }
    }

    /**
     * 대리 리스트 요청
     * MEMBER_NO        : 회원번호
     * REQUESTT_PAGE    : 검색 주소
     */
    private void requestNoticeList() {
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("MEMBER_NO", voLoginItem.MEMBER_NO);         // 회원번호
        postParams.put("ADDRESS", "송파구 석촌대로");                   // 검색 주소

        //프로그래스바 시작
        Util.showDialog();
        //사용내역 요청
        VolleyNetwork.getInstance(this).passwordChangeRequest(LOGIN_REQUEST, postParams, onResponseListener);
    }

    VolleyNetwork.OnResponseListener onResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String serverData) {
            /*
                {"agent_list": [{"TOTAL":"3"}],
                "DATA":[{"SEQ":"1","NAME":"김태현","AGENT_NAME":"관악 1호점","WASH_AREA":"관악구, 금천구,영등포구","RESERVE_TIME":"7/8/9/10/13/14/15/17/18/19/20"},{"SEQ":"2","ADDRESS":"송파구 석촌 호수로"},{"SEQ":"1","NAME":"김태현","AGENT_NAME":"관악 1호점","WASH_AREA":"관악구, 금천구,영등포구","RESERVE_TIME":"7/8/9/10/13/14/15/17/18/19/20"},{"SEQ":"2","ADDRESS":"송파구 석촌 호수로"}]}
             */

            Gson gSon = new Gson();
            agentResult = gSon.fromJson(serverData, AgentResult.class);

            voAgentItem.TOTAL    = agentResult.agent_list.get(0).TOTAL;

            voAgentDataItem     = agentResult.DATA;

            List<AgentContent.AgentItem> ITEMS = new ArrayList<AgentContent.AgentItem>();

            for(int i = 0; i < voAgentDataItem.size(); i++) {
                AgentContent.addItem(new AgentContent.AgentItem(
                        i,
                        voAgentDataItem.get(i).SEQ,
                        voAgentDataItem.get(i).NAME,
                        voAgentDataItem.get(i).AGENT_NAME,
                        voAgentDataItem.get(i).WASH_AREA,
                        voAgentDataItem.get(i).RESERVE_TIME
                ));
            }

            //프로그래스바 종료
            Util.dismiss();

            callAgentRecyclerViewAdapter();
        }

        @Override
        public void onResponseFailListener(VolleyError it) {

        }
    };

    private void callAgentRecyclerViewAdapter(){
        // 서버 연동 후 UseContent.ITEMS에 리스 항목 추가 작업 확인
        // Set the adapter - 이용내역 리스트 설정
        if(AgentContent.ITEMS.size() > 0) {
            View view = findViewById(R.id.agentList);
            if (view instanceof RecyclerView) {
                Context context = view.getContext();
                RecyclerView recyclerView = (RecyclerView) view;

                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(new AgentRecyclerViewAdapter(AgentContent.ITEMS, this));
            }
        }else{
            // TODO - 대리점 정보 없을 때 화면 UI 추가
//                LinearLayout view = findViewById(R.id.use_history_empty);
//                view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPrev:
                finish();
                break;
        }
    }

    @Override
    public void onAgentListInteraction(AgentItem agentItem) {
        Log.d("시간예약", "대리점주 id = " + agentItem.id);

        // TODO - 예약시간 설정 하고 예약 메인 화면으로 이동에 필요한 데이타 전송필요(Bundle)
        Intent intent = new Intent(this, ReservationMainActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("대리점정보", "대리점정보");
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
