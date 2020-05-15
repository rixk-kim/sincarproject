package com.sincar.customer.item;

public class CarRegisterItem {
    public String REGISTER_RESULT;
    public String CAUSE;
    public String CAR_PAY;
    public String CAR_TYPE;

    @Override
    public String toString() {
        return "CarRegisterItem{" +
                "REGISTER_RESULT='" + REGISTER_RESULT + '\'' +
                ", CAUSE='" + CAUSE + '\'' +
                ", CAR_PAY='" + CAR_PAY + '\'' +
                ", CAR_TYPE='" + CAR_TYPE + '\'' +
                '}';
    }
}
