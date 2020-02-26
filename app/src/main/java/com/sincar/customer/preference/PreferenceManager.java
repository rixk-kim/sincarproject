package com.sincar.customer.preference;

import android.content.Context;
import android.text.TextUtils;

/**
 * 2020.02.17 spirit
 * 프리퍼런스 관리
 * 1. 자동 로그인 : boolean (true : 자동 로그인 체크, false : 자동 로그인 해제)
 */
public class PreferenceManager {

    private static PreferenceManager preferenceManager;
    private Context mContext;
    private PreferenceData preferenceData;

    public static PreferenceManager getInstance() {
        if (preferenceManager == null) {
            synchronized (PreferenceManager.class) {
                preferenceManager = new PreferenceManager();
            }
        }
        return preferenceManager;
    }

    public PreferenceManager() {

    }

    public void init(Context context) {
        mContext = context;
        preferenceData = PreferenceData.newInstance(mContext);
    }

    // uuid
    public void setDeviceUUID(String uuid) {
        preferenceData.putString("UUID", uuid);
    }

    // uuid
    public String getDeviceUUID() {
        return preferenceData.getString("UUID", "");
    }

    // 업체 코드 셋팅
    public void setCompanyCode(String code) {
        preferenceData.putString("COMPANY_CODE", code);
    }

    // 업체 코드 확인
    public String getCompanyCode() {
        return preferenceData.getString("COMPANY_CODE", "");
    }

    // 업체 명 셋팅
    public void setCompanyName(String name) {
        preferenceData.putString("COMPANY_NAME", name);
    }

    // 업체 명 확인
    public String getCompanyName() {
        return preferenceData.getString("COMPANY_NAME", "");
    }

    // 자동 로그인 셋팅
    public void setCheckLogin(boolean isAutoLogin) {
        preferenceData.putBoolean("AUTO_LOGIN", isAutoLogin);
    }

    // 자동 로그인 값 확인
    public boolean getCheckLogin() {
        return preferenceData.getBoolean("AUTO_LOGIN", false);
    }

    // 자동 로그인 시 아이디 셋팅
    public void setUserId(String userId) {
        preferenceData.putString("USER_ID", userId);
    }

    // 자동 로그인 아이디 확인
    public String getUserId() {
        return preferenceData.getString("USER_ID", "");
    }

    // 자동 로그인 시 패스워드 셋팅
    public void setUserPwd(String pwd) {
        preferenceData.putString("USER_PWD", pwd);
    }

    // 자동 로그인 패스워드 확인
    public String getUserPwd() {
        return preferenceData.getString("USER_PWD", "");
    }

//    // 화면 꺼짐 셋팅
//    public void setDisplayMode(Boolean display_mode) {
//        preferenceData.putBoolean("DISPLAY_MODE", display_mode);
//    }
//
//    // 화면 꺼짐 확인
//    public Boolean getDisplayMode() {
//        return preferenceData.getBoolean("DISPLAY_MODE", false);
//    }
//
//    // 주야간 모드 셋팅
//    public void setSunsetMode(Boolean mode) {
//        preferenceData.putBoolean("SUNSET_MODE", mode);
//    }
//
//    // 주야간 모드 확인
//    public Boolean getSunsetMode() {
//        return preferenceData.getBoolean("SUNSET_MODE", false);
//    }
//
//
//    // 폰트 크기 셋팅
//    public void setFont(String font_level) {
//        preferenceData.putString("FONT_LEVEL", font_level);
//    }
//
//    // 폰트 크기 확인
//    public String getFont() {
//        String value = preferenceData.getString("FONT_LEVEL", "");
//        if (TextUtils.isEmpty(value)) {
//            return "1";
//        }else {
//            return value;
//        }
//    }
//
//    // 자동 수신 셋팅
//    public void setAutoReceive (String auto_time ) {
//        preferenceData.putString("AUTO_RECEIVE" , auto_time);
//    }
//
//    // 자동 수신 확인
//    public String getAutoReceive () {
//        return preferenceData.getString("AUTO_RECEIVE", "");
//    }



    // 사용자 고유키
    public void setUid(String uid) {
        preferenceData.putString("UID", uid);
    }

    public String getUid() {
        return preferenceData.getString("UID", "");
    }

}
