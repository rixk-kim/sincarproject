package com.sincar.customer.item;

import java.util.ArrayList;

public class OptionResult {
    public ArrayList<OptionItem> add_service = new ArrayList();
    public ArrayList<OptionDataItem> DATA = new ArrayList();

    @Override
    public String toString() {
        return "OptionResult{" +
                "add_service=" + add_service +
                ", data=" + DATA +
                '}';
    }
}