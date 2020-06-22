package com.sincar.customer.item;

import java.util.ArrayList;

public class RentCarAgentResult {

    public ArrayList<RentCarAgentItem> agent_list = new ArrayList();
    public ArrayList<RentCarAgentDataItem> DATA = new ArrayList();

    @Override
    public String toString() {
        return "AgentResult{" +
                "agent_list=" + agent_list +
                ", DATA=" + DATA +
                '}';
    }


}
