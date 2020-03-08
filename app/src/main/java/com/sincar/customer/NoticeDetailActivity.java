package com.sincar.customer;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sincar.customer.adapter.NoticeContentRecyclerViewAdapter;
import com.sincar.customer.adapter.content.NoticeContent;
import com.sincar.customer.network.DataObject;
import com.sincar.customer.network.JsonParser;
import com.sincar.customer.util.DataParser;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

public class NoticeDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private String noti_title;
    private String noti_date;
    private String noti_contents;

    private TextView nTitle;
    private TextView nDate;
    private TextView nContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);

        //이전 Activity 종료.
        NoticeActivity noticeActivity = (NoticeActivity)NoticeActivity._noticeActivity;
        noticeActivity.finish();

        Intent intent = getIntent(); /*데이터 수신*/
        noti_title    = intent.getExtras().getString("title");    /*String형*/
        noti_date     = intent.getExtras().getString("date");     /*String형*/
        noti_contents = intent.getExtras().getString("contents"); /*String형*/


        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        findViewById(R.id.notice_detail_btnPrev).setOnClickListener(this);
        //       findViewById(R.id.myinfo_btnNext).setOnClickListener(this);
        nTitle = (TextView) findViewById(R.id.notice_title);
        nTitle.setText(noti_title);

        nDate = (TextView) findViewById(R.id.notice_date);
        nDate.setText(noti_date);

        nContents = (TextView) findViewById(R.id.notice_contents);
        nContents.setText(noti_contents);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.notice_detail_btnPrev:
                //  TODO - 내정보
                intent = new Intent(this, NoticeActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, NoticeActivity.class);
        startActivity(intent);

        finish();
    }


}


