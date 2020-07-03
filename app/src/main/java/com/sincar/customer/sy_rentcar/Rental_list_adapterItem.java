package com.sincar.customer.sy_rentcar;

import java.util.ArrayList;
import java.util.List;

public class Rental_list_adapterItem {

    public static final List<Rental_List_Item> RENTAL_LIST_ITEM1 = new ArrayList<>();
    public static final List<Rental_List_Item> RENTAL_LIST_ITEM2 = new ArrayList<>();
    public static final List<Rental_List_Item> RENTAL_LIST_ITEM_BOTH = new ArrayList<>();

    public static void addItem1(Rental_List_Item item) {
        RENTAL_LIST_ITEM1.add(item);
    }

    public static void addItem2(Rental_List_Item item) {
        RENTAL_LIST_ITEM2.add(item);
    }

    public static void clearItem() {
        RENTAL_LIST_ITEM1.clear();
        RENTAL_LIST_ITEM2.clear();
    }

    public static void sumList() {
        RENTAL_LIST_ITEM_BOTH.addAll(RENTAL_LIST_ITEM1);
        RENTAL_LIST_ITEM_BOTH.addAll(RENTAL_LIST_ITEM2);
    }

    public static void divList() {
        clearItem();
        if (RENTAL_LIST_ITEM_BOTH.size() != 0) {
            int size = RENTAL_LIST_ITEM_BOTH.size();
            for (int i = 0; i < size; i++) {

                if (i % 2 == 0)
                    RENTAL_LIST_ITEM1.add(RENTAL_LIST_ITEM_BOTH.get(i));
                else
                    RENTAL_LIST_ITEM2.add(RENTAL_LIST_ITEM_BOTH.get(i));
            }
        }
        RENTAL_LIST_ITEM_BOTH.clear();
    }

    public static class Rental_List_Item {
        public final int id;
        public final String rentcar_seq;
        public final String rentcar_name;
        public final String rentcar_img_url;
        public final String rentcar_agent;
        public final String rentcar_agent_add;
        public final String rentcar_discount;
        public final String rentcar_price;
        public final String rentcar_fil_age;
        public final String rentcar_fil_type;
        public final String rentcar_fil_brand;
        public final String rentcar_sort_dist;
        public final String rentcar_sort_popu;

        public Rental_List_Item(int id, String rentcar_seq, String rentcar_name, String rentcar_img_url, String rentcar_agent,
                                String rentcar_agent_add, String rentcar_discount, String rentcar_price, String rentcar_fil_age,
                                String rentcar_fil_type, String rentcar_fil_brand, String rentcar_sort_dist, String rentcar_sort_popu) {
            this.id = id;
            this.rentcar_seq = rentcar_seq;
            this.rentcar_name = rentcar_name;
            this.rentcar_img_url = rentcar_img_url;
            this.rentcar_agent = rentcar_agent;
            this.rentcar_agent_add = rentcar_agent_add;
            this.rentcar_discount = rentcar_discount;
            this.rentcar_price = rentcar_price;
            this.rentcar_fil_age = rentcar_fil_age;
            this.rentcar_fil_type = rentcar_fil_type;
            this.rentcar_fil_brand = rentcar_fil_brand;
            this.rentcar_sort_dist = rentcar_sort_dist;
            this.rentcar_sort_popu = rentcar_sort_popu;
        }

    }
}
