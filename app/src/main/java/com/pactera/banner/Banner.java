package com.pactera.banner;


import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pactera.banner.SivinBanner.SivinBaseBanner;


/**
 * 广告轮播控件
 * Created by xiwen on 2016/3/16.
 */
public class Banner extends SivinBaseBanner<SquareBannerModel> {
    private Context mContext;
    public Banner(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }
    public Banner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    @Override
    public View onCreateItemView(int position) {
        ImageView iv = new ImageView(mContext);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        final SquareBannerModel item = mData.get(position);
        String imgUrl = item.getImageUrl();
        if (!TextUtils.equals("",imgUrl.trim())) {
            Glide.with(mContext)
                    .load(imgUrl)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(iv);
        } else {
            iv.setImageResource(R.mipmap.ic_launcher);
        }
        return iv;
    }
}
