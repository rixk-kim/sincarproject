package com.sincar.customer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sincar.customer.adapter.CouponeContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.CouponeContent;
import com.sincar.customer.network.DataObject;
import com.sincar.customer.network.JsonParser;
import com.sincar.customer.util.DataParser;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mContext = this;
        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        findViewById(R.id.setting_btnPrev).setOnClickListener(this);

        //약관 및 정책
        findViewById(R.id.setting_clause).setOnClickListener(this);
        //로그아웃
        findViewById(R.id.member_logout).setOnClickListener(this);
        //회원탈퇴
        findViewById(R.id.member_withdrawal).setOnClickListener(this);

        // TODO - 활성화 유무에 따라 서버 연동 항목 추가 작업
        //정보성 알림
        Switch mSwitch1 = findViewById(R.id.infoSwitch1);
        mSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
                if (isChecked){
                    Toast.makeText(mContext, "활성화", Toast.LENGTH_SHORT);
                }else{
                    Toast.makeText(mContext, "비활성화", Toast.LENGTH_SHORT);
                }
            }
        });

        //마켓팅 알림
        Switch mSwitch2 = findViewById(R.id.infoSwitch2);
        mSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
                if (isChecked){
                    Toast.makeText(mContext, "활성화", Toast.LENGTH_SHORT);
                }else{
                    Toast.makeText(mContext, "비활성화", Toast.LENGTH_SHORT);
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.setting_btnPrev:
                //  TODO - 내정보
                intent = new Intent(this, MyProfileSettingsActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.setting_clause:
                // TODO - 약관 및 정책
                Toast.makeText(this, "약관 및 정책으로 이동", Toast.LENGTH_SHORT);

                intent = new Intent(this, MyProfileSettingsActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.member_logout:
                // TODO - 로그아웃
                showLogoutAlertDialog(this);
                //Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT);
                break;

            case R.id.member_withdrawal:
                // TODO - 회원탈퇴
                Toast.makeText(this, "회원 탈퇴 되었습니다.", Toast.LENGTH_SHORT);
                break;
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
     * 로그아웃 알럿 다이얼로그
     * @param context
     */
    private void showLogoutAlertDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle(context.getString(R.string.notice));
        builder.setMessage("로그아웃 하시겠습니까?");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(mContext, LoginActivityPre.class);
                startActivity(intent);

                finish();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

}


