package com.bryanho.paparazzi.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.bryanho.paparazzi.R;
import com.bryanho.paparazzi.activities.MainActivity;
import com.bryanho.paparazzi.requests.JoinGameRequest;
import com.bryanho.paparazzi.responses.JoinGameResponse;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class JoinGameFragment extends PaparazziFragment {

    @BindView(R.id.game_room_name) EditText gameRoomName;

    public JoinGameFragment() {
    }

    public static JoinGameFragment newInstance() {
        return new JoinGameFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@Nonnull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_join_game, container, false);
        ButterKnife.bind(this, view);

        final Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            final MainActivity mainActivity = (MainActivity) activity;
            mainActivity.setToolbarTitle(getString(R.string.join_game));
        }

        setupGameService();
        return view;
    }

    @OnClick(R.id.join)
    public void joinGame() {
        final String name = gameRoomName.getEditableText().toString();
        if (name.length() != 0) {
            final JoinGameRequest joinGameRequest = new JoinGameRequest(name);
            final Observable<JoinGameResponse> joinGameResponseObservable = gameService.joinGame(joinGameRequest);
            joinGameResponseObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<JoinGameResponse>() {
                        @Override
                        public void onNext(JoinGameResponse joinGameResponse) {
                            if (joinGameResponse != null && joinGameResponse.getStatus() != null) {
                                final String status = joinGameResponse.getStatus();
                                if ("success".equals(status)) {
                                    gameRoomName.setText("");
                                } else {
                                    Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            System.err.println(e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        }
    }
}
