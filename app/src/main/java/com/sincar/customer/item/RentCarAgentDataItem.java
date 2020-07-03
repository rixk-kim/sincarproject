package com.sincar.customer.item;

import java.util.ArrayList;

public class RentCarAgentDataItem {


    public String RENTCAR_SEQ;
    public String RENTCAR_NAME;
    public String RENTCAR_IMG_URL;
    public String RENTCAR_AGENT;
    public String RENTCAR_AGENT_ADD;
    public String RENTCAR_DISCOUNT;
    public String RENTCAR_PRICE;
    public String RENTCAR_FIL_AGE;
    public String RENTCAR_FIL_TYPE;
    public String RENTCAR_FIL_BRAND;
    public String RENTCAR_SORT_DIST;
    public String RENTCAR_SORT_POPU;

    @Override
    public String toString() {
        return "RentCarAgentDataItem{" +
                ", RENTCAR_SEQ='" + RENTCAR_SEQ + '\'' +
                ", RENTCAR_NAME='" + RENTCAR_NAME + '\'' +
                ", RENTCAR_IMG_ULR='" + RENTCAR_IMG_URL + '\'' +
                ", RENTCAR_AGENT='" + RENTCAR_AGENT + '\'' +
                ", RENTCAR_AGENT_ADD='" + RENTCAR_AGENT_ADD + '\'' +
                ", RENTCAR_DISCOUNT'" + RENTCAR_DISCOUNT+ '\'' +
                ", RENTCAR_PRICE='" + RENTCAR_PRICE + '\'' +
                ", RENTCAR_FIL_AGE='" + RENTCAR_FIL_AGE + '\'' +
                ", RENTCAR_FIL_TYPE='" + RENTCAR_FIL_TYPE + '\'' +
                ", RENTCAR_FIL_BRAND'" + RENTCAR_FIL_BRAND + '\'' +
                ", RENTCAR_SORT_DIST'" + RENTCAR_SORT_DIST + '\'' +
                ", RENTCAR_SORT_POPU'" + RENTCAR_SORT_POPU + '\'' +
                '}';
    }
}
