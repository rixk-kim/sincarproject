package com.sincar.customer.item;

import java.util.ArrayList;

public class RentCarAgentDataItem {


    public String SEQ;
    public String NAME;
    public String AGENT_NAME;
    public String AGENT_IMG_URL;
    public String WASH_AREA;
    public String AGENT_STAUS;
    public String AGENT_ORDER;
    public String AGENT_NUMBER;

    @Override
    public String toString() {
        return "RentCarAgentDataItem{" +
                ", SEQ='" + SEQ + '\'' +
                ", NAME='" + NAME + '\'' +
                ", AGENT_NAME='" + AGENT_NAME + '\'' +
                ", AGENT_IMG_URL='" + AGENT_IMG_URL + '\'' +
                ", WASH_AREA='" + WASH_AREA + '\'' +
                ", AGENT_STAUS='" + AGENT_STAUS + '\'' +
                ", AGENT_ORDER='" + AGENT_ORDER + '\'' +
                ", AGENT_NUMBER='" + AGENT_NUMBER + '\'' +
                '}';
    }
}
