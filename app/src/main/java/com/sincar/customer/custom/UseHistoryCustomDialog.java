package com.sincar.customer.custom;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.sincar.customer.R;

public class UseHistoryCustomDialog  extends DialogFragment implements View.OnClickListener {
    private OnMyDialogResult history_mDialogResult;
    private TextView history_tvDlgbtn1, history_tvDlgbtn2, history_tvDlgbtn3,  history_tvCancel;
    int history_dlgCheck;

    public UseHistoryCustomDialog() {
    }

    public static UseHistoryCustomDialog getInstance() {
        UseHistoryCustomDialog e = new UseHistoryCustomDialog();
        return e;
    }

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.history_custom_dialog, container);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        history_tvDlgbtn1 = (TextView) v.findViewById(R.id.history_dlgBtn1);
        history_tvDlgbtn2 = (TextView) v.findViewById(R.id.history_dlgBtn2);
        history_tvDlgbtn3 = (TextView) v.findViewById(R.id.history_dlgBtn3);
        history_tvCancel = (TextView) v.findViewById(R.id.history_dlgCancel);
        if(getArguments() != null)
            history_dlgCheck = getArguments().getInt("history_dlgCheck");
        switch (history_dlgCheck) {
            case 0:
                history_tvDlgbtn1.setTextColor(getResources().getColor(R.color.agent_color_red));
                history_tvDlgbtn2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                history_tvDlgbtn3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
            case 1:
                history_tvDlgbtn2.setTextColor(getResources().getColor(R.color.agent_color_red));
                history_tvDlgbtn1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                history_tvDlgbtn3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
            case 2:
                history_tvDlgbtn3.setTextColor(getResources().getColor(R.color.agent_color_red));
                history_tvDlgbtn1.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                history_tvDlgbtn2.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;

            default:
                break;
        }
        history_tvDlgbtn1.setOnClickListener(this);
        history_tvDlgbtn2.setOnClickListener(this);
        history_tvDlgbtn3.setOnClickListener(this);
        history_tvCancel.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.history_dlgBtn1:
                if(history_mDialogResult != null) {
                    history_mDialogResult.finish(0);
                }
                dismiss();
                break;
            case R.id.history_dlgBtn2:
                if(history_mDialogResult != null) {
                    history_mDialogResult.finish(1);
                }
                dismiss();
                break;
            case R.id.history_dlgBtn3:
                if(history_mDialogResult != null) {
                    history_mDialogResult.finish(2);
                }
                dismiss();
                break;
            case R.id.history_dlgCancel:
                dismiss();
                break;
        }
    }

    public void setDialogResult(OnMyDialogResult dialogResult) {
        history_mDialogResult = dialogResult;
    }

    public interface OnMyDialogResult {
        void finish(int result);
    }
}
