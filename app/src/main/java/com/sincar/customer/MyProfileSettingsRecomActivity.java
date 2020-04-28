package com.sincar.customer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sincar.customer.item.RecommandResult;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.Util;

import java.util.HashMap;

import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.HWApplication.voRecommandItem;
import static com.sincar.customer.HWApplication.recommandResult;
import static com.sincar.customer.common.Constants.RECOMMEND_REGISTER_REQUEST;

public class MyProfileSettingsRecomActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView recomTextView, chuTextView, myinfo_visit_01, myinfo_visit_02;
    private EditText chu_code;
    private String recom_code;
    private LinearLayout myinfo_linearLayout13;
    private LinearLayout myinfo_linearLayout14;
    private Context rContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo_recom);

        rContext = this;
        recom_code = voLoginItem.MEMBER_RECOM_CODE;
        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        findViewById(R.id.myinfo_recom_btnPrev_layout).setOnClickListener(this);
        findViewById(R.id.recom_code_copy_btn).setOnClickListener(this);
        findViewById(R.id.myinfo_linearLayout11).setOnClickListener(this);


        recomTextView = (TextView) findViewById(R.id.recom_code);
        recomTextView.setText(recom_code);
 //       findViewById(R.id.myinfo_btnNext).setOnClickListener(this);
        myinfo_linearLayout13 = (LinearLayout) findViewById(R.id.myinfo_linearLayout13);
        chuTextView = (TextView) findViewById(R.id.recom_code1);
        myinfo_linearLayout14 = (LinearLayout) findViewById(R.id.myinfo_linearLayout14);

        chu_code = (EditText) findViewById(R.id.recom_code14);
        findViewById(R.id.chu_code_btn).setOnClickListener(this);


        myinfo_visit_01 = (TextView) findViewById(R.id.myinfo_visit_01);
        SpannableStringBuilder ssb = new SpannableStringBuilder(myinfo_visit_01.getText()); //내가 초대한 친구가 서비스\n이용할 때 마다 포인트
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#ff4b5b")), 15, 24, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 15, 24,Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        myinfo_visit_01.setText(ssb);

        myinfo_visit_02 = (TextView) findViewById(R.id.myinfo_visit_02);
        SpannableStringBuilder ssb2 = new SpannableStringBuilder(myinfo_visit_02.getText()); //추천 코드를 등록하면 즉시 이용\n가능한 5천원 할인쿠폰 발급!
        ssb2.setSpan(new ForegroundColorSpan(Color.parseColor("#ff4b5b")), 22, 25, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb2.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 22, 25,Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        myinfo_visit_02.setText(ssb2);


        if(!TextUtils.isEmpty(voLoginItem.REGISTER_RECOM_CODE)) {
            myinfo_linearLayout13.setVisibility(View.VISIBLE);
            myinfo_linearLayout14.setVisibility(View.GONE);
            chuTextView.setText(voLoginItem.REGISTER_RECOM_CODE);
        }else{
            myinfo_linearLayout13.setVisibility(View.GONE);
            myinfo_linearLayout14.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.myinfo_recom_btnPrev_layout:
                //  TODO - 내정보
                intent = new Intent(this, MyProfileSettingsActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.recom_code_copy_btn:
                //  TODO - 복사하기
                ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("추천코드", recom_code);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(this, "복사완료", Toast.LENGTH_LONG).show();

                break;

            case R.id.chu_code_btn:
                //  TODO - 추천 코드 전송하기
                if(chu_code == null || TextUtils.isEmpty(chu_code.getText().toString().trim()))
                {
                    showAlert();
                    return;
                }

//                if (chu_code != null || chu_code.getText().toString().trim().length() != 0) {
//                    if (chu_code.getText().toString().trim().length() != 6) {
//                        showAlert();
//                        return;
//                    }
//                }

                //서버전송
                requestRecomRegister();
 //               Toast.makeText(this, "전송완료", Toast.LENGTH_LONG).show();
                break;

            case R.id.myinfo_linearLayout11:
                //포인트의 info   => PointInfoActivity
                intent = new Intent(this, PointInfoActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 추천인코드 등록 요청
     * MEMBER_NO        : 회원번호
     * RECOM_CODE       : 추천인코드
     */
    private void requestRecomRegister() {
        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("MEMBER_NO", voLoginItem.MEMBER_NO);                     // 회원번호
        postParams.put("RECOM_CODE", chu_code.getText().toString().trim());     // 추천인코드

        //프로그래스바 시작
        Util.showDialog(this);
        //추천인코드 등록 요청
        VolleyNetwork.getInstance(this).serverDataRequest(RECOMMEND_REGISTER_REQUEST, postParams, onRecomRegisterResponseListener);
    }

    VolleyNetwork.OnResponseListener onRecomRegisterResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String it) {
            Gson gSon = new Gson();
            //LoginResult loginResult = gSon.fromJson(it, LoginResult.class);
            recommandResult = gSon.fromJson(it, RecommandResult.class);

            voRecommandItem.RECOMMEND_RESULT    = recommandResult.recommend.RECOMMEND_RESULT;
            voRecommandItem.CAUSE               = recommandResult.recommend.CAUSE;

            //프로그래스바 종료
            Util.dismiss();

            if("0".equals(voRecommandItem.RECOMMEND_RESULT)) {
                voLoginItem.REGISTER_RECOM_CODE = chu_code.getText().toString().trim();

                myinfo_linearLayout13.setVisibility(View.VISIBLE);
                myinfo_linearLayout14.setVisibility(View.GONE);
                chuTextView.setText(voLoginItem.REGISTER_RECOM_CODE);

                Toast.makeText(rContext, "추천인 코드 등록 되었습니다.", Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(rContext, "추천인 코드 등록 실패하였습니다. 재시도 해주세요.", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onResponseFailListener(VolleyError it) {
            //프로그래스바 종료
            Util.dismiss();

            Toast.makeText(rContext, "추천인 코드 등록 실패하였습니다. 재시도 해주세요.", Toast.LENGTH_LONG).show();
        }
    };

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 메세지
        String errorMsg = "코드 6자리를 입력해주세요";
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

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, MyProfileSettingsActivity.class);
        startActivity(intent);

        finish();
    }
}

