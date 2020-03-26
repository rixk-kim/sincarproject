package com.sincar.customer.item;

public class ReserveCancelItem {
    public String RESERVE_RESULT;
    public String CAUSE;

    @Override
    public String toString() {
        return "ReserveCancelItem{" +
                "RESERVE_RESULT='" + RESERVE_RESULT + '\'' +
                ", CAUSE='" + CAUSE + '\'' +
                '}';
    }
}
