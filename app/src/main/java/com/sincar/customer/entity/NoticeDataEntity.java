package com.sincar.customer.entity;

import com.sincar.customer.network.DataObject;

import java.util.ArrayList;

/**
 * 공지사항
 */
public class NoticeDataEntity {
    private String totalPage;               // 전체페이지
    private String currentPage;             // 현재페이지
    private String currentNum;              // 현재갯수

    private ArrayList<DataObject> noticeObject;     //이용내역 데이타 리스트

//    private String[] seq;                   // seq
//    private String[] title;                 // 제목
//    private String[] reg_date;              // 등록날짜
//    private String[] contents;              // 내용

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

    public String getCurrentNum() {
        return currentNum;
    }
    public void setCurrentNum(String currentNum) {
        this.currentNum = currentNum;
    }

    /**
     * seq
     * 제목
     * 등록날짜
     * 내용
     */
    public ArrayList<DataObject> getNoticeObject() {
        return noticeObject;
    }
    public void setNoticeObject(ArrayList<DataObject> noticeObject) {
        this.noticeObject = noticeObject;
    }

//    public String[] getSeq() {
//        return seq;
//    }
//    public void setSeq(String[] seq) {
//        this.seq = seq;
//    }
//
//    public String[] getTitle() {
//        return title;
//    }
//    public void setTitle(String[] title) {
//        this.title = title;
//    }
//
//    public String[] getReg_date() {
//        return reg_date;
//    }
//    public void setReg_date(String[] reg_date) {
//        this.reg_date = reg_date;
//    }
//
//    public String[] getContents() {
//        return contents;
//    }
//    public void setContents(String[] wash_address) {
//        this.contents = contents;
//    }
}


