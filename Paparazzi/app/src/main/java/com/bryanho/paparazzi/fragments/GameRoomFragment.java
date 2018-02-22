package com.bryanho.paparazzi.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bryanho.paparazzi.R;
import com.bryanho.paparazzi.activities.MainActivity;
import com.bryanho.paparazzi.adapters.GameRoomMessageAdapter;
import com.bryanho.paparazzi.dialog.InviteDialog;
import com.bryanho.paparazzi.objects.Game;
import com.bryanho.paparazzi.objects.GameInfo;
import com.bryanho.paparazzi.objects.Message;
import com.bryanho.paparazzi.objects.Player;
import com.bryanho.paparazzi.requests.SendMessageRequest;
import com.bryanho.paparazzi.responses.SendMessageResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class GameRoomFragment extends PaparazziFragment {

    private static final int FETCH_MESSAGES_INTERVAL_MILLISECONDS = 500;

    @BindView(R.id.game_room_messages) ListView messageList;
    @BindView(R.id.game_room_message_text) EditText gameRoomMessageText;

    private Game currentGame;
    private MainActivity mainActivity;

    public GameRoomFragment() {
    }

    public static GameRoomFragment newInstance() {
        return new GameRoomFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        final View view = inflater.inflate(R.layout.fragment_game_room, container, false);
        ButterKnife.bind(this, view);
        setupGameService();

        final Activity activity = getActivity();
        if (activity instanceof MainActivity && ((MainActivity) activity).currentGame != null) {
            mainActivity = (MainActivity) activity;
            currentGame = mainActivity.currentGame;
            final GameInfo gameInfo = currentGame.getGameInfo();
            if (gameInfo != null) {
                final String gameRoomName = gameInfo.getGameRoomName();
                mainActivity.setToolbarTitle(gameRoomName);
                final ImageView shareIcon = mainActivity.findViewById(R.id.share_icon);
                if (shareIcon != null) {
                    shareIcon.setVisibility(View.VISIBLE);
                    shareIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new InviteDialog(mainActivity, gameRoomName).show();
                        }
                    });
                }
            }
        } else {
            throw new IllegalStateException("Parent activity must be MainActivity and currentGame cannot be null!");
        }

        populateMessages();
        startMessageFetching();

        return view;
    }

    private void populateMessages() {
        final List<Message> messages = currentGame.getMessages();
        final Context context = getContext();
        if (context != null) {
            final GameRoomMessageAdapter gameRoomMessageAdapter = new GameRoomMessageAdapter(context, messages);
            messageList.setAdapter(gameRoomMessageAdapter);
            scrollToBottom();
        }
    }

    private void scrollToBottom() {
        messageList.post(new Runnable() {
            @Override
            public void run() {
                messageList.setSelection(messageList.getAdapter().getCount() - 1);
            }
        });
    }

    @OnClick(R.id.game_room_send_message)
    public void sendMessage() {
        final String messageText = gameRoomMessageText.getText().toString();
        if (messageText.length() != 0) {
            final SendMessageRequest sendMessageRequest = new SendMessageRequest(
                    currentGame.getGameId(),
                    new Message(new Player(), messageText)
            );
            final Observable<SendMessageResponse> sendMessageResponseObservable = gameService.sendMessage(sendMessageRequest);
            sendMessageResponseObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            System.err.println(throwable.getMessage());
                        }
                    })
                    .subscribe(new Consumer<SendMessageResponse>() {
                        @Override
                        public void accept(SendMessageResponse sendMessageResponse) throws Exception {
                            if (sendMessageResponse != null && "success".equals(sendMessageResponse.getMessageStatus())) {
                                gameRoomMessageText.setText("");
                            } else {
                                Toast.makeText(getContext(), getString(R.string.send_message_error), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void startMessageFetching() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Activity activity = getActivity();
                if (activity instanceof MainActivity) {
                    final MainActivity mainActivity = (MainActivity) activity;
                    for (Game game : mainActivity.games) {
                        if (game.getGameId() == currentGame.getGameId() && !game.equals(currentGame)) {
                            currentGame = game;
                            populateMessages();
                        }
                    }
                }
                handler.postDelayed(this, FETCH_MESSAGES_INTERVAL_MILLISECONDS);
            }
        };
        handler.post(runnable);
    }
}
