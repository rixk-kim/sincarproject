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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.Timer;
import java.util.TimerTask;

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


    //progress
    public static LoadingProgress mProgress;
    private static Timer timer;

    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE = 2;
    public static final int TYPE_NOT_CONNECTED = 3;

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
     * 특정 날짜에 대하여 요일을 구함(일 ~ 토)
     * @param date
     * @param
     * @return
     * @throws Exception
     */
    public static String getDateDay(String date) throws Exception {
        String day = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date nDate = dateFormat.parse(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK);

        switch (dayNum) {
            case 1:
                day = "일";
                break;
            case 2:
                day = "월";
                break;
            case 3:
                day = "화";
                break;
            case 4:
                day = "수";
                break;
            case 5:
                day = "목";
                break;
            case 6:
                day = "금";
                break;
            case 7:
                day = "토";
                break;

        }

        return day;
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
     * 현재 년월일 리턴
     *
     * @param
     * @param
     */
    public static String getYearMonthDay() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy.MM.dd(E) HH:mm", Locale.getDefault());

        String tmp_hour = mFormat.format(currentTime);

        return tmp_hour;
    }

    /**
     * 현재 년월일분초 리턴
     *
     * @param
     * @param
     */
    public static String getYearMonthDay1() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());

        String tmp_hour = mFormat.format(currentTime);

        return tmp_hour;
    }

    /**
     * 현재 년월일 리턴
     *
     * @param
     * @param
     */
    public static String getYearMonthDay2() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

        String tmp_hour = mFormat.format(currentTime);

        return tmp_hour;
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
        String path = Environment.getExternalStorageDirectory() + "/sincar/";
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
    public static void showDialog(final Context mContext) {
        //네트웍 연결상태 체크하여 연결된 네트웍이 있을때만 진행.
        if(getConnectivityStatus(mContext) < 3) {
            mProgress = null;
            mProgress = new LoadingProgress(mContext);

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mProgress.show();

                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            dismiss();
                        }
                    };

                    //Timer timer = new Timer();
                    timer = new Timer();
                    timer.schedule(task, 60000);

                    //timer.cancel();
                }
            });
        }else{
            Toast.makeText(mContext, "네트웍 설정을 확인한 후 다시 시도하세요.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 프로그래스 다이얼로그 해제
     */
    public static void dismiss() throws NullPointerException {
//        try {
//            if (mProgressDialog == null) {
//                return;
//            }
//            mProgressDialog.dismiss();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if(mProgress!=null){
            mProgress.dismiss();
            mProgress = null;
        }

        if(timer!=null) {
            timer.cancel();
        }
    }

    /**
     * 로딩 프로그래스 START.
     */
    public static void showProgress(Context mContext){
        //네트웍 연결상태 체크하여 연결된 네트웍이 있을때만 진행.
//        String acc_type = DAPPreference.get(this,Constants.PREFERENCE_ACC_TYPE,"");
//        if(!TextUtils.isEmpty(acc_type) && !"Unknown".equals(acc_type)) {
        if (mProgress == null) {
            mProgress = new LoadingProgress(mContext);
        }


        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                mProgress.show();
            }
        });
        //       }
    }

    /**
     * 로딩 프로그래스 END.
     */
    public static void hideProgress(){
        if(mProgress!=null){
            mProgress.dismiss();
        }
    }

    public static int getConnectivityStatus(Context context){ //해당 context의 서비스를 사용하기위해서 context객체를 받는다.
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo != null){
            int type = networkInfo.getType();
            if(type == ConnectivityManager.TYPE_MOBILE){//쓰리지나 LTE로 연결된것(모바일을 뜻한다.)
                return TYPE_MOBILE;
            }else if(type == ConnectivityManager.TYPE_WIFI){//와이파이 연결된것
                return TYPE_WIFI;
            }
        }
        return TYPE_NOT_CONNECTED;  //연결이 되지않은 상태
    }
}
