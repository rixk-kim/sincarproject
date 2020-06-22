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
        public final String imgUrl;
        public final String rental_Shop_Name;
        public final String rental_Car_Name;
        public final String discount_Per;
        public final String rental_Price;
       public final String rental_posi;
        public final double distance;

        public Rental_List_Item(String imgUrl, String rental_Shop_Name, String rental_Car_Name, String discount_Per, String rental_Price
                , String rental_posi, double distance) {
            this.imgUrl = imgUrl;
            this.rental_Shop_Name = rental_Shop_Name;
            this.rental_Car_Name = rental_Car_Name;
            this.discount_Per = discount_Per;
            this.rental_Price = rental_Price;
            this.rental_posi = rental_posi;
            this.distance = distance;
        }

    }
}
