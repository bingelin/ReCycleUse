package com.binge.recycleuse;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by binge on 2016/12/30.
 */
public class CycleRotationView extends FrameLayout {

    private Context mContext;
    private ViewPager mViewPager;
    private LinearLayout mPointGroup;

    private List<ImageView> mList;
    private Handler mHandler;

    private int pointSize = 20;
    private int pointMargin = 20;

    private long time = 3000;

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mListener;

    public CycleRotationView(Context context) {
        super(context);
    }

    public CycleRotationView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.mContext = context;
        mHandler = new Handler();
        mList = new ArrayList<>();
        initView(mContext);
    }

    private void initView(Context mContext) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.cycle_rotation_layout, this, true);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mPointGroup = (LinearLayout) view.findViewById(R.id.pointGroup);
    }

    public void setUrls(String[] urls) {
        if (urls == null || urls.length == 0) {
            this.setVisibility(GONE);
            return;
        }
        for (int i = 0; i < urls.length; i++) {
            ImageView img = new ImageView(mContext);
            Glide.with(mContext)
                    .load(urls[i])
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .into(img);
            mList.add(img);

            makePoints(i);
        }

        setUpWithAdapter();
        timerTask();

    }

    public void setTime(long i) {
        this.time = i;
    }

    public void setImages(int[] images) {
        if (images == null || images.length == 0) {
            this.setVisibility(GONE);
            return;
        }
        for (int i = 0; i < images.length; i++) {
            ImageView img = new ImageView(mContext);
            img.setImageResource(images[i]);
            mList.add(img);

            makePoints(i);
        }

        setUpWithAdapter();
        timerTask();

    }

    private void makePoints(int i) {
        ImageView point = new ImageView(mContext);
        point.setImageResource(R.drawable.shape_point_selector);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pointSize, pointSize);

        if (i > 0) {
            params.leftMargin = pointMargin;
            point.setSelected(false);
        } else {
            point.setSelected(true);
        }

        point.setLayoutParams(params);
        mPointGroup.addView(point);
    }

    private void timerTask() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int currentItem = mViewPager.getCurrentItem();
                if (currentItem == mViewPager.getAdapter().getCount() - 1) {
                    mViewPager.setCurrentItem(1);
                } else {
                    mViewPager.setCurrentItem(currentItem + 1);
                }
                mHandler.postDelayed(this, time);
            }
        }, time);

    }

    private void setUpWithAdapter() {
        mViewPager.setAdapter(new CycleAdapter());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            int lastPosition;
            @Override
            public void onPageSelected(int position) {
                position = position % mList.size();
                mPointGroup.getChildAt(position).setSelected(true);
                mPointGroup.getChildAt(lastPosition).setSelected(false);
                lastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private class CycleAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % mList.size();
            final View child = mList.get(position);
            if (child.getParent() != null) {
                container.removeView(child);
            }

            if (mListener != null) {
                final int finalPosition = position;
                child.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemClick(child, finalPosition);
                    }
                });
            }
            container.addView(mList.get(position));
            return mList.get(position);

        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public void setPointSize(int size) {
        this.pointSize = dp2px(size);
    }

    public void setPointMargin(int margin) {
        this.pointMargin = dp2px(margin);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());

    }

}

