package com.sincar.customer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sincar.customer.item.AuthResult;
import com.sincar.customer.network.VolleyNetwork;
import com.sincar.customer.util.Util;

import java.util.HashMap;

import static com.sincar.customer.HWApplication.authResult;
import static com.sincar.customer.HWApplication.voAuthItem;
import static com.sincar.customer.common.Constants.AUTH_NUMBER_REQUEST;
import static com.sincar.customer.common.Constants.LOGIN_REQUEST;

/**
 * 2020.03.24 spirit
 * 회원가입 - 인증 액티비티
 */
public class MemberAuthActivity extends Activity implements View.OnClickListener {
    private static final String TAG = LoginActivityPre.class.getSimpleName();
    //===================== 뷰 =====================
    private Button join_auth_btn;       //확인
    private EditText join_user_auth;    //인증코드 입력
    private String phone_number;
    private String path;
    private Context AContext;

    private TextView auth_time_counter; //시간을 보여주는 TextView
    CountDownTimer countDownTimer;
    private final int MILLISINFUTURE = 120 * 1000; //총 시간 (120초 = 2분)
    private final int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)
    //===================== 뷰 =====================
    public static MemberAuthActivity _memberAuthActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_join_auth);
        AContext = this;
        _memberAuthActivity = MemberAuthActivity.this;

        Intent intent = getIntent(); /*데이터 수신*/
        phone_number  = intent.getExtras().getString("phone_number");       /*String형*/
        path  = intent.getExtras().getString("path");       /*String형*/

        findViewById(R.id.auth_btnPrev).setOnClickListener(this);
        join_auth_btn = (Button) findViewById(R.id.join_auth_btn);
        join_auth_btn.setOnClickListener(this);

        join_user_auth = (EditText) findViewById(R.id.join_user_auth);

        // 핸드폰 번호 입력 후 확인 버튼 클릭 시
        join_user_auth.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // 비밀번호 입력으로 이동
                    if(TextUtils.isEmpty(join_user_auth.getText().toString().trim()))
                    {
                        Toast.makeText(MemberAuthActivity.this, "코드를 입력 해주세요.", Toast.LENGTH_LONG).show();
                    }else {
                        if (voAuthItem.AUTH_NUMBER.equals(join_user_auth.getText().toString().trim()))
                        {
                            //비밀번호 입력으로 이동
                            Intent intent = new Intent(AContext, PasswordChangeActivity.class);
                            //intent.putExtra("path", "join");

                            intent.putExtra("path", path);
                            intent.putExtra("phone_number", phone_number);
                            startActivity(intent);
                        }else{
                            showAuthErrorAlert();
                        }
                    }
                }
                return false;
            }
        });

        auth_time_counter   = (TextView) findViewById(R.id.auth_time_counter);      //줄어드는 시간을 나타내는 TextView
        findViewById(R.id.auth_resend).setOnClickListener(this);                    //재전송

        countDownTimer();
    }

    public void countDownTimer() { //카운트 다운 메소드

        auth_time_counter = (TextView) findViewById(R.id.auth_time_counter);            //줄어드는 시간을 나타내는 TextView

        if(countDownTimer != null){
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) { //(300초에서 1초 마다 계속 줄어듬)

                long emailAuthCount = millisUntilFinished / 1000;
                Log.d("Alex", emailAuthCount + "");

                if ((emailAuthCount - ((emailAuthCount / 60) * 60)) >= 10) { //초가 10보다 크면 그냥 출력
                    auth_time_counter.setText("남은시간 " + (emailAuthCount / 60) + " : " + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                } else { //초가 10보다 작으면 앞에 '0' 붙여서 같이 출력. ex) 02,03,04...
                    auth_time_counter.setText("남은시간 " + (emailAuthCount / 60) + " : 0" + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                }

                //emailAuthCount은 종료까지 남은 시간임. 1분 = 60초 되므로,
                // 분을 나타내기 위해서는 종료까지 남은 총 시간에 60을 나눠주면 그 몫이 분이 된다.
                // 분을 제외하고 남은 초를 나타내기 위해서는, (총 남은 시간 - (분*60) = 남은 초) 로 하면 된다.

            }


            @Override
            public void onFinish() { //시간이 다 되면 다이얼로그 종료
//                authDialog.cancel();
                Toast.makeText(MemberAuthActivity.this, "시간이 초과 되었습니다.", Toast.LENGTH_LONG).show();
                finish();
            }
        }.start();
//        emailAuth_btn.setOnClickListener(this);
    }

    /**
     * 에러 팝업
     */
    private void showAuthErrorAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MemberAuthActivity.this);
        // 메세지
        builder.setTitle(getString(R.string.notice));
        builder.setMessage(R.string.error_auth_msg);
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.auth_btnPrev:
                finish();
                break;
            case R.id.join_auth_btn:
                // 비밀번호 입력으로 이동
                if(TextUtils.isEmpty(join_user_auth.getText().toString().trim()))
                {
                    Toast.makeText(MemberAuthActivity.this, "코드를 입력 해주세요.", Toast.LENGTH_LONG).show();
                }else {
                    if (voAuthItem.AUTH_NUMBER.equals(join_user_auth.getText().toString().trim()))
                    {
                        countDownTimer.cancel();

                        //비밀번호 입력으로 이동
                        Intent intent = new Intent(this, PasswordChangeActivity.class);
                        intent.putExtra("path", path);
                        intent.putExtra("phone_number", phone_number);
                        startActivity(intent);
                    }else{
                        showAuthErrorAlert();
                    }
                }
                break;

            case R.id.auth_resend:
                //재전송
                requestAuthCode();
                break;

        }
    }

    private void requestAuthCode() {

        HashMap<String, String> postParams = new HashMap<String, String>();
        postParams.put("PHONE_NUMBER", phone_number);     // 사용자 전화번호

        //프로그래스바 시작
        Util.showDialog(this);

        //인증번호 요청
        VolleyNetwork.getInstance(this).serverDataRequest(AUTH_NUMBER_REQUEST, postParams, onReAuthResponseListener);
    }

    VolleyNetwork.OnResponseListener onReAuthResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String it) {
 //           it = " {\"auth_send\": {\"AUTH_RESULT\":\"0\",\"CAUSE\":\"\",\"AUTH_NUMBER\":\"123456\"}}";
            Gson gSon = new Gson();
            //LoginResult loginResult = gSon.fromJson(it, LoginResult.class);
            authResult = gSon.fromJson(it, AuthResult.class);

            voAuthItem.AUTH_RESULT      = authResult.auth_send.AUTH_RESULT;
            voAuthItem.CAUSE            = authResult.auth_send.CAUSE;
            voAuthItem.AUTH_NUMBER      = authResult.auth_send.AUTH_NUMBER;  //인증번호

            //프로그래스바 종료
            Util.dismiss();

            countDownTimer();

            Toast.makeText(MemberAuthActivity.this, "재전송 하였습니다.", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onResponseFailListener(VolleyError it) {
            //프로그래스바 종료
            Util.dismiss();

            Toast.makeText(MemberAuthActivity.this, "재전송에 실패하였습니다.", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Log.d("onPostCreate", "onDestroy");
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
    }

}

