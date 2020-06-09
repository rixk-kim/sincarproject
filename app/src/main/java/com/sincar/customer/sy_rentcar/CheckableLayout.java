package com.sincar.customer.sy_rentcar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.sincar.customer.R;

public class CheckableLayout extends ConstraintLayout implements Checkable {

    private boolean mIsChecked;

    public CheckableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mIsChecked = false;
    }

    @Override
    public boolean isChecked() {
        return mIsChecked;
    }

    @Override
    public void toggle() {
        ImageView iv = (ImageView)findViewById(R.id.ivRental_list_deli_list_item);

        setChecked(mIsChecked ? false : true);

    }

    @Override
    public void setChecked(boolean checked) {
        ImageView iv = (ImageView)findViewById(R.id.ivRental_list_deli_list_item);
        TextView tv = (TextView)findViewById(R.id.tvRental_list_deli_list_item);

        if(mIsChecked != checked) {
            mIsChecked = checked;
        }
        iv.setVisibility(mIsChecked ? VISIBLE : INVISIBLE);
        tv.setTextColor(mIsChecked ? getResources().getColor(R.color.agent_color_red) : getResources().getColor(R.color.agent_color_1NoOpp));
    }

}
