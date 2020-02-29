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
    private String myPoint;             // 내 포인트
    private String inviteNum;           // 초대한 친구 수

    private String inviteFriNum;        // 친구의 친구 수
    private String accumPoint;          // 누적 포인트
    private String adNum;               // 광고갯수

    private String[] friName;           //친구명
    private String[] useService;        //이용서비스
    private String[] saveDate;          //적립일자
    private String[] friPoint;          //포인트

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

    public String getMyPoint() {
        return myPoint;
    }
    public void setMyPoint(String myPoint) {
        this.myPoint = myPoint;
    }

    public String getAdNum() {
        return adNum;
    }
    public void setAdNum(String adNum) {
        this.adNum = adNum;
    }

    public String getInviteNum() {
        return inviteNum;
    }
    public void setInviteNum(String inviteNum) {
        this.inviteNum = inviteNum;
    }

    public String getInviteFriNumNum() {
        return inviteFriNum;
    }
    public void setInviteFriNumNum(String inviteFriNum) {
        this.inviteFriNum = inviteFriNum;
    }

    public String getAccumPoint() {
        return accumPoint;
    }
    public void setAccumPoint(String accumPoint) {
        this.accumPoint = accumPoint;
    }



    public String[] getFriName() {
        return friName;
    }
    public void setFriName(String[] inDate) {
        this.friName = friName;
    }

    public String[] getUseService() {
        return useService;
    }
    public void setUseService(String[] inDate) {
        this.useService = useService;
    }

    public String[] getSaveDate() {
        return saveDate;
    }
    public void setSaveDate(String[] inDate) {
        this.saveDate = saveDate;
    }

    public String[] getFriPoint() {
        return friPoint;
    }
    public void setFriPoint(String[] inDate) {
        this.friPoint = friPoint;
    }

    public String[] getAdDownloadImageUrl() {
        return adDownloadImageUrl;
    }
    public void setAdDownloadImageUrl(String[] inDate) {
        this.adDownloadImageUrl = adDownloadImageUrl;
    }
}
