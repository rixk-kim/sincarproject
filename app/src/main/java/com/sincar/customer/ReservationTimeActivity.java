package com.sincar.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sincar.customer.adapter.AgentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.AgentContent;
import com.sincar.customer.adapter.content.AgentContent.AgentItem;
import com.sincar.customer.adapter.content.TimeContent;
import com.sincar.customer.item.AgentDataItem;
import com.sincar.customer.item.AgentResult;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.sincar.customer.HWApplication.agentResult;
import static com.sincar.customer.HWApplication.voAgentDataItem;
import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.common.Constants.LOGIN_REQUEST;

public class ReservationTimeActivity extends AppCompatActivity
        implements View.OnClickListener, AgentRecyclerViewAdapter.OnAgentListInteractionListener {
    private Context reContext;
    private String reserve_address; //예약주소
    private String reserve_year;    //예약년도
    private String reserve_month;   //예약월
    private String reserve_day;     //예약일

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_time);
        reContext = this;

        Intent intent = getIntent(); /*데이터 수신*/
        reserve_address     = intent.getExtras().getString("reserve_address");    /*String형*/
        reserve_year        = intent.getExtras().getString("reserve_year");    /*String형*/
        reserve_month       = intent.getExtras().getString("reserve_month");    /*String형*/
        reserve_day         = intent.getExtras().getString("reserve_day");    /*String형*/

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        findViewById(R.id.btnPrev).setOnClickListener(this);

        String iiserverData = "{\"agent_list\": \n" +
                "\t{\"DATA\":[\n" +
                "\t{\"SEQ\":\"1\",\"NAME\":\"김태현1\",\"AGENT_NAME\":\"관악 1호점\",\"WASH_AREA\":\"관악구, 금천구,영등포구\",\n" +
                "\t\"TIME_INFO\":[{\"RESERVE_TIME\":\"07:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"08:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"09:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"10:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"11:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"12:00\", \"RESERVE_STATUS\":\"N\"},\n" +
                "\t{\"RESERVE_TIME\":\"13:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"14:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"15:00\", \"RESERVE_STATUS\":\"N\"},\n" +
                "\t{\"RESERVE_TIME\":\"16:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"17:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"18:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"19:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"20:00\", \"RESERVE_STATUS\":\"N\"}\n" +
                "\t]},\n" +
                "\t{\"SEQ\":\"2\",\"NAME\":\"김태현2\",\"AGENT_NAME\":\"관악 2호점\",\"WASH_AREA\":\"관악구, 금천구,영등포구\",\n" +
                "\t\"TIME_INFO\":[{\"RESERVE_TIME\":\"07:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"08:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"09:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"10:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"11:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"12:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"13:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"14:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"15:00\", \"RESERVE_STATUS\":\"N\"}\n" +
                "\t]},\n" +
                "\t{\"SEQ\":\"3\",\"NAME\":\"김태현3\",\"AGENT_NAME\":\"관악 3호점\",\"WASH_AREA\":\"관악구, 금천구,영등포구\",\n" +
                "\t\"TIME_INFO\":[{\"RESERVE_TIME\":\"07:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"08:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"09:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"10:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"11:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"12:00\", \"RESERVE_STATUS\":\"N\"},\n" +
                "\t{\"RESERVE_TIME\":\"13:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"14:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"15:00\", \"RESERVE_STATUS\":\"N\"},\n" +
                "\t{\"RESERVE_TIME\":\"16:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"17:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"18:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"19:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                "\t{\"RESERVE_TIME\":\"20:00\", \"RESERVE_STATUS\":\"N\"}\n" +
                "\t]}\n" +
                "\t]}\n" +
                "}";


        Gson gSon = new Gson();
        agentResult = gSon.fromJson(iiserverData, AgentResult.class);

        voAgentDataItem     = agentResult.agent_list;

        List<AgentContent.AgentItem> ITEMS = new ArrayList<AgentContent.AgentItem>();
        AgentContent.clearItem(); //초기화
