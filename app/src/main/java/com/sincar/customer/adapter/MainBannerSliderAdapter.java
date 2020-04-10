package com.sincar.customer.adapter;

import java.util.ArrayList;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

/**
 * 2020.04.09 spirit
 * 배너 class.
 */
public class MainBannerSliderAdapter extends SliderAdapter {

    ArrayList<String> imageUrls = new ArrayList<String>();

    public MainBannerSliderAdapter() {
    }

    public MainBannerSliderAdapter(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @Override
    public int getItemCount() {
        int size = 0;

        if (this.imageUrls.size() == 0) {
            size = imageUrls.size();    //max value 3;
        } else {
            size = this.imageUrls.size();
        }

        return size;
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
        if (this.imageUrls.size() == 0) {
            switch (position) {
                case 0:
                    viewHolder.bindImageSlide("https://assets.materialup.com/uploads/dcc07ea4-845a-463b-b5f0-4696574da5ed/preview.jpg");
                    break;
                case 1:
                    viewHolder.bindImageSlide("https://assets.materialup.com/uploads/20ded50d-cc85-4e72-9ce3-452671cf7a6d/preview.jpg");
                    break;
                case 2:
                    viewHolder.bindImageSlide("https://assets.materialup.com/uploads/76d63bbc-54a1-450a-a462-d90056be881b/preview.png");
                    break;
            }
        } else {
            if (position >= this.imageUrls.size()) {
                return;
            }

            viewHolder.bindImageSlide(this.imageUrls.get(position));
        }
    }
}
