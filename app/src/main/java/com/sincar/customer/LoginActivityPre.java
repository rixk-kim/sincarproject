package com.sincar.customer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * 2020.02.26 spirit
 * 로그인 , 회원가입 선택 액티비티
 */
public class LoginActivityPre extends Activity implements View.OnClickListener {
    private static final String TAG = LoginActivityPre.class.getSimpleName();
    //===================== 뷰 =====================
    private Button login_btn;
    private Button memberjoin_btn;
    //===================== 뷰 =====================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR | Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_layout_pre);

        login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(this);

        memberjoin_btn = (Button) findViewById(R.id.memberjoin_btn);
        memberjoin_btn.setOnClickListener(this);
    }

    /**
     * 앱 종료
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 로그인 버튼
            case R.id.login_btn:
                // 로그인 이동
                Intent intent = new Intent(LoginActivityPre.this, com.sincar.customer.LoginActivity.class);
                startActivity(intent);
                // 최초 생성 후 이동 시 제거
                //finish();
                break;

            case R.id.memberjoin_btn:
                //회원가입 이동
                Intent intent1 = new Intent(LoginActivityPre.this, com.sincar.customer.MemberShipActivity.class);
                startActivity(intent1);
                // 최초 생성 후 이동 시 제거
                //finish();
                break;

        }
    }
}
