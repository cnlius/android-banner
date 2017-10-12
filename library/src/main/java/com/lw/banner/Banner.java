package com.lw.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * banner
 * Created by liusong on 2017/9/29.
 */

public class Banner extends FrameLayout implements ViewPager.OnPageChangeListener {
    public static final String tag = "banner";
    private Context context;
    private FreeScroller mScroller;

    private List imageUrls;
    private List<View> imageViews;
    private ImageLoaderInterface imageLoader;
    private int count = 0;
    private int currentItem; //index of current item
    private boolean scrollable = BannerConfig.SCROLLABLE;
    private int scrollDuration = BannerConfig.SCROLL_DURATION; //slide switch duration
    private boolean isAutoPlay = BannerConfig.IS_AUTO_PLAY;
    private int delayTime = BannerConfig.DELAY_TIME; // auto play interval

    private int pageMargin = 0;
    private int pageLimit = BannerConfig.PAGE_LIMIT; //viewpager preload page number
    private int leftPageWidth = 0;
    private int rightPageWidth = 0;

    private BannerPagerAdapter adapter;
    private OnItemClickListener onItemClickListener;
    private ViewPager.OnPageChangeListener onPageChangeListener;
    private ImageView.ScaleType emptyImageScaleType = ImageView.ScaleType.FIT_XY;
    private Integer emptyImageRes;

    private WeakHandler handler = new WeakHandler();
    private FreeViewPager viewPager;
    private ImageView emptyView;
    private View rootView;

    public Banner(@NonNull Context context) {
        this(context, null);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        imageUrls = new ArrayList<>();
        imageViews = new ArrayList<>();
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        handleTypedArray(context, attrs);
        rootView = LayoutInflater.from(context).inflate(R.layout.banner, this, true);
        viewPager = rootView.findViewById(R.id.view_pager);
        emptyView = rootView.findViewById(R.id.empty_view);
        initViewPagerScroll();
    }

