package com.sincar.customer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * 2020.04.09
 * 사용안함
 */
public class BusinessHourMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_hour_main);

        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        // ActionBar 설정
        // 타이틀 설정
        ActionBar ab = getSupportActionBar() ;
        ab.setTitle("") ;

        // ActionBar 그림자 제거
        ab.setBackgroundDrawable(null);

        // up icon 설정(show: true, hide: false)
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.filled);

        // 배경 색상 변경 - 배경 흰색으로 할 경우 아이콘 색상 변경 필요
        // colorPrimary color 변경
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
