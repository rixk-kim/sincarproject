package com.sincar.customer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import com.sincar.customer.R;
import com.sincar.customer.UseDetailActivity;
import com.sincar.customer.adapter.content.UseContent;

import java.util.List;

public class UseContentRecyclerViewAdapter extends RecyclerView.Adapter<com.sincar.customer.adapter.UseContentRecyclerViewAdapter.ViewHolder> {

    private final List<UseContent.UseItem> mValues;
    private Context mContext;
    private String use_pos;
    private LinearLayout mLayout;
    private int itemCount = 0;

    public UseContentRecyclerViewAdapter(Context context, List<UseContent.UseItem> items) {
        mContext = context;
        mValues = items;
        itemCount = items.size();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public com.sincar.customer.adapter.UseContentRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.use_list_item, parent, false);

        return new com.sincar.customer.adapter.UseContentRecyclerViewAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final com.sincar.customer.adapter.UseContentRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mUseSeq = Integer.parseInt(mValues.get(position).seq);
        holder.reserve_status = Integer.parseInt(mValues.get(position).reserve_status);
        holder.reserve_time.setText(mValues.get(position).reserve_time);
        holder.wash_address.setText(mValues.get(position).wash_address);
        holder.wash_agent.setText(mValues.get(position).wash_agent);
        holder.use_pay.setText(mValues.get(position).use_pay);
        holder.agent_mobile = mValues.get(position).agent_mobile;

        holder.common_pay = mValues.get(position).common_pay;       //기본요금
        holder.coupone_pay = mValues.get(position).coupone_pay;       //할인요금
        holder.approve_info = mValues.get(position).approve_info;       //결재정보
        holder.car_info = mValues.get(position).car_info;       //차량정보
        holder.car_number = mValues.get(position).car_number;       //차량번호

        use_pos = String.valueOf(position);

        //전화걸기
        holder.call_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String call_number = "tel:" + holder.agent_mobile;
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(call_number));
                mContext.startActivity(callIntent);

                Toast.makeText(mContext, "전화걸기 요청", Toast.LENGTH_SHORT).show();
            }
        });

        holder.reserve_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO - 리스트 선택 시 이벤트 핸들러 추가 필요하면 여기에서 해주기
                // 상세보기
                Intent intent = new Intent(mContext, UseDetailActivity.class);
                intent.putExtra("common_pay", mValues.get(Integer.parseInt(use_pos)).common_pay);
                intent.putExtra("coupone_pay", mValues.get(Integer.parseInt(use_pos)).coupone_pay);
                intent.putExtra("approve_info", mValues.get(Integer.parseInt(use_pos)).approve_info);
                intent.putExtra("use_pay", mValues.get(Integer.parseInt(use_pos)).use_pay);
                intent.putExtra("reserve_time", mValues.get(Integer.parseInt(use_pos)).reserve_time);
                intent.putExtra("wash_address", mValues.get(Integer.parseInt(use_pos)).wash_address);
                intent.putExtra("wash_agent", mValues.get(Integer.parseInt(use_pos)).wash_agent);
                intent.putExtra("agent_mobile", mValues.get(Integer.parseInt(use_pos)).agent_mobile);
                intent.putExtra("car_info", mValues.get(Integer.parseInt(use_pos)).car_info);
                intent.putExtra("car_number", mValues.get(Integer.parseInt(use_pos)).car_number);
                mContext.startActivity(intent);
                Toast.makeText(mContext, "상세보기 요청", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public int mUseSeq;
        public int reserve_status;


        public final TextView reserve_time;
        public final TextView wash_address;
        public final TextView wash_agent;
        public final TextView use_pay;
        public String agent_mobile;

        public String common_pay;       //기본요금
        public String coupone_pay;      //할인요금
        public String approve_info;     //결재정보
        public String car_info;         //차량정보
        public String car_number;       //차량번호


        public final TextView reserve_view;
        public final TextView call_view;

        public UseContent.UseItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mUseSeq         = 0;
            reserve_status  = 0;
            reserve_view    = view.findViewById(R.id.use_column1);
            reserve_time    = view.findViewById(R.id.use_column3);
            wash_address    = view.findViewById(R.id.use_column5);
            wash_agent      = view.findViewById(R.id.use_column7);
            call_view       = view.findViewById(R.id.use_column8);
            use_pay         = view.findViewById(R.id.use_column10);
            agent_mobile    = "";

            common_pay      = "";       //기본요금
            coupone_pay     = "";       //할인요금
            approve_info    = "";       //결재정보
            car_info        = "";       //차량정보
            car_number      = "";       //차량번호

        }
    }
}
