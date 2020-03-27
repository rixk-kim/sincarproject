package com.sincar.customer.item;

import java.util.ArrayList;

public class SearchWordResult {
    public ArrayList<SearchWordItem> address_search = new ArrayList();
    public ArrayList<SearchWordDataItem> DATA = new ArrayList();

    @Override
    public String toString() {
        return "SearchWordResult{" +
                "address_search=" + address_search +
                ", DATA=" + DATA +
                '}';
    }
}
