package com.sincar.customer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sincar.customer.CarManageActivity;
import com.sincar.customer.R;
import com.sincar.customer.adapter.content.CarContent;

import java.util.List;

/**
 * 2020.04.09 spirit
 * 차량정보 class
 */
public class CarContentRecyclerViewAdapter extends RecyclerView.Adapter<com.sincar.customer.adapter.CarContentRecyclerViewAdapter.ViewHolder>
        implements View.OnClickListener {

    private final List<CarContent.CarItem> mValues;
    private Context mContext;
    private LinearLayout mLayout;
    private String carTitle;    //제조사
    private String carName;     //차량이름
    private String carNumber;   //차량번호
    private String carPay;      //차량
    private String carType;     //차량타입
    private String path;        //진입경로 구분분
    public CarContentRecyclerViewAdapter(Context context, List<CarContent.CarItem> items, String path) {
        mContext    = context;
        mValues     = items;
        this.path   = path;
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
                // 리스트 선택 시 이벤트 핸들러 추가 필요하면 여기에서 해주기
                // 카드 삭제
                //Toast.makeText(mContext, "차량 삭제 요청 : " + holder.mCarSeq, Toast.LENGTH_SHORT).show();
                ((CarManageActivity)CarManageActivity.carContext).carDelete(String.valueOf(holder.mCarSeq));
            }
        });
    }

    @Override
    public void onClick(View v) {
        CarContent.CarItem item = (CarContent.CarItem)v.getTag();

        for (CarContent.CarItem carItem : mValues) {
            carItem.car_selected = false;
        }

        if("reserve".equals(path))
        {
            mValues.get(item.id).car_selected = true;

            carTitle    = mValues.get(item.id).car_title;   //제조사
            carName     = mValues.get(item.id).car_name;    //차종
            carNumber   = mValues.get(item.id).car_number;  //차번호
            carPay      = mValues.get(item.id).car_pay;     //기본 세차비용
            carType     = mValues.get(item.id).car_type;    //차량타입
        }

        notifyDataSetChanged();
    }

    /**
     * @return 차량 제조사
     */
    public String getItemCompanyName()
    {
        return carTitle;
    }

    /**
     * @return 차량 이름
     */
    public String getItemcarName()
    {
        return carName;
    }

    /**
     * @return 차량 번호
     */
    public String getItemcarNumber()
    {
        return carNumber;
    }

    /**
     * @return 차량 기본 세차비용
     */
    public String getItemcarPay()
    {
        return carPay;
    }

    /**
     * @return 차량 타입
     */
    public String getItemcarType()
    {
        return carType;
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


