package com.sincar.customer;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ActiveAreaSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_area_settingsctivity);

            init();
        }

        /**
         * 화면 초기화
         */
        private void init() {
            hideActionBar();

            findViewById(R.id.btnBack).setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnBack:
                    finish();
                    break;
            }
        }

        //==============================================================================================

        private void hideActionBar() {
            ActionBar ab = getSupportActionBar();
            ab.hide();
        }

        private void actionBarInit() {
            // ActionBar 설정
            // 타이틀 설정
            ActionBar ab = getSupportActionBar();
            ab.setTitle("비밀번호 변경");

            // ActionBar 그림자 제거
            ab.setBackgroundDrawable(null);

            // up icon 설정(show: true, hide: false)
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeAsUpIndicator(R.drawable.filled);

            // 배경 색상 변경 - 배경 흰색으로 할 경우 아이콘 색상 변경 필요
            // colorPrimary color 변경
        }
}
