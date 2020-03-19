package com.sincar.customer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Browser;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import com.sincar.customer.util.Utility;

import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.util.Utility.isPackageInstalled;

public class PayApproveActivity extends Activity {
    final String MERCHANT_URL = "https://sincar.co.kr/api/payment/index.asp";

    WebView mWeb;
    Toast toast = null;

    LinearLayout ll_item01;
    EditText et_url;
    Button btn_urlgo;

    private static final int LAUNCHED_ACTIVITY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_approve);

        Intent intent = getIntent();
        String urlString = null;

        mWeb = (WebView) findViewById(R.id.web);
        ll_item01 = (LinearLayout) findViewById(R.id.ll_item01);
        et_url = (EditText) findViewById(R.id.et_url);
        et_url.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // 요기서 입력된 이벤트가 무엇인지 찾아서 실행해 줌
                switch (actionId) {
                    case EditorInfo.IME_ACTION_GO:
                        btn_urlgo.performClick();
                        break;
                }
                return false;
            }
        });
        btn_urlgo = (Button) findViewById(R.id.btn_urlgo);
        btn_urlgo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mWeb.loadUrl(et_url.getText().toString());
                ll_item01.setVisibility(View.GONE);
            }
        });

        mWeb.setWebViewClient(new MyWebClient());
        mWeb.setWebChromeClient(new MyWebChromeClient());
        WebSettings set = mWeb.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(true);

        mWeb.addJavascriptInterface(new AndroidBridge(), "BRIDGE");

        //Webview 설정 - 캐시사용
        /**
         * WebView에서 캐시사용 관련 Default 설정은 WebSettings.LOAD_DEFAULT 입니다.
         * ex) mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
         * 가급적 캐시 사용 설정을 변경하지 않을것을 권고 드립니다.
         * @중요 : 'WebSettings.LOAD_CACHE_ELSE_NETWORK' 로 변경금지.
         */

        // Webview 설정 - 쿠키 등 결제를 위한 설정
        /**************************************************************
         * 안드로이드 5.0 이상으로 tagetSDK를 설정하여 빌드한경우 아래 구문을 추가하여 주십시요
         **************************************************************/
        if( android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            mWeb.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(mWeb, true);
        }

        /**************************************************************
         * 안드로이드 5.0 이전 버전으로 tagetSDK를 설정하여 빌드한경우 아래 구문을 추가하여 주십시요
         **************************************************************/
        /*
        CookieManager cookieManager = CookieManager.Z();
        cookieManager.setAcceptCookie(true);
        */

        String postParams = "MEMBER_NO=" + voLoginItem.MEMBER_NO;
        postParams += "&AMOUNT=1004";

 //       mWeb.postUrl(MERCHANT_URL, EncodingUtils.getBytes(postParams, "EUC-KR"));

        mWeb.loadUrl(MERCHANT_URL + "?" + postParams); // 테스트URL을 넣어주세요
    }

    class MyWebChromeClient extends WebChromeClient {
        ProgressBar pb_item01 = (ProgressBar) findViewById(R.id.pb_item01);

        public void onProgressChanged(WebView view, int progress) {
            pb_item01.setProgress(progress); // ProgressBar값 설정

            if (progress == 100) { // 모두 로딩시 Progressbar를 숨김
                pb_item01.setVisibility(View.GONE);
            } else {
                pb_item01.setVisibility(View.VISIBLE);
            }
        }

        public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
            new AlertDialog.Builder(PayApproveActivity.this).setTitle("").setMessage(message).setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            }).setCancelable(false).create().show();
            // showToast(message);
            // result.confirm();
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            new AlertDialog.Builder(PayApproveActivity.this).setTitle("").setMessage(message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            }).create().show();
            return true;
        }
    }

    public void showAlert(String message, String positiveButton, DialogInterface.OnClickListener positiveListener, String negativeButton, DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(message);
        alert.setPositiveButton(positiveButton, positiveListener);
        alert.setNegativeButton(negativeButton, negativeListener);
        alert.show();
    }

    class MyWebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(final WebView view, String url) {
            Log.d("================url::", url);
            if ((url.startsWith("http://") || url.startsWith("https://")) && url.endsWith(".apk")) {
                downloadFile(url);
                return super.shouldOverrideUrlLoading(view, url);
            } else if ((url.startsWith("http://") || url.startsWith("https://")) && (url.contains("market.android.com") || url.contains("m.ahnlab.com/kr/site/download"))) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(intent);
                    return true;
                } catch (ActivityNotFoundException e) {
                    return false;
                }
            } else if (url.startsWith("http://") || url.startsWith("https://")) {
                view.loadUrl(url);
                return true;
            } else if (url != null
                    && (url.contains("vguard") || url.contains("droidxantivirus") || url.contains("smhyundaiansimclick://")
                    || url.contains("smshinhanansimclick://") || url.contains("smshinhancardusim://") || url.contains("smartwall://") || url.contains("appfree://")
                    || url.contains("v3mobile") || url.endsWith(".apk") || url.contains("market://") || url.contains("ansimclick")
                    || url.contains("market://details?id=com.shcard.smartpay") || url.contains("shinhan-sr-ansimclick://"))) {
                return callApp(url);
            } else if (url.startsWith("smartxpay-transfer://")) {
                boolean isatallFlag = isPackageInstalled(getApplicationContext(), "kr.co.uplus.ecredit");
                if (isatallFlag) {
                    boolean override = false;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.putExtra(Browser.EXTRA_APPLICATION_ID, getPackageName());

                    try {
                        startActivity(intent);
                        override = true;
                    } catch (ActivityNotFoundException ex) {
                    }
                    return override;
                } else {
                    showAlert("확인버튼을 누르시면 구글플레이로 이동합니다.", "확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(("market://details?id=kr.co.uplus.ecredit")));
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.putExtra(Browser.EXTRA_APPLICATION_ID, getPackageName());
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                        }
                    }, "���", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    return true;
                }
            } else if (url.startsWith("ispmobile://")) {
                boolean isatallFlag = isPackageInstalled(getApplicationContext(), "kvp.jjy.MispAndroid320");
                if (isatallFlag) {
                    boolean override = false;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.putExtra(Browser.EXTRA_APPLICATION_ID, getPackageName());

                    try {
                        startActivity(intent);
                        override = true;
                    } catch (ActivityNotFoundException ex) {
                    }
                    return override;
                } else {
                    showAlert("확인버튼을 누르시면 구글플레이로 이동합니다.", "확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            view.loadUrl("http://mobile.vpay.co.kr/jsp/MISP/andown.jsp");
                        }
                    }, "���", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    return true;
                }
            } else if (url.startsWith("paypin://")) {
                boolean isatallFlag = isPackageInstalled(getApplicationContext(), "com.skp.android.paypin");
                if (isatallFlag) {
                    boolean override = false;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.putExtra(Browser.EXTRA_APPLICATION_ID, getPackageName());

                    try {
                        startActivity(intent);
                        override = true;
                    } catch (ActivityNotFoundException ex) {
                    }
                    return override;
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(("market://details?id=com.skp.android.paypin&feature=search_result#?t=W251bGwsMSwxLDEsImNvbS5za3AuYW5kcm9pZC5wYXlwaW4iXQ..")));
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.putExtra(Browser.EXTRA_APPLICATION_ID, getPackageName());
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                }
            } else if (url.startsWith("lguthepay://")) {
                boolean isatallFlag = isPackageInstalled(getApplicationContext(), "com.lguplus.paynow");
                if (isatallFlag) {
                    boolean override = false;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.putExtra(Browser.EXTRA_APPLICATION_ID, getPackageName());

                    try {
                        startActivity(intent);
                        override = true;
                    } catch (ActivityNotFoundException ex) {
                    }
                    return override;
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(("market://details?id=com.lguplus.paynow")));
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.putExtra(Browser.EXTRA_APPLICATION_ID, getPackageName());
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    return true;
                }
            } else {
                return callApp(url);
            }
        }

        // �ܺ� �� ȣ��
        public boolean callApp(String url) {
            Intent intent = null;
            // ����Ʈ ���ռ� üũ : 2014 .01�߰�
            try {
                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
//                Log.e("intent getScheme     +++===>", intent.getScheme());
//                Log.e("intent getDataString +++===>", intent.getDataString());
            } catch (URISyntaxException ex) {
                Log.e("Browser", "Bad URI " + url + ":" + ex.getMessage());
                return false;
            }
            try {
                boolean retval = true;
                //chrome ���� ��� : 2014.01 �߰�
                if (url.startsWith("intent")) { // chrome ���� ���
                    // 앱설치 체크를 합니다.

                    // 국민 앱카드가 설치 되지 않은 경우
                    // 국민카드(+앱카드)로 변경 하여 호출 하고
                    // 국민카드(+앱카드)도 없는 경우에 국민카드(+앱카드)마켓으로 이동
                    if (getPackageManager().resolveActivity(intent, 0) == null && "com.kbcard.cxh.appcard".equals(intent.getPackage())) {
                        intent.setPackage("com.kbcard.kbkookmincard");	// 국민카드(+앱카드)로 변경

                        if (getPackageManager().resolveActivity(intent, 0) == null ) {
                            String packagename = intent.getPackage();
                            if (packagename != null) {
                                Uri uri = Uri.parse("market://search?q=pname:" + packagename);
                                intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                                retval = true;
                            }
                        } else {
                            intent.addCategory(Intent.CATEGORY_BROWSABLE);
                            intent.setComponent(null);
                            try {
                                if (startActivityIfNeeded(intent, -1)) {
                                    retval = true;
                                }
                            } catch (ActivityNotFoundException ex) {
                                retval = false;
                            }
                        }
                    } else if (getPackageManager().resolveActivity(intent, 0) == null ) {	// �� �̼�ġ �� ���� �̵�
                        String packagename = intent.getPackage();
                        if (packagename != null) {
                            Uri uri = Uri.parse("market://search?q=pname:" + packagename);
                            intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                            retval = true;
                        }
                    } else {
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setComponent(null);
                        try {
                            if (startActivityIfNeeded(intent, -1)) {
                                retval = true;
                            }
                        } catch (ActivityNotFoundException ex) {
                            retval = false;
                        }
                    }
                } else { // 구 방식
                    Uri uri = Uri.parse(url);
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    retval = true;
                }
                return retval;
            } catch (ActivityNotFoundException e) {
                Log.e("error ===>", e.getMessage());
                e.printStackTrace();
                return false;
            }
        }

        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            int keyCode = event.getKeyCode();
            if ((keyCode == KeyEvent.KEYCODE_DPAD_LEFT) && mWeb.canGoBack()) {
                mWeb.goBack();
                return true;
            } else if ((keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) && mWeb.canGoForward()) {
                mWeb.goForward();
                return true;
            }
            return false;
        }

        // DownloadFileTask생성 및 실행
        private void downloadFile(String mUrl) {
            new DownloadFileTask().execute(mUrl);
        }

        // AsyncTask<Params,Progress,Result>
        private class DownloadFileTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... urls) {
                URL myFileUrl = null;
                try {
                    myFileUrl = new URL(urls[0]);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();

                    // 다운 받는 파일의 경로는 sdcard/ 에 저장되며 sdcard에 접근하려면 uses-permission에
                    // android.permission.WRITE_EXTERNAL_STORAGE을 추가해야만 가능.
                    String mPath = "sdcard/v3mobile.apk";
                    FileOutputStream fos;
                    File f = new File(mPath);
                    if (f.createNewFile()) {
                        fos = new FileOutputStream(mPath);
                        int read;
                        while ((read = is.read()) != -1) {
                            fos.write(read);
                        }
                        fos.close();
                    }

                    return "v3mobile.apk";
                } catch (IOException e) {
                    e.printStackTrace();
                    return "";
                }
            }

            @Override
            protected void onPostExecute(String filename) {
                if (!"".equals(filename)) {
                    Toast.makeText(getApplicationContext(), "download complete", Toast.LENGTH_LONG).show();

                    //  안드로이드 패키지 매니저를 사용한 어플리케이션 설치.
                    File apkFile = new File(Environment.getExternalStorageDirectory() + "/" + filename);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.e("===============", "onNewIntent!!");
        if (intent != null) {
            if (Intent.ACTION_VIEW.equals(intent.getAction())) {
                Uri uri = intent.getData();
                Log.e("================uri", uri.toString());
                if (String.valueOf(uri).startsWith("ispcincha")) { // ISP 커스텀스키마를 넣어주세요
                    String result = uri.getQueryParameter("result");
                    if ("success".equals(result)) {
                        mWeb.loadUrl("javascript:doPostProcess();");
                    } else if ("cancel".equals(result)) {
                        mWeb.loadUrl("javascript:doCancelProcess();");
                    } else {
                        mWeb.loadUrl("javascript:doNoteProcess();");
                    }
                } else if (String.valueOf(uri).startsWith("sincha")) { // 계좌이체 커스텀스키마를 넣어주세요
                    // 계좌이체는 WebView가 아무일을 하지 않아도 됨
                } else if (String.valueOf(uri).startsWith("paypinsincha")) { // paypin 커스텀스키마를 넣어주세요
                    mWeb.loadUrl("javascript:doPostProcess();");
                } else if (String.valueOf(uri).startsWith("paynowsincha")) { // paynow 커스텀스키마를 넣어주세요
                    // paynow는 WebView가 아무일을 하지 않아도 됨
                }
            }
        }
    }

    private void showToast(String message) {
        if (toast == null) {
            toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast.setText(message);
            toast.show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWeb.canGoBack()) {
            mWeb.goBack();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (ll_item01.getVisibility() == View.GONE) {
                ll_item01.setVisibility(View.VISIBLE);
            } else {
                ll_item01.setVisibility(View.GONE);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     *
     * 	계좌이체 결과값을 받아와 오류시 해당 메세지를, 성공시에는 결과 페이지를 호출한다.
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String resVal = data.getExtras().getString("bankpay_value");
        String resCode = data.getExtras().getString("bankpay_code");
        Log.i("NICE","resCode : "+ resCode);
        Log.i("NICE","resVal : "+ resVal);

//        if("091".equals(resCode)){//계좌이체 결제를 취소한 경우
//            Utility.AlertDialog("인증 오류", "계좌이체 결제를 취소하였습니다.",  PayApproveActivity.this);
//            mWebView.postUrl(MERCHANT_URL, null);
//        } else if("060".equals(resCode)){
//            Utility.AlertDialog("인증 오류", "타임아웃",  PayApproveActivity.this);
//            mWebView.postUrl(MERCHANT_URL, null);
//        } else if("050".equals(resCode)){
//            Utility.AlertDialog("인증 오류", "전자서명 실패",  PayApproveActivity.this);
//            mWebView.postUrl(MERCHANT_URL, null);
//        } else if("040".equals(resCode)){
//            Utility.AlertDialog("인증 오류", "OTP/보안카드 처리 실패",  PayApproveActivity.this);
//            mWebView.postUrl(MERCHANT_URL, null);
//        } else if("030".equals(resCode)){
//            Utility.AlertDialog("인증 오류", "인증모듈 초기화 오류",  PayApproveActivity.this);
//            mWebView.postUrl(MERCHANT_URL, null);
//        } else if("000".equals(resCode)){	// 성공일 경우
//            String postData = "callbackparam2="+BANK_TID+"&bankpay_code="+resCode+"&bankpay_value="+resVal;
//            mWebView.postUrl(NICE_BANK_URL, EncodingUtils.getBytes(postData,"euc-kr"));
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_web_view, menu);
        return true;
    }

    final class AndroidBridge {
        @JavascriptInterface //이게 있어야 웹에서 실행이 가능합니다.
        public void payAndroid(String reponseData, String message) {
            Toast.makeText(getApplicationContext(), "웹에서 클릭했어요 => " + reponseData, Toast.LENGTH_SHORT).show();
            System.out.println("[spirit] 웹에서 호출 reponseData =>" + reponseData);
        }
    }
}
