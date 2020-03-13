package com.sincar.customer.adapter.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressContent {

    /**
     * An array of AddressItem items.
     */
    public static final List<AddressItem> ITEMS = new ArrayList<>();

    /**
     * A map of AddressItem items, by ID.
     */
    public static final Map<Integer, AddressItem> ITEM_MAP = new HashMap();

    private static final int COUNT = 5;

    static {
        // Add some sample items.
        // TODO - 서버 연동 작업 후 Dummy 아이템 추가 코드 삭제 필요
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    public static void addItem(AddressItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static AddressItem createDummyItem(int position) {
        return new AddressItem(position, "송파구청","송파구 송파동 32");
    }

    /**
     * A AddressItem item representing a piece of content.
     */
    public static class AddressItem {
        public final int id;
        public final String title;
        public final String description;

        public AddressItem(int id, String title, String description) {
            this.id = id;
            this.title = title;
            this.description = description;
        }
    }
}
