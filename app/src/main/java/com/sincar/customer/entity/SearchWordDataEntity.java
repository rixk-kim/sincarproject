package com.sincar.customer.entity;

import com.sincar.customer.network.DataObject;

import java.util.ArrayList;

/**
 * 최근 검색어
 */
public class SearchWordDataEntity {
    private String totalPage;               // 전체페이지
    private String currentPage;             // 현재페이지
    private String currentNum;              // 현재갯수

    private ArrayList<DataObject> searchWordObject;     //최근 검색 데이타 리스트


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
     * 검색어 seq
     * 최근 검색어
     */
    public ArrayList<DataObject> getSearchWordObject() {
        return searchWordObject;
    }
    public void setSearchWordObject(ArrayList<DataObject> searchWordObject) {
        this.searchWordObject = searchWordObject;
    }

}
