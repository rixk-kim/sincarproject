package com.sincar.customer.sy_rentcar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Browser;
import android.text.TextUtils;
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

import com.sincar.customer.MainActivity;
import com.sincar.customer.R;
import com.sincar.customer.util.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import static com.sincar.customer.HWApplication.voLoginItem;
import static com.sincar.customer.HWApplication.voReserveItem;
import static com.sincar.customer.MapsActivity._mMapsActivity;
import static com.sincar.customer.MapsActivity.homeKeyPressed;
import static com.sincar.customer.common.Constants.RENTCAR_PAY_REQUEST;
import static com.sincar.customer.util.Utility.isPackageInstalled;

public class Rental_pay_web extends AppCompatActivity {

    private Context pContext;

    WebView rental_mWeb;

    LinearLayout rental_item;
    EditText rental_et_url;
    Button rental_btn_urlgo;

    private String rent_approve_number = ""; // 예약번호
    private String rent_amount = ""; // 결제금액
    private String rent_delivery_amount = ""; // 딜리버리 금액
    private String rent_insurance_amount = ""; // 보험 금액
    private String rent_reserve_year = ""; //예약연도
    private String rent_reserve_date = ""; //예약날짜(월포함)
    private String rent_reserve_time = ""; //예약시간
    private String rent_return_year = ""; //반납연도
    private String rent_return_date = ""; //반납날짜(월포함)
    private String rent_return_time = ""; //반납시간
    private String rent_rentcar_car = ""; //대여차량
    private String rent_rentcar_num = ""; //대여 차량 번호
    private String rent_rentcar_agent = ""; //지점 정보
    private String rent_rentcar_res_add = null; //배차위치 (없을때 null)
    private String rent_rentcar_ret_add = null; // 반납위치 (없을때 null)
    private String insu_seq = "";   //보험 seq
    private String insu_name = "";  //보험 명
    private String select_delivery; // 딜리버리 선택
    private String coupone_seq = "";    //쿠폰 seq
    private String use_my_point = "";       //사용 포인트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_pay_web);

        Intent intent = getIntent();

