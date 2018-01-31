package com.bryanho.paparazzi;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseIntArray;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Activity {
    @BindView(R.id.bottom_navigation_view_handler) BottomNavigationViewHandler bottomNavigationViewHandler;
    @BindView(R.id.view_pager) ViewPager viewPager;

    private SparseIntArray items = new SparseIntArray(3);
    private List<Fragment> fragmentList = new ArrayList<>();

    private MyGamesFragment myGamesFragment = MyGamesFragment.newInstance();
    private NearbyGameFragment nearbyGameFragment = NearbyGameFragment.newInstance();
    private SettingsFragment settingsFragment = SettingsFragment.newInstance();
    private NewGameFragment newGameFragment = NewGameFragment.newInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupViews();
        setupFragments();
        setupListeners();
    }

    private void setupViews() {
        bottomNavigationViewHandler.enableAnimation(false);
        bottomNavigationViewHandler.enableShiftingMode(false);
        bottomNavigationViewHandler.enableItemShiftingMode(false);
        bottomNavigationViewHandler.setCurrentItem(0);
    }

    private void setupFragments() {
        fragmentList.add(newGameFragment);
        fragmentList.add(nearbyGameFragment);
        fragmentList.add(settingsFragment);

        items.put(R.id.menu_games, 0);
        items.put(R.id.menu_nearby, 1);
        items.put(R.id.menu_settings, 2);

        viewPager.setAdapter(new VpAdapter(getFragmentManager(), fragmentList));
    }

    private void setupListeners() {
        bottomNavigationViewHandler.setOnNavigationItemSelectedListener(new BottomNavigationViewHandler.OnNavigationItemSelectedListener() {
            private int previousPosition = -1;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                final int position = items.get(item.getItemId());
                if (previousPosition != position) {
                    previousPosition = position;
                    viewPager.setCurrentItem(position);
                }
                return true;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationViewHandler.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private static class VpAdapter extends FragmentPagerAdapter {
        private List<Fragment> data;

        private VpAdapter(FragmentManager fm, List<Fragment> data) {
            super(fm);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }
    }
}
