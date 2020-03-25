package com.sincar.customer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.sincar.customer.item.AuthResult;
import com.sincar.customer.network.VolleyNetwork;

import java.util.HashMap;

import static com.sincar.customer.HWApplication.authResult;
import static com.sincar.customer.HWApplication.voAuthItem;
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
    private TextView auth_resend;

    private TextView auth_time_counter; //시간을 보여주는 TextView
 //   EditText emailAuth_number; //인증 번호를 입력 하는 칸
 //   Button emailAuth_btn; // 인증버튼
    CountDownTimer countDownTimer;
    private final int MILLISINFUTURE = 120 * 1000; //총 시간 (120초 = 2분)
    private final int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)
    //===================== 뷰 =====================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_join_auth);

        Intent intent = getIntent(); /*데이터 수신*/
        phone_number  = intent.getExtras().getString("phone_number");       /*String형*/

        join_auth_btn = (Button) findViewById(R.id.join_auth_btn);
        join_auth_btn.setOnClickListener(this);

        join_user_auth = (EditText) findViewById(R.id.join_user_auth);

        auth_time_counter   = (TextView) findViewById(R.id.auth_time_counter);      //줄어드는 시간을 나타내는 TextView
        findViewById(R.id.auth_resend).setOnClickListener(this);                    //재전송

        countDownTimer();
    }

    public void countDownTimer() { //카운트 다운 메소드

        auth_time_counter = (TextView) findViewById(R.id.auth_time_counter);            //줄어드는 시간을 나타내는 TextView

//        emailAuth_number = (EditText) dialogLayout.findViewById(R.id.emailAuth_number);
        //사용자 인증 번호 입력창
//        emailAuth_btn = (Button) dialogLayout.findViewById(R.id.emailAuth_btn);
        //인증하기 버튼


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

 //               authDialog.cancel();

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

            case R.id.join_auth_btn:
                // TODO - 비밀번호 입력으로 이동
                //join_user_auth.getText().toString().trim()
                if(TextUtils.isEmpty(join_user_auth.getText().toString().trim()))
                {
                    Toast.makeText(MemberAuthActivity.this, "코드를 입력 해주세요.", Toast.LENGTH_LONG).show();
                }else {
                    if (voAuthItem.AUTH_NUMBER.equals(join_user_auth.getText().toString().trim()))
                    {
                        //비밀번호 입력으로 이동
                        Intent intent = new Intent(this, PasswordChangeActivity.class);
                        intent.putExtra("path", "join");
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
        postParams.put("phoneNumber", phone_number);     // 사용자 전화번호

        //인증번호 요청
        VolleyNetwork.getInstance(this).passwordChangeRequest(LOGIN_REQUEST, postParams, onAuthResponseListener);
    }

    VolleyNetwork.OnResponseListener onAuthResponseListener = new VolleyNetwork.OnResponseListener() {
        @Override
        public void onResponseSuccessListener(String it) {
            it = " {\"auth_send\": {\"AUTH_RESULT\":\"0\",\"CAUSE\":\"\",\"AUTH_NUMBER\":\"123456\"}}";
            Gson gSon = new Gson();
            //LoginResult loginResult = gSon.fromJson(it, LoginResult.class);
            authResult = gSon.fromJson(it, AuthResult.class);

            voAuthItem.AUTH_RESULT      = authResult.auth_send.AUTH_RESULT;
            voAuthItem.CAUSE            = authResult.auth_send.CAUSE;
            voAuthItem.AUTH_NUMBER      = authResult.auth_send.AUTH_NUMBER;  //인증번호

            countDownTimer();

            Toast.makeText(MemberAuthActivity.this, "재전송 하였습니다.", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onResponseFailListener(VolleyError it) {
            Toast.makeText(MemberAuthActivity.this, "재전송에 실패하였습니다.", Toast.LENGTH_LONG).show();
        }
    };
}

