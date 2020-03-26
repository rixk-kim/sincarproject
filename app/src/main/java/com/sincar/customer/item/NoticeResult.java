package com.sincar.customer.item;

import java.util.ArrayList;

public class NoticeResult {
    public ArrayList<NoticeItem> notice = new ArrayList();
    public ArrayList<NoticeDataItem> data = new ArrayList();

    @Override
    public String toString() {
        return "NoticeResult{" +
                "notice=" + notice +
                ", data=" + data +
                '}';
    }
}

