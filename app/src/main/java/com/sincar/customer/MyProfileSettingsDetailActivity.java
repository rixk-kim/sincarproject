package com.sincar.customer;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static com.sincar.customer.HWApplication.voLoginItem;


public class MyProfileSettingsDetailActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo_detail);

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        findViewById(R.id.myinfo_profile_btnPrev_layout).setOnClickListener(this);
        findViewById(R.id.myinfo_btnNext).setOnClickListener(this);

        TextView myinfo_user_name = findViewById(R.id.myinfo_user_name);
        myinfo_user_name.setText(voLoginItem.MEMBER_NAME);

        TextView user_mobile_number = findViewById(R.id.user_mobile_number);
        user_mobile_number.setText(voLoginItem.MEMBER_PHONE);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.myinfo_profile_btnPrev_layout:
                //  내정보
                intent = new Intent(this, MyProfileSettingsActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.myinfo_btnNext:
                //  비밀번호변경
                intent = new Intent(this, PasswordChangeActivity.class);
                intent.putExtra("path", "change");
                startActivity(intent);
//                finish();
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
}