//        for(int i = 0; i < voAgentDataItem.DATA.size(); i++) {
        int i=0;
        for(com.sincar.customer.item.AgentItem agentItem : voAgentDataItem.DATA) {

            int j=0;
            ArrayList<TimeContent.TimeItem> reserve_info = new ArrayList<>();
            for(com.sincar.customer.item.TimeItem item: agentItem.TIME_INFO) {
                reserve_info.add(new TimeContent.TimeItem(
                        i,
                        j++,
                        item.RESERVE_TIME,
                        item.RESERVE_STATUS,
                        false
                ));
            }

            AgentContent.addItem(new AgentContent.AgentItem(
                    i++,
                    agentItem.SEQ,
                    agentItem.NAME,
                    agentItem.AGENT_NAME,
                    agentItem.WASH_AREA,
                    //agentItem.TIME_INFO
                    reserve_info
            ));
        }

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
        postParams.put("REQUEST_DATE", "20200320");                   //날짜

        //프로그래스바 시작
        Util.showDialog();
        //사용내역 요청
        VolleyNetwork.getInstance(this).passwordChangeRequest(LOGIN_REQUEST, postParams, onResponseListener);
    }

    VolleyNetwork.OnResponseListener onResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String serverData) {
            serverData = "{\"agent_list\": \n" +
                    "\t{\"DATA\":[\n" +
                    "\t{\"SEQ\":\"1\",\"NAME\":\"김태현1\",\"AGENT_NAME\":\"관악 1호점\",\"WASH_AREA\":\"관악구, 금천구,영등포구\",\n" +
                    "\t\"TIME_INFO\":[{\"RESERVE_TIME\":\"07:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"08:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"09:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"10:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"11:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"12:00\", \"RESERVE_STATUS\":\"N\"},\n" +
                    "\t{\"RESERVE_TIME\":\"13:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"14:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"15:00\", \"RESERVE_STATUS\":\"N\"},\n" +
                    "\t{\"RESERVE_TIME\":\"16:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"17:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"18:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"19:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"20:00\", \"RESERVE_STATUS\":\"N\"}\n" +
                    "\t]},\n" +
                    "\t{\"SEQ\":\"2\",\"NAME\":\"김태현2\",\"AGENT_NAME\":\"관악 2호점\",\"WASH_AREA\":\"관악구, 금천구,영등포구\",\n" +
                    "\t\"TIME_INFO\":[{\"RESERVE_TIME\":\"07:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"08:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"09:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"10:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"11:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"12:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"13:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"14:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"15:00\", \"RESERVE_STATUS\":\"N\"}\n" +
                    "\t]},\n" +
                    "\t{\"SEQ\":\"3\",\"NAME\":\"김태현3\",\"AGENT_NAME\":\"관악 3호점\",\"WASH_AREA\":\"관악구, 금천구,영등포구\",\n" +
                    "\t\"TIME_INFO\":[{\"RESERVE_TIME\":\"07:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"08:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"09:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"10:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"11:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"12:00\", \"RESERVE_STATUS\":\"N\"},\n" +
                    "\t{\"RESERVE_TIME\":\"13:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"14:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"15:00\", \"RESERVE_STATUS\":\"N\"},\n" +
                    "\t{\"RESERVE_TIME\":\"16:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"17:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"18:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"19:00\", \"RESERVE_STATUS\":\"Y\"},\n" +
                    "\t{\"RESERVE_TIME\":\"20:00\", \"RESERVE_STATUS\":\"N\"}\n" +
                    "\t]}\n" +
                    "\t]}\n" +
                    "}";


            Gson gSon = new Gson();
            agentResult = gSon.fromJson(serverData, AgentResult.class);

            voAgentDataItem     = agentResult.agent_list;

            List<AgentContent.AgentItem> ITEMS = new ArrayList<AgentContent.AgentItem>();
            AgentContent.clearItem(); //초기화
//            for(int i = 0; i < voAgentDataItem.DATA.size(); i++) {
//                AgentContent.addItem(new AgentContent.AgentItem(
//                        i,
//                        voAgentDataItem.DATA.get(i).SEQ,
//                        voAgentDataItem.DATA.get(i).NAME,
//                        voAgentDataItem.DATA.get(i).AGENT_NAME,
//                        voAgentDataItem.DATA.get(i).WASH_AREA,
//                        voAgentDataItem.DATA.get(i).TIME_INFO
//                ));
//            }

            //프로그래스바 종료
            Util.dismiss();

            callAgentRecyclerViewAdapter();
        }

        @Override
        public void onResponseFailListener(VolleyError it) {

        }
    };

    private void callAgentRecyclerViewAdapter(){
        // 서버 연동 후 AgentContent.ITEMS에 리스 항목 추가 작업 확인
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
        //finish();
    }
}
