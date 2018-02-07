package com.bryanho.paparazzi.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bryanho.paparazzi.adapters.GameRoomMessageAdapter;
import com.bryanho.paparazzi.activities.MainActivity;
import com.bryanho.paparazzi.R;
import com.bryanho.paparazzi.objects.Game;
import com.bryanho.paparazzi.objects.GameInfo;
import com.bryanho.paparazzi.objects.Message;
import com.bryanho.paparazzi.objects.Player;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameRoomFragment extends PaparazziFragment {

    @BindView(R.id.game_room_messages) ListView messageList;
    @BindView(R.id.game_room_name) TextView gameRoomName;

    private Game currentGame;

    public GameRoomFragment() {
    }

    public static GameRoomFragment newInstance() {
        return new GameRoomFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_game_room, container, false);
        ButterKnife.bind(this, view);

        final Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            currentGame = ((MainActivity) activity).currentGame;
        } else {
            throw new IllegalStateException("Parent activity must be MainActivity and currentGame cannot be null!");
        }

        final GameInfo gameInfo = currentGame.getGameInfo();
        if (gameInfo != null) {
            gameRoomName.setText(gameInfo.getGameRoomName());
        }

        populateMessages();

        return view;
    }

    private void populateMessages() {
        // TODO: Uncomment below line for correct messages for each Game
        // final List<Message> messages = currentGame.getMessages();
        final List<Message> messages = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            final String message = "Message" + i;
            if (Math.random() < 0.5) {
                messages.add(new Message(new Player(), message));
            } else {
                messages.add(new Message(new Player("playerID"), message));
            }
        }

        final Context context = getContext();
        if (context != null) {
            final GameRoomMessageAdapter gameRoomMessageAdapter = new GameRoomMessageAdapter(context, messages);
            messageList.setAdapter(gameRoomMessageAdapter);
        }
    }
}
