package com.sincar.customer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import com.sincar.customer.R;
import com.sincar.customer.adapter.content.OptionContent.OptionItem;
import static com.sincar.customer.util.Util.setAddMoneyDot;

/**
 * 2020.04.09 spirit
 * 부가 서비스 class
 */
public class OptionServiceRecyclerViewAdapter extends RecyclerView.Adapter<OptionServiceRecyclerViewAdapter.ViewHolder> {

    private final List<OptionItem> mValues;
    private int tmpPosition = -1;
    public OptionServiceRecyclerViewAdapter(List<OptionItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_option_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);

        holder.mChecked.setChecked(mValues.get(position).checked);
        holder.mOptionName.setText(mValues.get(position).option_name);
        holder.mOptionPay.setText("+" + setAddMoneyDot(mValues.get(position).option_pay) + "원");
        tmpPosition = position;
        holder.mChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 체크박스 이벤트 핸들러 추가
                //mValues.get(tmpPosition).checked  = isChecked;
                holder.mItem.checked = isChecked;
                System.out.println("[spirit] checked => " + isChecked);

            }
        });
        holder.mOptionName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 리스트 선택 시 이벤트 핸들러 추가 필요하면 여기에서 해주기
                if(holder.mItem.checked) {
                    holder.mItem.checked = false;
                    holder.mChecked.setChecked(false);
                }else{
                    holder.mItem.checked = true;
                    holder.mChecked.setChecked(true);
                }
            }
        });

        holder.mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 리스트 선택 시 이벤트 핸들러 추가 필요하면 여기에서 해주기
                Toast.makeText(v.getContext(), mValues.get(position).option_info, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public final CheckBox mChecked;
        public final TextView mOptionName;
        public final ImageView mInfo;
        public final TextView mOptionPay;

        public OptionItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mChecked = view.findViewById(R.id.checkBoxOption);
            mOptionName = view.findViewById(R.id.option_name);
            mInfo = view.findViewById(R.id.btnOptionInfo);
            mOptionPay = view.findViewById(R.id.option_pay);
        }
    }
}