    /**
     * handle custom attrs
     *
     * @param context
     * @param attrs
     */
    private void handleTypedArray(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Banner);
        delayTime = typedArray.getInt(R.styleable.Banner_delayTime, BannerConfig.DELAY_TIME);
        scrollable = typedArray.getBoolean(R.styleable.Banner_scrollable, BannerConfig.SCROLLABLE);
        isAutoPlay = typedArray.getBoolean(R.styleable.Banner_isAutoPlay, BannerConfig.IS_AUTO_PLAY);
        scrollDuration = typedArray.getInt(R.styleable.Banner_scrollDuration, BannerConfig.SCROLL_DURATION);
        pageMargin = typedArray.getDimensionPixelSize(R.styleable.Banner_pageMargin, 0);
        pageLimit = typedArray.getInt(R.styleable.Banner_pageLimit, BannerConfig.PAGE_LIMIT);
        leftPageWidth = typedArray.getDimensionPixelSize(R.styleable.Banner_leftPageWidth, 0);
        rightPageWidth = typedArray.getDimensionPixelSize(R.styleable.Banner_rightPageWidth, 0);
        emptyImageRes = typedArray.getResourceId(R.styleable.Banner_emptyImage, 0);
        typedArray.recycle();
    }

    public Banner setImages(List<?> imageUrls) {
        this.imageUrls.addAll(imageUrls);
        this.count = imageUrls.size();
        return this;
    }

    public Banner setLeftPageWidth(int leftPageWidth) {
        this.leftPageWidth = leftPageWidth;
        return this;
    }

    public Banner setRightPageWidth(int rightPageWidth) {
        this.rightPageWidth = rightPageWidth;
        return this;
    }

    /**
     * set page transformer style
     *
     * @param reverseDrawingOrder
     * @param transformer
     * @return
     */
    public Banner setPageTransformer(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer) {
        viewPager.setPageTransformer(reverseDrawingOrder, transformer);
        return this;
    }

    /**
     * set viewpager scrollable
     *
     * @param scrollable
     * @return
     */
    public Banner setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
        return this;
    }

    public Banner addOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
        return this;
    }

    /**
     * set a image loader
     *
     * @param imageLoader
     * @return
     */
    public Banner setImageLoader(ImageLoaderInterface imageLoader) {
        this.imageLoader = imageLoader;
        return this;
    }

    /**
     * whether auto play
     *
     * @param isAutoPlay
     * @return
     */
    public Banner isAutoPlay(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
        return this;
    }

    /**
     * set interval for auto play
     *
     * @param delayTime unit/Millisecond
     * @return
     */
    public Banner setDelayTime(int delayTime) {
        this.delayTime = delayTime;
        return this;
    }

    /**
     * set distance for pages
     *
     * @param pageMargin
     * @return
     */
    public Banner setPageMargin(int pageMargin) {
        this.pageMargin = pageMargin;
        return this;
    }

    public FreeViewPager getBannerViewPager() {
        return viewPager;
    }

    /**
     * set preload pages
     *
     * @param pageLimit
     * @return
     */
    public Banner setPageLimit(int pageLimit) {
        this.pageLimit = pageLimit;
        return this;
    }

    /**
     * banner update
     *
     * @param imageUrls
     */
    public void update(List<?> imageUrls) {
        this.imageUrls.clear();
        this.imageViews.clear();
        this.imageUrls.addAll(imageUrls);
        this.count = this.imageUrls.size();
        init();
    }

    public void init() {
        setImageList(imageUrls);
        setData();
    }

    public Banner setEmptyImageScaleType(ImageView.ScaleType emptyScaleType) {
        this.emptyImageScaleType = emptyScaleType;
        return this;
    }

    public Banner setEmptyImageRes(int emptyImageRes) {
        this.emptyImageRes = emptyImageRes;
        return this;
    }

    public ImageView getEmptyView() {
        return emptyView;
    }

    private void setImageList(List<?> imagesUrl) {
        if (imagesUrl == null || imagesUrl.size() <= 0) {
            emptyView.setVisibility(VISIBLE);
            emptyView.setScaleType(emptyImageScaleType);
            if (emptyImageRes != null && emptyImageRes != 0) {
                emptyView.setImageResource(emptyImageRes);
            }
            FrameLayout.LayoutParams layoutParams = (LayoutParams) emptyView.getLayoutParams();
            layoutParams.setMargins(leftPageWidth, 0, rightPageWidth, 0);

            Log.e(tag, "The image data set is empty.");
            return;
        }
        emptyView.setVisibility(GONE);

        if (imageLoader == null) {
            Log.e(tag, "Please set image loader");
            return;
        }
        imageViews.clear();
        // banner is count+2
        // show a page when handle count=1
        int items = count > 1 ? count + 1 : 0;
        // add banner images
        for (int i = 0; i <= items; i++) {
            View imageView = imageLoader.createImageView(context);
            Object url = null;
            if (i == 0) {
                url = imagesUrl.get(count - 1);
            } else if (i == count + 1) {
                url = imagesUrl.get(0);
            } else {
                url = imagesUrl.get(i - 1);
            }
            imageViews.add(imageView);
            imageLoader.displayImage(context, url, imageView);
        }
    }

    private void setData() {
        if (adapter == null) {
            adapter = new BannerPagerAdapter();
            viewPager.addOnPageChangeListener(this);
        }

        FrameLayout.LayoutParams layoutParams = (LayoutParams) viewPager.getLayoutParams();
        layoutParams.setMargins(leftPageWidth, 0, rightPageWidth, 0);

        // set page distance
        viewPager.setPageMargin(pageMargin);
        viewPager.setOffscreenPageLimit(pageLimit);
        rootView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // make viewpager get touchEvent
                return viewPager.dispatchTouchEvent(motionEvent);
            }
        });

        viewPager.setAdapter(adapter);
        viewPager.setFocusable(true);
        viewPager.setCurrentItem(1);
        currentItem = 1; //init item index=1

        if (scrollable && count > 1) {
            viewPager.setScrollable(true);
        } else {
            viewPager.setScrollable(false);
        }
        if (isAutoPlay) {
            startAutoPlay();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // stop auto play on touch
        if (isAutoPlay) {
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                startAutoPlay();
            } else if (action == MotionEvent.ACTION_DOWN) {
                stopAutoPlay();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /* onPageChangeListener start */

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageSelected(position);
        }
        currentItem = position;
    }

    /**
     * Use onPageChangeListener to implement the loop
     *
     * @param state
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrollStateChanged(state);
        }

        switch (state) {
            case 0://No operation
                if (currentItem == 0) {
                    // smoothScroll=false
                    viewPager.setCurrentItem(count, false);
                } else if (currentItem == count + 1) {
                    viewPager.setCurrentItem(1, false);
                }
                break;
            case 1://start Sliding
                if (currentItem == count + 1) {
                    viewPager.setCurrentItem(1, false);
                } else if (currentItem == 0) {
                    viewPager.setCurrentItem(count, false);
                }
                break;
            case 2://end Sliding
                break;
        }
    }

    /* onPageChangeListener end */

    public Banner setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
        return this;
    }

    /**
     * get real index
     *
     * @param position
     * @return the index starts at 0
     */
    public int getRealPosition(int position) {
        int realPosition = (position - 1) % count;
        if (realPosition < 0) {
            //Leftmost item=banner last index
            realPosition += count;
        }
        return realPosition;
    }

    /*BannerViewPager的adapter*/
    class BannerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            container.addView(imageViews.get(position));
            View view = imageViews.get(position);
            if (onItemClickListener != null) {
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.callBack(getRealPosition(position));
                    }
                });
            }
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public interface OnItemClickListener {
        void callBack(int position);
    }

    private void initViewPagerScroll() {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new FreeScroller(context);
            mScroller.setDuration(scrollDuration);
            mField.set(viewPager, mScroller);
        } catch (Exception e) {
            Log.e(tag, e.getMessage());
        }
    }

    public void releaseBanner() {
        handler.removeCallbacksAndMessages(null);
    }

     /* loop auto play start */

    public void startAutoPlay() {
        isAutoPlay = true;
        handler.removeCallbacks(task);
        handler.postDelayed(task, delayTime);
    }

    public void stopAutoPlay() {
        handler.removeCallbacks(task);
    }

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (count > 1 && isAutoPlay) {
                currentItem = currentItem % (count + 1) + 1;
//                Log.i(tag, "curr:" + currentItem + " count:" + count);
                if (currentItem == 1) {
                    viewPager.setCurrentItem(currentItem, false);
                    handler.post(task);
                } else {
                    viewPager.setCurrentItem(currentItem);
                    handler.postDelayed(task, delayTime);
                }
            }
        }
    };

    /* loop auto play end */

}
