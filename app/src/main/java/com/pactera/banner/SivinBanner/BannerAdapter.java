package com.pactera.banner.SivinBanner;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sivin on 2016/5/1.
 */
public abstract class BannerAdapter<T> {
    private List<T> mDatas;

    public List<T> getDatas() {
        return mDatas;
    }

    public BannerAdapter(List<T> datas) {
        mDatas = datas;
    }

    public void setImageViewSource(ImageView imageView,int position) {
        bindImage(imageView, mDatas.get(position));
    }

    public void selectTips(TextView tv, int position) {
        if (mDatas != null && mDatas.size() > 0)
            bindTips(tv, mDatas.get(position));
    }

    protected abstract void bindTips(TextView tv, T t);

    public abstract void bindImage(ImageView imageView, T t);


}
