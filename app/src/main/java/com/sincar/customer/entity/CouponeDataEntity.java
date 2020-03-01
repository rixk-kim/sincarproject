package com.sincar.customer.entity;

import com.sincar.customer.network.DataObject;
import java.util.ArrayList;

public class CouponeDataEntity {
    private String totalPage;               // 전체페이지
    private String currentPage;             // 현재페이지
    private String currentNum;              // 현재갯수

    private ArrayList<DataObject> couponeObject;     //쿠폰 데이타 리스트


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
     * 쿠폰 seq
     * 제목
     * 내용
     * 유효기간
     * 사용유무
    */
    public ArrayList<DataObject> getCouponeObject() {
        return couponeObject;
    }
    public void setCouponeObject(ArrayList<DataObject> couponeObject) {
        this.couponeObject = couponeObject;
    }

}
