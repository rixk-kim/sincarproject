package com.sincar.customer.adapter.content;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UseContent {

    /**
     * An array of CardItem items.
     */
    public static final List<UseItem> ITEMS = new ArrayList<UseItem>();

    /**
     * A map of CardItem items, by ID.
     */
    public static final Map<Integer, UseItem> ITEM_MAP = new HashMap();

    private static final int COUNT = 5;

//    static {
//        // Add some sample items.
//        // 서버 연동 작업 후 Dummy 아이템 추가 코드 삭제 필요
//        for (int i = 1; i <= COUNT; i++) {
//            addItem(createDummyItem(i));
//        }
//    }

    public static void addItem(UseItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static void clearItem() {
        ITEMS.clear();
        ITEM_MAP.clear();
    }

//    public static UseItem createDummyItem(int position) {
//        return new UseItem(position, "1","0", "12/22 " + position, "", "송파구 석촌호수로 274 (실내)", "관악 1호점 (김태현) ", "43,000원",
//                "01012345678", "신차로", "45,000원", "2000원", "케이뱅크 ****3840",
//                "현대 싼타페", "12가 3238");
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
     * A UseItem item representing a piece of content.
     * 예약상태, 예약시간, 세차 장소, 대리점 명, 이용 요금, 전화번호
     */
    public static class UseItem {
        public final int id;
        public final String seq;
        public final String reserve_status;
        public final String reserve_time;
        public final String cancel_time;
        public final String wash_address;
        public final String wash_agent;
        public final String use_pay;
        public final String agent_mobile;

        public final String reserve_name;
        public final String common_pay;
        public final String coupone_pay;
        public final String approve_info;
        public final String car_info;
        public final String car_number;
        public final String point_pay;

        public final String coupone_seq;
        public final String agent_seq;
        public final String add_service;
        public final String car_company;
        public final String wash_place;


        public UseItem(int id, String seq, String reserve_status, String reserve_time, String cancel_time, String wash_address, String wash_agent, String use_pay,
                       String agent_mobile, String reserve_name, String common_pay, String coupone_pay, String approve_info, String car_info, String car_number,
                        String point_pay, String coupone_seq, String agent_seq, String add_service, String car_company, String wash_place) {
            this.id = id;
            this.seq = seq;
            this.reserve_status = reserve_status;
            this.reserve_time   = reserve_time;
            this.cancel_time    = cancel_time;
            this.wash_address   = wash_address;
            this.wash_agent     = wash_agent;
            this.use_pay        = use_pay;
            this.agent_mobile   = agent_mobile;

            this.reserve_name   = reserve_name;     //예약자명
            this.common_pay     = common_pay ;      //기본요금
            this.coupone_pay    = coupone_pay;      //할인요금
            this.approve_info   = approve_info;     //결재정보
            this.car_info       = car_info;         //차량정보
            this.car_number     = car_number;       //차량번호
            //TODO - point 연동되면 추가
            if(TextUtils.isEmpty(point_pay))
            {
                this.point_pay = "0";
            }else {
                this.point_pay = point_pay;        //포인트요금
            }

            this.coupone_seq = coupone_seq;
            this.agent_seq   = agent_seq;
            this.add_service = add_service;
            this.car_company = car_company;
            this.wash_place  = wash_place;
        }
    }
}



