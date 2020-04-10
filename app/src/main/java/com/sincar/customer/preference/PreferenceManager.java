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

    // 사용자 고유키
    public void setUid(String uid) {
        preferenceData.putString("UID", uid);
    }

    public String getUid() {
        return preferenceData.getString("UID", "");
    }

    // 버전체크 셋팅
    public void setVersionCheck(boolean isVersionCheck) {
        preferenceData.putBoolean("VERSION_CHECK", isVersionCheck);
    }

    // 자동 로그인 값 확인
    public boolean getVersionCheck() {
        return preferenceData.getBoolean("VERSION_CHECK", false);
    }
}
