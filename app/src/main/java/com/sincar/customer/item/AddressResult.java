package com.sincar.customer.item;

import java.util.ArrayList;

public class AddressResult {

    public ArrayList<AddressItem> search_list = new ArrayList();
    public ArrayList<AddressDataItem> data = new ArrayList();

    @Override
    public String toString() {
        return "AddressResult{" +
                "search_list=" + search_list +
                ", data=" + data +
                '}';
    }
}

