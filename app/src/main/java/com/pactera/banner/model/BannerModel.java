package com.pactera.banner.model;

/**
 * Created by xiwen on 2016/4/11.
 */
public class BannerModel {

    private String imageUrl;
    private String mTips;

    public String getTips() {
        return mTips;
    }

    public void setTips(String tips) {
        mTips = tips;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