        rental_mWeb = (WebView) findViewById(R.id.rental_web);
        rental_item = (LinearLayout) findViewById(R.id.rental_item);
        rental_et_url = (EditText) findViewById(R.id.rental_et_url);
        rental_et_url.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // 요기서 입력된 이벤트가 무엇인지 찾아서 실행해 줌
                switch (actionId) {
                    case EditorInfo.IME_ACTION_GO:
                        rental_btn_urlgo.performClick();
                        break;
                }
                return false;
            }
        });
        rental_btn_urlgo = (Button) findViewById(R.id.rental_btn_urlgo);
        rental_btn_urlgo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                rental_mWeb.loadUrl(rental_et_url.getText().toString());
                rental_item.setVisibility(View.GONE);
            }
        });

        rental_mWeb.setWebViewClient(new Rental_pay_web.MyWebClient());
        rental_mWeb.setWebChromeClient(new Rental_pay_web.MyWebChromeClient());
        WebSettings set = rental_mWeb.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(true);

        rent_amount = intent.getStringExtra("AMOUNT");                      //결제 금액(보험,딜리버리 금액 제외)
        rent_delivery_amount = intent.getStringExtra("DELIVERY_AMOUNT");           //딜리버리 금액
        rent_insurance_amount = intent.getStringExtra("INSURANCE_AMOUNT");         //보험 금액
        rent_reserve_year = intent.getStringExtra("RESERVE_YEAR");              //예약 연도
        rent_reserve_date = intent.getStringExtra("RESERVE_DATE");              //예약 날짜
        rent_reserve_time = intent.getStringExtra("RESERVE_TIME");              //예약 시간
        rent_return_year = intent.getStringExtra("RETURN_YEAR");                //반납 연도
        rent_return_date = intent.getStringExtra("RETURN_DATE");                //반납 날짜
        rent_return_time = intent.getStringExtra("RETURN_TIME");                //반납 시간
        rent_rentcar_car = intent.getStringExtra("RENTCAR_CAR");                //대여 차종
        rent_rentcar_num = intent.getStringExtra("RENCAR_NUM");                 //대여 차량 번호
        rent_rentcar_agent = intent.getStringExtra("RENTCAR_AGENT");            //지점 이름
        rent_rentcar_res_add = intent.getStringExtra("RENTCAR_RES_ADD");        //딜리버리 배차 위치(없으면 Null)
        rent_rentcar_ret_add = intent.getStringExtra("RENTCAR_RET_ADD");        //딜리버리 반납 위치(없으면 Null)
        insu_seq = intent.getStringExtra("INSURANCE_SEQ");
        insu_name = intent.getStringExtra("INSURANCE_NAME");
        select_delivery = intent.getStringExtra("RENTCAR_DEL_TYPE");
        coupone_seq = intent.getStringExtra("COUPON_AMOUNT");
        use_my_point = intent.getStringExtra("POINT_AMOUNT");

        rent_approve_number =  voLoginItem.MEMBER_NO + "R" + Util.getYearMonthDay1();

        String postParams = "MEMBER_NO = " + voLoginItem.MEMBER_NO;
        postParams += "&AMOUNT = " + rent_amount;
        postParams += "&APPROVE_NUMBER = " + rent_approve_number;
        postParams += "&RENTCAR_DEL_TYPE = " + select_delivery;
        postParams += "&DELIVERY_AMOUNT = " + rent_delivery_amount;
        postParams += "&INSURANCE_SEQ = " + insu_seq;
        postParams += "&INSURANCE_NAME = " + insu_name;
        postParams += "&INSURANCE_AMOUNT = " + rent_insurance_amount;
        postParams += "&COUPON_AMOUNT = " + coupone_seq;
        postParams += "&POINT_AMOUNT = " + use_my_point;
        postParams += "&RESERVE_YEAR = " + rent_reserve_year;
        postParams += "&RESERVE_DATE = " + rent_reserve_date;
        postParams += "&RESERVE_TIME = " + rent_reserve_time;
        postParams += "&RETURN_YEAR = " + rent_return_year;
        postParams += "&RETURN_DATE = " + rent_return_date;
        postParams += "&RETURN_TIME = " + rent_return_time;
        postParams += "&RENTCAR_CAR = " + rent_rentcar_car;
        postParams += "&RENTCAR_NUM = " + rent_rentcar_num;
        postParams += "&RENTCAR_AGENT = " + rent_rentcar_agent;
        postParams += "&RENTCAR_RES_ADD = " + rent_rentcar_res_add;
        postParams += "&RENTCAR_RET_ADD = " + rent_rentcar_ret_add;

        rental_mWeb.addJavascriptInterface(new Rental_pay_web.AndroidBridge(), "BRIDGE");

        /**************************************************************
         * 안드로이드 5.0 이상으로 tagetSDK를 설정하여 빌드한경우 아래 구문을 추가하여 주십시요
         **************************************************************/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rental_mWeb.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.setAcceptThirdPartyCookies(rental_mWeb, true);
        }

        if(Integer.parseInt(rent_amount) <= 0) {
            //실 결제 금액이 없을때 들어가야할 코드 결제 페이지로 가지 말아야함
        } else {
            rental_mWeb.loadUrl(RENTCAR_PAY_REQUEST + "?" + postParams);
        }
    }

    class MyWebChromeClient extends WebChromeClient {
        ProgressBar rentalpb = (ProgressBar) findViewById(R.id.rental_pb);

        public void onProgressChanged(WebView view, int progress) {
            rentalpb.setProgress(progress); // ProgressBar값 설정

            if (progress == 100) { // 모두 로딩시 Progressbar를 숨김
                rentalpb.setVisibility(View.GONE);
            } else {
                rentalpb.setVisibility(View.VISIBLE);
            }
        }

        public boolean onJsAlert(WebView view, String url, String message, final android.webkit.JsResult result) {
            new AlertDialog.Builder(Rental_pay_web.this).setTitle("").setMessage(message).setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
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
            new AlertDialog.Builder(Rental_pay_web.this).setTitle("").setMessage(message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
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
                    }, "취소", new DialogInterface.OnClickListener() {
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
                        intent.setPackage("com.kbcard.kbkookmincard");    // 국민카드(+앱카드)로 변경

                        if (getPackageManager().resolveActivity(intent, 0) == null) {
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
                    } else if (getPackageManager().resolveActivity(intent, 0) == null) {    // �� �̼�ġ �� ���� �̵�
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
            if ((keyCode == KeyEvent.KEYCODE_DPAD_LEFT) && rental_mWeb.canGoBack()) {
                rental_mWeb.goBack();
                return true;
            } else if ((keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) && rental_mWeb.canGoForward()) {
                rental_mWeb.goForward();
                return true;
            }
            return false;
        }
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("===============", "onNewIntent!!");
        if (intent != null) {
            if (Intent.ACTION_VIEW.equals(intent.getAction())) {
                Uri uri = intent.getData();
                Log.e("================uri", uri.toString());
                if (String.valueOf(uri).startsWith("ispcincha")) { // ISP 커스텀스키마를 넣어주세요
                    String result = uri.getQueryParameter("result");
                    if ("success".equals(result)) {
                        rental_mWeb.loadUrl("javascript:doPostProcess();");
                    } else if ("cancel".equals(result)) {
                        rental_mWeb.loadUrl("javascript:doCancelProcess();");
                    } else {
                        rental_mWeb.loadUrl("javascript:doNoteProcess();");
                    }
                } else if (String.valueOf(uri).startsWith("sincha")) { // 계좌이체 커스텀스키마를 넣어주세요
                    // 계좌이체는 WebView가 아무일을 하지 않아도 됨
                } else if (String.valueOf(uri).startsWith("paypinsincha")) { // paypin 커스텀스키마를 넣어주세요
                    rental_mWeb.loadUrl("javascript:doPostProcess();");
                } else if (String.valueOf(uri).startsWith("paynowsincha")) { // paynow 커스텀스키마를 넣어주세요
                    // paynow는 WebView가 아무일을 하지 않아도 됨
                }
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && rental_mWeb.canGoBack()) {
            rental_mWeb.goBack();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (rental_item.getVisibility() == View.GONE) {
                rental_item.setVisibility(View.VISIBLE);
            } else {
                rental_item.setVisibility(View.GONE);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 계좌이체 결과값을 받아와 오류시 해당 메세지를, 성공시에는 결과 페이지를 호출한다.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String resVal = data.getExtras().getString("bankpay_value");
        String resCode = data.getExtras().getString("bankpay_code");
        Log.i("NICE", "resCode : " + resCode);
        Log.i("NICE", "resVal : " + resVal);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_web_view, menu);
        return true;
    }

    final class AndroidBridge {
        @JavascriptInterface //이게 있어야 웹에서 실행이 가능합니다.
        public void payAndroid(String reponseData, String message, String MY_POINT) {

            System.out.println("[spirit] 웹에서 호출 reponseData =>" + reponseData);

            if ("success".equals(reponseData)) {
                //TODO - 데이타 서버 전송 후 예약완료 페이지로 이동. 포인트 사용했을 시 갱신해주기
                if (!TextUtils.isEmpty(MY_POINT)) {
                    voReserveItem.MY_POINT = MY_POINT;
                }

                //완료 페이지 이동
                Intent intent = new Intent(pContext, Rental_approve.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(pContext, message, Toast.LENGTH_SHORT).show();

                Intent intent;
                intent = new Intent(pContext, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
    }

//    //결재 정보 DB입력
//    private void rentCarRequestReserveInfo() {
//        HashMap<String, String> postParams = new HashMap<String, String>();
//
//        rent_approve_number =  voLoginItem.MEMBER_NO + "R" + Util.getYearMonthDay1();
//
//        postParams.put("MEMBER_NO", voLoginItem.MEMBER_NO);         // 회원번호
//        postParams.put("AMOUNT", rent_amount);                      // 결재 금액
//        postParams.put("APPROVE_NUMBER", rent_approve_number);      // 예약번호
//        postParams.put("RENTCAR_DEL_TYPE", select_delivery);        // 딜리버리 선택방식
//        postParams.put("DELIVERY_AMOUNT", rent_delivery_amount);    // 딜리버리 금액
//        postParams.put("INSURANCE_SEQ", insu_seq);                  // 보험 seq
//        postParams.put("INSURANCE_NAME", insu_name);                // 보험 명
//        postParams.put("INSURANCE_AMOUNT", rent_insurance_amount);  // 보험 금액
//        postParams.put("COUPON_AMOUNT", coupone_seq);               // 쿠폰 사용
//        postParams.put("POINT_AMOUNT", use_my_point);               // 포인트 사용양
//        postParams.put("RESERVE_YEAR", rent_reserve_year);          // 예약 연도
//        postParams.put("RESERVE_DATE", rent_reserve_date);          // 예약 날짜
//        postParams.put("RESERVE_TIME", rent_reserve_time);          // 예약 시간
//        postParams.put("RETURN_YEAR", rent_return_year);            // 반납 연도
//        postParams.put("RETURN_DATE", rent_return_date);            // 반납 날짜
//        postParams.put("RETURN_TIME", rent_return_time);            // 반납 시간
//        postParams.put("RENTCAR_CAR", rent_rentcar_car);            // 대여 차량
//        postParams.put("RENTCAR_NUM", rent_rentcar_num);            // 대여 차량 번호
//        postParams.put("RENTCAR_AGENT", rent_rentcar_agent);        // 대여 지점 이름
//        postParams.put("RENTCAR_RES_ADD", rent_rentcar_res_add);    // 딜리버리 배차 위치
//        postParams.put("RENTCAR_RET_ADD", rent_rentcar_ret_add);    // 딜리버리 반납 위치
//
//
//
//        //프로그래스바 시작
//        Util.showDialog(this);
//        //사용내역 요청
//        VolleyNetwork.getInstance(this).serverDataRequest(RENTCAR_PAY_REQUEST, postParams, onReserveResponseListener);
//
//        //test
//        //VolleyNetwork.getInstance(this).serverDataRequest(TEST_REQUEST, postParams, onReserveResponseListener);
//
//    }
//
//    VolleyNetwork.OnResponseListener onReserveResponseListener = new VolleyNetwork.OnResponseListener() {
//        @Override
//        public void onResponseSuccessListener(String serverData) {
//            /*
//                  {"reserve": [{"RESERVE_RESULT":"0","CAUSE":"","MY_POINT":"24000"}]}
//                  {"reserve": [{"RESERVE_RESULT":"1","CAUSE":"형식에 맞지 않습니다.","MY_POINT":""}]}
//             */
//
//            //서버 저장하고 다음 이동
//            Gson gSon = new Gson();
//            try {
//                rentcarReserveResult = gSon.fromJson(serverData, RentcarReserveResult.class);
//
//                voRentcarReserveItem.RENTCAR_response = rentcarReserveResult.rentcarReserve.get(0).RENTCAR_response;
//                voRentcarReserveItem.RENTCAR_message = rentcarReserveResult.rentcarReserve.get(0).RENTCAR_message;
//                voRentcarReserveItem.RENTCAR_MYPOINT = rentcarReserveResult.rentcarReserve.get(0).RENTCAR_MYPOINT;
//
//                //프로그래스바 종료
//                Util.dismiss();
//
//                System.out.println("[spirit]RENTCAR_RESERVE_RESULT ====>" + voRentcarReserveItem.RENTCAR_response);
//                if ("success".equals(voRentcarReserveItem.RENTCAR_response)) {
//                    voLoginItem.MY_POINT = voRentcarReserveItem.RENTCAR_MYPOINT;  //포인트 갱신
//
//                    //성공화면 이동
//                    Intent intent = new Intent(pContext, Rental_approve.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    Toast.makeText(pContext, voRentcarReserveItem.RENTCAR_message + "\n다시 예약 부탁 드립니다.", Toast.LENGTH_SHORT).show();
//
//                    finish();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//
//                //프로그래스바 종료
//                Util.dismiss();
//
//                Toast.makeText(pContext, "오류 발생하였습니다. 다시 예약 부탁 드립니다.", Toast.LENGTH_SHORT).show();
//
//                finish();
//            }
//        }
//
//        @Override
//        public void onResponseFailListener(VolleyError it) {
//
//            NetworkResponse response = it.networkResponse;
//            if(it instanceof ServerError && response != null) {
//                try {
//                    String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
//                    Log.e("VolleyError", res);
//                } catch (UnsupportedEncodingException e1) {
//                    e1.printStackTrace();
//                }
//            }
//            Log.e("VolleyError", "onResponseFailListener : " + it);
//
//            //프로그래스바 종료
//            Util.dismiss();
//        }
//    };

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        homeKeyPressed = true;
        _mMapsActivity.onPause();
    }


    @Override
    public void onBackPressed() {
        if (rental_mWeb.canGoBack()) {
            rental_mWeb.goBack();
        } else {
            Intent intent;
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}
