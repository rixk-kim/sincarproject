package com.sincar.customer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.sincar.customer.HWApplication.voLoginItem;

public class MyProfileSettingsRecomActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView recomTextView, chuTextView;
    private EditText chu_code;
    private String recom_code;
    private LinearLayout myinfo_linearLayout13;
    private LinearLayout myinfo_linearLayout14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo_recom);
        recom_code = voLoginItem.MEMBER_RECOM_CODE;
        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        findViewById(R.id.myinfo_recom_btnPrev).setOnClickListener(this);
        findViewById(R.id.recom_code_copy_btn).setOnClickListener(this);

        recomTextView = (TextView) findViewById(R.id.recom_code);
        recomTextView.setText(recom_code);
 //       findViewById(R.id.myinfo_btnNext).setOnClickListener(this);
        myinfo_linearLayout13 = (LinearLayout) findViewById(R.id.myinfo_linearLayout13);
        chuTextView = (TextView) findViewById(R.id.recom_code1);
        myinfo_linearLayout14 = (LinearLayout) findViewById(R.id.myinfo_linearLayout14);

        chu_code = (EditText) findViewById(R.id.recom_code14);
        findViewById(R.id.chu_code_btn).setOnClickListener(this);

        if(!TextUtils.isEmpty(voLoginItem.REGISTER_RECOM_CODE)) {
            myinfo_linearLayout13.setVisibility(View.VISIBLE);
            myinfo_linearLayout14.setVisibility(View.GONE);
            chuTextView.setText(voLoginItem.REGISTER_RECOM_CODE);
        }else{
            myinfo_linearLayout13.setVisibility(View.GONE);
            myinfo_linearLayout14.setVisibility(View.VISIBLE);
        }
        // myinfo_user_name => 이름
        // user_mobile_number => 휴대폰 번호

        // TODO - 서버 연동하여 이름, 휴대폰 번호 값 가지고 와서 설정해주기
//        TextView myinfo_user_name = findViewById(R.id.myinfo_user_name);
//        myinfo_user_name.setText("홍길동");
//
//        TextView user_mobile_number = findViewById(R.id.user_mobile_number);
//        user_mobile_number.setText("010-1234-5678");
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.myinfo_recom_btnPrev:
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
                if (chu_code != null || chu_code.getText().toString().trim().length() != 0) {
                    if (chu_code.getText().toString().trim().length() == 0) {
                        showAlert();
                        return;
                    }
                }

                //서버전송

                Toast.makeText(this, "전송완료", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 메세지
        String errorMsg = "코드를 입력해주세요";
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

