package com.sincar.customer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class CarRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Context cContext;
    private TextView car_select_colume1;
    private TextView car_select_colume2;
    private String car_reg_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_register);
        cContext = this;

        Intent intent = getIntent(); /*데이터 수신*/
        car_reg_path    = intent.getExtras().getString("path");    /*String형*/

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    @SuppressLint("ResourceAsColor")
    private void init() {
        findViewById(R.id.car_reg_btnPrev).setOnClickListener(this);    //이전
        findViewById(R.id.btnNext1).setOnClickListener(this);           //제조사 선택
        findViewById(R.id.btnNext2).setOnClickListener(this);           //차량 선택
        findViewById(R.id.car_reg_btn).setOnClickListener(this);        //확인



        // myinfo_user_name => 이름
        // user_mobile_number => 휴대폰 번호

        // TODO - 서버 연동하여 이름, 휴대폰 번호 값 가지고 와서 설정해주기
        car_select_colume1 = findViewById(R.id.car_select_colume1);
//        car_select_colume1.setText("홍길동");
//
        car_select_colume2 = findViewById(R.id.car_select_colume2);
//        user_mobile_number.setText("010-1234-5678");

    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.car_reg_btnPrev:
                //  TODO - 내정보
                intent = new Intent(this, CarManageActivity.class);
                intent.putExtra("path", car_reg_path);
                startActivity(intent);
                finish();
                break;

            case R.id.btnNext1:
                //  TODO - 제조사 선택. 서버 요청 후 응답 받으면 setCompanyDialog() 호출.
                setCompanyDialog();
                break;

            case R.id.btnNext2:
                //  TODO - 차량 선택. 서버 요청 후 응답 받으면 setCarDialog() 호출.
                setCarDialog();
                break;

            case R.id.car_reg_btn:
                //  TODO - 차량 등록
                Toast.makeText(this, "차량이 등록 되었습니다.", Toast.LENGTH_SHORT).show();

                //  TODO - 차량 등록 후 리스트 이동
                intent = new Intent(this, CarManageActivity.class);
                intent.putExtra("path", car_reg_path);
                startActivity(intent);
                finish();
                break;

        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, CarManageActivity.class);
        intent.putExtra("path", car_reg_path);
        startActivity(intent);

        finish();
    }

    /**
     * 제조사 선택 Dialog
     */
    private void setCompanyDialog()
    {
        final String company_name[] = {
                "1.현대","기아","포드","르노삼성","한국지엠","쌍용","캐딜락","쉐보레","GMC","허머",
                "올즈모빌","폰티악","새턴","포드","링컨","머큐리","크라이슬러","닷지","지프","헤네시",
                "테슬라 모터스","BMW","마이바흐","벤츠","스마트","폭스바겐","아우디","굼바트","피아트","마이바흐",
                "페라리","람보르기니","알파로메오","마세라티","란치아","파가니","르노","링컨","머큐리","크라이슬러",
                "닷지","지프","헤네시","테슬라 모터스","BMW","마이바흐","벤츠","현대","기아","벤츠",
                "포드","르노삼성","한국지엠","쌍용","캐딜락","쉐보레","GMC","허머","올즈모빌","폰티악",
                "새턴","포드","링컨","머큐리","크라이슬러","닷지","지프","헤네시","테슬라 모터스","70.BMW"

        };

        LayoutInflater inflater = (LayoutInflater) cContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        //R.layout.dialog는 xml 파일명이고  R.id.popup은 보여줄 레이아웃 아이디
        View layout = inflater.inflate(R.layout.select_company_dialog,(ViewGroup) findViewById(R.id.popup));
        AlertDialog.Builder aDialog = new AlertDialog.Builder(cContext);

        // 커스텀 아답타 생성
        final CompanyAdapter adapter = new CompanyAdapter (
                getApplicationContext(),
                R.layout.company_row,       // GridView 항목의 레이아웃 row.xml
                company_name);    // 데이터

        GridView gv = (GridView)layout.findViewById(R.id.gridView1);
        gv.setAdapter(adapter);  // 커스텀 아답타를 GridView 에 적용

        aDialog.setView(layout); //dialog.xml 파일을 뷰로 셋팅

        //팝업창 생성
        final AlertDialog ad = aDialog.create();

        // GridView 아이템을 클릭하면 상단 텍스트뷰에 position 출력
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //tv.setText("position : " + position);
                if(car_select_colume1 != null) {
                    car_select_colume1.setText(company_name[position]);
                }
                Toast.makeText(cContext, "position : " + adapter.getCount(), Toast.LENGTH_SHORT).show();
                ad.dismiss();
            }
        });

        ad.show();//보여줌!
    }

    class CompanyAdapter extends BaseAdapter {
        Context context;
        int layout;
        String car_company[];
        LayoutInflater inf;

        public CompanyAdapter(Context context, int layout, String[] car_company) {
            this.context = context;
            this.layout = layout;
            this.car_company = car_company;
            inf = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return car_company.length;
        }

        @Override
        public Object getItem(int position) {
            return car_company[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null)
                convertView = inf.inflate(layout, null);
            TextView iv = (TextView)convertView.findViewById(R.id.textCompanyView1);
            iv.setText(car_company[position]);

            return convertView;
        }
    }

    /**
     * 차량 선택
     */
    /**
     * 제조사 선택 Dialog
     */
    private void setCarDialog()
    {
        final String company_name[] = {
                "1.코롤라","F-시리즈","아반떼","골프","로간","X-트레일","폴로","투싼","센트라","HR-V",
                "11.코롤라","F-시리즈","아반떼","골프","로간","X-트레일","폴로","투싼","센트라","HR-V",
                "21.코롤라","F-시리즈","아반떼","골프","로간","X-트레일","폴로","투싼","센트라","HR-V",
                "31.코롤라","F-시리즈","아반떼","골프","로간","X-트레일","폴로","투싼","센트라","HR-V",
                "41.코롤라","F-시리즈","아반떼","골프","로간","X-트레일","폴로","투싼","센트라","HR-V",
                "51.코롤라","F-시리즈","아반떼","골프","로간","X-트레일","폴로","투싼","센트라","HR-V",
                "61.코롤라","F-시리즈","아반떼","골프","로간","X-트레일","폴로","투싼","센트라","70.HR-V",

        };

        LayoutInflater inflater = (LayoutInflater) cContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        //R.layout.dialog는 xml 파일명이고  R.id.popup은 보여줄 레이아웃 아이디
        View layout = inflater.inflate(R.layout.select_car_dialog,(ViewGroup) findViewById(R.id.car_popup));
        AlertDialog.Builder aDialog = new AlertDialog.Builder(cContext);

        // 커스텀 아답타 생성
        final CarAdapter adapter = new CarAdapter (
                getApplicationContext(),
                R.layout.car_row,       // GridView 항목의 레이아웃 row.xml
                company_name);    // 데이터

        GridView gv = (GridView)layout.findViewById(R.id.car_gridView1);
        gv.setAdapter(adapter);  // 커스텀 아답타를 GridView 에 적용

        aDialog.setView(layout); //dialog.xml 파일을 뷰로 셋팅
        //팝업창 생성
        final AlertDialog ad = aDialog.create();

        // GridView 아이템을 클릭하면 상단 텍스트뷰에 position 출력
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //tv.setText("position : " + position);
                if(car_select_colume2 != null) {
                    car_select_colume2.setText(company_name[position]);
                }
                Toast.makeText(cContext, "position : " + adapter.getCount(), Toast.LENGTH_SHORT).show();

                ad.dismiss();;
            }
        });
        ad.show();//보여줌!

    }

    class CarAdapter extends BaseAdapter {
        Context context;
        int layout;
        String car_name[];
        LayoutInflater inf;

        public CarAdapter(Context context, int layout, String[] car_name) {
            this.context = context;
            this.layout = layout;
            this.car_name = car_name;
            inf = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return car_name.length;
        }

        @Override
        public Object getItem(int position) {
            return car_name[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null)
                convertView = inf.inflate(layout, null);
            TextView iv = (TextView)convertView.findViewById(R.id.textCarView1);
            iv.setText(car_name[position]);

            return convertView;
        }
    }
}


