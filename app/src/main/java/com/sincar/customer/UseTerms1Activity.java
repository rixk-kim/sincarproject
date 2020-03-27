package com.sincar.customer;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class UseTerms1Activity extends AppCompatActivity implements View.OnClickListener {
    private String variable;
    WebView mWebView;
    private String link_url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent(); /*데이터 수신*/
        variable    = intent.getExtras().getString("path");    /*String형*/

        setContentView(R.layout.activity_use_terms_1);


        findViewById(R.id.useterms1_btnPrev).setOnClickListener(this);
//        if("1".equals(variable) || "5".equals(variable))
//        {
//            setContentView(R.layout.activity_use_terms_1);  //서비스 이용약관
//        }else if("2".equals(variable) || "6".equals(variable))
//        {
//            setContentView(R.layout.activity_use_terms_2);  //개인정보 처리방침
//        }else if("3".equals(variable) || "7".equals(variable))
//        {
//            setContentView(R.layout.activity_use_terms_3);  //위치기반서비스 이용약관
//        }else
//        {
//            setContentView(R.layout.activity_use_terms_4);  //마케팅 정보 수신동의
//        }
        // 화면 초기화
        init();
    }

    /**
     * 화면 초기화
     */
    private void init() {
        final ProgressDialog asyncDialog = new ProgressDialog(this);

        // 웹뷰 셋팅
        mWebView = (WebView) findViewById(R.id.nWebView);
        //mWebView.setWebViewClient(new WebClient()); // 응용프로그램에서 직접 url 처리
        WebSettings set = mWebView.getSettings();
        set.setJavaScriptEnabled(true);

        set.setUseWideViewPort(true);		// Wide viewport를 사용하도록 설정
        set.setJavaScriptEnabled(true); 	// 자바스크립트를 실행할 수 있도록 설정
        set.setDisplayZoomControls(false);	// zoom 컨트롤이 안보이게
        set.setLoadWithOverviewMode(true);	// 페이지가 크더라도 전체가 보이게 하기

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {// https 이미지.
            set.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.setLongClickable(false);	// 롱클릭 막기

//

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                asyncDialog.setMessage("Loading");
                asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                asyncDialog.show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("[spirit] check url -> " + url);
                if (view.canGoBack()) {
                    view.loadUrl(url);
                    return true;
                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                asyncDialog.dismiss();
                super.onPageFinished(view, url);
            }
        });

        if("1".equals(variable) || "5".equals(variable))
        {
            link_url = "http://sincar.co.kr/api/IF_SINCAR_AGENT_026_AGREE_01.asp";
        }else if("2".equals(variable) || "6".equals(variable))
        {
            link_url = "http://sincar.co.kr/api/IF_SINCAR_AGENT_026_AGREE_02.asp";
        }else if("3".equals(variable) || "7".equals(variable))
        {
            link_url = "http://sincar.co.kr/api/IF_SINCAR_AGENT_026_AGREE_03.asp";
        }else
        {
            link_url = "http://sincar.co.kr/api/IF_SINCAR_AGENT_026_AGREE_04.asp";
        }

        mWebView.loadUrl(link_url);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.useterms1_btnPrev:
                //  내정보
                int tmp_variable = Integer.parseInt(variable);
                if(tmp_variable < 5) {
                    intent = new Intent(this, UseTermsActivity.class);
                    startActivity(intent);
                }
                finish();
                break;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        int tmp_variable = Integer.parseInt(variable);
        if(tmp_variable < 5) {
            Intent intent = new Intent(this, UseTermsActivity.class);
            startActivity(intent);
        }
        finish();
    }
}


