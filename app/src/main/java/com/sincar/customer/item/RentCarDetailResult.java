package com.sincar.customer.item;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class RentCarDetailResult {

    public ArrayList<RentCarDetailItem> rentcar_detail = new ArrayList<>();

    @NonNull
    @Override
    public String toString() {
        return "RentCarDetailResult{" +
                "rentcar_detail=" + rentcar_detail + '}';
    }
}
