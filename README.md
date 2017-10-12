# android-banner

注：本banner仅仅是banner，不包含指示器、变换动画、图片加载器等多余的代码，作者认为这些部件完全应该分离出来，**详细用法见案例app**。

### 项目地址：https://github.com/cnlius/android-banner

### overview

![效果图](https://github.com/cnlius/android-banner/blob/master/screenshot/overview.gif)

### usage

#### Gradle

```
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compile 'com.github.cnlius:banner:1.0.0'
}
```

#### ProGuard

```
-keep class com.lw.banner.** {*;}
```

#### Layout


```xml
<com.lw.banner.Banner
    android:id="@+id/banner"
    android:layout_width="match_parent"
    android:layout_height="160dp" />
```

xml中可设置的一些属性

属性名称| 属性值类型 | 作用
---|---|---
delayTime | integer | 循环轮播时间间隔
scrollDuration | integer | banner滚动过程持续时间
isAutoPlay | boolean | 是否自动轮播
scrollable | boolean | banner是否可以手动滑动
pageMargin | dimension | banner页面之间的间隔
pageLimit | integer | banner预加载页面数
leftPageWidth | dimension | 当前页面左侧页面可以显示的宽度
rightPageWidth | dimension | 当前页面右侧页面可以显示的宽度
emptyImage | reference | banner为空时的占位图片资源引用

案例：xml中设置循环轮播时间间隔
```xml
<com.lw.banner.Banner
    android:id="@+id/banner"
    android:layout_width="match_parent"
    android:layout_height="160dp"
    app:delayTime="1000"/>
```

#### Code


```java
List<String> urls=getUrls();
banner.setImages(urls)
        .setImageLoader(new GlideImageLoader())
        .init();       
```

代码中可以配置的一些属性：
```java
banner.setImages(Arrays.asList(imgRes)) //设置图片资源
        //.setImageLoader(new FrescoImageLoader()) // 设置fresco图片加载器
        .setImageLoader(new GlideImageLoader()) //设置Glide图片加载器
        //.setPageTransformer(true, new PageScaleYTransformer()) //页面切换动画
        //.addOnPageChangeListener(this) //设置banner的viewpager改变监听
        .setOnItemClickListener(new Banner.OnItemClickListener() {
            @Override
            public void callBack(int position) {
                Toast.makeText(MainActivity.this, "position=" + position, Toast.LENGTH_SHORT).show();
            }
        }) //设置banner点击监听
        //.setScrollable(false) // 禁用banner手动滑动
        //.setDelayTime(1000) // 循环轮播时间间隔
        //.setPageLimit(4) // 设置预加载页面数量
        //.setPageMargin(20) // banner页面之间的间隔
        //.setRightPageWidth(50) // 当前页面右侧页面可以显示的宽度
        //.setLeftPageWidth(50) // 当前页面左侧页面可以显示的宽度
        //.setEmptyImageRes(R.mipmap.no_banner) // banner为空时占位图
        //.setEmptyImageScaleType(ImageView.ScaleType.FIT_CENTER) // 占位图的缩放类型,默认FIT_XY
        .isAutoPlay(true) // 设置自动循环轮播
        .init();
// 拿到占位图ImageView对象
ImageView emptyView = mBinding.banner2.getEmptyView();
emptyView.setImageResource(R.mipmap.mn_01);
// 拿到BannerViewPager对象
BannerViewPager bannerViewPager = mBinding.banner2.getBannerViewPager();      
```

生命周期中控制循环轮播的周期
```java
@Override
protected void onStart() {
    super.onStart();
    //开始轮播
    banner.startAutoPlay();
}
@Override
protected void onStop() {
    super.onStop();
    //结束轮播
    banner.stopAutoPlay();
}
```

### 参考

https://github.com/youth5201314/banner

