package com.sincar.customer.item;

import java.util.ArrayList;

public class AgentDataItem {
    public ArrayList<AgentItem> DATA = new ArrayList();

    @Override
    public String toString() {
        return "AgentDataItem{" +
                "DATA=" + DATA +
                '}';
    }
}


// {"agent_list":
//	{"TOTAL":"3"}
//	,
//	"DATA":[
//		{"SEQ":"1","NAME":"김태현","AGENT_NAME":"관악 1호점","WASH_AREA":"관악구, 금천구,영등포구",
//		"TIME_INFO":[{"RESERVE_TIME":"07:00", "RESERVE_STATUS":"Y"},{"RESERVE_TIME":"08:00", "RESERVE_STATUS":"Y"},...]},
//		{"SEQ":"1","NAME":"김태현","AGENT_NAME":"관악 1호점","WASH_AREA":"관악구, 금천구,영등포구",
//			"예약정보":[{"RESERVE_TIME":"07:00", "RESERVE_STATUS":"Y"},{"RESERVE_TIME":"08:00", "RESERVE_STATUS":"Y"},...
//	]
//}