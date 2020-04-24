package com.sincar.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.sincar.customer.HWApplication.agentResult;
import static com.sincar.customer.HWApplication.voAgentDataItem;
import static com.sincar.customer.HWApplication.voAgentItem;
import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.common.Constants.AGENT_LIST_REQUEST;
import static com.sincar.customer.common.Constants.LOGIN_REQUEST;
import static com.sincar.customer.util.Util.getHour;

public class ReservationTimeActivity extends AppCompatActivity
        implements View.OnClickListener, AgentRecyclerViewAdapter.OnAgentListInteractionListener {
    private Context reContext;
    private String reserve_address; //예약주소
    private String reserve_year;    //예약년도
    private String reserve_month;   //예약월
    private String reserve_day;     //예약일
    private String search_keyword;  //검색어

    private AgentRecyclerViewAdapter mAgentRecyclerViewAdapter;

    //페이지 처리
    private int request_page = 1;                           // 페이징변수. 초기 값은 0 이다.
    private final int request_offset = 20;                  // 한 페이지마다 로드할 데이터 갯수.

    public static ReservationTimeActivity _reservationTimeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_time);
        reContext = this;
        _reservationTimeActivity = ReservationTimeActivity.this;

        Intent intent = getIntent(); /*데이터 수신*/
        reserve_address     = intent.getExtras().getString("reserve_address");    /*String형*/
        search_keyword      = intent.getExtras().getString("search_keyword");    /*String형*/
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
        findViewById(R.id.btnPrev_layout1).setOnClickListener(this);
        AgentContent.clearItem(); //초기화
        requestAgentList();
    }

    /**
     * 대리점 리스트 요청
     * MEMBER_NO        : 회원번호
     * REQUESTT_PAGE    : 검색 주소
     */
    private void requestAgentList() {
        String reserve_date = "";
        reserve_date = reserve_year;
        if(reserve_month.length() < 2)
        {
            reserve_date += "0" + reserve_month;
        }else{
            reserve_date += reserve_month;
        }

        if(reserve_day.length() < 2)
        {
            reserve_date += "0" + reserve_day;
        }else{
            reserve_date += reserve_day;
        }

        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("MEMBER_NO", voLoginItem.MEMBER_NO);                 // 회원번호
        postParams.put("ADDRESS", reserve_address);                         // 검색 주소
//        postParams.put("ADDRESS", "관악구");                                 // 검색 주소
        postParams.put("REQUEST_DATE", reserve_date);                       // 날짜
        postParams.put("REQUEST_PAGE", String.valueOf(request_page));       // 요청페이지
        postParams.put("REQUEST_NUM", String.valueOf(request_offset));      // 요청갯수
        postParams.put("SEARCH_WORD", String.valueOf(search_keyword));      // 검색어


        //프로그래스바 시작
        Util.showDialog(this);
        //사용내역 요청
        VolleyNetwork.getInstance(this).serverDataRequest(AGENT_LIST_REQUEST, postParams, onAgentListResponseListener);
    }



    VolleyNetwork.OnResponseListener onAgentListResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String serverData) {

            Gson gSon = new Gson();
            agentResult = gSon.fromJson(serverData, AgentResult.class);

            voAgentItem.TOTAL              = agentResult.agent_list.get(0).TOTAL;
            voAgentItem.CURRENT_PAGE       = agentResult.agent_list.get(0).CURRENT_PAGE;
            voAgentItem.CURRENT_NUM        = agentResult.agent_list.get(0).CURRENT_NUM;

            voAgentDataItem     = agentResult.DATA;

            // 내림차순 정렬
            Collections.sort(voAgentDataItem, new AgentContentComparator());
//            Collections.reverse(voAgentDataItem);


            List<AgentContent.AgentItem> ITEMS = new ArrayList<AgentContent.AgentItem>();

//        for(int i = 0; i < voAgentDataItem.DATA.size(); i++) {
//            int i=0;
 //           for(com.sincar.customer.item.AgentItem agentItem : voAgentDataItem.get(i)..DATA) {
            for(int i = 0; i < voAgentDataItem.size(); i++) {

                int j=0;
                int count = 0;
                ArrayList<TimeContent.TimeItem> reserve_info = new ArrayList<>();
                //for(com.sincar.customer.item.TimeItem item: voAgentDataItem.get(j).TIME_INFO) {
                for(j = 0; j < voAgentDataItem.get(i).TIME_INFO.size(); j++){
                    //if(!TextUtils.isEmpty(item.RESERVE_TIME)) {
                    if(!TextUtils.isEmpty(voAgentDataItem.get(i).TIME_INFO.get(j).RESERVE_TIME)) {
                        int cTime = Util.getHour();
                        String sTime = voAgentDataItem.get(i).TIME_INFO.get(j).RESERVE_TIME.substring(0,2);

                        int compare = Util.getYearMonthDay2().compareTo(reserve_year+reserve_month+reserve_day);
                        //if(Integer.parseInt(reserve_year) >= Util.getYear() && Integer.parseInt(reserve_month) >= Util.getMonth() && Integer.parseInt(reserve_day) >= Util.getDay()){
             //           if (cTime < Integer.parseInt(sTime)) {
                        if(compare < 0){
                            if("Y".equals(voAgentDataItem.get(i).TIME_INFO.get(j).RESERVE_STATUS)) {
                                reserve_info.add(new TimeContent.TimeItem(
                                        i,
                                        count++,
                                        voAgentDataItem.get(i).TIME_INFO.get(j).RESERVE_TIME,
                                        voAgentDataItem.get(i).TIME_INFO.get(j).RESERVE_STATUS,
                                        false
                                ));
                            }
                        } else if(compare == 0){
                            if( cTime < Integer.parseInt(sTime))
                            {
                                if("Y".equals(voAgentDataItem.get(i).TIME_INFO.get(j).RESERVE_STATUS)) {
                                    reserve_info.add(new TimeContent.TimeItem(
                                            i,
                                            count++,
                                            voAgentDataItem.get(i).TIME_INFO.get(j).RESERVE_TIME,
                                            voAgentDataItem.get(i).TIME_INFO.get(j).RESERVE_STATUS,
                                            false
                                    ));
                                }
                            }
                        }
                    }
                }

                AgentContent.addItem(new AgentContent.AgentItem(
                        i,
                        voAgentDataItem.get(i).SEQ,
                        voAgentDataItem.get(i).NAME,
                        voAgentDataItem.get(i).AGENT_NAME,
                        voAgentDataItem.get(i).AGENT_IMG_URL,
                        voAgentDataItem.get(i).WASH_AREA,
                        voAgentDataItem.get(i).AGENT_STAUS,
                        voAgentDataItem.get(i).AGENT_ORDER,
                        //agentItem.TIME_INFO
                        reserve_info
                ));
            }

            int size = AgentContent.ITEMS.size();

            //프로그래스바 종료
            Util.dismiss();

            callAgentRecyclerViewAdapter();
        }

        @Override
        public void onResponseFailListener(VolleyError it) {
            //프로그래스바 종료
            Util.dismiss();
        }
    };

    /**
     *  내림차순 정렬
     */
    class AgentContentComparator implements Comparator<AgentDataItem> {
        @Override
        public int compare(AgentDataItem o1, AgentDataItem o2) {
            return o1.AGENT_ORDER.compareTo(o2.AGENT_ORDER);
        }
    }

