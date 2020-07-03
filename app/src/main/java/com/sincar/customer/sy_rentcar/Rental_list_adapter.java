package com.sincar.customer.sy_rentcar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sincar.customer.R;

import java.util.List;

public class Rental_list_adapter extends RecyclerView.Adapter<Rental_list_adapter.ViewHolder> {

    private List<Rental_list_adapterItem.Rental_List_Item> rentalValue1;
    private List<Rental_list_adapterItem.Rental_List_Item> rentalValue2;

    //차량 클릭시 이벤트 처리 (상세페이지로 이동)
    private OnRentalListInteractionListener mOnRentalListInteractionListener = null;

    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout conRental_1st,conRental_2nd;
        ImageView ivRental_Car1, ivRental_Car2;
        TextView tvRental_Shop1, tvRental_Shop2;
        TextView tvRental_CarName1, tvRental_CarName2;
        TextView tvRental_Dis1, tvRental_Dis2;
        TextView tvRental_Price1, tvRental_Price2;

        ViewHolder(View itemView) {
            super(itemView);
            //리사이클러뷰 아이템중 왼쪽
            ivRental_Car1 = itemView.findViewById(R.id.rent_list_iv1);
            tvRental_Shop1 = itemView.findViewById(R.id.rent_list_tv1_1);
            tvRental_CarName1 = itemView.findViewById(R.id.rent_list_tv1_2);
            tvRental_Dis1 = itemView.findViewById(R.id.rent_list_tv1_3);
            tvRental_Price1 = itemView.findViewById(R.id.rent_list_tv1_4);
            //리싸이클러뷰 아이템중 오른쪽
            ivRental_Car2 = itemView.findViewById(R.id.rent_list_iv2);
            tvRental_Shop2 = itemView.findViewById(R.id.rent_list_tv2_1);
            tvRental_CarName2 = itemView.findViewById(R.id.rent_list_tv2_2);
            tvRental_Dis2 = itemView.findViewById(R.id.rent_list_tv2_3);
            tvRental_Price2 = itemView.findViewById(R.id.rent_list_tv2_4);

            conRental_1st = itemView.findViewById(R.id.rent_list_con1);
            conRental_2nd = itemView.findViewById(R.id.rent_list_con2);

            //아이템 클릭시 이벤트 추후 데이터 전달 필요
            conRental_1st.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        if(mOnRentalListInteractionListener != null) {
                            mOnRentalListInteractionListener.onRentalListInteractionListener(Rental_list_adapterItem.RENTAL_LIST_ITEM1.get(pos));
                        }
                    }
                }
            });
            conRental_2nd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        if(mOnRentalListInteractionListener != null) {
                            mOnRentalListInteractionListener.onRentalListInteractionListener(Rental_list_adapterItem.RENTAL_LIST_ITEM2.get(pos));
                        }
                    }
                }
            });
        }
    }

    //생성자
    public Rental_list_adapter(List<Rental_list_adapterItem.Rental_List_Item> list1, List<Rental_list_adapterItem.Rental_List_Item> list2, Context context) {
        rentalValue1 = list1;
        rentalValue2 = list2;

        this.mContext = context;
    }

    @NonNull
    @Override
    public Rental_list_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.activity_rental_list_recycler_item, parent, false);
        Rental_list_adapter.ViewHolder vh = new Rental_list_adapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(rentalValue1.size() > position) {
            String ivUrl1 = rentalValue1.get(position).rentcar_img_url;
            String tvRental_Shop1 = rentalValue1.get(position).rentcar_agent;
            String tvRental_CarName1 = rentalValue1.get(position).rentcar_name;
            String tvRental_dis1 = rentalValue1.get(position).rentcar_discount;
            String tvRental_Price1 = rentalValue1.get(position).rentcar_price;

            Glide.with(mContext).load(ivUrl1).into(holder.ivRental_Car1);
            holder.tvRental_Shop1.setText(tvRental_Shop1);
            holder.tvRental_CarName1.setText(tvRental_CarName1);
            holder.tvRental_Dis1.setText(tvRental_dis1);
            holder.tvRental_Price1.setText(tvRental_Price1 + "원");
        }

        if(rentalValue2.size() > position) {
            String ivUrl2 = rentalValue2.get(position).rentcar_img_url;
            String tvRental_Shop2 = rentalValue2.get(position).rentcar_agent;
            String tvRental_CarName2 = rentalValue2.get(position).rentcar_name;
            String tvRental_dis2 = rentalValue2.get(position).rentcar_discount;
            String tvRental_Price2 = rentalValue2.get(position).rentcar_price;

            Glide.with(mContext).load(ivUrl2).into(holder.ivRental_Car2);
            holder.tvRental_Shop2.setText(tvRental_Shop2);
            holder.tvRental_CarName2.setText(tvRental_CarName2);
            holder.tvRental_Dis2.setText(tvRental_dis2);
            holder.tvRental_Price2.setText(tvRental_Price2 + "원");
        }

        //아이템 수가 홀수일 때 마지막 아이템의 오른쪽 Constrainlayout을 삭제
        if((rentalValue1.size() + rentalValue2.size()) % 2 != 0) {
            if((rentalValue1.size() + rentalValue2.size()) / 2  == position)
                holder.conRental_2nd.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if((rentalValue1.size() + rentalValue2.size()) % 2 == 0) {
            return (rentalValue1.size() + rentalValue2.size()) / 2;
        } else {
            return (rentalValue1.size() + rentalValue2.size()) / 2 + 1;
        }
    }

    public interface OnRentalListInteractionListener {
        void onRentalListInteractionListener(Rental_list_adapterItem.Rental_List_Item rental_list_item);
    }

    public void setOnItemClickListener(OnRentalListInteractionListener listener) {
        this.mOnRentalListInteractionListener = listener;
    }
}
