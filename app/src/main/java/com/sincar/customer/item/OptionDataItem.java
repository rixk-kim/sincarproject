package com.sincar.customer.item;

public class OptionDataItem {
    public String SEQ;
    public String SERVICE_NAME;
    public String SERVICE_DETAIL;
    public String USE_PAY;

    @Override
    public String toString() {
        return "OptionItem{" +
                "SEQ='" + SEQ + '\'' +
                ", SERVICE_NAME='" + SERVICE_NAME + '\'' +
                ", SERVICE_DETAIL='" + SERVICE_DETAIL + '\'' +
                ", USE_PAY='" + USE_PAY + '\'' +
                "}";
    }
}
