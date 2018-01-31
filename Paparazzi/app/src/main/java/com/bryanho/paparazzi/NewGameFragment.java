package com.bryanho.paparazzi;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.bryanho.paparazzi.objects.Game;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewGameFragment extends Fragment {

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
        return view;
    }

    @OnClick(R.id.create)
    public void createGame() {
        final String gameRoomNameText = gameRoomName.getEditableText().toString();
        try {
            final int maximumPlayersVal = Integer.parseInt(maximumPlayers.getEditableText().toString());
            final int gameDurationVal = Integer.parseInt(gameDuration.getEditableText().toString());

            final Game game = new Game(gameRoomNameText, maximumPlayersVal, gameDurationVal);
            System.out.println(game);
        } catch (NumberFormatException ex) {
            Toast.makeText(getContext(), getString(R.string.please_enter_valid_values), Toast.LENGTH_SHORT).show();
        }
    }
}
