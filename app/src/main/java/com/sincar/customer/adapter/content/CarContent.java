package com.sincar.customer.adapter.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarContent {

    /**
     * An array of CardItem items.
     */
    public static final List<CarItem> ITEMS = new ArrayList<CarItem>();

    /**
     * A map of CardItem items, by ID.
     */
    public static final Map<Integer, CarItem> ITEM_MAP = new HashMap();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        // TODO - 서버 연동 작업 후 Dummy 아이템 추가 코드 삭제 필요
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(CarItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static CarItem createDummyItem(int position) {
        return new CarItem(position, "포드",String.valueOf(position), "익스플로러 스포츠 트랙", "12가 1234",false);
    }

    /**
     * A CarItem item representing a piece of content.
     */
    public static class CarItem {
        public final int id;
        public final String car_title;
        public final String car_seq;
        public final String car_name;
        public final String car_number;

        public boolean car_selected;

        public CarItem(int id, String title, String seq, String name, String car_number, boolean car_selected) {
            this.id = id;
            this.car_title  = title;
            this.car_seq    = seq;
            this.car_name   = name;
            this.car_number = car_number;
            this.car_selected = car_selected;
        }
    }
}

