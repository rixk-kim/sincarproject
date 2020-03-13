package com.sincar.customer.item;

public class AgentDataItem {
    public String SEQ;
    public String NAME;
    public String AGENT_NAME;
    public String WASH_AREA;
    public String RESERVE_TIME;

    @Override
    public String toString() {
        return "AgentItem{" +
                "SEQ='" + SEQ + '\'' +
                ", NAME='" + NAME + '\'' +
                ", AGENT_NAME='" + AGENT_NAME + '\'' +
                ", WASH_AREA='" + WASH_AREA + '\'' +
                ", RESERVE_TIME='" + RESERVE_TIME + '\'' +
                "}";
    }
}
