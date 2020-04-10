package com.sincar.customer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.sincar.customer.R;
import com.sincar.customer.adapter.content.ChargeContent.ChargeItem;
import java.util.List;

import static com.sincar.customer.util.Util.setAddMoneyDot;

/**
 * 2020.04.09 spirit
 * 이용요금 class (기본 요금, 부가서비스)
 */
public class ChargeContentRecyclerViewAdapter extends RecyclerView.Adapter<ChargeContentRecyclerViewAdapter.ViewHolder>  {

    private final List<ChargeItem> mValues;

    public ChargeContentRecyclerViewAdapter(List<ChargeItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_charge, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mChargeTitle.setText(mValues.get(position).chargeTitle);
        holder.mChargeAmount.setText(setAddMoneyDot(mValues.get(position).chargeAmount) + "원");

        holder.mView.setOnClickListener(new View.OnClickListener() {
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

        public final TextView mChargeTitle;
        public final TextView mChargeAmount;

        public ChargeItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mChargeTitle = view.findViewById(R.id.charge_title);
            mChargeAmount = view.findViewById(R.id.charge_amount);
        }
    }
}