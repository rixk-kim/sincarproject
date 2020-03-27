package com.sincar.customer.item;

import java.util.ArrayList;

public class ReserveResult {
    public ArrayList<ReserveItem> reserve = new ArrayList();

    @Override
    public String toString() {
        return "ReserveResult{" +
                "reserve=" + reserve +
                '}';
    }
}
