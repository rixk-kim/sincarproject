package com.sincar.customer.item;

public class DataItem {
    public String FRI_NAME;
    public String USE_SERVICE;
    public String SAVE_DATE;
    public String FRI_POINT;

    @Override
    public String toString() {
        return "DataItem{" +
                "FRI_NAME='" + FRI_NAME + '\'' +
                ", USE_SERVICE='" + USE_SERVICE + '\'' +
                ", SAVE_DATE='" + SAVE_DATE + '\'' +
                ", FRI_POINT='" + FRI_POINT + '\'' +
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
//      {"FRI_NAME":"이하영","USE_SERVICE":"스팀세차","SAVE_DATE":"20.02.28","FRI_POINT":"120"}],
// "advertise":[
//      {"AD_IMAGE_URL":"http://~~"},
//      {"AD_IMAGE_URL":"http://~~"},
//      {"AD_IMAGE_URL":"http://~~"}
// ]}