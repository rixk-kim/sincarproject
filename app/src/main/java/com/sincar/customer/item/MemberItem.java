package com.sincar.customer.item;

public class MemberItem {
    public String MEMBER_RESULT;
    public String APP_VERSION;
    public String APK_URL;

    @Override
    public String toString() {
        return "MemberItem{" +
                "MEMBER_RESULT='" + MEMBER_RESULT + '\'' +
                ", APP_VERSION='" + APP_VERSION + '\'' +
                ", APK_URL='" + APK_URL + '\'' +
                '}';
    }
}
