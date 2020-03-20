package com.sincar.customer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sincar.customer.adapter.CarContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.CarContent;
import com.sincar.customer.item.CarRegisterResult;
import com.sincar.customer.item.CarResult;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.sincar.customer.HWApplication.carRegisterResult;
import static com.sincar.customer.HWApplication.carResult;
import static com.sincar.customer.HWApplication.voCarDataItem;
import static com.sincar.customer.HWApplication.voCarItem;
import static com.sincar.customer.HWApplication.voCarListDataItem;
import static com.sincar.customer.HWApplication.voCarRegisterItem;
import static com.sincar.customer.HWApplication.voCompanyListDataItem;
import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.common.Constants.LOGIN_REQUEST;

public class CarRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Context cContext;
    private TextView car_select_colume1;
    private TextView car_select_colume2;
    private EditText car_select_colume3;
    private String car_wash_pay;
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

        // 서버 연동하여 제조사, 모델명, 차량 번호 값 가지고 와서 설정해주기.
        car_select_colume1 = (TextView) findViewById(R.id.car_select_colume1);
        car_select_colume2 = (TextView) findViewById(R.id.car_select_colume2);
        car_select_colume3 = (EditText) findViewById(R.id.car_select_colume3);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.car_reg_btnPrev:
                //  내정보
                intent = new Intent(this, CarManageActivity.class);
                intent.putExtra("path", car_reg_path);
                startActivity(intent);
                finish();
                break;

            case R.id.btnNext1:
                //  제조사 선택. 서버 요청 후 응답 받으면 setCompanyDialog() 호출.
                setCompanyDialog();
                break;

            case R.id.btnNext2:
                //  차량 선택. 서버 요청 후 응답 받으면 setCarDialog() 호출.
                setCarDialog();
                break;

            case R.id.car_reg_btn:
                //  TODO - 차량 등록 후 결과값으로 차량 기본 세차비용 받어오자.
                //requestCarRegister();
                Toast.makeText(this, "차량이 등록 되었습니다.", Toast.LENGTH_SHORT).show();

                //  TODO - 차량 등록 후 리스트 이동
                if ("reserveMain".equals(car_reg_path))
                {
                    //reserve_carname//car_select_colume1 : 회사이름
                    intent = new Intent(this, ReservationMainActivity.class);
                    intent.putExtra("reserve_companyname", car_select_colume1.getText().toString());
                    intent.putExtra("reserve_carname", car_select_colume2.getText().toString());
                    intent.putExtra("reserve_carnumber", car_select_colume3.getText().toString());
                    intent.putExtra("car_wash_pay", "50000");
                    setResult(RESULT_OK, intent);
                    //startActivity(intent);
                    finish();
                }else {
                    intent = new Intent(this, CarManageActivity.class);
                    intent.putExtra("path", car_reg_path);
                    startActivity(intent);
                }
                finish();
                break;

        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent;
        if ("reserveMain".equals(car_reg_path))
        {
            //reserve_carname//car_select_colume1 : 회사이름
            intent = new Intent(this, ReservationMainActivity.class);
            intent.putExtra("reserve_companyname", car_select_colume1.getText().toString());
            intent.putExtra("reserve_carname", car_select_colume2.getText().toString());
            intent.putExtra("reserve_carnumber", car_select_colume3.getText().toString());
            intent.putExtra("car_wash_pay", "50000");
            setResult(RESULT_OK, intent);
            //startActivity(intent);
            finish();
        }else {

            intent = new Intent(this, CarManageActivity.class);
            intent.putExtra("path", car_reg_path);
            startActivity(intent);
        }
        finish();
    }

    /**
     * 차량 서버 등록
     * MEMBER_NO     : 회원번호
     * CAR_COMPANY   : 제조사
     * CAR_MODEL     : 모델명
     * CAR_NUMBER    : 차량번호
     */
    private void requestCarRegister() {
        if (car_select_colume1 != null || car_select_colume1.getText().toString().trim().length() != 0) {
            if (car_select_colume1.getText().toString().trim().length() == 0) {
                showCarErrorAlert("1");
                return;
            }
        }

        if (car_select_colume2 != null || car_select_colume2.getText().toString().trim().length() != 0) {
            if (car_select_colume2.getText().toString().trim().length() == 0) {
                showCarErrorAlert("2");
                return;
            }
        }

        if (car_select_colume3 != null || car_select_colume3.getText().toString().trim().length() != 0) {
            if (car_select_colume3.getText().toString().trim().length() == 0) {
                showCarErrorAlert("3");
                return;
            }
        }

        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("MEMBER_NO", voLoginItem.MEMBER_NO);             // 회원번호
        postParams.put("CAR_COMPANY", voLoginItem.MEMBER_PHONE);        // 제조사
        postParams.put("CAR_MODEL", "1");                               // 모델명
        postParams.put("CAR_NUMBER", "20");                             // 차량번호

        //프로그래스바 시작
        Util.showDialog();
        //사용내역 요청
        VolleyNetwork.getInstance(this).passwordChangeRequest(LOGIN_REQUEST, postParams, onCarRegisterResponseListener);
    }

    /**
     * 에러 팝업
     * 1~ 2 서버 통신 후 에러 메세지
     * 3 ~5 내부 유효성 검사 메세지
     * @param resultCode : 내용 코드
     */
    private void showCarErrorAlert(String resultCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(cContext);
        // 메세지
        String errorMsg = "";
        if (resultCode.equals("1")) {
            errorMsg = getString(R.string.input_company_name_msg);
        } else if (resultCode.equals("2")) {
            errorMsg = getString(R.string.input_car_name_msg);
        } else if (resultCode.equals("3")) {
            errorMsg = getString(R.string.input_car_number_msg);
        }

        builder.setTitle(getString(R.string.notice));
        builder.setMessage(errorMsg);
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        }).show();
    }

    VolleyNetwork.OnResponseListener onCarRegisterResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String serverData) {
            /*
                 {"car_register": [{"PROFILE_RESULT":"0","CAUSE":"","CAR_PAY":"55000"}]}
             */

            Gson gSon = new Gson();
            carRegisterResult = gSon.fromJson(serverData, CarRegisterResult.class);

            voCarRegisterItem.REGISTERRESULT    = carRegisterResult.car_register.get(0).REGISTERRESULT;
            voCarRegisterItem.CAUSE             = carRegisterResult.car_register.get(0).CAUSE;
            voCarRegisterItem.CAR_PAY           = carRegisterResult.car_register.get(0).CAR_PAY;

            //프로그래스바 종료
            Util.dismiss();



            if("0".equals(voCarRegisterItem.REGISTERRESULT)) {
                Toast.makeText(cContext, "차량이 등록 되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent;
                if ("reserveMain".equals(car_reg_path)) {
                    //reserve_carname//car_select_colume1 : 회사이름
                    intent = new Intent(cContext, ReservationMainActivity.class);
                    intent.putExtra("reserve_companyname", car_select_colume1.getText().toString());
                    intent.putExtra("reserve_carname", car_select_colume2.getText().toString());
                    intent.putExtra("reserve_carnumber", car_select_colume3.getText().toString());
                    intent.putExtra("car_wash_pay", voCarRegisterItem.CAR_PAY);
                    setResult(RESULT_OK, intent);
                    //startActivity(intent);
                    finish();
                } else {
                    intent = new Intent(cContext, CarManageActivity.class);
                    intent.putExtra("path", car_reg_path);
                    startActivity(intent);
                }
                finish();
            }else{
                Toast.makeText(cContext, "차량 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        @Override
        public void onResponseFailListener(VolleyError it) {
            Toast.makeText(cContext, "차량 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
    };

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
        /**
         * 서버 연동 후  아래 코드로 변경
         */
/*
        String[] company_name_1 = new String[voCompanyListDataItem.size()];
        String[] company_code = new String[voCompanyListDataItem.size()];
        for(int i = 0; i < voCompanyListDataItem.size(); i++)
        {
            company_name_1[i] = voCompanyListDataItem.get(0).CAR_COMPANY;
            company_code[i] = voCompanyListDataItem.get(0).CAR_COMPANY;
        }

        final CompanyAdapter adapter = new CompanyAdapter (
                getApplicationContext(),
                R.layout.company_row,       // GridView 항목의 레이아웃 row.xml
                company_name_1);    // 데이터
*/
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
/**
 * 서버 연동 후  아래 코드로 변경
 */
/*
        String[] car_company_code = new String[voCarListDataItem.size()];
        String[] car_name = new String[voCarListDataItem.size()];
        String[] car_code = new String[voCarListDataItem.size()];
        String[] car_wash_pay = new String[voCompanyListDataItem.size()];
        for(int i = 0; i < voCompanyListDataItem.size(); i++)
        {
            car_company_code[i] = voCarListDataItem.get(0).CAR_COMPANY_CODE;
            car_name[i]         = voCarListDataItem.get(0).CAR_NAME;
            car_code[i]         = voCarListDataItem.get(0).CAR_CODE;
            car_wash_pay[i]     = voCarListDataItem.get(0).CAR_WASH_PAY;
        }

        final CarAdapter adapter = new CarAdapter (
                getApplicationContext(),
                R.layout.car_row,       // GridView 항목의 레이아웃 row.xml
                car_name);    // 데이터
*/

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


