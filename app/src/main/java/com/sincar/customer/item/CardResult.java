package com.sincar.customer.item;

import java.util.ArrayList;

public class CardResult {

    public ArrayList<CardItem> card_list = new ArrayList();
    public ArrayList<CardDataItem> DATA = new ArrayList();

    @Override
    public String toString() {
        return "CardResult{" +
                "card_list=" + card_list +
                ", data=" + DATA +
                '}';
    }
}
