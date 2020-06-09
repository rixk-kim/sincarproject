package com.sincar.customer.sy_rentcar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sincar.customer.R;

import java.util.ArrayList;

public class Rental_list_detail_deli_listAdapter extends BaseAdapter {

    private ArrayList<Rental_list_detail_deli_listview_item> listViewItemList = new ArrayList<Rental_list_detail_deli_listview_item>();

    public Rental_list_detail_deli_listAdapter() {

    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflateer = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflateer.inflate(R.layout.rental_list_detail_deli_listview_item, parent, false);
        }

        TextView list_itme_str = (TextView) convertView.findViewById(R.id.tvRental_list_deli_list_item);
        ImageView checkDrawable = (ImageView) convertView.findViewById(R.id.ivRental_list_deli_list_item);

        Rental_list_detail_deli_listview_item listViewItem = listViewItemList.get(position);

        list_itme_str.setText(listViewItem.getList_item_str());
        checkDrawable.setImageDrawable(listViewItem.getCheckDrawable());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    //아이템 데이터 추가를 위한 함수
    public void addItem(String itemList, Drawable check) {
        Rental_list_detail_deli_listview_item item = new Rental_list_detail_deli_listview_item();

        item.setList_item_str(itemList);
        item.setCheck(check);

        listViewItemList.add(item);
    }
}
