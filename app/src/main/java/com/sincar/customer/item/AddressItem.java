package com.sincar.customer.item;

public class AddressItem {
    public String TOTAL;
    public String CURRENT_PAGE;
    public String CURRENT_NUM;

    @Override
    public String toString() {
        return "AddressItem{" +
                "TOTAL='" + TOTAL + '\'' +
                ", CURRENT_PAGE='" + CURRENT_PAGE + '\'' +
                ", CURRENT_NUM='" + CURRENT_NUM + '\'' +
                '}';
    }
}