package com.sincar.customer.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.sincar.customer.MapsActivity;
import com.sincar.customer.R;
import com.sincar.customer.adapter.content.AddressContent;

import java.util.List;

public class AddressContentRecyclerViewAdapter extends RecyclerView.Adapter<AddressContentRecyclerViewAdapter.ViewHolder>
        implements View.OnClickListener{

    private final List<AddressContent.AddressItem> mValues;
    private int select_position;
    private String detail_address;

    public AddressContentRecyclerViewAdapter(List<AddressContent.AddressItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_search_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mView.setTag(holder.mItem);
        holder.mView.setOnClickListener(this);

        setViewLayout(holder, position);

//        holder.mItem = mValues.get(position);
//        holder.seq = mValues.get(position).seq;
//        holder.mTitle.setText(mValues.get(position).title);
//        holder.mDescription.setText(mValues.get(position).description);
//        detail_address = mValues.get(position).description;
//
//        select_position = position;
//
//        holder.mIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 리스트 선택 시 이벤트 핸들러 추가 필요하면 여기에서 해주기
//                // 임시로 주소 검색 화면 finish()처리함.
//                // event handler로 해서 지도 앱으로 주서 전달 필요.
//                Intent intent = new Intent(((Activity)holder.mView.getContext()), MapsActivity.class);
//                intent.putExtra("search_result", detail_address);
//                ((Activity)holder.mView.getContext()).setResult(((Activity)holder.mView.getContext()).RESULT_OK, intent);
//                ((Activity)holder.mView.getContext()).finish();
//
//            }
//        });
    }

    private void setViewLayout(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.seq = mValues.get(position).seq;
        holder.mTitle.setText(mValues.get(position).title);
        holder.mDescription.setText(mValues.get(position).description);
        detail_address = mValues.get(position).description;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public void onClick(View v) {
        AddressContent.AddressItem item = (AddressContent.AddressItem)v.getTag();

        Intent intent = new Intent(((Activity)v.getContext()), MapsActivity.class);
        intent.putExtra("search_result", mValues.get(item.id).description);
        ((Activity)v.getContext()).setResult(((Activity)v.getContext()).RESULT_OK, intent);
        ((Activity)v.getContext()).finish();

        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        //public final LinearLayout mIcon;
        public final ConstraintLayout mIcon;
        public String seq;
        public final TextView mTitle;
        public final TextView mDescription;

        public AddressContent.AddressItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            //mIcon = view.findViewById(R.id.btnAddressIcon);
            mIcon = view.findViewById(R.id.btnAddressLayout);
            seq = "";
            mTitle = view.findViewById(R.id.address_title);
            mDescription = view.findViewById(R.id.address_desc);
        }
    }
}
