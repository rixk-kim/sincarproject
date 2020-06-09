package com.sincar.customer.sy_rentcar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.sincar.customer.R;

/**
 * 렌탈 리스트용 Custom Dialog
 */
public class CustomDialog extends DialogFragment implements View.OnClickListener {

    OnMyDialogResult mDialogResult;
    private TextView tvDlgbtn1, tvDlgbtn2, tvDlgbtn3,  tvCancel;
    int dlgCheck;

    public CustomDialog() {
    }

    public static CustomDialog getInstance() {
        CustomDialog e = new CustomDialog();
        return e;
    }

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sy_custom_dialog, container);
        //다이얼로그 모서리를 둥글게 만들기 위한 background설정 메소드
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        //다이얼로그 밖 터치시 취소 되는 설정 금지
        setCancelable(false);

        tvDlgbtn1 = (TextView) v.findViewById(R.id.dlgBtn1);
        tvDlgbtn2 = (TextView) v.findViewById(R.id.dlgBtn2);
        tvDlgbtn3 = (TextView) v.findViewById(R.id.dlgBtn3);
        tvCancel = (TextView) v.findViewById(R.id.dlgCancel);
        //렌탈 리스트 액티비티에서 넘어온 데이터 변수에 적용(체크된 아이템 구분)
        if(getArguments() != null)
            dlgCheck = getArguments().getInt("dlgCheck");
        //변수 데이터에 따라 아이템 체크 0: 거리순 1: 가격순 2: 인기순
        switch (dlgCheck) {
            case 0:
                //체크된 아이템은 텍스트칼라를 Red로 변환 및 체크 이미지 적용
                tvDlgbtn1.setTextColor(getResources().getColor(R.color.agent_color_red));
                tvDlgbtn2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                tvDlgbtn3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
            case 1:
                tvDlgbtn2.setTextColor(getResources().getColor(R.color.agent_color_red));
                tvDlgbtn1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                tvDlgbtn3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
            case 2:
                tvDlgbtn3.setTextColor(getResources().getColor(R.color.agent_color_red));
                tvDlgbtn1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                tvDlgbtn2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
            default:
                break;
        }
        tvDlgbtn1.setOnClickListener(this);
        tvDlgbtn2.setOnClickListener(this);
        tvDlgbtn3.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

        return v;
    }

    //아이템 클릭시 클릭된 아이템 구분 변수를 렌탈 리스트 액티비티에 넘김 0: 거리순 1: 가격순 2: 인기순
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dlgBtn1:
                if(mDialogResult != null) {
                    mDialogResult.finish(0);
                }
                dismiss();
                break;
            case R.id.dlgBtn2:
                if(mDialogResult != null) {
                    mDialogResult.finish(1);
                }
                dismiss();
                break;
            case R.id.dlgBtn3:
                if(mDialogResult != null) {
                    mDialogResult.finish(2);
                }
                dismiss();
                break;
            case R.id.dlgCancel:
                dismiss();
                break;
        }
    }

    //
//    public CustomDialog(@NonNull Context context) {
//        super(context, R.style.sy_dialog);
//
//        //다이얼로그 밖의 화면은 흐리게 만들어줌
////        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
////        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
////        layoutParams.dimAmount = 0.6f;
////        getWindow().setAttributes(layoutParams);


    //다이얼로그 종료시 결과 전송을 위한 메소드

    public void setDialogResult(OnMyDialogResult dialogResult) {
        mDialogResult = dialogResult;
    }

    public interface OnMyDialogResult {
        void finish(int result);
    }
}
