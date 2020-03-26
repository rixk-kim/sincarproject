package com.sincar.customer.item;

public class PointDataItem {
    public String SEQ;
    public String FRI_NAME;
    public String USE_SERVICE;
    public String SAVE_DATE;
    public String FRI_POINT;

    @Override
    public String toString() {
        return "PointDataItem{" +
                "SEQ='" + SEQ + '\'' +
                ", FRI_NAME='" + FRI_NAME + '\'' +
                ", USE_SERVICE='" + USE_SERVICE + '\'' +
                ", SAVE_DATE='" + SAVE_DATE + '\'' +
                ", FRI_POINT='" + FRI_POINT + '\'' +
                "}";
    }
}
