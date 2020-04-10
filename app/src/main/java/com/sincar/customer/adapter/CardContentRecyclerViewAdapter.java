package com.sincar.customer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import com.sincar.customer.R;
import com.sincar.customer.adapter.content.CardContent;
import java.util.List;

/**
 * 202.04.09 spirit
 * 카드 정보 class : 사용 안함.
 */
public class CardContentRecyclerViewAdapter extends RecyclerView.Adapter<com.sincar.customer.adapter.CardContentRecyclerViewAdapter.ViewHolder> {

    private final List<CardContent.CardItem> mValues;
    private Context mContext;
    private String card_pos;
    private LinearLayout mLayout;
    private int itemCount = 0;

    public CardContentRecyclerViewAdapter(Context context, List<CardContent.CardItem> items) {
        mContext = context;
        mValues = items;
        itemCount = items.size();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public com.sincar.customer.adapter.CardContentRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_list_item, parent, false);

        return new com.sincar.customer.adapter.CardContentRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final com.sincar.customer.adapter.CardContentRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mCardTitle.setText(mValues.get(position).title);
        holder.mCardInfo.setText(mValues.get(position).info);
        holder.mCardSeq = Integer.parseInt(mValues.get(position).seq);

        card_pos = String.valueOf(position);

        holder.mCardDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 리스트 선택 시 이벤트 핸들러 추가 필요하면 여기에서 해주기
                // 카드 삭제
                Toast.makeText(mContext, "카드 삭제 요청", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public final TextView mCardTitle;
        public final TextView mCardInfo;
        public final ImageView mCardDelete;
        public int mCardSeq;


        public CardContent.CardItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mCardTitle      = view.findViewById(R.id.card_column1);
            mCardInfo       = view.findViewById(R.id.card_column2);
            mCardDelete     = view.findViewById(R.id.card_delete);
//            mPoint          = view.findViewById(R.id.notice_column4);
            mCardSeq = 0;
        }
    }
}


