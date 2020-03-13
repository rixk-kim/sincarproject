package com.sincar.customer.item;

import java.util.ArrayList;

public class CarResult {

    public ArrayList<CarItem> car_list = new ArrayList();
    public ArrayList<CarDataItem> DATA = new ArrayList();

    @Override
    public String toString() {
        return "CarResult{" +
                "car_list=" + car_list +
                ", data=" + DATA +
                '}';
    }
}
