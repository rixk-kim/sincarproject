package com.sincar.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        findViewById(R.id.btnPrev).setOnClickListener(this);
        ((TextView)findViewById(R.id.title_activity)).setText(R.string.payment_title);
        findViewById(R.id.btnNext).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch(v.getId())
        {
            case R.id.btnPrev:
                finish();
                break;

        }

    }
}
