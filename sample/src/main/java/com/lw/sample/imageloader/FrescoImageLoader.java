package com.lw.sample.imageloader;

import android.content.Context;
import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lw.banner.ImageLoaderInterface;

public class FrescoImageLoader implements ImageLoaderInterface<SimpleDraweeView> {
    @Override
    public void displayImage(Context context, Object path, SimpleDraweeView imageView) {
        //用fresco加载图片
        Uri uri = Uri.parse("res://com.jason.banner/"+String.valueOf(path));
        imageView.setImageURI(uri);

    }

    //提供createImageView 方法，方便fresco自定义ImageView
    @Override
    public SimpleDraweeView createImageView(Context context) {
        SimpleDraweeView simpleDraweeView = new SimpleDraweeView(context);
        return simpleDraweeView;
    }
}
