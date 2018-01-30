package com.bryanho.paparazzi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

public class BottomNavigationViewHandler extends BottomNavigationView {
    private ViewPager mViewPager;
    private MyOnNavigationItemSelectedListener mMyOnNavigationItemSelectedListener;
    private BottomNavigationViewHandlerOnPageChangeListener mPageChangeListener;
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

    public void setCurrentItem(int item) {
        if (item < 0 || item >= getMaxItemCount()) {
            throw new ArrayIndexOutOfBoundsException("item is out of bounds, we expected 0 - " + (getMaxItemCount() - 1) + ". Actually " + item);
        }

        // Get Menu View
        final BottomNavigationMenuView menuView = getBottomNavigationMenuView();

        // Get Buttons
        final BottomNavigationItemView[] buttons = getBottomNavigationItemViews();

        // Get OnClickListener
        final View.OnClickListener mOnClickListener = getField(menuView.getClass(), menuView, "mOnClickListener");
        if (mOnClickListener != null) {
            // Call OnClickListener
            mOnClickListener.onClick(buttons[item]);
        }
    }

    @Override
    public void setOnNavigationItemSelectedListener(@Nullable OnNavigationItemSelectedListener listener) {
        // if not set up with view pager, the same with father
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
        /*
         * 1 private final BottomNavigationMenuView mMenuView;
         * 2 private BottomNavigationItemView[] mButtons;
         */
        final BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        mButtons = getField(mMenuView.getClass(), mMenuView, "mButtons");
        return mButtons;
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

    private static class BottomNavigationViewHandlerOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private final WeakReference<BottomNavigationViewHandler> bottomNavigationViewHandlerWeakReference;

        public BottomNavigationViewHandlerOnPageChangeListener(BottomNavigationViewHandler bottomNavigationViewHandler) {
            bottomNavigationViewHandlerWeakReference = new WeakReference<>(bottomNavigationViewHandler);
        }

        @Override
        public void onPageScrollStateChanged(final int state) {
        }

        @Override
        public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(final int position) {
            final BottomNavigationViewHandler bottomNavigationViewHandler = bottomNavigationViewHandlerWeakReference.get();
            if (null != bottomNavigationViewHandler) {
                bottomNavigationViewHandler.setCurrentItem(position);
            }
        }
    }

    private static class MyOnNavigationItemSelectedListener implements OnNavigationItemSelectedListener {
        private OnNavigationItemSelectedListener listener;
        private final WeakReference<ViewPager> viewPagerRef;
        private boolean smoothScroll;
        private SparseIntArray items;// used for change ViewPager selected item
        private int previousPosition = -1;

        MyOnNavigationItemSelectedListener(ViewPager viewPager, BottomNavigationViewHandler bottomNavigationViewHandler, boolean smoothScroll, OnNavigationItemSelectedListener listener) {
            this.viewPagerRef = new WeakReference<>(viewPager);
            this.listener = listener;
            this.smoothScroll = smoothScroll;

            // create items
            final Menu menu = bottomNavigationViewHandler.getMenu();
            int size = menu.size();
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
            if (previousPosition == position) {
                return true;
            }

            // user listener
            if (null != listener) {
                final boolean bool = listener.onNavigationItemSelected(item);
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

            // update previous position
            previousPosition = position;

            return true;
        }
    }
}
