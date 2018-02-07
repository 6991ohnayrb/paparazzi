package com.bryanho.paparazzi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bryanho.paparazzi.R;
import com.bryanho.paparazzi.objects.Player;
import com.bryanho.paparazzi.responses.LoginResponse;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MyGamesFragment extends PaparazziFragment {

    public MyGamesFragment() {
    }

    public static MyGamesFragment newInstance() {
        return new MyGamesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_my_games, container, false);
        ButterKnife.bind(this, view);
        setupGameService();
        return view;
    }

    @OnClick(R.id.test_button)
    public void testButtonClicked() {
        final Player player = new Player();
        final Observable<LoginResponse> loginResponseObservable = gameService.login(player);
        loginResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginResponse>() {
                    @Override
                    public void accept(LoginResponse loginResponse) throws Exception {
                        if (loginResponse != null) {
                            System.out.println(loginResponse);
                        }
                    }
                });
    }
}
