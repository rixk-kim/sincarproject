package com.sincar.customer.item;

import java.util.ArrayList;

public class PointResult {
    public ArrayList<PointItem> point_list = new ArrayList();
    public ArrayList<PointDataItem> data = new ArrayList();

    @Override
    public String toString() {
        return "PointResult{" +
                "point_list=" + point_list +
                ", data=" + data +
                '}';
    }
}
