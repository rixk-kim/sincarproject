package com.sincar.customer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import com.sincar.customer.R;
import com.sincar.customer.adapter.content.CarContent;
import com.sincar.customer.adapter.content.TimeContent;

import java.util.List;

public class CarContentRecyclerViewAdapter extends RecyclerView.Adapter<com.sincar.customer.adapter.CarContentRecyclerViewAdapter.ViewHolder>
        implements View.OnClickListener {

    private final List<CarContent.CarItem> mValues;
    private Context mContext;
    private LinearLayout mLayout;
    private String carName;
    private String carNumber;
    private String carPay;

    public CarContentRecyclerViewAdapter(Context context, List<CarContent.CarItem> items) {
        mContext = context;
        mValues = items;
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
        holder.mView.setTag(holder.mItem);
        holder.mView.setOnClickListener(this);

        setViewLayout(holder, position);

//        holder.mItem = mValues.get(position);
//
//        holder.mCarTitle.setText(mValues.get(position).car_title);
//        holder.mCarSeq = Integer.parseInt(mValues.get(position).car_seq);
//        holder.mCarName.setText(mValues.get(position).car_name);
//        holder.mCarNumber.setText(mValues.get(position).car_number);
//
//        car_pos = String.valueOf(position);
//
//        holder.mCarDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO - 리스트 선택 시 이벤트 핸들러 추가 필요하면 여기에서 해주기
//                // 카드 삭제
//                Toast.makeText(mContext, "차량 삭제 요청 : " + holder.mCarSeq, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        mLayout = holder.mView.findViewById(R.id.car_layout);
//        mLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO - 리스트 선택 시 이벤트 핸들러 추가 필요하면 여기에서 해주기
//                // 차량 선택시 배경 색상 변경해주기
////                selectStatus[Integer.parseInt(car_pos)] = "true";
//                Toast.makeText(mContext, "select", Toast.LENGTH_SHORT).show();
//                mLayout.setBackgroundResource(R.color.card_background_color);
//            }
//        });
    }

    private void setViewLayout(final ViewHolder holder, final int position) {
        holder.mCarTitle.setText(mValues.get(position).car_title);
        holder.mCarSeq = Integer.parseInt(mValues.get(position).car_seq);
        holder.mCarName.setText(mValues.get(position).car_name);
        holder.mCarNumber.setText(mValues.get(position).car_number);

        mLayout = holder.mView.findViewById(R.id.car_layout);

        if (holder.mItem.car_selected == true) {
            mLayout.setBackgroundResource(R.color.card_background_color);
        } else {
            mLayout.setBackgroundResource(R.color.base_background_color);
        }

        holder.mCarDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO - 리스트 선택 시 이벤트 핸들러 추가 필요하면 여기에서 해주기
                // 카드 삭제
                Toast.makeText(mContext, "차량 삭제 요청 : " + holder.mCarSeq, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        CarContent.CarItem item = (CarContent.CarItem)v.getTag();

        for (CarContent.CarItem carItem : mValues) {
            carItem.car_selected = false;
        }
//        if (prePosition >= 0 && prePosition < mValues.size()) {
//            mValues.get(prePosition).selected = false;
//        }
//        prePosition = item.id;

        mValues.get(item.id-1).car_selected = true;

        carName     = mValues.get(item.id-1).car_name;
        carNumber   = mValues.get(item.id-1).car_number;
        carPay      = mValues.get(item.id-1).car_pay;
//        mValues.get(Integer.parseInt(item.car_seq)).agentPosition = agentPosition;
        notifyDataSetChanged();

//        if (mListener != null) {
//            mListener.OnCarListInteractionListener(item);
//        }
    }

    public String getItemcarName()
    {
        return carName;
    }

    public String getItemcarNumber()
    {
        return carNumber;
    }

    public String getItemcarPay()
    {
        return carPay;
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


    public interface OnCarListInteractionListener {
        void onCarListInteraction(CarContent.CarItem carItem);
    }
}


