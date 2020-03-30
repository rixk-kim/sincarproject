package com.sincar.customer.adapter.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionContent {

    /**
     * An array of OptionItem items.
     */
    public static final List<OptionItem> ITEMS = new ArrayList<>();

    /**
     * A map of OptionItem items, by ID.
     */
    public static final Map<Integer, OptionItem> ITEM_MAP = new HashMap();

    private static final int COUNT = 3;

//    static {
//        // Add some sample items.
//        // 서버 연동 작업 후 Dummy 아이템 추가 코드 삭제 필요
//        for (int i = 1; i <= COUNT; i++) {
//            addItem(createDummyItem(i));
//        }
//    }

    public static void addItem(OptionItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

//    private static OptionItem createDummyItem(int position) {
//        return new OptionItem(position, "1", "가니쉬 코팅", "부가 서비스 정보", "6000", false);
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
     * A OptionItem item representing a piece of content.
     */
    public static class OptionItem {
        public final int id;
        public String option_seq;
        public String option_name;
        public String option_info;
        public String option_pay;
        public boolean checked;

        public OptionItem(int id, String option_seq, String option_name, String option_info, String option_pay, Boolean checked) {
            this.id = id;
            this.option_seq  = option_seq;
            this.option_name = option_name;
            this.option_info = option_info;
            this.option_pay  = option_pay;
            this.checked = checked;
        }
    }
}
