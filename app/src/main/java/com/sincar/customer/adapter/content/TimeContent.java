package com.sincar.customer.adapter.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeContent {

    /**
     * An array of TimeItem items.
     */
    public static final List<TimeItem> ITEMS = new ArrayList<>();

    /**
     * A map of TimeItem items, by ID.
     */
    public static final Map<Integer, TimeItem> ITEM_MAP = new HashMap();

    private static final int COUNT = 12;

    static {
        // Add some sample items.
        // TODO - 서버 연동 작업 후 Dummy 아이템 추가 코드 삭제 필요
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(TimeItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.position, item);
    }

    private static TimeItem createDummyItem(int position) {
        return new TimeItem(0,position-1,position + ":00", true, false);
    }

    /**
     * A TimeItem item representing a piece of content.
     */
    public static class TimeItem {
        public int agentPosition;  // 점주 position
        public int position;       // Time position

        public String reservation_time;
        public boolean enable;
        public boolean selected;

        public TimeItem(int agentPosition, int position,
                              String reservation_time, boolean enable, boolean selected) {
            this.agentPosition = agentPosition;
            this.position = position;
            this.reservation_time = reservation_time;
            this.enable = enable;
            this.selected = selected;
        }
    }
}
