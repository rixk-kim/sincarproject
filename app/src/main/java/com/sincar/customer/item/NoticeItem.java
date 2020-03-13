package com.sincar.customer.item;

public class NoticeItem {
    public String TOTAL;
    public String CURRENT_PAGE;
    public String CURRENT_NUM;

    @Override
    public String toString() {
        return "NoticeItem{" +
                "TOTAL='" + TOTAL + '\'' +
                ", CURRENT_PAGE='" + CURRENT_PAGE + '\'' +
                ", CURRENT_NUM='" + CURRENT_NUM + '\'' +
                '}';
    }
}