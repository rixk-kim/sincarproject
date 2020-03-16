package com.sincar.customer.adapter.content;

import com.sincar.customer.item.TimeItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentContent {

    /**
     * An array of AgentItem items.
     */
    public static final List<AgentItem> ITEMS = new ArrayList<>();

    /**
     * A map of AgentItem items, by ID.
     */
    public static final Map<Integer, AgentItem> ITEM_MAP = new HashMap();

    private static final int COUNT = 5;

    static {
        // Add some sample items.
        // TODO - 서버 연동 작업 후 Dummy 아이템 추가 코드 삭제 필요
        for (int i = 1; i <= COUNT; i++) {
  //          addItem(createDummyItem(i));
        }
    }

    public static void addItem(AgentItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static void clearItem() {
        ITEMS.clear();// .add(item);
        ITEM_MAP.clear();   // .put(item.id, item);
    }

//    private static AgentItem createDummyItem(int position) {
//        return new AgentItem(position-1,
//                "",
//                "김태현",
//                "관악구 1호점",
//                "관악구,서초구,송파구,강동구",
//                "7/8/9/10/11/12/1/2/3/4/5/6/7");
//    }

    /**
     * A AgentItem item representing a piece of content.
     */
    public static class AgentItem {
        public final int id;

        public final String agent_photo;
        public final String agent_name;
        public final String branch_area;
        public final String wash_area;
        public final ArrayList<TimeItem> reserve_info;

        public AgentItem(int id,
                              String agent_photo,
                              String agent_name,
                              String branch_area,
                              String wash_area,
                         ArrayList<TimeItem> reserve_info) {
            this.id = id;
            this.agent_photo = agent_photo;
            this.agent_name = agent_name;
            this.branch_area = branch_area;
            this.wash_area = wash_area;
            this.reserve_info = reserve_info;
        }
    }
}
