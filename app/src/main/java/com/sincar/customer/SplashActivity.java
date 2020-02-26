package com.sincar.customer;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.sincar.customer.R;
import com.sincar.customer.util.DataParser;
import com.sincar.customer.util.Util;
import com.sincar.customer.common.Constants;

import com.sincar.customer.network.DataObject;
import com.sincar.customer.network.JsonParser;
import com.sincar.customer.network.NetWorkController;
import com.sincar.customer.preference.PreferenceManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * 2020.02.17 spirit
 * 스플래쉬 액티비티
 * 1. 버전 체크 (업데이트 확인)
 * 2. 마시멜로 (API Lev 23 확인)
 */
public class SplashActivity extends Activity {

    //==================== 마시멜로 퍼미션 ====================
    public static final int REQUEST_READ_PHONE_STATE = 5000;
    public static final int REQUEST_ACCESS_FINE_LOCATION = 5001;
    public static final int REQUEST_CAMERA = 5002;
    //==================== 마시멜로 퍼미션 ====================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        // 마시멜로 체크 및 알림
        checkPermission();
    }

    /**
     * UUID , 버전 체크 시작
     */
    private void startSplash() {
        //필요시 UUID 사용.
        String uuid = "";

        try {
            uuid = PreferenceManager.getInstance().getDeviceUUID();

            // 생성한 UUID 가 존재 하지 않을 경우 신규 생성
            if (uuid == null || uuid.trim().toString().length() == 0) {
                // UUID 신규 생성
                uuid = createUuid();
            }

            // 생성된 UUID 저장
            PreferenceManager.getInstance().setDeviceUUID(uuid);

            // 기본 체크
            //checkApp(uuid);

            startApp();

        } catch (NullPointerException e) {
            Toast.makeText(SplashActivity.this, getString(R.string.fail_to_start_msg), Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(SplashActivity.this, getString(R.string.fail_to_start_msg), Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    /**
     * UUID 신규 생성
     * @return uuid
     */
    private String createUuid() {
        final String id = (PreferenceManager.getInstance().getDeviceUUID() == null ? "" : PreferenceManager.getInstance().getDeviceUUID());
        UUID uuid = null;
        if (id == "") {
            final String androidId = Settings.Secure.getString(SplashActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
            try {
                if (!"9774d56d682e549c".equals(androidId)) {
                    uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                } else {
                    //final String deviceId = ((TelephonyManager) SplashActivity.this.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return null;
                    }
                    final String deviceId = ((TelephonyManager) SplashActivity.this.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                    uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
                }
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            PreferenceManager.getInstance().setDeviceUUID(uuid.toString());
        } else {
            return id;
        }
        return uuid.toString();
    }

    /**
     * 앱 종료
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * 로그인 화면 이동
     */
    private void startApp() {

        // 로그인 이동
        Intent intent = new Intent(SplashActivity.this, com.sincar.customer.LoginActivity.class);
        startActivity(intent);
        // 최초 생성 후 이동 시 제거
        finish();

    }


    /**
     * 안드로이드 OS 마시멜로 퍼미션 체크
     */
    protected void checkPermission() {
        // SDK 버전 체크
        if (Build.VERSION.SDK_INT >= 23) {
            boolean phoneInt = permissionCheck(SplashActivity.this, Manifest.permission.READ_PHONE_STATE);
            boolean cameraInt = permissionCheck(SplashActivity.this, Manifest.permission.CAMERA);
            boolean writeInt = permissionCheck(SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            boolean readInt = permissionCheck(SplashActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            boolean locationInt = permissionCheck(SplashActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
//            boolean bluetooth = permissionCheck(SplashActivity.this, Manifest.permission.BLUETOOTH);
//            boolean bluetoothAdmin = permissionCheck(SplashActivity.this, Manifest.permission.BLUETOOTH_ADMIN);
            boolean internet = permissionCheck(SplashActivity.this, Manifest.permission.INTERNET);

            ArrayList<String> permission = new ArrayList<String>();
            if (!phoneInt) {
                permission.add(Manifest.permission.READ_PHONE_STATE);
            }

            if (!cameraInt) {
                permission.add(Manifest.permission.CAMERA);
            }

            if (!writeInt) {
                permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

            if (!readInt) {
                permission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }

            if (!locationInt) {
                permission.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }

//            if (!bluetooth) {
//                permission.add(Manifest.permission.BLUETOOTH);
//            }
//
//            if (!bluetoothAdmin) {
//                permission.add(Manifest.permission.BLUETOOTH_ADMIN);
//            }

            if (!internet) {
                permission.add(Manifest.permission.INTERNET);
            }

            if (!(phoneInt && cameraInt && writeInt && readInt && locationInt)) {
                showPopup(SplashActivity.this, permission.toArray(new String[permission.size()]), REQUEST_READ_PHONE_STATE);
            } else {
                startSplash();
            }
        } else {
            startSplash();
        }
    }

    /**
     * 마시멜로 퍼미션 체크 후 사용자 선택 알럿 호출
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                ArrayList<String> permissionList = new ArrayList<String>();
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        permissionList.add(permissions[i]);
                    }
                }

                if (permissionList.size() > 0) {
                    boolean bool = false;
                    for (int i = 0; i < permissionList.size(); i++) {
                        if (!shouldShowRequestPermissionRationale(permissionList.get(i))) {
                            break;
                        }
                    }

                    StringBuilder formatArgs = new StringBuilder();
                    for (int i = 0; i < permissionList.size(); i++) {
                        if (permissionList.get(i).equals(Manifest.permission.READ_PHONE_STATE)) {
                            formatArgs.append("전화번호 읽기");
                        } else if (permissionList.get(i).equals(Manifest.permission.CAMERA)) {
                            if (formatArgs.length() > 0) {
                                formatArgs.append(", 카메라 사용");
                            } else {
                                formatArgs.append("카메라 사용");
                            }
                        } else if (permissionList.get(i).equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                                permissionList.get(i).equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            if (!bool) {
                                bool = true;
                                if (formatArgs.length() > 0) {
                                    formatArgs.append(", 내장메모리 사용");
                                } else {
                                    formatArgs.append("내장메모리 사용");
                                }
                            }
                        } else if (permissionList.get(i).equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                            if (formatArgs.length() > 0) {
                                formatArgs.append(", 위치정보 사용");
                            } else {
                                formatArgs.append("위치정보 사용");
                            }
                        } else if (permissionList.get(i).equals(Manifest.permission.INTERNET)) {
                            if (formatArgs.length() > 0) {
                                formatArgs.append(", 인터넷 사용");
                            } else {
                                formatArgs.append("인터넷 사용");
                            }
                        }
                    }

                    String format = getString(R.string.read_phone_state_fails_content_msg, formatArgs.toString());
                    showAlert(SplashActivity.this, format);
                }else {

                    startSplash();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 퍼미션 체크
     * @param context
     * @param permission
     * @return
     */
    public static boolean permissionCheck(Context context, String permission) {
        int checkPermission = ContextCompat.checkSelfPermission(context, permission);
        if (checkPermission == PackageManager.PERMISSION_DENIED) {
            return false;
        }
        return true;
    }

    /**
     * 퍼미션 요청 팝업
     * @param activity
     * @param permission
     * @param callBackCode
     */
    private void showPopup(Activity activity, String[] permission, int callBackCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission[0])) {
            ActivityCompat.requestPermissions(activity, permission, callBackCode);
        } else {
            ActivityCompat.requestPermissions(activity, permission, callBackCode);
        }
    }

    /**
     * 퍼미션 경고 알럿
     * @param context
     */
    public static void showAlert(final Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.notice));
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                context.startActivity(intent);

                ((Activity)context).finish();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ((Activity)context).finish();
            }
        }).show();
    }

    ////////////////////////////////////// 퍼미션 체크 //////////////////////////////////////

}
