package com.sincar.customer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sincar.customer.R;
import com.sincar.customer.adapter.content.PointContent;

import java.util.List;

public class PointContentRecyclerViewAdapter extends RecyclerView.Adapter<PointContentRecyclerViewAdapter.ViewHolder> {

    private final List<PointContent.PointItem> mValues;

    public PointContentRecyclerViewAdapter(List<PointContent.PointItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.point_history_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mUsername.setText(mValues.get(position).username);
        holder.mServiceType.setText(mValues.get(position).service_type);
        holder.mDate.setText(mValues.get(position).date);
        holder.mPoint.setText(mValues.get(position).point);

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

        public final TextView mUsername;
        public final TextView mServiceType;
        public final TextView mDate;
        public final TextView mPoint;


        public PointContent.PointItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mUsername = view.findViewById(R.id.column1);
            mServiceType = view.findViewById(R.id.column2);
            mDate = view.findViewById(R.id.column3);
            mPoint = view.findViewById(R.id.column4);
        }
    }
}
