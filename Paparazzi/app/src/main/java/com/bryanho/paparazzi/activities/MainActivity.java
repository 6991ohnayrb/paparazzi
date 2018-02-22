package com.bryanho.paparazzi.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bryanho.paparazzi.R;
import com.bryanho.paparazzi.adapters.GameAdapter;
import com.bryanho.paparazzi.fragments.GameRoomFragment;
import com.bryanho.paparazzi.fragments.JoinGameFragment;
import com.bryanho.paparazzi.fragments.MyGamesFragment;
import com.bryanho.paparazzi.fragments.NewGameFragment;
import com.bryanho.paparazzi.fragments.SettingsFragment;
import com.bryanho.paparazzi.objects.Game;
import com.bryanho.paparazzi.objects.Player;
import com.bryanho.paparazzi.requests.GetGamesRequest;
import com.bryanho.paparazzi.responses.GamesResponse;
import com.bryanho.paparazzi.util.FacebookUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends PaparazziActivity {

    private static final int FETCH_GAMES_INTERVAL_MILLISECONDS = 500;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title) TextView toolbarTitle;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.navigation_drawer) LinearLayout navigationDrawer;
    @BindView(R.id.user_greeting) TextView userGreeting;
    @BindView(R.id.menu_games_list) ListView gamesList;

    public Game currentGame;
    public List<Game> games = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeStethoscope(getApplicationContext());
        ButterKnife.bind(this);
        setToolbarLeftIcon();
        setGreeting();
        startGameRoomFetching();
        setMenuGamesList();

        // TODO: Remove this after testing HTTP request
        navigateToFragment(MyGamesFragment.newInstance());
    }

    public void setToolbarTitle(String string) {
        toolbarTitle.setText(string);
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

    private void startGameRoomFetching() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                fetchGames();
                handler.postDelayed(this, FETCH_GAMES_INTERVAL_MILLISECONDS);
            }
        };
        handler.post(runnable);
    }

    private void fetchGames() {
        final Observable<GamesResponse> gamesResponseObservable = gameService.getGames(new GetGamesRequest(new Player()));
        gamesResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.err.println(throwable.getMessage());
                    }
                })
                .subscribe(new Consumer<GamesResponse>() {
                    @Override
                    public void accept(GamesResponse gamesResponse) throws Exception {
                        if (gamesResponse != null && gamesResponse.getGames() != null) {
                            final List<Game> responseGames = gamesResponse.getGames();
                            if (games == null || !games.equals(responseGames)) {
                                games = gamesResponse.getGames();
                                setMenuGamesList();
                            }
                        }
                    }
                });
    }

    private void setMenuGamesList() {
        final Context context = getApplicationContext();
        if (context != null && games != null) {
            final GameAdapter gameAdapter = new GameAdapter(context, games);
            gamesList.setAdapter(gameAdapter);
            gamesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    currentGame = (Game) gamesList.getItemAtPosition(position);
                    navigateToFragment(GameRoomFragment.newInstance());
                    drawerLayout.closeDrawer(navigationDrawer);
                }
            });
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

    @OnClick(R.id.menu_join_game)
    public void menuJoinGame() {
        navigateToFragment(JoinGameFragment.newInstance());
        drawerLayout.closeDrawer(navigationDrawer);
    }

    @OnClick(R.id.menu_settings)
    public void menuSettingsClicked() {
        navigateToFragment(SettingsFragment.newInstance());
        drawerLayout.closeDrawer(navigationDrawer);
    }
}
