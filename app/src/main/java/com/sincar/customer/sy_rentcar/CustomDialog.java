package com.sincar.customer.sy_rentcar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.sincar.customer.R;

public class CustomDialog extends Dialog {

    OnMyDialogResult mDialogResult;
    private TextView tvCancel;
    private RadioButton tvDlgbtn1, tvDlgbtn2, tvDlgbtn3;
    int dlgCheck = 0;

    public CustomDialog(@NonNull Context context) {
        super(context);

        //다이얼로그 밖의 화면은 흐리게 만들어줌
//        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        layoutParams.dimAmount = 0.6f;
//        getWindow().setAttributes(layoutParams);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sy_custom_dialog);

        tvDlgbtn1 = (RadioButton) findViewById(R.id.dlgBtn1);
        tvDlgbtn2 = (RadioButton)findViewById(R.id.dlgBtn2);
        tvDlgbtn3 = (RadioButton)findViewById(R.id.dlgBtn3);
        tvCancel = (TextView) findViewById(R.id.dlgCancel);

        tvDlgbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDialogResult != null) {
                    mDialogResult.finish(0);
                }
                tvDlgbtn1.setChecked(true);
                tvDlgbtn2.setChecked(false);
                tvDlgbtn3.setChecked(false);
                dlgCheck = 0;
                dismiss();
            }
        });

        tvDlgbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDialogResult != null) {
                    mDialogResult.finish(1);
                }
                tvDlgbtn1.setChecked(false);
                tvDlgbtn2.setChecked(true);
                tvDlgbtn3.setChecked(false);
                dlgCheck = 1;
                dismiss();
            }
        });
        tvDlgbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDialogResult != null) {
                    mDialogResult.finish(2);
                }
                tvDlgbtn1.setChecked(false);
                tvDlgbtn2.setChecked(false);
                tvDlgbtn3.setChecked(true);
                dlgCheck = 2;
                dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    public void setDialogItem (int dialogItem) {
        dlgCheck = dialogItem;
        switch (dlgCheck) {
            case 0:
                tvDlgbtn1.setTextColor(R.color.agent_color_red);
                tvDlgbtn2.setTextColor(R.color.agent_color_1NoOpp);
                tvDlgbtn3.setTextColor(R.color.agent_color_1NoOpp);
                break;
            case 1:
                tvDlgbtn1.setTextColor(R.color.agent_color_1NoOpp);
                tvDlgbtn2.setTextColor(R.color.agent_color_red);
                tvDlgbtn3.setTextColor(R.color.agent_color_1NoOpp);
                break;
            case 2:
                tvDlgbtn1.setTextColor(R.color.agent_color_1NoOpp);
                tvDlgbtn2.setTextColor(R.color.agent_color_1NoOpp);
                tvDlgbtn3.setTextColor(R.color.agent_color_red);
                break;
            default:
                break;
        }
    }

    public void setDialogResult(OnMyDialogResult dialogResult) {
        mDialogResult = dialogResult;
    }

    public interface OnMyDialogResult {
        void finish(int result);
    }
}
