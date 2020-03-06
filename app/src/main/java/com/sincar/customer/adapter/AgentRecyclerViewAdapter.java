package com.sincar.customer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sincar.customer.R;
import com.sincar.customer.adapter.content.AddressContent;
import com.sincar.customer.adapter.content.AgentContent;
import com.sincar.customer.adapter.content.TimeContent;

import java.util.List;

public class AgentRecyclerViewAdapter extends RecyclerView.Adapter<AgentRecyclerViewAdapter.ViewHolder> {

    private final int timeSpanCount = 4;
    private final List<AgentContent.AgentItem> mValues;

    public AgentRecyclerViewAdapter(List<AgentContent.AgentItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.agent_list_item, parent, false);

        // Time List 만들기
        View subView = view.findViewById(R.id.reservationTimeList);
        if (subView instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) subView;

            recyclerView.setLayoutManager(new GridLayoutManager(context, timeSpanCount));
            recyclerView.setAdapter(new TimeRecyclerViewAdapter(TimeContent.ITEMS));
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        // TODO : 대리점주 사진 작업 하기
        // holder.mAgentPhoto.setText(mValues.get(position).agent_photo);
        holder.mAgentName.setText(mValues.get(position).agent_name);
        holder.mBranchName.setText(mValues.get(position).branch_area);
        holder.mWashArea.setText(mValues.get(position).wash_area);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public final ImageView mAgentPhoto;
        public final TextView mAgentName;
        public final TextView mBranchName;
        public final TextView mWashArea;

        public AgentContent.AgentItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mAgentPhoto = view.findViewById(R.id.agent_photo);
            mAgentName = view.findViewById(R.id.agent_name);
            mBranchName = view.findViewById(R.id.branch_name);
            mWashArea = view.findViewById(R.id.wash_area);
        }
    }
}
