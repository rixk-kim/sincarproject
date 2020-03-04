package com.sincar.customer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sincar.customer.R;
import com.sincar.customer.adapter.content.AgentTimeContent;
import com.sincar.customer.adapter.content.PointContent;

import java.util.List;

public class AgentTimeRecyclerViewAdapter extends RecyclerView.Adapter<AgentTimeRecyclerViewAdapter.ViewHolder> {

    private final List<AgentTimeContent.AgentTimeItem> mValues;

    public AgentTimeRecyclerViewAdapter(List<AgentTimeContent.AgentTimeItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.agent_time_list_item, parent, false);
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

        public AgentTimeContent.AgentTimeItem mItem;

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
