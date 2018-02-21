package com.bryanho.paparazzi.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.bryanho.paparazzi.R;
import com.bryanho.paparazzi.objects.Game;
import com.bryanho.paparazzi.requests.CreateGameRequest;
import com.bryanho.paparazzi.responses.CreateGameResponse;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NewGameFragment extends PaparazziFragment {

    @BindView(R.id.game_room_name) EditText gameRoomName;
    @BindView(R.id.maximum_players) EditText maximumPlayers;
    @BindView(R.id.game_duration) EditText gameDuration;

    public NewGameFragment() {
    }

    public static NewGameFragment newInstance() {
        return new NewGameFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_new_game, container, false);
        ButterKnife.bind(this, view);
        setupGameService();
        return view;
    }

    @OnClick(R.id.create)
    public void createGame() {
        final String gameRoomNameText = gameRoomName.getEditableText().toString();
        try {
            final int maximumPlayersVal = Integer.parseInt(maximumPlayers.getEditableText().toString());
            final int gameDurationVal = Integer.parseInt(gameDuration.getEditableText().toString());
            final Game game = new Game(gameRoomNameText, maximumPlayersVal, gameDurationVal);

            final CreateGameRequest createGameRequest = new CreateGameRequest(game);
            final Observable<CreateGameResponse> createGameResponseObservable = gameService.createGame(createGameRequest);
            createGameResponseObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            System.err.println(throwable.getMessage());
                        }
                    })
                    .subscribe(new Consumer<CreateGameResponse>() {
                        @Override
                        public void accept(CreateGameResponse createGameResponse) throws Exception {
                            if (createGameResponse != null && createGameResponse.getMessageStatus() != null) {
                                final String messageStatus = createGameResponse.getMessageStatus();
                                if ("success".equals(messageStatus)) {
                                    clearFields();
                                } else {
                                    Toast.makeText(getContext(), messageStatus, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        } catch (NumberFormatException ex) {
            Toast.makeText(getContext(), getString(R.string.please_enter_valid_values), Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        gameRoomName.setText("");
        maximumPlayers.setText("");
        gameDuration.setText("");
    }
}
