package com.sincar.customer.adapter.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardContent {

    /**
     * An array of CardItem items.
     */
    public static final List<CardItem> ITEMS = new ArrayList<CardItem>();

    /**
     * A map of CardItem items, by ID.
     */
    public static final Map<Integer, CardItem> ITEM_MAP = new HashMap();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        // TODO - 서버 연동 작업 후 Dummy 아이템 추가 코드 삭제 필요
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    public static void addItem(CardItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static CardItem createDummyItem(int position) {
        return new CardItem(position, "1","BC카드", "****-****-****-1234", false);
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
    public static class CardItem {
        public final int id;
        public final String title;
        public final String seq;
        public final String info;
        public boolean card_selected;


        public CardItem(int id, String seq, String title, String info, boolean card_selected) {
            this.id         = id;
            this.seq        = seq;
            this.title      = title;
            this.info       = info;
            this.card_selected = card_selected;
        }
    }
}


