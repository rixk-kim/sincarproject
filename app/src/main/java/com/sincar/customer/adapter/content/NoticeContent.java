package com.sincar.customer.adapter.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoticeContent {

    /**
     * An array of PointItem items.
     */
    public static final List<NoticeItem> ITEMS = new ArrayList<NoticeItem>();

    /**
     * A map of PointItem items, by ID.
     */
    public static final Map<Integer, NoticeItem> ITEM_MAP = new HashMap();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        // TODO - 서버 연동 작업 후 Dummy 아이템 추가 코드 삭제 필요
//        for (int i = 1; i <= COUNT; i++) {
//            addItem(createDummyItem(i));
//        }
    }

    public static void addItem(NoticeItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static void clearItem() {
        ITEMS.clear();
        ITEM_MAP.clear();
    }

//    private static NoticeItem createDummyItem(int position) {
//        return new NoticeItem(position, "1","[이벤트]할인쿠폰 이벤트", "2020.03.01", "SIN 그룹에서 스팀워시 대리점을 모집합니다. 송파지사, 서초지사에서 모집중이며 자세한 사항은 고객센터를 통해 연락부탁드립니다. SIN 그룹에서 스팀워시 대리점을 모집합니다. 송파지사, 서초지사에서 모집중이며 자세한 사항은 고객센터를 통해 연락부탁드립니다. SIN 그룹에서 스팀워시 대리점을 모집합니다. 송파지사, 서초지사에서 모집중이며 자세한 사항은 고객센터를 통해 연락부탁드립니다. SIN 그룹에서 스팀워시 대리점을 모집합니다. 송파지사, 서초지사에서 모집중이며 자세한 사항은 고객센터를 통해 연락부탁드립니다. 감사합니다. ");
//    }

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
    public static class NoticeItem {
        public final int id;
        public final String title;
        public final String seq;
        public final String date;
        public final String contents;


        public NoticeItem(int id, String seq, String title, String date, String contents) {
            this.id         = id;
            this.seq        = seq;
            this.title      = title;
            this.date       = date;
            this.contents   = contents;
        }
    }
}

