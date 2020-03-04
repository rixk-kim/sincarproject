package com.sincar.customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sincar.customer.network.DataObject;
import com.sincar.customer.network.JsonParser;
import com.sincar.customer.util.DataParser;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

public class NoticeActivity extends AppCompatActivity implements View.OnClickListener {
    /*
     * 리스트 뷰 관련 변수 선언
     */
    private ListView listView;                      // 리스트뷰
    private boolean lastItemVisibleFlag = false;    // 리스트 스크롤이 마지막 셀(맨 바닥)로 이동했는지 체크할 변수
    //private List<String> list;                      // String 데이터를 담고있는 리스트
//    private ArrayList<ItemData> list;
//    private ListViewAdapter adapter;                // 리스트뷰의 아답터
    private int page = 0;                           // 페이징변수. 초기 값은 0 이다.
    private final int OFFSET = 20;                  // 한 페이지마다 로드할 데이터 갯수.
    //    private ProgressBar progressBar;                // 데이터 로딩중을 표시할 프로그레스바
    private boolean mLockListView = false;          // 데이터 불러올때 중복안되게 하기위한 변수

    private int request_page = 1;                   // 서버 요청 페이지

    private ArrayList<DataObject> final_item;

    private FreightListAdapter adfreight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        findViewById(R.id.notice_btnPrev).setOnClickListener(this);
        //       findViewById(R.id.myinfo_btnNext).setOnClickListener(this);

        listView = (ListView) findViewById(R.id.listview);


        // myinfo_user_name => 이름
        // user_mobile_number => 휴대폰 번호

        // TODO - 서버 연동하여 이름, 휴대폰 번호 값 가지고 와서 설정해주기
//        TextView myinfo_user_name = findViewById(R.id.myinfo_user_name);
//        myinfo_user_name.setText("홍길동");
//
//        TextView user_mobile_number = findViewById(R.id.user_mobile_number);
//        user_mobile_number.setText("010-1234-5678");

        //test
        reponseData("test");
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.notice_btnPrev:
                //  TODO - 내정보
                intent = new Intent(this, MyProfileSettingsActivity.class);
                startActivity(intent);
                finish();
                break;
//            case R.id.myinfo_btnNext:
//                //  TODO - 비밀번호변경
//                intent = new Intent(this, PasswordChangeActivity.class);
//                startActivity(intent);
//                finish();
//                break;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, MyProfileSettingsActivity.class);
        startActivity(intent);

        finish();
    }

    /**
     * 공지사항 리스트 요청
     */
    private void requestFreightList() {
        try {
//            NetWorkController mNetworkController = new NetWorkController(Constants.FREIGHT_LIST_REQUEST, MainActivity.this);
//            HashMap<String, String> postParams = new HashMap<String, String>();
//            // 지역명
//            postParams.put("area", "1");
//            // 회원번호
//            //postParams.put("spno", "123456789");
//            // 요청 페이지
//            System.out.println("[spirit] 요청 페이지 => " + String.valueOf(request_page));
//            postParams.put("page", String.valueOf(request_page));
//            // 요청 갯수
//            postParams.put("LinePerPage", "20");

            // 리스트에 다음 데이터를 입력할 동안에 이 메소드가 또 호출되지 않도록 mLockListView 를 true로 설정한다.
            mLockListView = true;

//            mNetworkController.setHttpConnectionListner(this);
//            mNetworkController.sendParams(postParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reponseData(String result)
    {
        result = "{\"freight_info\": [{\"seq\":\"1\",\"화물번호\":\"test11\",\"상차지\":\"test12\"},{\"seq\":\"2\",\"화물번호\":\"test21\",\"상차지\":\"test22\"}]}";
        ArrayList<DataObject> data = JsonParser.getData(result);

        final_item = DataParser.getFromParamtoArray(data, "freight_info");

        //final DataObject item = DataParser.getFromParamtoItem(data, "freight_info");
        Gson gson = new Gson();

        //}else{
        adfreight = new FreightListAdapter(NoticeActivity.this, 0, final_item, listView.getCount());
        //}

        if (adfreight.isEmpty()) {
            Toast.makeText(this, "adpater is null.....2 ", Toast.LENGTH_SHORT).show();
        }

        listView.setAdapter(adfreight);

        //데이터를 갱신하고, 중복 로딩 체크하는 Lock을 했던 mLockListView변수를 풀어준다.
        mLockListView = false;


        adfreight.notifyDataSetChanged();
    }

    public class FreightListAdapter extends ArrayAdapter {

        private Context mContext;
        //private ArrayList<DataObject> items;
        private ArrayList<DataObject> items;
        private ViewHolder holder;
        private LayoutInflater inflate;
        private int list_size = 0;

        public FreightListAdapter(Context context, int resource, ArrayList<DataObject> items, int count) {
            super(context, resource, items);
            this.mContext = context;
            this.items = items;
            this.inflate = LayoutInflater.from(context);
            this.list_size = count;
            //this.items.
            //System.out.println("[spirit] items => " + listView.getCount());
            //listView.getCount();
        }

        @Override
        public DataObject getItem(int position) {
            return items.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View layout = convertView;
            final int pos = position ;

            System.out.println("[spirit]pos   ===> " +  pos);

            if (layout == null) {
                layout = inflate.inflate(R.layout.listview_item,null);

                holder = new ViewHolder();
                holder.title = (TextView) layout.findViewById(R.id.textTitle);
                holder.date = (TextView) layout.findViewById(R.id.textDate);
                holder.select_button = (Button) layout.findViewById(R.id.select_list);
                layout.setTag(holder);
            } else {
                holder = (ViewHolder) layout.getTag();
            }

            if (items != null) {
                DataObject item = getItem(position);
                // 지점 코드
                //holder.store_cd_tv.setText(item.getValue("Scode"));
                // 지점 명
                //holder.store_nm_tv.setText(item.getValue("name"));
                // 지점 구분 명 등등 (유동적)
                // 타입으로 명칭 호출
                //holder.receive_data_date_tv.setText(typeName(item.getValue("type")));


                holder.title.setText(item.getValue("SEQ"));
                holder.date.setText(item.getValue("sang_addr"));
            }

            if (position % 2 == 0) {
                layout.setBackgroundResource(android.R.color.white);
            } else {
                layout.setBackgroundResource(R.color.color_table_1);
            }



            holder.title.setTextColor(ContextCompat.getColor(mContext, R.color.default_text_color));
            holder.date.setTextColor(ContextCompat.getColor(mContext, R.color.default_text_color));
            holder.select_button.setTextColor(ContextCompat.getColor(mContext, R.color.default_text_color));

/*
            final Button select_btn = (Button) layout.findViewById(R.id.select_list);
            select_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        //상세보기로 이동
                        //Toast.makeText(this, position + "", Toast.LENGTH_LONG).show();
                        System.out.println("[spirit]"+pos+"번째 선택.");
                        System.out.println("[spirit] SEQ : "+items.get(pos).getValue("SEQ"));

//                        Intent intent = new Intent(mContext, FreightDetail.class);
//                        intent.putExtra("SEQ", items.get(pos).getValue("SEQ"));
//                        mContext.startActivity(intent);

                        //startDetail(v.getId());



                        //test_view((int)v.getTag());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
*/
            return layout;
        }
    }

    private class ViewHolder {

        // 지점 코드, 지점명, 마스터 수신일
        TextView title, date, receive_data_date_tv;
        public Button select_button;

    }
}


