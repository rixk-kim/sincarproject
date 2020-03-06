package com.sincar.customer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.sincar.customer.R;
import com.sincar.customer.adapter.content.TimeContent;

import java.util.List;

public class TimeRecyclerViewAdapter extends RecyclerView.Adapter<TimeRecyclerViewAdapter.ViewHolder> {

    private final List<TimeContent.TimeItem> mValues;

    public TimeRecyclerViewAdapter(List<TimeContent.TimeItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        setViewLayout(holder, position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public final ConstraintLayout mTimeEnableLayout;
        public final ConstraintLayout mTimeDisableLayout;

        public final TextView mTimeEnableText;
        public final TextView mTimeDisableText;

        public TimeContent.TimeItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mTimeEnableLayout = view.findViewById(R.id.timeEnableLayout);
            mTimeDisableLayout = view.findViewById(R.id.timeDisableLayout);

            mTimeEnableText = view.findViewById(R.id.time_enable_text);
            mTimeDisableText = view.findViewById(R.id.time_disable_text);
        }
    }

    private void setViewLayout(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        if (holder.mItem.enable) {
            holder.mTimeEnableLayout.setVisibility(View.VISIBLE);
            holder.mTimeDisableLayout.setVisibility(View.GONE);

            if (holder.mItem.selected == true) {
                holder.mTimeEnableLayout.setBackgroundResource(R.drawable.time_button_background_selected);
            } else {
                holder.mTimeEnableLayout.setBackgroundResource(R.drawable.time_button_background_normal);
            }

            holder.mTimeEnableText.setText(mValues.get(position).reservation_time);
        } else {
            holder.mTimeEnableLayout.setVisibility(View.GONE);
            holder.mTimeDisableLayout.setVisibility(View.VISIBLE);

            holder.mTimeDisableLayout.setBackgroundResource(R.drawable.time_button_background_normal);
            holder.mTimeDisableText.setText(mValues.get(position).reservation_time);
        }
    }
}
