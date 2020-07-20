package com.sincar.customer.sy_rentcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.sincar.customer.HWApplication.rentCarDetailResult;

import com.sincar.customer.R;
import com.sincar.customer.item.RentCarDetailItem;
import com.sincar.customer.item.RentCarDetailResult;
import com.sincar.customer.util.Util;

import java.util.List;

public class Rental_list_detail_insu_recycle_item extends RecyclerView.Adapter<Rental_list_detail_insu_recycle_item.ViewHolder> {

    private List<RentCarDetailItem.Rentcar_Insurance> rentalInsu;
    private int checkPos = 99;

    //보험 선택 클릭 리스너
    private OnRentalInsuInteractionListener mOnRentalInsuInteractionListener = null;

    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        CheckBox checkBox;
        TextView tvInsuName, tvInsuPri, tvInsuInfo;
        ImageView ivInsuInfo;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linear_rental_insu);
            checkBox = itemView.findViewById(R.id.delete2_checkbox);
            tvInsuName = itemView.findViewById(R.id.rental_insu_name);
            ivInsuInfo = itemView.findViewById(R.id.rental_insu_info);
            tvInsuPri = itemView.findViewById(R.id.rental_car_insurance);
            tvInsuInfo = itemView.findViewById(R.id.tvInsu_Info);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mOnRentalInsuInteractionListener != null) {
                            if (checkPos == pos)
                                checkPos = 99;
                            else
                                checkPos = pos;
                            mOnRentalInsuInteractionListener.onRentalInsuInteractionListener(rentalInsu.get(pos));
                        }
                    }
                }
            });
        }
    }

    //생성자
    public Rental_list_detail_insu_recycle_item(List<RentCarDetailItem.Rentcar_Insurance> list, Context context) {
        rentalInsu = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.activity_rental_list_detail_insu_recycle_item, parent, false);
        Rental_list_detail_insu_recycle_item.ViewHolder vh = new Rental_list_detail_insu_recycle_item.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (rentalInsu.size() > position && !"".equals(rentalInsu.get(0).CURRENT_INSU_NAME)) {
            String insuName = rentalInsu.get(position).CURRENT_INSU_NAME;
            String insuPri = rentalInsu.get(position).CURRENT_INSU_PRI;

            holder.tvInsuName.setText(insuName);
            holder.tvInsuPri.setText(Util.setAddMoneyDot(insuPri) + "원");
            holder.checkBox.setChecked(false);
            if (checkPos == position)
                holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setVisibility(View.GONE);
            holder.tvInsuName.setText("보험이 없습니다.");
            holder.tvInsuPri.setText("");
            holder.ivInsuInfo.setVisibility(View.GONE);
            holder.tvInsuInfo.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return rentalInsu.size();
    }

    public int getItemPosition() { return checkPos; }

    public interface OnRentalInsuInteractionListener {
        void onRentalInsuInteractionListener(RentCarDetailItem.Rentcar_Insurance rentcar_insurance);
    }

    public void setOnItemClickListener(OnRentalInsuInteractionListener listener) {
        this.mOnRentalInsuInteractionListener = listener;
    }
}