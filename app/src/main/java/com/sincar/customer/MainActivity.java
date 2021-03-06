package com.sincar.customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sincar.customer.adapter.MainBannerSliderAdapter;
import com.sincar.customer.adapter.content.UseContent;
import com.sincar.customer.service.PicassoImageLoadingService;
import com.sincar.customer.sharing.ShareActivity;
import com.sincar.customer.util.Util;

import java.security.MessageDigest;
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
    public static MainActivity _mMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main);
        if(_mMainActivity == null) _mMainActivity = this;
        mContext = this;

        // 화면 초기화
        init();

        //getAppKeyHash();
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
        findViewById(R.id.btnMainMenu5).setOnClickListener(this);
        findViewById(R.id.btnMainMenu6).setOnClickListener(this);
        findViewById(R.id.btnCarRegisterClose).setOnClickListener(this);
        findViewById(R.id.btnCarRegister).setOnClickListener(this);

        mCustomerName = (TextView) findViewById(R.id.customerName);
        if("1".equals(voLoginItem.MEMBER_TYPE))
        {
            mCustomerName.setText(voLoginItem.MEMBER_NAME + "(매니져)님의 포인트");
        }else {
            mCustomerName.setText(voLoginItem.MEMBER_NAME + "님의 포인트");
        }

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
                if(position <= 0) position = 0;

                if(!TextUtils.isEmpty(voAdvertiseItem.get(position).AD_LINK_URL)) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    Uri u = Uri.parse(voAdvertiseItem.get(position).AD_LINK_URL);
                    i.setData(u);
                    startActivity(i);
                }else {
                    Toast.makeText(getApplicationContext(), "서비스 준비중입니다.", Toast.LENGTH_SHORT).show();
                }
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
                        //UseContent.clearItem(); //초기화

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

    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                //자체 hash값
                String something = Base64.encodeToString(md.digest(), Base64.NO_WRAP); //new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
                System.out.println("[spirit] Hash key1 : "+something);

                //구글 앱서명 SHA-1값
                byte[] sha1 = {
                        0x0F, 0x71, 0x23, 0x55, 0x6C, (byte)0x8E, (byte)0xE7, (byte)0xD6, (byte)0xD4, (byte)0xCC, 0x4D, (byte)0x9E, (byte)0x9D, (byte)0xA9, (byte)0xEC, 0x05, 0x47, 0x39, (byte)0xD6, (byte)0xA1
                };
                System.out.println("[spirit] Hash key2 : "+Base64.encodeToString(sha1, Base64.NO_WRAP));


            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }



    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.btnCustomerPoint:
                // 포인트 보기
                intent = new Intent(this, PointHistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.btnMainMenu1:
                // 스팀세차
                intent = new Intent(this, MapsActivity.class);
                intent.putExtra("menu", "steam");
                startActivity(intent);
                break;
            case R.id.btnMainMenu2:
                // TODO - 렌트카
                //Toast.makeText(getApplicationContext(), "대리운전 준비중입니다.", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, MapsActivity.class);
                intent.putExtra("menu", "driver");
                startActivity(intent);
                break;
            case R.id.btnMainMenu3:
                // TODO - 차량공유
                //Toast.makeText(getApplicationContext(), "카페어링 준비중입니다.", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, com.sincar.customer.sharing.ShareActivity.class);
                intent.putExtra("menu", "pairing");
                startActivity(intent);
                break;
            case R.id.btnMainMenu4:
                // TODO - 승차공유
                //Toast.makeText(getApplicationContext(), "카쉐어 준비중입니다.", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, MapsActivity.class);
                intent.putExtra("menu", "sharing");
                startActivity(intent);
                break;
            case R.id.btnMainMenu5:
                // TODO - 퀵・탁송
                //Toast.makeText(getApplicationContext(), "카페어링 준비중입니다.", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, MapsActivity.class);
                intent.putExtra("menu", "quick");
                startActivity(intent);
                break;
            case R.id.btnMainMenu6:
                // TODO - 배달라이더
                //Toast.makeText(getApplicationContext(), "카쉐어 준비중입니다.", Toast.LENGTH_SHORT).show();
                intent = new Intent(this, MapsActivity.class);
                intent.putExtra("menu", "rider");
                startActivity(intent);
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
