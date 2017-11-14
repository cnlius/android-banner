package com.lw.sample.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;


/**
 * viewpager切换动画：按高度差缩放
 */
public class PageScaleYTransformer implements ViewPager.PageTransformer {

    /**
     * 根据page的position变化
     * > 不可见：(-infinity,-1),(1,+infinity)
     * > 部分可见：[-1,0)，(0,1]
     * > 完全可见：0
     * Created by liusong on 2017/9/28.
     */
    @Override
    public void transformPage(View page, float position) {
        // Log.i("LOG_CAT", "view=" + page + ",position=" + position);
        if (position < -1) {
            page.setScaleY(0.9f);
        } else if (position < 0) {
            page.setScaleY(0.1f * position + 1);
        } else if (position < 1) {
            page.setScaleY(-0.1f * position + 1);
        } else {
            page.setScaleY(0.9f);
        }
    }
}
