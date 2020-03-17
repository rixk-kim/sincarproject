package com.sincar.customer.adapter.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CouponeContent {

    /**
     * An array of CouponeItem items.
     */
    public static final List<CouponeItem> ITEMS = new ArrayList<CouponeItem>();

    /**
     * A map of CouponeItem items, by ID.
     */
    public static final Map<Integer, CouponeItem> ITEM_MAP = new HashMap();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        // TODO - 서버 연동 작업 후 Dummy 아이템 추가 코드 삭제 필요
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    public static void addItem(CouponeItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static void clearItem() {
        ITEMS.clear();// .add(item);
        ITEM_MAP.clear();   // .put(item.id, item);
    }

    private static CouponeItem createDummyItem(int position) {
        return new CouponeItem(position, "1","2000원", "추천인 할인쿠폰", "~2020.12.31", "회원 가입을 축하해요!", "N", false);
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
    public static class CouponeItem {
        public final int id;
        public final String title;
        public final String seq;
        public final String pay;
        public final String date;
        public final String contents;
        public final String coupone_yn;
        public boolean coupone_selected;

        public CouponeItem(int id, String seq, String pay, String title, String date, String contents, String coupone_yn, boolean coupone_selected) {
            this.id         = id;
            this.seq        = seq;
            this.pay        = pay;
            this.title      = title;
            this.date       = date;
            this.contents   = contents;
            this.coupone_yn = coupone_yn;
            this.coupone_selected = coupone_selected;
        }
    }
}

