package com.sincar.customer.service;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import ss.com.bannerslider.ImageLoadingService;

public class PicassoImageLoadingService implements ImageLoadingService {

    public PicassoImageLoadingService() {
    }

    @Override
    public void loadImage(String url, ImageView imageView) {
        setImageViewRadius(imageView);

        Picasso.get().load(url).into(imageView);
    }

    @Override
    public void loadImage(int resource, ImageView imageView) {
        setImageViewRadius(imageView);

        Picasso.get().load(resource).into(imageView);
    }

    @Override
    public void loadImage(String url, int placeHolder, int errorDrawable, ImageView imageView) {
        setImageViewRadius(imageView);

        Picasso.get().load(url).placeholder(placeHolder).error(errorDrawable).into(imageView);
    }

    // TODO - ImageView Radius 속성 동작 안됨. 이미지에 라운드 처리 필요
    private void setImageViewRadius(ImageView imageView) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(100);
        imageView.setBackground(gradientDrawable);
    }
}
