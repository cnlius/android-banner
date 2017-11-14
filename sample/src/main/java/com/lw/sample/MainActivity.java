package com.lw.sample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.lw.sample.databinding.ActivityMainBinding;
import com.lw.sample.imageloader.FrescoImageLoader;
import com.lw.sample.imageloader.GlideImageLoader;
import com.lw.sample.transformer.PageScaleYTransformer;
import com.lw.banner.Banner;
import com.lw.banner.FreeViewPager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    boolean isOk;
    private ActivityMainBinding mBinding;
    private Integer[] imgRes = {
            R.mipmap.mn,
            R.mipmap.mn_01,
            R.mipmap.mn_02,
            R.mipmap.mn_03
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initBanner1();
        initBanner2();
    }

    private void initBanner1() {
        mBinding.banner.setImages(Arrays.asList(imgRes))
                .setImageLoader(new FrescoImageLoader()) // 设置fresco图片加载器
                .setPageTransformer(true, new PageScaleYTransformer()) //页面切换动画
                .setOnItemClickListener(new Banner.OnItemClickListener() {
                    @Override
                    public void callBack(int position) {
                        Toast.makeText(MainActivity.this, "position=" + position, Toast.LENGTH_SHORT).show();
                    }
                })
                .setPageMargin(20)
                .setRightPageWidth(50)
                .setLeftPageWidth(50)
                .setEmptyImageRes(R.mipmap.no_banner)
                .init();
    }

    private void initBanner2() {
        mBinding.banner2.setImages(Arrays.asList(imgRes)) //设置图片资源
                .setImageLoader(new GlideImageLoader()) //设置Glide图片加载器
                //.setImageLoader(new FrescoImageLoader()) // 设置fresco图片加载器
                //.setPageTransformer(true, new PageScaleYTransformer()) //页面切换动画
                .addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                }) //设置banner改变监听
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
        FreeViewPager bannerViewPager = mBinding.banner2.getBannerViewPager();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                isOk = !isOk;
                List<Integer> images = new ArrayList<>();
                if (isOk) {
                    images.clear();
                    mBinding.btn.setText("填充banner");
                } else {
                    images.addAll(Arrays.asList(imgRes));
                    mBinding.btn.setText("清空banner");

                }
                mBinding.banner.update(images);
                mBinding.banner2.update(images);
                break;
        }
    }

    //关于轮播，如果你需要考虑更好的体验，可以这么操作
    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        mBinding.banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        mBinding.banner.stopAutoPlay();
    }
}
