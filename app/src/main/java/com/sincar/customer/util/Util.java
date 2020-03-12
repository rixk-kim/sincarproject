package com.sincar.customer.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.sincar.customer.R;

import com.sincar.customer.preference.PreferenceManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * 2020. 02. 17 박정기
 * 공통 컴포넌트 클래스
 */
public class Util {

    private static Context mUtilContext;
    private static final int PROGRESS_DIALOG = 1001;
    private static final int PROGRESS_NFC_DIALOG = 1002;
    private static final int PROGRESS_STOP = 600;

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_BARCODE = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_SEND = 5;

    public static final String DEVICE_NAME = "device_name";

    // Name of the connected device
    private String mConnectedDeviceName = null;

    private static AlertDialog.Builder builder;

    private static DialogInterface OptionDialog = null;

    private static String request_year = null;
    private static String request_month = null;
    private static String request_day = null;

    // 프로그래스 바
    public static ProgressDialog mProgressDialog;


    /**
     * 현재 년도 리턴
     *
     * @param
     * @param
     */
    public static int getYear() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        String year = yearFormat.format(currentTime);

        System.out.println("[spirit]year =>"  +year);

        return Integer.parseInt(year);
    }

    /**
     * 현재 월 리턴
     *
     * @param
     * @param
     */
    public static int getMonth() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        String month = monthFormat.format(currentTime);

        return Integer.parseInt(month);
    }

    /**
     * 현재 일 리턴
     *
     * @param
     * @param
     */
    public static int getDay() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());
        String day = dayFormat.format(currentTime);

        return Integer.parseInt(day);
    }

    /**
     * 현재 시간 리턴
     *
     * @param
     * @param
     */
    public static int getHour() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH", Locale.getDefault());
        String hour = hourFormat.format(currentTime);

        return Integer.parseInt(hour);
    }

    /**
     * 버전 정보 리턴
     *
     * @return
     */
    public static String getServerVersion(Context mContext) {
        try {
            PackageInfo pi = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 공통 폴더 존재 확인
     *
     * @return
     */
    public static String makeDirectory() {
        String path = Environment.getExternalStorageDirectory() + "/DANO/";
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();
        return path;
    }

    /**
     * Long 형 전표 번호 스트링 변환
     * 최소 6 자리 -> 문자형 10 자리 리턴
     *
     * @param slipNumber
     * @param isNextLine : 다음 줄 넘김 여부
     * @return
     */
    public static String getSlipNumberToString(Long slipNumber, boolean isNextLine) {

        String slipStr = String.valueOf(slipNumber);

        try {
            StringBuffer slipSb = new StringBuffer();
            if (slipStr.length() == 6) {
                slipSb.append("000");
            } else if (slipStr.length() == 7) {
                slipSb.append("00");
            } else if (slipStr.length() == 8) {
                slipSb.append("0");
            }

            // 10자리 채움
            slipStr = slipSb.toString() + slipStr;

            if (isNextLine) {
                // 4 자리 끊어 다음 줄 넘김
                slipStr = slipStr.substring(0, 4) + "\n" + slipStr.substring(4, slipStr.length());
            }

        } catch (Exception e) {
            e.toString();
        }

        return slipStr;
    }

    /**
     * 금액 3자리 마다 . 추가
     *
     * @param money
     * @return
     */
    public static String setAddMoneyDot(String money) {

        StringBuffer sb = new StringBuffer();
        StringBuffer result = new StringBuffer();
        try {
            sb.append(money);
            String reverseMoney = sb.reverse().toString();

            for (int i = 0; i < reverseMoney.length(); i++) {
                if (i != 0 && i % 3 == 0) {
                    result.append(",");
                    result.append(reverseMoney.charAt(i));
                } else {
                    result.append(reverseMoney.charAt(i));
                }
            }

            result = result.reverse();

        } catch (Exception e) {
            result = new StringBuffer();
            result.append(money);
            return result.toString();
        }

        return result.toString();
    }

    /**
     * 확인 용 알럿
     *
     * @param mContext
     * @param msg
     * @param isNegativeBtn
     * @param isCancelAble
     */
    public static void commonAlert(Context mContext, String msg, boolean isNegativeBtn, boolean isCancelAble) {

        if (OptionDialog != null) {
            OptionDialog.dismiss();
        }

        builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getString(R.string.notice));
        builder.setMessage(msg);
        if (isCancelAble) {
            builder.setCancelable(true);
        }
        builder.setPositiveButton(mContext.getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        if (isNegativeBtn) {
            builder.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        OptionDialog = builder.show();
    }

    /**
     * Mac Address 추출
     *
     * @return
     */
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    public static String addDotDate(int date) {

        String strDate = "";

        try {
            strDate = String.valueOf(date);
            StringBuffer oldStringBuffer = new StringBuffer(strDate);
            oldStringBuffer.insert(4, ". ");
            oldStringBuffer.insert(8, ". ");
            return oldStringBuffer.toString();

        } catch (Exception e) {
            e.toString();
            return "0";
        }
    }



    private static InputMethodManager mInputMethodManager;

    public static void showKeyboard(Context context, EditText edittext) {
        if (mInputMethodManager == null) {
            mInputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        mInputMethodManager.showSoftInput(edittext, 0);
    }

    public static void hideKeyboard(Context context, EditText edittext) {
        if (mInputMethodManager == null) {
            mInputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        }

        mInputMethodManager.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
    }

    /**
     * Bitmpa 파일 추가
     * @param bitmap
     */
    public static void SaveBitmapToFileCache(Context mContext, Bitmap bitmap) {

        String strFilePath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/image.jpg";
        File fileCacheItem = new File(strFilePath);
        OutputStream out = null;

        try {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * byte 반환 및 한글 깨짐 방지
     */
    public static String cutStr(String cutStr, int maxLen) throws UnsupportedEncodingException {

        byte [] strByte = cutStr.getBytes("UTF-8");

        if( strByte.length < maxLen ) {
            return cutStr;
        }

        int cnt = 0;

        for( int i = 0; i < maxLen; i++ ) {
            if( strByte[i] < 0 ) {
                cnt++;
            }
        }

        String r_str;

        if(cnt % 2 == 0){
            r_str = new String(strByte, 0, maxLen);
            Log.d("r_str", r_str);
        }else{
            r_str = new String(strByte, 0, maxLen + 1);
            Log.d("r_str + 1", r_str);
        }

        Log.d("r_str + 2", new String(strByte, 0, maxLen + 2));
        Log.d("r_str + 3", new String(strByte, 0, maxLen + 2));

        return r_str;
    }

    public static int getScreenWidthSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return  size.x;
    }

    public static int getScreenHeightSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return  size.y;
    }

    /**
     * font 크기 조절
     */

    /**
     * 프로그래스 다이얼로그 보여주기
     */
    public static void showDialog() {
        try {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
            mProgressDialog = new ProgressDialog(mUtilContext);
            mProgressDialog.setMessage(mUtilContext.getString(R.string.connect_server_msg));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 프로그래스 다이얼로그 해제
     */
    public static void dismiss() throws NullPointerException {
        try {
            if (mProgressDialog == null) {
                return;
            }
            mProgressDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
