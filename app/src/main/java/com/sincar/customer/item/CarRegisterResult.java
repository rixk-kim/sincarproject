package com.sincar.customer.item;

import java.util.ArrayList;

public class CarRegisterResult {

    public ArrayList<CarRegisterItem> car_register = new ArrayList();
//    public ArrayList<CarDataItem> DATA = new ArrayList();

    @Override
    public String toString() {
        return "CarRegisterResult{" +
                "car_register=" + car_register +
                '}';
    }
}
