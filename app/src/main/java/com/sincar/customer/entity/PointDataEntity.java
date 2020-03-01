package com.sincar.customer.entity;

import com.sincar.customer.network.DataObject;

import java.util.ArrayList;

/**
 * 포인트
 */
public class PointDataEntity {
    private String totalPage;           // 전체페이지
    private String currentPage;         // 현재페이지
    private String myPoint;             // 내 포인트
    private String seq;                 // seq

    private String inviteNum;           // 친구의 수
    private String inviteFriNum;        // 친구의 친구 수
    private String accumPoint;          // 누적 포인트

    private ArrayList<DataObject> pointObject;     //포인트 네역
//    private String[] friName;           //친구명
//    private String[] useService;        //이용서비스
//    private String[] saveDate;          //적립일자
//    private String[] friPoint;          //포인트


    public String getTotalPage() {
        return totalPage;
    }
    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }

    public String getCurrentPage() {
        return currentPage;
    }
    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getMyPoint() {
        return myPoint;
    }
    public void setMyPoint(String myPoint) {
        this.myPoint = myPoint;
    }

    public String getSeq() {
        return seq;
    }
    public void setSeq(String seq) {
        this.seq = seq;
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



    public ArrayList<DataObject> getPointObject() {
        return pointObject;
    }
    public void setPointObject(ArrayList<DataObject> pointObject) {
        this.pointObject = pointObject;
    }

//    public String[] getUseService() {
//        return useService;
//    }
//    public void setUseService(String[] inDate) {
//        this.useService = useService;
//    }
//
//    public String[] getSaveDate() {
//        return saveDate;
//    }
//    public void setSaveDate(String[] inDate) {
//        this.saveDate = saveDate;
//    }
//
//    public String[] getFriPoint() {
//        return friPoint;
//    }
//    public void setFriPoint(String[] inDate) {
//        this.friPoint = friPoint;
//    }
}
