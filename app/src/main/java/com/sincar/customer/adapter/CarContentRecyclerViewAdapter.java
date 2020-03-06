package com.sincar.customer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.sincar.customer.NoticeActivity;
import com.sincar.customer.NoticeDetailActivity;
import com.sincar.customer.R;
import com.sincar.customer.SplashActivity;
import com.sincar.customer.adapter.content.CarContent;

import java.util.List;

public class CarContentRecyclerViewAdapter extends RecyclerView.Adapter<com.sincar.customer.adapter.CarContentRecyclerViewAdapter.ViewHolder> {

    private final List<CarContent.CarItem> mValues;
    private Context mContext;
    private String car_pos;
    private LinearLayout mLayout;
    private int itemCount = 0;
    private String[] selectStatus;

    public CarContentRecyclerViewAdapter(Context context, List<CarContent.CarItem> items) {
        mContext = context;
        mValues = items;
        itemCount = items.size();
        selectStatus = new String[itemCount];
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public com.sincar.customer.adapter.CarContentRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_list_item, parent, false);

        return new com.sincar.customer.adapter.CarContentRecyclerViewAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final com.sincar.customer.adapter.CarContentRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mCarTitle.setText(mValues.get(position).car_title);
        holder.mCarSeq = Integer.parseInt(mValues.get(position).car_seq);
        holder.mCarName.setText(mValues.get(position).car_name);
        holder.mCarNumber.setText(mValues.get(position).car_number);

        car_pos = String.valueOf(position);

        selectStatus[position] = "false";

        holder.mCarDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO - 리스트 선택 시 이벤트 핸들러 추가 필요하면 여기에서 해주기
                // 카드 삭제
                Toast.makeText(mContext, "차량 삭제 요청 : " + holder.mCarSeq, Toast.LENGTH_SHORT).show();
            }
        });

        mLayout = holder.mView.findViewById(R.id.car_layout);
        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO - 리스트 선택 시 이벤트 핸들러 추가 필요하면 여기에서 해주기
                // 차량 선택시 배경 색상 변경해주기
//                selectStatus[Integer.parseInt(car_pos)] = "true";
                Toast.makeText(mContext, "select", Toast.LENGTH_SHORT).show();
                mLayout.setBackgroundResource(R.color.card_background_color);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public final TextView mCarTitle;
        public final TextView mCarName;
        public final TextView mCarNumber;
        public final ImageView mCarDelete;
        public int mCarSeq;


        public CarContent.CarItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mCarTitle      = view.findViewById(R.id.car_column1);
            mCarName       = view.findViewById(R.id.car_column2);
            mCarNumber     = view.findViewById(R.id.car_column3);
            mCarDelete     = view.findViewById(R.id.car_delete);
            mCarSeq = 0;
        }
    }
}


