package com.sincar.customer.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.sincar.customer.R;

public class LoadingProgress extends ProgressDialog {
    private ImageView mLoadingView;

    public LoadingProgress(final Context context) {
        super(context, R.style.CustomAlertDialog);
    }

    public LoadingProgress(final Context context,final int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        setContentView(R.layout.loading_progress);
        mLoadingView = findViewById(R.id.loading);
        if(mLoadingView.getBackground() instanceof AnimationDrawable){
            AnimationDrawable animation = (AnimationDrawable) mLoadingView.getBackground();
            animation.start();
        }
        setCancelable(false);
    }

}
