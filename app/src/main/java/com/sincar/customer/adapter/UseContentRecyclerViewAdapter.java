package com.sincar.customer.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.sincar.customer.R;
import com.sincar.customer.UseDetailActivity;
import com.sincar.customer.adapter.content.UseContent;
import com.sincar.customer.util.Util;

import java.util.List;

import static com.sincar.customer.util.Util.setAddMoneyDot;
import static java.security.AccessController.getContext;

public class UseContentRecyclerViewAdapter extends RecyclerView.Adapter<com.sincar.customer.adapter.UseContentRecyclerViewAdapter.ViewHolder> {

    private final List<UseContent.UseItem> mValues;
    private Context mContext;
    private String use_pos;
    private LinearLayout mLayout;
    private int itemCount = 0;
    private Activity useActivity;
    private LinearLayout useLinearLayout;
    private String send_reserve_time, send_use_pay, send_wash_address, send_wash_agent;

    public UseContentRecyclerViewAdapter(Context context, List<UseContent.UseItem> items, Activity uActivity) {
        mContext    = context;
        mValues     = items;
        itemCount   = items.size();
        useActivity = uActivity;
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

        holder.mUseSeq = mValues.get(position).seq;
        holder.reserve_status = mValues.get(position).reserve_status;

        holder.reserve_time.setText(mValues.get(position).reserve_time);
        send_reserve_time = mValues.get(position).reserve_time;

        holder.cancel_time = mValues.get(position).cancel_time;
        holder.wash_address.setText(mValues.get(position).wash_address);
        send_wash_address = mValues.get(position).wash_address;

        holder.wash_agent.setText(mValues.get(position).wash_agent);
        send_wash_agent = mValues.get(position).wash_agent;

        holder.use_pay.setText(Util.setAddMoneyDot(mValues.get(position).use_pay) + "원");
        send_use_pay = mValues.get(position).use_pay;

        holder.agent_mobile = mValues.get(position).agent_mobile;

        holder.common_pay = mValues.get(position).common_pay;       //기본요금
        holder.coupone_pay = mValues.get(position).coupone_pay;       //할인요금
        holder.approve_info = mValues.get(position).approve_info;       //결재정보
        holder.car_info = mValues.get(position).car_info;       //차량정보
        holder.car_number = mValues.get(position).car_number;       //차량번호

        //0: 예약중, 1:완료 , 2: 예약취소
        if("0".equals(holder.reserve_status)) {
            holder.reserve_view.setText("예약 건");
        }else if("1".equals(holder.reserve_status)) {
            holder.reserve_view.setText("완료 건");
        }else if("2".equals(holder.reserve_status)) {
            holder.reserve_view.setText("예약취소 건");
        }

        use_pos = String.valueOf(position);

        //전화걸기
        holder.call_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String call_number = "tel:" + holder.agent_mobile;
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(call_number));
//                mContext.startActivity(callIntent);

                //====권한체크부분====//
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(useActivity, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    //권한을 허용하지 않는 경우
                } else {
                    //권한을 허용한 경우
                    try {
                        mContext.startActivity(callIntent);
                    } catch(SecurityException e) {
                        e.printStackTrace();
                    }
                }

//                Toast.makeText(mContext, "전화걸기 요청", Toast.LENGTH_SHORT).show();
            }
        });

        holder.useLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 리스트 선택 시 이벤트 핸들러 추가 필요하면 여기에서 해주기
                // 상세보기
                Intent intent = new Intent(mContext, UseDetailActivity.class);
                intent.putExtra("reserve_seq", holder.mUseSeq);
                intent.putExtra("reserve_status", holder.reserve_status);
                intent.putExtra("common_pay", holder.common_pay);
                intent.putExtra("coupone_pay", holder.coupone_pay);
                intent.putExtra("approve_info", holder.approve_info);
                intent.putExtra("use_pay", send_use_pay);
                intent.putExtra("reserve_time", send_reserve_time);

                intent.putExtra("cencel_time", holder.cancel_time);
                intent.putExtra("wash_address", send_wash_address);
                intent.putExtra("wash_agent", send_wash_agent);
                intent.putExtra("agent_mobile", holder.agent_mobile);
                intent.putExtra("car_info", holder.car_info);
                intent.putExtra("car_number", holder.car_number);
                mContext.startActivity(intent);

//                Toast.makeText(mContext, "상세보기 요청", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void viewRenew() {
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public String mUseSeq;
        public String reserve_status;
        private LinearLayout useLinearLayout;

        public final TextView reserve_time;
        public final TextView wash_address;
        public final TextView wash_agent;
        public final TextView use_pay;
        public String agent_mobile;

        public String cancel_time;      //예약취소시간
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

            mUseSeq         = "";
            reserve_status  = "";
            reserve_view    = view.findViewById(R.id.use_column1);
            reserve_time    = view.findViewById(R.id.use_column3);
            wash_address    = view.findViewById(R.id.use_column5);
            wash_agent      = view.findViewById(R.id.use_column7);
            call_view       = view.findViewById(R.id.use_column8);
            use_pay         = view.findViewById(R.id.use_column10);
            agent_mobile    = "";

            cancel_time     = "";       //예약취소시간
            common_pay      = "";       //기본요금
            coupone_pay     = "";       //할인요금
            approve_info    = "";       //결재정보
            car_info        = "";       //차량정보
            car_number      = "";       //차량번호
            useLinearLayout = view.findViewById(R.id.use_layout);
        }
    }
}
