package com.sincar.customer.entity;

public class LoginDataEntity
{
    private String memberNo;            // 회원번호
    private String appVersion;          // App 버전
    private String apkDownloadUrl;      // App 다운로드 URL
    private String memberName;          // 회원이름
    private String memberPhone;         // 회원전화번호

    private String memberRecomCode;     // 내 추천코드
    private String profileDownloadUrl;  // 프로필 다운로드 URL
    private String licenseDownloadUrl;  // 면허증 다운로드 URL
    private String adNum;               // 광고갯수
    private String[] adDownloadImageUrl;    //광고 다운로드 이미지 URL

    public String getMemberNo() {
        return memberNo;
    }
    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getAppVersion() {
        return appVersion;
    }
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getApkDownloadUrl() {
        return apkDownloadUrl;
    }
    public void setApkDownloadUrl(String apkDownloadUrl) {
        this.apkDownloadUrl = apkDownloadUrl;
    }

    public String getMemberName() {
        return memberName;
    }
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberPhone() {
        return memberPhone;
    }
    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getMemberRecomCode() {
        return memberRecomCode;
    }
    public void setMemberRecomCode(String memberRecomCode) {
        this.memberRecomCode = memberRecomCode;
    }

    public String getProfileDownloadUrl() {
        return profileDownloadUrl;
    }
    public void setProfileDownloadUrl(String profileDownloadUrl) {
        this.profileDownloadUrl = profileDownloadUrl;
    }

    public String getLicenseDownloadUrl() {
        return licenseDownloadUrl;
    }
    public void setLicenseDownloadUrl(String licenseDownloadUrl) {
        this.licenseDownloadUrl = licenseDownloadUrl;
    }

    public String getAdNum() {
        return adNum;
    }
    public void setAdNum(String adNum) {
        this.adNum = adNum;
    }

    public String[] getAdDownloadImageUrl() {
        return adDownloadImageUrl;
    }
    public void setAdDownloadImageUrl(String[] inDate) {
        this.adDownloadImageUrl = adDownloadImageUrl;
    }
}
