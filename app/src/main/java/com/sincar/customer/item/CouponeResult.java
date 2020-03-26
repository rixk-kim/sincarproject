package com.sincar.customer.item;

import java.util.ArrayList;

public class CouponeResult {

    public ArrayList<CouponeItem> coupone_list = new ArrayList();
    public ArrayList<CouponeDataItem> data = new ArrayList();

    @Override
    public String toString() {
        return "CouponeResult{" +
                "coupone_list=" + coupone_list +
                ", data=" + data +
                '}';
    }
}
