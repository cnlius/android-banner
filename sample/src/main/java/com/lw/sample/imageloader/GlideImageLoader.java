package com.lw.sample.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lw.banner.ImageLoaderInterface;


public class GlideImageLoader implements ImageLoaderInterface<ImageView> {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        Glide.with(context.getApplicationContext())
                .load(path)
                .into(imageView);
    }

    @Override
    public ImageView createImageView(Context context) {
        ImageView imageView = new ImageView(context);
        return imageView;
    }

}
