package com.bryanho.paparazzi;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bryanho.paparazzi.objects.Game;
import com.bryanho.paparazzi.util.FacebookUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends PaparazziActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.navigation_drawer) LinearLayout navigationDrawer;
    @BindView(R.id.user_greeting) TextView userGreeting;
    @BindView(R.id.menu_games_list) ListView gamesList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setToolbarLeftIcon();
        setGreeting();
        setMenuGamesList();
    }

    private void setToolbarLeftIcon() {
        toolbar.setTitleTextColor(getColor(R.color.white));
        toolbar.setNavigationIcon(R.drawable.ic_settings);
        toolbar.setNavigationOnClickListener(onNavigationClickListener);
    }

    private View.OnClickListener onNavigationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (userGreeting.getVisibility() == View.GONE) {
                setGreeting();
            }
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

    private void setMenuGamesList() {
        final Context context = getApplicationContext();

        // TODO: Fix this later, just for testing now
        final List<Game> gameList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            gameList.add(new Game("Room " + i, 0, 0));
        }

        if (context != null && gameList != null) {
            final GameAdapter gameAdapter = new GameAdapter(context, gameList);
            gamesList.setAdapter(gameAdapter);
        }
    }

    @OnClick(R.id.menu_explore_games)
    public void menuHomeClicked() {
        // TODO: Navigate to ExploreGamesFragment.java
    }

    @OnClick(R.id.menu_new_game)
    public void menuNewGame() {
        navigateToFragment(NewGameFragment.newInstance());
        drawerLayout.closeDrawer(navigationDrawer);
    }

    @OnClick(R.id.menu_settings)
    public void menuSettingsClicked() {
        navigateToFragment(SettingsFragment.newInstance());
        drawerLayout.closeDrawer(navigationDrawer);
    }
}