//  List<AgentContent.AgentItem> ITEMS = new ArrayList<AgentContent.AgentItem>();

    private void callAgentRecyclerViewAdapter(){
        // 서버 연동 후 AgentContent.ITEMS에 리스 항목 추가 작업 확인
        // Set the adapter - 이용내역 리스트 설정
        if(AgentContent.ITEMS.size() > 0) {
            View view = findViewById(R.id.agentList);
            if (view instanceof RecyclerView) {
                Context context = view.getContext();
                RecyclerView recyclerView = (RecyclerView) view;

                LinearLayout view1 = findViewById(R.id.agent_history_empty);

                view.setVisibility(View.VISIBLE);
                view1.setVisibility(View.GONE);

                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                mAgentRecyclerViewAdapter = new AgentRecyclerViewAdapter(AgentContent.ITEMS, this, reContext);
                recyclerView.setAdapter(mAgentRecyclerViewAdapter);

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
                            if( Integer.parseInt(voAgentItem.TOTAL) % request_offset == 0 ) {
                                lastPageNum = (int)Math.floor(Integer.parseInt(voAgentItem.TOTAL)/request_offset);
                            }
                            else {
                                lastPageNum = (int)Math.floor(Integer.parseInt(voAgentItem.TOTAL)/request_offset) + 1;
                            }

                            if(lastPageNum > request_page)
                            {
                                //다음 페이지 요청
                                request_page+=1;
                                requestAgentList();
                            }
                        }

                    }
                });
            }
        }else{
            //대리점 정보 없을 때 화면 UI 추가
            View view = findViewById(R.id.agentList);
            view.setVisibility(View.GONE);

            LinearLayout view1 = findViewById(R.id.agent_history_empty);
            view1.setVisibility(View.VISIBLE);
 //           Toast.makeText(this, "대리점 정보가 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPrev_layout1:
                finish();
                break;
        }
    }

    @Override
    public void onAgentListInteraction(AgentItem agentItem) {
        Log.d("시간예약", "대리점주 id = " + mAgentRecyclerViewAdapter.getAgentPosition());
        Log.d("시간예약", "대리점주 시간1 = " + mAgentRecyclerViewAdapter.getTimePosition());
        //agentItem.id => seq
        Log.d("시간예약", "대리점주 시간2 = " +  mAgentRecyclerViewAdapter.getStatusPosition());

        if ("Y".equals(mAgentRecyclerViewAdapter.getStatusPosition())) {
            // 예약시간 설정 하고 예약 메인 화면으로 이동에 필요한 데이타 전송필요(Bundle)
            Intent intent = new Intent(this, ReservationMainActivity.class);

            Bundle bundle = new Bundle();
            bundle.putString("reserve_address", reserve_address);   //주소
            bundle.putString("reserve_year", reserve_year);         //년
            bundle.putString("reserve_month", reserve_month);       //월
            bundle.putString("reserve_day", reserve_day);           //일
            bundle.putString("agent_seq", mAgentRecyclerViewAdapter.getAgentSeq());
            bundle.putString("agent_company", String.valueOf(agentItem.branch_area));    //예약 대리점주 지역
            bundle.putString("agent_time", mAgentRecyclerViewAdapter.getTimePosition());    // 예약시간
            intent.putExtras(bundle);
            startActivity(intent);
            //finish();
        }else{
            Toast.makeText(this, "예약 완료된 시간입니다. 다른 시간을 선택해주세요", Toast.LENGTH_SHORT).show();
        }
    }


}

//    // 내림차순
//    class Descending implements Comparator<integer> {
//
//        @Override
//        public int compare(Integer o1, Integer o2) {
//            return o2.compareTo(o1);
//        }
//
//    }
//
//    // 오름차순
//    class Ascending implements Comparator<integer> {
//
//        @Override
//        public int compare(Integer o1, Integer o2) {
//            return o1.compareTo(o2);
//        }
//
//    }
