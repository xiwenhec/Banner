package com.pactera.banner;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.xiwen.okhttputils.OkHttpUtils;
import com.xiwen.okhttputils.callback.BitmapCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Banner mBanner;
    private List<SquareBannerModel> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBanner = (Banner) findViewById(R.id.banner);
//        SquareBannerModel model =null;
//        model = new SquareBannerModel();
//        model.setImageUrl("http://img04.tooopen.com/images/20130712/tooopen_17270713.jpg");
//        mDatas.add(model);
//        model = new SquareBannerModel();
//        model.setImageUrl("http://img02.tooopen.com/images/20160409/tooopen_sy_158839197671.jpg");
//        mDatas.add(model);
//        model = new SquareBannerModel();
//        model.setImageUrl("http://img02.tooopen.com/images/20160324/tooopen_sy_157026478417.jpg");
//        mDatas.add(model);
//        SquareBannerModel model = null;
//        model = new SquareBannerModel();
//        model.setImageUrl("http://img04.tooopen.com/images/20130712/tooopen_17270713.jpg");
//        mDatas.add(model);
//        model = new SquareBannerModel();
//        model.setImageUrl("http://img02.tooopen.com/images/20160409/tooopen_sy_158839197671.jpg");
//        mDatas.add(model);
//        model = new SquareBannerModel();
//        model.setImageUrl("http://img02.tooopen.com/images/20160324/tooopen_sy_157026478417.jpg");
//        mDatas.add(model);
        mBanner.setSource(mDatas);
//        mBanner.goScroll();

        OkHttpUtils.get()
                .url("http://img02.tooopen.com/images/20160324/tooopen_sy_157026478417.jpg")
                .build().execute(new BitmapCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(Bitmap bitmap) {
                Log.d(TAG, "onResponse: ");
                SquareBannerModel model = null;
                model = new SquareBannerModel();
                model.setImageUrl("http://img04.tooopen.com/images/20130712/tooopen_17270713.jpg");
                mDatas.add(model);
                model = new SquareBannerModel();
                model.setImageUrl("http://img02.tooopen.com/images/20160409/tooopen_sy_158839197671.jpg");
                mDatas.add(model);
                model = new SquareBannerModel();
                model.setImageUrl("http://img02.tooopen.com/images/20160324/tooopen_sy_157026478417.jpg");
                mDatas.add(model);
                mBanner.notifiDataHasChanged();
            }
        });
    }

}
