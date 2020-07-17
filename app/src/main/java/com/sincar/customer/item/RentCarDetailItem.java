package com.sincar.customer.item;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class RentCarDetailItem {

    public String CURRENT_CAR_URL;
    public String CURRENT_CAR_NUM;
    public String CURRENT_CAR_FUEL;
    public String CURRENT_CAR_FPY;
    public String CURRENT_CAR_COLOR;
    public String CURRENT_CAR_MADE;
    public String CURRENT_CAR_AGE;
    public String CURRENT_CAR_MAN;
    public String CURRENT_AGENT_ADDRESS;
    public String CURRENT_AGENT_ADD_LAT;
    public String CURRENT_AGENT_ADD_LON;
    public String CURRENT_AGENT_TIME;
    public String CURRENT_AGENT_TIME_WKD;
    public String CURRENT_AGENT_DELI;
    public Current_agent_del_pri CURRENT_AGENT_DEL_PRI;
    public String CURRENT_AGENT_DEL_TIM;
    public String CURRENT_AGENT_DEL_POS;
    public String CURRENT_PRICE;
    public ArrayList<Rentcar_Insurance> CURRENT_INSURANCE = new ArrayList<>();

    public class Current_agent_del_pri {
        public String ROUND_TRIP;
        public String DISPATCH;
        public String RETURN;
    }

    @NonNull
    @Override
    public String toString() {
        return "rentcar_detail {" +
                ", CURRENT_CAR_URL='" + CURRENT_CAR_URL + '\'' +
                ", CURRENT_CAR_NUM='" + CURRENT_CAR_NUM + '\'' +
                ", CURRENT_CAR_FUEL='" + CURRENT_CAR_FUEL + '\'' +
                ", CURRENT_CAR_FPY='" + CURRENT_CAR_FPY + '\'' +
                ", CURRENT_CAR_COLOR='" + CURRENT_CAR_COLOR + '\'' +
                ", CURRENT_CAR_MADE='" + CURRENT_CAR_MADE + '\'' +
                ", CURRENT_CAR_AGE='" + CURRENT_CAR_AGE + '\'' +
                ", CURRENT_CAR_MAN='" + CURRENT_CAR_MAN + '\'' +
                ", CURRENT_AGENT_ADDRESS='" + CURRENT_AGENT_ADDRESS + '\'' +
                ", CURRENT_AGENT_ADD_LAT='" + CURRENT_AGENT_ADD_LAT + '\'' +
                ", CURRENT_AGENT_ADD_LON='" + CURRENT_AGENT_ADD_LON + '\'' +
                ", CURRENT_AGENT_TIME='" + CURRENT_AGENT_TIME + '\'' +
                ", CURRENT_AGENT_TIME_WKD='" + CURRENT_AGENT_TIME_WKD + '\'' +
                ", CURRENT_AGENT_DELI='" + CURRENT_AGENT_DELI + '\'' +
                ", CURRENT_AGENT_DEL_PRI='" + CURRENT_AGENT_DEL_PRI + '\'' +
                ", CURRENT_AGENT_DEL_TIM='" + CURRENT_AGENT_DEL_TIM + '\'' +
                ", CURRENT_AGENT_DEL_POS='" + CURRENT_AGENT_DEL_POS + '\'' +
                ", CURRENT_PRICE='" + CURRENT_PRICE + '\'' +
                ", CURRENT_INSURANCE='" + CURRENT_INSURANCE + '\'' + '}';
    }

    public class Rentcar_Insurance {
        public String CURRENT_INSU_SEQ;
        public String CURRENT_INSU_NAME;
        public String CURRENT_INSU_PRI;

        @NonNull
        @Override
        public String toString() {
            return "CURRENT_INSURANCE{" +
                    "CURRENT_INSU_SEQ=" + CURRENT_INSU_SEQ + '\'' +
                    "CURRENT_INSU_NAME=" + CURRENT_INSU_NAME + '\'' +
                    "CURRENT_INSU_PRI=" + CURRENT_INSU_PRI + '\'' +'}';
        }
    }
}
