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

    //커스텀 리스트뷰에서 아이템 체크를 위한 커스텀 레이아웃

    private boolean mIsChecked; //체크 확인 변수

    public CheckableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mIsChecked = false;
    }

    //체크 확인 메소드
    @Override
    public boolean isChecked() {
        return mIsChecked;
    }

    //토글 체크된 상태에 따라 반대로 변환
    @Override
    public void toggle() {
        ImageView iv = (ImageView)findViewById(R.id.ivRental_list_deli_list_item);

        setChecked(mIsChecked ? false : true);

    }

    //체크 확인 메소드 및 체크에 따른 상태 변환

    @Override
    public void setChecked(boolean checked) {
        ImageView iv = (ImageView)findViewById(R.id.ivRental_list_deli_list_item);
        TextView tv = (TextView)findViewById(R.id.tvRental_list_deli_list_item);

        if(mIsChecked != checked) {
            mIsChecked = checked;
        }
        //체크시 체크이미지 표시 및 텍스트칼라 Red로 변경
        iv.setVisibility(mIsChecked ? VISIBLE : INVISIBLE);
        tv.setTextColor(mIsChecked ? getResources().getColor(R.color.agent_color_red) : getResources().getColor(R.color.agent_color_1NoOpp));
    }

}
