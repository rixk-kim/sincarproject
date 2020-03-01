package com.sincar.customer.entity;

import com.sincar.customer.network.DataObject;

import java.util.ArrayList;

/**
 * 등록카드
 */
public class CardDataEntity {
    private String totalPage;               // 전체페이지
    private String currentNum;              // 현재갯수

    private ArrayList<DataObject> cardObject;     //카드 데이타 리스트


    public String getTotalPage() {
        return totalPage;
    }
    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }

    public String getCurrentNum() {
        return currentNum;
    }
    public void setCurrentNum(String currentNum) {
        this.currentNum = currentNum;
    }

    public ArrayList<DataObject> getCardObject() {
        return cardObject;
    }
    public void setCardObject(ArrayList<DataObject> cardObject) {
        this.cardObject = cardObject;
    }

}



