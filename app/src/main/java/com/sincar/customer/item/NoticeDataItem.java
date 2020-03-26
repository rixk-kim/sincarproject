package com.sincar.customer.item;

public class NoticeDataItem {
    public String SEQ;
    public String TITLE;
    public String REG_DATE;
    public String CONTENT;

    @Override
    public String toString() {
        return "DataItem{" +
                "SEQ='" + SEQ + '\'' +
                ", TITLE='" + TITLE + '\'' +
                ", REG_DATE='" + REG_DATE + '\'' +
                ", CONTENT='" + CONTENT + '\'' +
                "}";
    }
}