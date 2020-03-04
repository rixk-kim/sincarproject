package com.sincar.customer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sincar.customer.R;
import com.sincar.customer.adapter.content.AddressContent;

import java.util.List;

public class AddressContentRecyclerViewAdapter extends RecyclerView.Adapter<AddressContentRecyclerViewAdapter.ViewHolder> {

    private final List<AddressContent.AddressItem> mValues;

    public AddressContentRecyclerViewAdapter(List<AddressContent.AddressItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_address_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mTitle.setText(mValues.get(position).title);
        holder.mDescription.setText(mValues.get(position).description);

        holder.mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO - 리스트 선택 시 이벤트 핸들러 추가 필요하면 여기에서 해주기
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public final LinearLayout mIcon;
        public final TextView mTitle;
        public final TextView mDescription;

        public AddressContent.AddressItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mIcon = view.findViewById(R.id.btnAddressIcon);
            mTitle = view.findViewById(R.id.address_title);
            mDescription = view.findViewById(R.id.address_desc);
        }
    }
}
