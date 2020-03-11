package com.sincar.customer.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.sincar.customer.NoticeDetailActivity;
import com.sincar.customer.R;
import com.sincar.customer.adapter.content.NoticeContent;
import java.util.List;

public class NoticeContentRecyclerViewAdapter extends RecyclerView.Adapter<com.sincar.customer.adapter.NoticeContentRecyclerViewAdapter.ViewHolder> {

    private final List<NoticeContent.NoticeItem> mValues;
    private Context mContext;
    private String notice_pos;

    public NoticeContentRecyclerViewAdapter(Context context, List<NoticeContent.NoticeItem> items) {
        mContext = context;
        mValues = items;
    }

    @Override
    public com.sincar.customer.adapter.NoticeContentRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notice_list_item, parent, false);
        return new com.sincar.customer.adapter.NoticeContentRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final com.sincar.customer.adapter.NoticeContentRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mNotiTitle.setText(mValues.get(position).title);
        holder.mNotiDate.setText(mValues.get(position).date);
        holder.mNotiSeq = Integer.parseInt(mValues.get(position).seq);
        holder.mNotiContent = mValues.get(position).contents;

        notice_pos = String.valueOf(position);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO - 리스트 선택 시 이벤트 핸들러 추가 필요하면 여기에서 해주기
                Intent intent = new Intent(mContext, NoticeDetailActivity.class);
                intent.putExtra("title", mValues.get(Integer.parseInt(notice_pos)).title);
                intent.putExtra("date", mValues.get(Integer.parseInt(notice_pos)).date);
                intent.putExtra("contents", mValues.get(Integer.parseInt(notice_pos)).contents);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public final TextView mNotiTitle;
        public final TextView mNotiDate;
        public int mNotiSeq;
        public String mNotiContent;


        public NoticeContent.NoticeItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mNotiTitle      = view.findViewById(R.id.notice_column1);
            mNotiDate       = view.findViewById(R.id.notice_column2);
//            mDate           = view.findViewById(R.id.notice_column3);
//            mPoint          = view.findViewById(R.id.notice_column4);
            mNotiSeq = 0;
            mNotiContent = null;
        }
    }
}

