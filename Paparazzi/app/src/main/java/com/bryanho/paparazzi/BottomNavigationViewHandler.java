package com.bryanho.paparazzi;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

public class BottomNavigationViewHandler extends BottomNavigationView {
    private int mShiftAmount;
    private float mScaleUpFactor;
    private float mScaleDownFactor;
    private boolean animationRecord;
    private float mLargeLabelSize;
    private float mSmallLabelSize;

    private MyOnNavigationItemSelectedListener mMyOnNavigationItemSelectedListener;
    private BottomNavigationMenuView mMenuView;
    private BottomNavigationItemView[] mButtons;

    public BottomNavigationViewHandler(Context context) {
        super(context);
    }

    public BottomNavigationViewHandler(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomNavigationViewHandler(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void enableAnimation(boolean enable) {
        final BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        final BottomNavigationItemView[] mButtons = getBottomNavigationItemViews();

        for (BottomNavigationItemView button : mButtons) {
            final TextView mLargeLabel = getField(button.getClass(), button, "mLargeLabel");
            final TextView mSmallLabel = getField(button.getClass(), button, "mSmallLabel");

            // if disable animation, need animationRecord the source value
            if (!enable) {
                if (!animationRecord) {
                    animationRecord = true;
                    mShiftAmount = getField(button.getClass(), button, "mShiftAmount");
                    mScaleUpFactor = getField(button.getClass(), button, "mScaleUpFactor");
                    mScaleDownFactor = getField(button.getClass(), button, "mScaleDownFactor");

                    mLargeLabelSize = mLargeLabel.getTextSize();
                    mSmallLabelSize = mSmallLabel.getTextSize();
                }
                // disable
                setField(button.getClass(), button, "mShiftAmount", 0);
                setField(button.getClass(), button, "mScaleUpFactor", 1);
                setField(button.getClass(), button, "mScaleDownFactor", 1);

                // let the mLargeLabel font size equal to mSmallLabel
                mLargeLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSmallLabelSize);
            } else {
                if (!animationRecord) {
                    return;
                }
                // enable animation
                setField(button.getClass(), button, "mShiftAmount", mShiftAmount);
                setField(button.getClass(), button, "mScaleUpFactor", mScaleUpFactor);
                setField(button.getClass(), button, "mScaleDownFactor", mScaleDownFactor);
                // restore
                mLargeLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLargeLabelSize);
            }
        }
        mMenuView.updateMenuView();
    }

    public void enableShiftingMode(boolean enable) {
        final BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        setField(mMenuView.getClass(), mMenuView, "mShiftingMode", enable);
        mMenuView.updateMenuView();
    }

    public void enableItemShiftingMode(boolean enable) {
        final BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        final BottomNavigationItemView[] mButtons = getBottomNavigationItemViews();
        for (BottomNavigationItemView button : mButtons) {
            setField(button.getClass(), button, "mShiftingMode", enable);
        }
        mMenuView.updateMenuView();
    }

    public void setCurrentItem(int item) {
        if (item < 0 || item >= getMaxItemCount()) {
            throw new ArrayIndexOutOfBoundsException("item is out of bounds, we expected 0 - " + (getMaxItemCount() - 1) + ". Actually " + item);
        }

        final BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        final BottomNavigationItemView[] mButtons = getBottomNavigationItemViews();
        final View.OnClickListener mOnClickListener = getField(mMenuView.getClass(), mMenuView, "mOnClickListener");
        if (mOnClickListener != null) {
            mOnClickListener.onClick(mButtons[item]);
        }
    }

    @Override
    public void setOnNavigationItemSelectedListener(@Nullable OnNavigationItemSelectedListener listener) {
        if (null == mMyOnNavigationItemSelectedListener) {
            super.setOnNavigationItemSelectedListener(listener);
            return;
        }
        mMyOnNavigationItemSelectedListener.setOnNavigationItemSelectedListener(listener);
    }

    private BottomNavigationMenuView getBottomNavigationMenuView() {
        if (null == mMenuView) {
            mMenuView = getField(BottomNavigationView.class, this, "mMenuView");
        }
        return mMenuView;
    }

    public BottomNavigationItemView[] getBottomNavigationItemViews() {
        if (null != mButtons) {
            return mButtons;
        }

        final BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        mButtons = getField(mMenuView.getClass(), mMenuView, "mButtons");
        return mButtons;
    }

    public BottomNavigationItemView getBottomNavigationItemView(int position) {
        return getBottomNavigationItemViews()[position];
    }

    private <T> T getField(Class targetClass, Object instance, String fieldName) {
        try {
            final Field field = targetClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setField(Class targetClass, Object instance, String fieldName, Object value) {
        try {
            final Field field = targetClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class BottomNavigationViewHandlerOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private final WeakReference<BottomNavigationViewHandler> mBnveRef;

        private BottomNavigationViewHandlerOnPageChangeListener(BottomNavigationViewHandler bnve) {
            mBnveRef = new WeakReference<>(bnve);
        }

        @Override
        public void onPageScrollStateChanged(final int state) {
        }

        @Override
        public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(final int position) {
            final BottomNavigationViewHandler bnve = mBnveRef.get();
            if (null != bnve) {
                bnve.setCurrentItem(position);
            }
        }
    }

    private static class MyOnNavigationItemSelectedListener implements OnNavigationItemSelectedListener {
        private OnNavigationItemSelectedListener listener;
        private final WeakReference<ViewPager> viewPagerRef;
        private boolean smoothScroll;
        private SparseIntArray items;// used for change ViewPager selected item
        private int previousPosition = -1;

        MyOnNavigationItemSelectedListener(ViewPager viewPager, BottomNavigationViewHandler bnve, boolean smoothScroll, OnNavigationItemSelectedListener listener) {
            this.viewPagerRef = new WeakReference<>(viewPager);
            this.listener = listener;
            this.smoothScroll = smoothScroll;

            final Menu menu = bnve.getMenu();
            final int size = menu.size();
            items = new SparseIntArray(size);
            for (int i = 0; i < size; i++) {
                int itemId = menu.getItem(i).getItemId();
                items.put(itemId, i);
            }
        }

        private void setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener listener) {
            this.listener = listener;
        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            final int position = items.get(item.getItemId());
            // only set item when item changed
            if (previousPosition == position) {
                return true;
            }

            // user listener
            if (null != listener) {
                boolean bool = listener.onNavigationItemSelected(item);
                // if the selected is invalid, no need change the view pager
                if (!bool) {
                    return false;
                }
            }

            // change view pager
            final ViewPager viewPager = viewPagerRef.get();
            if (null == viewPager) {
                return false;
            }

            viewPager.setCurrentItem(items.get(item.getItemId()), smoothScroll);
            previousPosition = position;
            return true;
        }
    }
}
