package com.sincar.customer.item;

public class CardItem {
    public String TOTAL;
    public String CURRENT_PAGE;
    public String CURRENT_NUM;

    @Override
    public String toString() {
        return "CardItem{" +
                "TOTAL='" + TOTAL + '\'' +
                ", CURRENT_PAGE='" + CURRENT_PAGE + '\'' +
                ", CURRENT_NUM='" + CURRENT_NUM + '\'' +
                '}';
    }
}
