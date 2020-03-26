package com.sincar.customer.item;

import java.util.ArrayList;

public class UseResult {
    public ArrayList<UseItem> use_list = new ArrayList();
    public ArrayList<UseDataItem> data = new ArrayList();

    @Override
    public String toString() {
        return "UseResult{" +
                "use_list=" + use_list +
                ", data=" + data +
                '}';
    }
}