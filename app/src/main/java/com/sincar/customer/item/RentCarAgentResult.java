package com.sincar.customer.item;

import java.util.ArrayList;

public class RentCarAgentResult {

    public ArrayList<RentCarAgentItem> rentcar_list = new ArrayList();
    public ArrayList<RentCarAgentDataItem> data = new ArrayList();

    @Override
    public String toString() {
        return "AgentResult{" +
                "agent_list=" + rentcar_list +
                ", DATA=" + data +
                '}';
    }
}
//
// {"rentcar_list": [{"TOTAL":"100","CURRENT_PAGE":"1","CURRENT_NUM":"20"}],
//         "data": [{"RENTCAR_SEQ":"1","RENTCAR_NAME":"현대 그랜저 HG300","RENTCAR_IMG_URL":"http:// ~~",...},
//         {"RENTCAR_SEQ":"2","RENTCAR_NAME":"벤츠 S 클래스 S 450 4Matic","RENTCAR_IMG_URL":"http:// ~~",...}..]}