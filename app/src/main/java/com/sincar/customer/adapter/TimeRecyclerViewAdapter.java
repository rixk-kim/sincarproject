package com.sincar.customer.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.sincar.customer.R;
import com.sincar.customer.adapter.content.TimeContent.TimeItem;

import java.util.List;

public class TimeRecyclerViewAdapter extends RecyclerView.Adapter<TimeRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {

    private int agentPosition;
    private final List<TimeItem> mValues;
    private OnTimeListInteractionListener mListener;

//    private int prePosition;

    public TimeRecyclerViewAdapter(int agentPosition, List<TimeItem> items,
                                   OnTimeListInteractionListener listener) {
        this.agentPosition = agentPosition;
        mValues = items;
        if (listener != null && listener instanceof OnTimeListInteractionListener) {
            mListener = listener;
        }
//        prePosition = -1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        TimeItem item = mValues.get(position);
        holder.mItem = mValues.get(position);

        holder.mView.setTag(item);

        holder.mView.setOnClickListener(this);

        setViewLayout(holder, position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public void onClick(View v) {
        TimeItem item = (TimeItem)v.getTag();

        Log.d("시간선택", "changedViewLayout()::position = " + item.position);

        for (TimeItem timeItem : mValues) {
            timeItem.selected = false;
        }
//        if (prePosition >= 0 && prePosition < mValues.size()) {
//            mValues.get(prePosition).selected = false;
//        }
//        prePosition = item.id;

        mValues.get(item.position).selected = true;
        mValues.get(item.position).agentPosition = agentPosition;
        notifyDataSetChanged();

        if (mListener != null) {
            mListener.onTimeListInteraction(item);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public TimeItem mItem;

        public final LinearLayout mBtnTime;

        public final ConstraintLayout mTimeEnableLayout;
        public final ConstraintLayout mTimeDisableLayout;

        public final TextView mTimeEnableText;
        public final TextView mTimeDisableText;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mBtnTime = view.findViewById(R.id.btnReservationTime);

            mTimeEnableLayout = view.findViewById(R.id.timeEnableLayout);
            mTimeDisableLayout = view.findViewById(R.id.timeDisableLayout);

            mTimeEnableText = view.findViewById(R.id.time_enable_text);
            mTimeDisableText = view.findViewById(R.id.time_disable_text);
        }
    }

    private void setViewLayout(final ViewHolder holder, final int position) {
        if (holder.mItem.enable) {
            holder.mTimeEnableLayout.setVisibility(View.VISIBLE);
            holder.mTimeDisableLayout.setVisibility(View.GONE);

            if (holder.mItem.selected == true) {
                holder.mTimeEnableText.setBackgroundResource(R.drawable.time_button_background_selected);
            } else {
                holder.mTimeEnableText.setBackgroundResource(R.drawable.time_button_background_normal);
            }

            holder.mTimeEnableText.setText(mValues.get(position).reservation_time);
        } else {
            holder.mTimeEnableLayout.setVisibility(View.GONE);
            holder.mTimeDisableLayout.setVisibility(View.VISIBLE);

            holder.mTimeDisableLayout.setBackgroundResource(R.drawable.time_button_background_normal);
            holder.mTimeDisableText.setText(mValues.get(position).reservation_time);
        }
    }

    public interface OnTimeListInteractionListener {
        void onTimeListInteraction(TimeItem timeItem);
    }
}
