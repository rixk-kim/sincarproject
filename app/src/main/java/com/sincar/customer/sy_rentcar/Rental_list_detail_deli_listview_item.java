package com.sincar.customer.sy_rentcar;

import android.graphics.drawable.Drawable;

public class Rental_list_detail_deli_listview_item {

    //커스텀 리스트뷰를 위한 커스텀리스트뷰아이템 클래스
    private String list_item_str;
    private Drawable checkDrawable;

    public void setList_item_str(String str) {
        list_item_str = str;
    }

    public void setCheck(Drawable check) {
        checkDrawable = check;
    }

    public String getList_item_str() {
        return this.list_item_str;
    }

    public Drawable getCheckDrawable() {
        return this.checkDrawable;
    }
}


