package com.sincar.customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sincar.customer.adapter.MainBannerSliderAdapter;
import com.sincar.customer.service.PicassoImageLoadingService;
import com.sincar.customer.util.Util;

import java.util.ArrayList;

import static com.sincar.customer.HWApplication.voAdvertiseItem;
import static com.sincar.customer.HWApplication.voLoginItem;

import ss.com.bannerslider.Slider;
import ss.com.bannerslider.event.OnSlideClickListener;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    private MenuItem prevBottomNavigation;
    private ConstraintLayout mConstraintLayout;
    private TextView mCustomerName, mCustomerPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    @SuppressLint("NewApi")
    private void init() {
        findViewById(R.id.btnCustomerPoint).setOnClickListener(this);
        findViewById(R.id.btnMainMenu1).setOnClickListener(this);
        findViewById(R.id.btnMainMenu2).setOnClickListener(this);
        findViewById(R.id.btnMainMenu3).setOnClickListener(this);
        findViewById(R.id.btnMainMenu4).setOnClickListener(this);
        findViewById(R.id.btnCarRegisterClose).setOnClickListener(this);
        findViewById(R.id.btnCarRegister).setOnClickListener(this);

        mCustomerName = (TextView) findViewById(R.id.customerName);
        mCustomerName.setText(voLoginItem.MEMBER_NAME + "님의 포인트");

        mCustomerPoint = (TextView) findViewById(R.id.customerPoint);
        mCustomerPoint.setText(Util.setAddMoneyDot(voLoginItem.MY_POINT));

        mConstraintLayout = (ConstraintLayout) findViewById(R.id.layout_car_register);

        // image slider setting
        PicassoImageLoadingService picassoService = new PicassoImageLoadingService();
        Slider.init(picassoService);
        Slider slider = findViewById(R.id.banner_slider);

        ArrayList<String> voImageUrls = new ArrayList<String>();
        for(int i = 0; i < voAdvertiseItem.size(); i++)
        {
            voImageUrls.add(i, voAdvertiseItem.get(i).AD_IMAGE_URL);
        }

        slider.setAdapter(new MainBannerSliderAdapter(voImageUrls));
        slider.setOnSlideClickListener(new OnSlideClickListener() {
            @Override
            public void onSlideClick(int position) {
                // Banner 선택 시 필요한 동작 추가
                // voAdvertiseItem.get(i).AD_LINK_URL ==> 웹뷰 띄워주자.
                Intent i = new Intent(Intent.ACTION_VIEW);
                Uri u = Uri.parse(voAdvertiseItem.get(position).AD_LINK_URL);
                i.setData(u);
                startActivity(i);

                Toast.makeText(getApplicationContext(), "Banner selected is " + voAdvertiseItem.get(position).AD_LINK_URL, Toast.LENGTH_LONG).show();
            }
        });

        //하단메뉴 고정(0:홈)
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        prevBottomNavigation = bottomNavigationView.getMenu().getItem(0);
        prevBottomNavigation.setChecked(true);

        // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.bottom_home: {
                        // HOME
                        break;
                    }
                    case R.id.bottom_use_history: {
                        intent = new Intent(mContext, UseHistoryActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                    case R.id.bottom_myinfo: {
                        intent = new Intent(mContext, MyProfileSettingsActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }

                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.btnCustomerPoint:
                // TODO - 포인트 보기
                intent = new Intent(this, PointHistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.btnMainMenu1:
                // TODO - 스팀세차
                intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                break;
            case R.id.btnMainMenu2:
                // TODO - 대리운전
                break;
            case R.id.btnMainMenu3:
                // TODO - 카페어링
                break;
            case R.id.btnMainMenu4:
                // TODO - 카쉐어
                break;
            case R.id.btnCarRegisterClose:
                // 배너 차량등록 종료
                mConstraintLayout.setVisibility(View.GONE);
                break;
            case R.id.btnCarRegister:
                // 배너 차량등록
                intent = new Intent(this, CarManageActivity.class);
                intent.putExtra("path", "main");
                startActivity(intent);
                finish();
                break;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, LoginActivityPre.class);
        startActivity(intent);

        finish();
    }
}
