package com.sincar.customer.item;

import java.util.ArrayList;

public class AgentResult {

    public ArrayList<AgentItem> agent_list = new ArrayList();
    public ArrayList<AgentDataItem> DATA = new ArrayList();

    @Override
    public String toString() {
        return "AgentResult{" +
                "agent_list=" + agent_list +
                ", data=" + DATA +
                '}';
    }
}