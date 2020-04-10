package com.sincar.customer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sincar.customer.R;
import com.sincar.customer.adapter.content.AgentContent;
import com.sincar.customer.adapter.content.AgentContent.AgentItem;
import com.sincar.customer.adapter.content.TimeContent;
import com.sincar.customer.adapter.content.TimeContent.TimeItem;

import java.util.ArrayList;
import java.util.List;

import static com.sincar.customer.adapter.content.AgentContent.ITEMS;

/**
 * 2020.04.09 spirit
 * 대리점주 정보 class
 */
public class AgentRecyclerViewAdapter extends RecyclerView.Adapter<AgentRecyclerViewAdapter.ViewHolder> implements TimeRecyclerViewAdapter.OnTimeListInteractionListener {

    private final int timeSpanCount = 4;        //시간 가로 갯수 설정.
    private OnAgentListInteractionListener mListener;
    private final List<AgentContent.AgentItem> mValues;

    private int prevAgentPosition = -1;     //이전 선택 대리점주 position.
    private int prevTimePosition = -1;      //이전 선택 시간 position.

    private String reserveSeq;              //예약 SEQ
    private String reserveTime;             //예약 시간
    private String reserveStatus;           //예약 상태
    private Context arContext;

    public AgentRecyclerViewAdapter(List<AgentContent.AgentItem> items, OnAgentListInteractionListener listener, Context context) {
        mValues = items;
        if (listener != null && listener instanceof OnAgentListInteractionListener) {
            mListener = listener;
        }
        this.arContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_agent, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        // TODO : 대리점주 사진 작업 하기
        // holder.mAgentPhoto.setText(mValues.get(position).agent_photo);
        holder.mAgentName.setText(mValues.get(position).agent_name);
        holder.mBranchName.setText(mValues.get(position).branch_area);
        holder.mWashArea.setText(mValues.get(position).wash_area);
        if(TextUtils.isEmpty(mValues.get(position).agent_staus))
        {
            holder.agent_status = "Y";
        }else {
            holder.agent_status = mValues.get(position).agent_staus;
        }
//        holder.agent_status = "N";

        if("N".equals(holder.agent_status))
        {
            holder.mAgentName.setTextColor(R.color.agent_color_1);
            holder.mBranchName.setTextColor(R.color.agent_color_1);
            holder.mWashAreaTitle.setTextColor(R.color.agent_color_1);
            holder.mWashArea.setTextColor(R.color.agent_color_1);
        }

        List<TimeContent.TimeItem> ITEMS = new ArrayList<TimeItem>();


        // Time List 만들기
        View subView = holder.mView.findViewById(R.id.reservationTimeList);
        if (subView instanceof RecyclerView) {
            Context context = holder.mView.getContext();
            RecyclerView recyclerView = (RecyclerView) subView;

            recyclerView.setLayoutManager(new GridLayoutManager(context, timeSpanCount));
//            recyclerView.setAdapter(new TimeRecyclerViewAdapter(position, ITEMS, this));
            recyclerView.setAdapter(new TimeRecyclerViewAdapter(position, holder.mItem.reserve_info, this, arContext,  holder.agent_status));
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public void onTimeListInteraction(TimeItem timeItem) {
        Log.d("대리점주", "position = " + timeItem.position);
        Log.d("대리점주", "selected = " + timeItem.selected);
        Log.d("대리점주", "agentPosition = " + timeItem.agentPosition);
        Log.d("대리점주", "num = " + mValues.get(timeItem.agentPosition).reserve_info.get(0));
        Log.d("대리점주", "seq = " + mValues.get(timeItem.agentPosition).agent_seq);
//        mValues.set(timeItem.agentPosition, TimeContent.TimeItem("","","","",false);    // get(timeItem.selected);

        if (prevAgentPosition != -1
                && (prevAgentPosition != timeItem.agentPosition)
                && prevTimePosition != -1
        ){
            ITEMS.get(prevAgentPosition).reserve_info.get(prevTimePosition).selected = false;
        }

        prevAgentPosition = timeItem.agentPosition;
        prevTimePosition = timeItem.position;

        reserveTime = mValues.get(timeItem.agentPosition).reserve_info.get(timeItem.position).reservation_time; //예약시간
        reserveStatus = mValues.get(timeItem.agentPosition).reserve_info.get(timeItem.position).reservation_status; //예약여부
        reserveSeq = mValues.get(timeItem.agentPosition).agent_seq; //대리점 번호

        Log.d("포지션", "prevAgentPosition = " + prevAgentPosition);
        Log.d("포지션", "prevTimePosition = " + prevTimePosition);

        notifyDataSetChanged();

        if (mListener != null) {
            mListener.onAgentListInteraction(mValues.get(timeItem.agentPosition));
        }
    }

    public int getAgentPosition()
    {
        return prevAgentPosition;
    }

    public String getTimePosition()
    {
        return reserveTime;
    }

    public String getStatusPosition()
    {
        return reserveStatus;
    }

    public String getAgentSeq() {
        return reserveSeq;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public final ImageView mAgentPhoto;
        public final TextView mAgentName;
        public final TextView mBranchName;
        public final TextView mWashArea;
        public String agent_status;
        public final TextView mWashAreaTitle;

        public AgentContent.AgentItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mAgentPhoto     = view.findViewById(R.id.agent_photo);
            mAgentName      = view.findViewById(R.id.agent_name);
            mBranchName     = view.findViewById(R.id.branch_name);
            mWashArea       = view.findViewById(R.id.wash_area);
            mWashAreaTitle  = view.findViewById(R.id.wash_area_title);
            agent_status    = "";
        }
    }

    public interface OnAgentListInteractionListener {
        void onAgentListInteraction(AgentItem agentItem);
    }
}
