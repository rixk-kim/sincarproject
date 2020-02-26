package com.sincar.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class MemberShipActivity extends Activity implements View.OnClickListener{
    //https://www.sincar.co.kr/member/index.asp
    BottomNavigationView bottomNavigationView;

    WebView mWebView;
    private String register_url = "https://www.sincar.co.kr/member/index.asp";
    private Context mContext;

    private Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.membership);

        mContext = this;

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_clause);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
        //actionBar.setTitle("");
        //actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow);
        //actionBar.setDisplayHomeAsUpEnabled(true);

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



/*
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
                {
                    System.out.println("[spirit] request-> "+ request);
                    if(request.isRedirect())
                    {
                        view.loadUrl(register_url);
                        return true;
                    }
                    return false;
                }
            });
        }else {
 */
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
//        }
        mWebView.loadUrl(register_url);


//        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_clause);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.go_home:
//                        //Home 이동
//                        Intent intent = new Intent(ClauseActivity.this, MainActivity.class);
//                        startActivity(intent);
//
//                        finish();
//                        break;
//
//                    case R.id.go_setting:
//                        //화물 리스트 갱신
//                        Intent intent1 = new Intent(ClauseActivity.this, SettingsActivity.class);
//                        intent1.putExtra("freight_id", "12345676");
//                        startActivity(intent1);
//
//                        finish();
//                        break;
//                }
//                return true;
//            }
//        });
    }

    @Override
    //public void onKeyDown(int keyCode, KeyEvent event) {
    public void onBackPressed(){
//        if (mWebView.getOriginalUrl().equalsIgnoreCase(register_url)) {
//            super.onBackPressed();
//        }else
        System.out.println("[spirit] mWebView.canGoBack() => " +mWebView.canGoBack());
        if (mWebView.canGoBack()) {
            mWebView.goBack();
//            return true;
        }else{
            super.onBackPressed();

            //
            Intent intent = new Intent(MemberShipActivity.this, com.sincar.customer.LoginActivityPre.class);
            startActivity(intent);
            // 최초 생성 후 이동 시 제거
            finish();
        }
//        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnBack:
                // 메인 이동
                Intent intent = new Intent(MemberShipActivity.this, com.sincar.customer.LoginActivityPre.class);
                startActivity(intent);
                // 최초 생성 후 이동 시 제거
                finish();
                break;
        }
    }
}
