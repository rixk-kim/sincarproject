package com.sincar.customer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.sincar.customer.R;
import com.sincar.customer.adapter.content.TimeContent.TimeItem;

import java.util.List;

public class TimeRecyclerViewAdapter extends RecyclerView.Adapter<TimeRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {

    private int agentPosition;
    private final List<TimeItem> mValues;
    private OnTimeListInteractionListener mListener;
    private Context trContext;
    private String agent_status;

//    private int prePosition;

    public TimeRecyclerViewAdapter(int agentPosition, List<TimeItem> items,
                                   OnTimeListInteractionListener listener, Context context, String agent_status) {
        this.agentPosition = agentPosition;
        mValues = items;
        if (listener != null && listener instanceof OnTimeListInteractionListener) {
            mListener = listener;
        }
        this.trContext = context;
        this.agent_status = agent_status;
//        prePosition = -1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_time, parent, false);
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
        if("Y".equals(agent_status)) {
            TimeItem item = (TimeItem) v.getTag();

            Log.d("시간선택", "changedViewLayout()::position = " + item.position);

            for (TimeItem timeItem : mValues) {
                timeItem.selected = false;
            }

            mValues.get(item.position).selected = true;
            mValues.get(item.position).agentPosition = agentPosition;
            notifyDataSetChanged();

            if (mListener != null) {
                mListener.onTimeListInteraction(item);
            }
        }else{
            Toast.makeText(trContext, "비활동지역 대리점입니다.", Toast.LENGTH_SHORT).show();
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

    @SuppressLint("ResourceAsColor")
    private void setViewLayout(final ViewHolder holder, final int position) {
        if ("Y".equals(holder.mItem.reservation_status)) {
            holder.mTimeEnableLayout.setVisibility(View.VISIBLE);
            holder.mTimeDisableLayout.setVisibility(View.GONE);

            if (holder.mItem.selected == true && this.agentPosition == holder.mItem.agentPosition) {
                holder.mTimeEnableText.setBackgroundResource(R.drawable.time_button_background_selected);
            } else {
                holder.mTimeEnableText.setBackgroundResource(R.drawable.time_button_background_normal);
            }

            holder.mTimeEnableText.setText(mValues.get(position).reservation_time);

            if("N".equals(agent_status))
            {
                holder.mTimeEnableText.setTextColor(R.color.agent_color_1);
            }
        } else {
            holder.mTimeEnableLayout.setVisibility(View.GONE);
            holder.mTimeDisableLayout.setVisibility(View.VISIBLE);

            holder.mTimeDisableLayout.setBackgroundResource(R.drawable.time_button_background_normal);
            holder.mTimeDisableText.setText(mValues.get(position).reservation_time);

            if("N".equals(agent_status))
            {
                holder.mTimeDisableText.setTextColor(R.color.agent_color_1);
            }
        }



    }

    public interface OnTimeListInteractionListener {
        void onTimeListInteraction(TimeItem timeItem);
    }
}
