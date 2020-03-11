package com.sincar.customer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.sincar.customer.R;
import com.sincar.customer.adapter.content.CouponeContent;
import java.util.List;

public class CouponeContentRecyclerViewAdapter extends RecyclerView.Adapter<com.sincar.customer.adapter.CouponeContentRecyclerViewAdapter.ViewHolder> {

    private final List<CouponeContent.CouponeItem> mValues;
    private Context mContext;
    private String notice_pos;

    public CouponeContentRecyclerViewAdapter(Context context, List<CouponeContent.CouponeItem> items) {
        mContext = context;
        mValues = items;
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

        holder.mCouponeTitle.setText(mValues.get(position).title);
        holder.mCouponeDate.setText(mValues.get(position).date);
        holder.mCouponeSeq = Integer.parseInt(mValues.get(position).seq);
        holder.mCouponeContent.setText(mValues.get(position).contents);

        notice_pos = String.valueOf(position);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO - 리스트 선택 시 이벤트 핸들러 추가 필요하면 여기에서 해주기
//                Intent intent = new Intent(mContext, NoticeDetailActivity.class);
//                intent.putExtra("title", mValues.get(Integer.parseInt(notice_pos)).title);
//                intent.putExtra("date", mValues.get(Integer.parseInt(notice_pos)).date);
//                intent.putExtra("contents", mValues.get(Integer.parseInt(notice_pos)).contents);
//                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public final TextView mCouponeTitle;
        public final TextView mCouponeDate;
        public int mCouponeSeq;
        public final TextView mCouponeContent;


        public CouponeContent.CouponeItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mCouponeTitle      = view.findViewById(R.id.coupone_column1);
            mCouponeDate       = view.findViewById(R.id.coupone_column3);
            mCouponeContent    = view.findViewById(R.id.coupone_column2);
//            mPoint          = view.findViewById(R.id.notice_column4);
            mCouponeSeq = 0;
        }
    }
}


