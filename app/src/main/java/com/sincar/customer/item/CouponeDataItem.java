package com.sincar.customer.item;

public class CouponeDataItem {
    public String COUPONE_SEQ;
    public String COUPONE_TITLE;
    public String COUPONE_CONTENT;
    public String COUPONE_DATE;
    public String COUPONE_YN;


    @Override
    public String toString() {
        return "DataItem{" +
                "COUPONE_SEQ='" + COUPONE_SEQ + '\'' +
                ", COUPONE_TITLE='" + COUPONE_TITLE + '\'' +
                ", COUPONE_CONTENT='" + COUPONE_CONTENT + '\'' +
                ", COUPONE_DATE='" + COUPONE_DATE + '\'' +
                ", COUPONE_YN='" + COUPONE_YN + '\'' +
                "}";
    }
}

