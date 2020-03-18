package com.sincar.customer.item;

public class LoginItem {
    public String REGISTER;
    public String CAUSE;
    public String MEMBER_NO;
    public String VERSION;
    public String APK_URL;
    public String MEMBER_NAME;
    public String MEMBER_PHONE;
    public String MEMBER_RECOM_CODE;
    public String REGISTER_RECOM_CODE;
    public String PROFILE_DOWN_URL;
    public String LICENSE_DOWN_URL;
    public String AD_NUM;
    public String MY_POINT;
    public String INVITE_NUM;       //초대한 친구 명수
    public String INVITE_FRI_NUM;   //친구의 친구 명수
    public String ACCUM_POINT;      //누적포인트

    @Override
    public String toString() {
        return "LoginItem{" +
                "REGISTER='" + REGISTER + '\'' +
                ", CAUSE='" + CAUSE + '\'' +
                ", MEMBER_NO='" + MEMBER_NO + '\'' +
                ", VERSION='" + VERSION + '\'' +
                ", APK_URL='" + APK_URL + '\'' +
                ", MEMBER_NAME='" + MEMBER_NAME + '\'' +
                ", MEMBER_PHONE='" + MEMBER_PHONE + '\'' +
                ", MEMBER_RECOM_CODE='" + MEMBER_RECOM_CODE + '\'' +
                ", REGISTER_RECOM_CODE='" + REGISTER_RECOM_CODE + '\'' +
                ", PROFILE_DOWN_URL='" + PROFILE_DOWN_URL + '\'' +
                ", LICENSE_DOWN_URL='" + LICENSE_DOWN_URL + '\'' +
                ", AD_NUM='" + AD_NUM + '\'' +
                ", MY_POINT='" + MY_POINT + '\'' +
                ", INVITE_NUM='" + INVITE_NUM + '\'' +
                ", INVITE_FRI_NUM='" + INVITE_FRI_NUM + '\'' +
                ", ACCUM_POINT='" + ACCUM_POINT + '\'' +
                '}';
    }
}

// {login: [
//      {"REGISTER":"1",
//      "CAUSE":"비밀번호 오류",
//      "MEMBER_NO":"12345",
//      "VERSION":"1.0.1",
//      "APK_URL":"http://sincar.co.kr/apk/manager_1.0.1.apk",
//      "MEMBER_NAME":"신차로",
//      "MEMBER_PHONE":"01012345678",
//      "MEMBER_RECOM_CODE":"FCF816",
//      "PROFILE_DOWN_URL":"http://~~",
//      "LICENSE_DOWN_URL":"http://~~",
//      "AD_NUM":"3",
//      "MY_POINT":"5,430",
//      "INVITE_NUM":"7",
//      "INVITE_FRI_NUM":"7",
//      "ACCUM_POINT":"3,870"}
//  ],
// "DATA":[
//      {"FRI_NAME":"김민정","USE_SERVICE":"스팀세차","SAVE_DATE":"20.02.26","FRI_POINT":"100"},
//      {"FRI_NAME":"이하영","USE_SERVICE":"스팀세차","SAVE_DATE":"20.02.28","FRI_POINT":"120"}
//  ],
// "advertise":[
//      {"AD_IMAGE_URL":"http://~~"},
//      {"AD_IMAGE_URL":"http://~~"},
//      {"AD_IMAGE_URL":"http://~~"}
// ]}