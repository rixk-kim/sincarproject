package com.sincar.customer.item;

public class CarListDataItem {
    public String CAR_COMPANY_CODE;
//    public String CAR_COMPANY;
    public String CAR_NAME;
    public String CAR_CODE;
    public String CAR_WASH_PAY;

    @Override
    public String toString() {
        return "CarListDataItem{" +
                "CAR_COMPANY_CODE='" + CAR_COMPANY_CODE + '\'' +
//                "CAR_COMPANY='" + CAR_COMPANY + '\'' +
                "CAR_NAME='" + CAR_NAME + '\'' +
                "CAR_CODE='" + CAR_CODE + '\'' +
                "CAR_WASH_PAY='" + CAR_WASH_PAY + '\'' +
                '}';

    }
}
