package com.sincar.customer.item;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class RentcarReserveResult {
    public ArrayList<RentcarReserveItem> rentcarReserve = new ArrayList<>();

    @NonNull
    @Override
    public String toString() {
        return "RentcarReserveResult{" +
                "rentcarReserve=" + rentcarReserve +
                '}';
    }
}
