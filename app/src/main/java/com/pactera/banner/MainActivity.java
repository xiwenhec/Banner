package com.pactera.banner;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pactera.banner.model.BannerModel;
import com.sivin.Banner;
import com.sivin.BannerAdapter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Banner mBanner;
    private List<BannerModel> mDatas = new ArrayList<>();
    Button mRefreshButton;
    int num = 1;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        mBanner = (Banner) findViewById(R.id.id_banner);

        BannerAdapter adapter = new BannerAdapter<BannerModel>(mDatas) {
            @Override
            protected void bindTips(TextView tv, BannerModel bannerModel) {
                tv.setText(bannerModel.getTips());
            }

            @Override
            public void bindImage(ImageView imageView, BannerModel bannerModel) {
                Glide.with(mContext)
                        .load(bannerModel.getImageUrl())
                        .placeholder(R.mipmap.empty)
                        .error(R.mipmap.error)
                        .into(imageView);
            }

        };
        mBanner.setBannerAdapter(adapter);

        mRefreshButton = (Button) findViewById(R.id.button);




        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNetData();
            }
        });

        getNetData();
    }

    private void getNetData() {
        Log.d(TAG, "getNetData: ");
        OkHttpUtils.get()
                .url("https://gw.alicdn.com/tps/i3/TB1J9GqJXXXXXcZaXXXdIns_XXX-1125-352.jpg_q50.jpg")
                .build().execute(new BitmapCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onError: okhttp 网路访问异常" + "Exception" + e.getMessage() + "exception+fillInStackTrace()" + e.fillInStackTrace());
            }

            @Override
            public void onResponse(Bitmap bitmap) {
                Log.d(TAG, "onResponse: " + num++);
                if (num % 2 == 1)
                    getData();
                else {
                    getData2();
                }

            }
        });
    }

    private void getData() {
        mDatas.clear();
        BannerModel model = null;
        model = new BannerModel();
        model.setImageUrl("https://gma.alicdn.com/simba/img/TB1FS.AJpXXXXc_XpXXSutbFXXX.jpg_q50.jpg");
        model.setTips("这是页面1");
        mDatas.add(model);
        model = new BannerModel();
        model.setImageUrl("https://gw.alicdn.com/tps/i3/TB1J9GqJXXXXXcZaXXXdIns_XXX-1125-352.jpg_q50.jpg");
        model.setTips("这是页面2");
        mDatas.add(model);
        model = new BannerModel();
        model.setImageUrl("https://gma.alicdn.com/simba/img/TB1txffHVXXXXayXVXXSutbFXXX.jpg_q50.jpg");
        model.setTips("这是页面3");
        mDatas.add(model);
        model = new BannerModel();
        model.setImageUrl("https://gw.alicdn.com/tps/TB1fW3ZJpXXXXb_XpXXXXXXXXXX-1125-352.jpg_q50.jpg");
        model.setTips("这是页面4");
        mDatas.add(model);
        model = new BannerModel();
        model.setImageUrl("https://gw.alicdn.com/tps/i2/TB1ku8oMFXXXXciXpXXdIns_XXX-1125-352.jpg_q50.jpg");
        model.setTips("这是页面5");
        mDatas.add(model);
        mBanner.notifyDataHasChanged();
    }

    private void getData2() {
        mDatas.clear();
        BannerModel model = null;
        model = new BannerModel();
        model.setImageUrl("https://gw.alicdn.com/tps/i3/TB1J9GqJXXXXXcZaXXXdIns_XXX-1125-352.jpg_q50.jpg");
        model.setTips("这是页面1");
        mDatas.add(model);
        model = new BannerModel();
        model.setImageUrl("https://gma.alicdn.com/simba/img/TB1txffHVXXXXayXVXXSutbFXXX.jpg_q50.jpg");
        model.setTips("这是页面2");
        mDatas.add(model);
        model = new BannerModel();
        model.setImageUrl("https://gw.alicdn.com/tps/i2/TB1ku8oMFXXXXciXpXXdIns_XXX-1125-352.jpg_q50.jpg");
        model.setTips("这是页面3");
        mDatas.add(model);
        model.setImageUrl("https://gma.alicdn.com/simba/img/TB1txffHVXXXXayXVXXSutbFXXX.jpg_q50.jpg");
        model.setTips("这是页面4");
        mDatas.add(model);
        mBanner.notifyDataHasChanged();
    }

}
