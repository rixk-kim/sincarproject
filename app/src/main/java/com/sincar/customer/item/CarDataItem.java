package com.sincar.customer.item;

public class CarDataItem {
    public String CAR_SEQ;
    public String CAR_COMPANY;
    public String CAR_MODEL;
    public String CAR_NUMBER;
    public String CAR_SELECTED;


    @Override
    public String toString() {
        return "CarItem{" +
                "CAR_SEQ='" + CAR_SEQ + '\'' +
                ", CAR_COMPANY='" + CAR_COMPANY + '\'' +
                ", CAR_MODEL='" + CAR_MODEL + '\'' +
                ", CAR_NUMBER='" + CAR_NUMBER + '\'' +
                ", CAR_SELECTED='" + CAR_SELECTED + '\'' +
                "}";
    }
}
