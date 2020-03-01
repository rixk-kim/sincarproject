package com.sincar.customer.entity;

import com.sincar.customer.network.DataObject;

import java.util.ArrayList;

/**
 * 등록차량
 */
public class RegisterCarDataEntity {
    private String totalNum;               // 전체갯수

    private ArrayList<DataObject> registerCarObject;     //등록차량 데이타 리스트


    public String getTotalPage() {
        return totalNum;
    }
    public void setTotalPage(String totalNum) {
        this.totalNum = totalNum;
    }

    /**
     * 카드 seq
     * 제조사
     * 모델명
     * 차량번호
     */
    public ArrayList<DataObject> getRegisterCarObject() {
        return registerCarObject;
    }
    public void setRegisterCarObject(ArrayList<DataObject> registerCarObject) {
        this.registerCarObject = registerCarObject;
    }

}
