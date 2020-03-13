package com.sincar.customer.adapter.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PointContent {

    /**
     * An array of PointItem items.
     */
    public static final List<PointItem> ITEMS = new ArrayList<PointItem>();

    /**
     * A map of PointItem items, by ID.
     */
    public static final Map<Integer, PointItem> ITEM_MAP = new HashMap();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        // 서버 연동 작업 후 Dummy 아이템 추가 코드 삭제 필요
//        for (int i = 1; i <= COUNT; i++) {
//            addItem(createDummyItem(i));
//        }
    }

    public static void addItem(PointItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static PointItem createDummyItem(int position) {
        return new PointItem(position, "김민정","스팀세차", "20.03.01", "+100");
    }

//    private static PointItem makeDetails(int position) {
//        StringBuilder builder = new StringBuilder();
//        builder.append("Details about Item: ").append(position);
//        for (int i = 0; i < position; i++) {
//            builder.append("\nMore details information here.");
//        }
//        return builder.toString();
//        return ITEM_MAP.get(position);
//    }

    /**
     * A PointItem item representing a piece of content.
     */
    public static class PointItem {
        public final int id;
        public final String username;
        public final String service_type;
        public final String date;
        public final String point;

        public PointItem(int id, String username, String service_type, String date, String point) {
            this.id = id;
            this.username = username;
            this.service_type = service_type;
            this.date = date;
            this.point = point;
        }
    }
}
