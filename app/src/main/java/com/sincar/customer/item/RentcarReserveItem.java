package com.sincar.customer.item;

public class RentcarReserveItem {
    public String RENTCAR_response;
    public String RENTCAR_message;
    public String RENTCAR_MYPOINT;

    @Override
    public String toString() {
        return "ReserveItem{" +
                "RESERVE_RESULT='" + RENTCAR_response + '\'' +
                ", CAUSE='" + RENTCAR_message + '\'' +
                ", MY_POINT='" + RENTCAR_MYPOINT + '\'' +
                '}';
    }
}
