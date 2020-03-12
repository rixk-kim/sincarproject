package com.sincar.customer.adapter.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChargeContent {

    /**
     * An array of ChargeItem items.
     */
    public static final List<ChargeItem> ITEMS = new ArrayList<>();

    /**
     * A map of ChargeItem items, by ID.
     */
    public static final Map<Integer, ChargeItem> ITEM_MAP = new HashMap();

    private static final int COUNT = 3;

    static {
        // Add some sample items.
        // TODO - 서버 연동 작업 후 Dummy 아이템 추가 코드 삭제 필요
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(ChargeItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.position, item);
    }

    private static ChargeItem createDummyItem(int position) {
        return new ChargeItem(position,"스팀세차 내부/외부 (소형)", "50,000원");
    }

    /**
     * A ChargeItem item representing a piece of content.
     */
    public static class ChargeItem {

        public int position;
        public String chargeTitle;
        public String chargeAmount;

        public ChargeItem(int position, String chargeTitle, String chargeAmount) {
            this.position = position;
            this.chargeTitle = chargeTitle;
            this.chargeAmount = chargeAmount;
        }
    }
}
