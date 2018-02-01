package com.bryanho.paparazzi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bryanho.paparazzi.util.FacebookUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends PaparazziActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.navigation_drawer) LinearLayout navigationDrawer;
    @BindView(R.id.user_greeting) TextView userGreeting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setToolbarLeftIcon();
        setGreeting();
    }

    private void setToolbarLeftIcon() {
        toolbar.setTitleTextColor(getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_settings);
        toolbar.setNavigationOnClickListener(onNavigationClickListener);
    }

    private View.OnClickListener onNavigationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            drawerLayout.openDrawer(navigationDrawer);
        }
    };

    private void setGreeting() {
        final String fullName = FacebookUtil.getFullName();
        if (fullName != null) {
            userGreeting.setText(getResources().getString(R.string.greeting_personalized, FacebookUtil.getFullName()));
        } else {
            userGreeting.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.menu_explore_games)
    public void menuHomeClicked() {
        navigateToFragment(NewGameFragment.newInstance());
        drawerLayout.closeDrawer(navigationDrawer);
    }
}
