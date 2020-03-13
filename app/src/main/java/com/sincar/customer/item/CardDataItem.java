package com.sincar.customer.item;

public class CardDataItem {
    public String CARD_SEQ;
    public String CARD_NAME;
    public String CARD_NUMBER;
    public String CARD_SELECTED;


    @Override
    public String toString() {
        return "CardItem{" +
                "CARD_SEQ='" + CARD_SEQ + '\'' +
                ", CARD_NAME='" + CARD_NAME + '\'' +
                ", CARD_NUMBER='" + CARD_NUMBER + '\'' +
                ", CARD_SELECTED='" + CARD_SELECTED + '\'' +
                "}";
    }
}
