package com.sincar.customer.item;

public class UseDataItem {
    public String SEQ;
    public String RESERVE_STATUS;
    public String RESERVE_TIME;
    public String CANCEL_TIME;
    public String WASH_ADDRESS;
    public String AGENT;
    public String USE_PAY;
    public String RESERVE_NAME;
    public String RESERVE_PHONE;
    public String COMMON_PAY;
    public String COUPONE;
    public String CHARGE_INFO;
    public String CAR_MODEL;
    public String CAR_NUMBER;

    @Override
    public String toString() {
        return "DataItem{" +
                "SEQ='" + SEQ + '\'' +
                ", RESERVE_STATUS='" + RESERVE_STATUS + '\'' +
                ", RESERVE_TIME='" + RESERVE_TIME + '\'' +
                ", CANCEL_TIME='" + CANCEL_TIME + '\'' +
                ", WASH_ADDRESS='" + WASH_ADDRESS + '\'' +
                ", AGENT='" + AGENT + '\'' +
                ", USE_PAY='" + USE_PAY + '\'' +
                ", RESERVE_NAME='" + RESERVE_NAME + '\'' +
                ", RESERVE_PHONE='" + RESERVE_PHONE + '\'' +
                ", COMMON_PAY='" + COMMON_PAY + '\'' +
                ", COUPONE='" + COUPONE + '\'' +
                ", CHARGE_INFO='" + CHARGE_INFO + '\'' +
                ", CAR_MODEL='" + CAR_MODEL + '\'' +
                ", CAR_NUMBER='" + CAR_NUMBER + '\'' +
                "}";
    }
}
