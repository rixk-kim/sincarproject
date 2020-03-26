package com.sincar.customer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.sincar.customer.R;
import com.sincar.customer.adapter.content.CouponeContent;
import com.sincar.customer.adapter.content.OptionContent;

import java.util.List;

public class CouponeContentRecyclerViewAdapter extends RecyclerView.Adapter<com.sincar.customer.adapter.CouponeContentRecyclerViewAdapter.ViewHolder>
        implements View.OnClickListener{

    private final List<CouponeContent.CouponeItem> mValues;
    private Context mContext;
    private String coupone_pos;
    private LinearLayout mLayout;
    private String coupone_seq;
    private String coupone_pay;
    private String path;
    private int pre_position = -1;

    public CouponeContentRecyclerViewAdapter(Context context, List<CouponeContent.CouponeItem> items, String path) {
        mContext = context;
        mValues = items;
        this.path = path;
    }

    @Override
    public com.sincar.customer.adapter.CouponeContentRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coupone_list_item, parent, false);
        return new com.sincar.customer.adapter.CouponeContentRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final com.sincar.customer.adapter.CouponeContentRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mView.setTag(holder.mItem);
        holder.mView.setOnClickListener(this);

        setViewLayout(holder, position);

//        holder.mItem = mValues.get(position);
//
//        holder.mCouponeSeq = Integer.parseInt(mValues.get(position).seq);
//        holder.mCouponeTitle.setText(mValues.get(position).title);
//        holder.mCouponeDate.setText(mValues.get(position).date);
//        holder.mCouponeContent.setText(mValues.get(position).contents);
//        holder.mCouponeUseYn  = mValues.get(position).coupone_yn;
//
//        coupone_pos = String.valueOf(position);
//
//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 리스트 선택 없음. 이벤트 핸들러 추가 필요하면 여기에서 해주기
//            }
//        });
    }

    private void setViewLayout(final ViewHolder holder, final int position) {
        holder.mCouponeSeq = Integer.parseInt(mValues.get(position).seq);
        holder.mCouponePay.setText(mValues.get(position).pay);
        holder.mCouponeTitle.setText(mValues.get(position).title);
        holder.mCouponeDate.setText(mValues.get(position).date);
        holder.mCouponeContent.setText(mValues.get(position).contents);
        holder.mCouponeUseYn  = mValues.get(position).coupone_yn;

        mLayout = holder.mView.findViewById(R.id.coupone_layout);

        if (holder.mItem.coupone_selected == true) {
            mLayout.setBackgroundResource(R.color.card_background_color);
        } else {
            mLayout.setBackgroundResource(R.color.base_background_color);
        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public void onClick(View v) {
        CouponeContent.CouponeItem item = (CouponeContent.CouponeItem)v.getTag();

        for (CouponeContent.CouponeItem couponeItem : mValues) {
            couponeItem.coupone_selected = false;
        }

        if("payment".equals(path)) {
            mValues.get(item.id).coupone_selected = true;

            coupone_seq = mValues.get(item.id).seq;
            coupone_pay = mValues.get(item.id).pay;

            pre_position = item.id;
        }

        notifyDataSetChanged();
    }

    public String getCouponeSeq()
    {
        return coupone_seq;
    }

    public String getCouponePay()
    {
        return coupone_pay;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public int mCouponeSeq;
        public final TextView mCouponePay;
        public final TextView mCouponeTitle;
        public final TextView mCouponeDate;
        public final TextView mCouponeContent;
        public String mCouponeUseYn;


        public CouponeContent.CouponeItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mCouponeSeq = 0;
            mCouponePay        = view.findViewById(R.id.coupone_column1);
            mCouponeTitle      = view.findViewById(R.id.coupone_column2);
            mCouponeDate       = view.findViewById(R.id.coupone_column3);
            mCouponeContent    = view.findViewById(R.id.coupone_column4);
//            mPoint          = view.findViewById(R.id.notice_column4);
            mCouponeUseYn = "";
        }
    }
}


