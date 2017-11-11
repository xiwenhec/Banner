###如何使用

首先在gradle文件中添加
    
     compile 'com.xiwenhec:banner:1.0.5'

与`listView`的实现习惯，增加了一个适配器，调用者只需要这样一下几步就可以完成:
第一步：在xml代码写入控件
```xml
   <com.sivin.Banner 
        android:id="@+id/id_banner"
        android:layout_width="match_parent"
        android:layout_height="180dp"
        app:banner_pointGravity="right"
        />
```
　　第二步：java代码中绑定控件
```java
 mBanner = (Banner) findViewById(R.id.id_banner);
```

　　第三步：实例化适配器，并设置适配器，建议您在`new BannerAdapter<BannerModel>`的时候将后面的`<>`中的泛型加上，然后在根据工具提示实现未完成的方法。这里你需要重写`bindTips(TextView tv, BannerModel bannerModel)`和`bindImage(ImageView imageView, BannerModel bannerModel)`方法，方法的第二个参数就是你加入的泛型类型。其中`mDatas`你的`banner`的数据集合，具体过程使用就会有所体会。
　　
注意：不要忘了`mDatas`的初始化，合理的做法应该这样的` private List<BannerModel> mDatas = new ArrayList<>();`
```java
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
```
　　最后一步：告诉banner数据不部署完成，为什么这样做呢，正常情况下，我们的数据都是从网络上异步加载的，一般的情况下会以集合的形式传递过来，当我们在完成网络加载的时候，改变了`mDatas`数据，然后调用`mBanner.notifiDataHasChanged();`通知`banner`就行了，使用起来和`listview`的习惯是不是很相似呢，对就是这样
```java
 mBanner.notifyDataHasChanged();
```

为了更加优美的控制`banner`控件，这里提供了很多可控制的属性，我们可以这样做：

```xml
    <com.sivin.Banner
        android:id="@+id/id_banner"
        android:layout_width="match_parent"
        android:layout_height="180dp"
        app:banner_pointContainerLeftRightPadding="10dp"
        app:banner_pointTopBottomMargin="6dp"
        app:banner_pointLeftRightMargin="3dp"
        app:banner_pointGravity="right"
        app:banner_pointContainerBackground="#33000000"
        app:banner_pointDrawable="@drawable/selector_banner_point"
        app:banner_pointAutoPlayInterval="3"
        app:banner_pageChangeDuration="800"
        app:banner_tipTextColor="#FFFFFF"
        app:banner_tipTextSize="12sp"/>
```
