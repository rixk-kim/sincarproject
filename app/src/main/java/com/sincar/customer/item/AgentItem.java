package com.sincar.customer.item;

import java.util.ArrayList;

public class AgentItem {
//    public String SEQ;
//    public String NAME;
//    public String AGENT_NAME;
//    public String WASH_AREA;
//
//    public ArrayList<TimeItem> TIME_INFO = new ArrayList();
//
//    @Override
//    public String toString() {
//        return "AgentItem{" +
//                ", SEQ='" + SEQ + '\'' +
//                ", NAME='" + NAME + '\'' +
//                ", AGENT_NAME='" + AGENT_NAME + '\'' +
//                ", WASH_AREA='" + WASH_AREA + '\'' +
//                ", TIME_INFO=" + TIME_INFO +
//                '}';
//    }
    public String TOTAL;
    public String CURRENT_PAGE;
    public String CURRENT_NUM;

    @Override
    public String toString() {
        return "AgentItem{" +
                "TOTAL='" + TOTAL + '\'' +
                ", CURRENT_PAGE='" + CURRENT_PAGE + '\'' +
                ", CURRENT_NUM='" + CURRENT_NUM + '\'' +
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