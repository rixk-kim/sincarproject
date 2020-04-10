package com.sincar.customer.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.sincar.customer.MapsActivity;
import com.sincar.customer.R;
import com.sincar.customer.adapter.content.AddressContent;

import java.util.List;

/**
 * 2020.04.09 spirit.
 * 주소검색 class
 */
public class AddressContentRecyclerViewAdapter extends RecyclerView.Adapter<AddressContentRecyclerViewAdapter.ViewHolder>
        implements View.OnClickListener{

    private final List<AddressContent.AddressItem> mValues;
    private int select_position;
    private String search_keyword;      //검색어
    private String detail_address;      //검색어에 대한 상세주소


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
        intent.putExtra("search_keyword", mValues.get(item.id).title);
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
            mIcon   = view.findViewById(R.id.btnAddressLayout);
            seq     = "";
            mTitle  = view.findViewById(R.id.address_title);
            mDescription = view.findViewById(R.id.address_desc);
        }
    }
}
