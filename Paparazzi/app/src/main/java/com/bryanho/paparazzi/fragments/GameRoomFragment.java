package com.bryanho.paparazzi.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bryanho.paparazzi.R;
import com.bryanho.paparazzi.activities.MainActivity;
import com.bryanho.paparazzi.adapters.GameRoomMessageAdapter;
import com.bryanho.paparazzi.dialog.GameInfoDialog;
import com.bryanho.paparazzi.dialog.InviteDialog;
import com.bryanho.paparazzi.objects.Game;
import com.bryanho.paparazzi.objects.GameInfo;
import com.bryanho.paparazzi.objects.Message;
import com.bryanho.paparazzi.objects.Player;
import com.bryanho.paparazzi.requests.SendMessageRequest;
import com.bryanho.paparazzi.requests.StartGameRequest;
import com.bryanho.paparazzi.responses.SendMessageResponse;
import com.bryanho.paparazzi.responses.StartGameResponse;

import java.io.ByteArrayOutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class GameRoomFragment extends PaparazziFragment {

    private static final int CAMERA_RESULT_CODE = 0;
    private static final int CAMERA_PERMISSION_REQUEST = 1;
    private static final int FETCH_MESSAGES_INTERVAL_MILLISECONDS = 500;

    @BindView(R.id.game_room_start_game) Button startGame;
    @BindView(R.id.game_room_your_target) TextView paparazziText;
    @BindView(R.id.game_room_messages) ListView messageList;
    @BindView(R.id.game_room_message_text) EditText gameRoomMessageText;
    @BindView(R.id.attached_image_layout) RelativeLayout attachedImageLayout;
    @BindView(R.id.attached_image) ImageView attachedImage;
    @BindView(R.id.message_layout) LinearLayout messageLayout;

    private Game currentGame;
    private MainActivity mainActivity;
    private Bitmap currentBitmap;

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
                final TextView toolbarTitle = mainActivity.findViewById(R.id.toolbar_title);
                final FragmentManager fragmentManager = getFragmentManager();
                if (toolbarTitle != null && fragmentManager != null) {
                    toolbarTitle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new GameInfoDialog().show(fragmentManager, null);
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
        final List<Player> players = currentGame.getPlayers();
        if (players != null && players.size() > 0 && players.get(0).equals(new Player())) {
            startGame.setVisibility(currentGame.getStarted() == 0 ? View.VISIBLE : View.GONE);
        }

        final Player paparazzi = currentGame.getPaparazzi();
        final Player target = currentGame.getTarget();
        if (paparazzi != null && paparazzi.equals(new Player()) && target != null) {
            final String targetFullName = target.getFirstName() + " " + target.getLastName();
            paparazziText.setText(getString(R.string.your_target_is_name, targetFullName));
            paparazziText.setVisibility(View.VISIBLE);
        } else {
            paparazziText.setVisibility(View.GONE);
        }

        final List<Message> messages = currentGame.getMessages();
        final Context context = getContext();
        if (context != null) {
            final GameRoomMessageAdapter gameRoomMessageAdapter = new GameRoomMessageAdapter(context, messages, currentGame, gameService);
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
        SendMessageRequest sendMessageRequest;
        if (currentBitmap != null) {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            currentBitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
            byte[] b = byteArrayOutputStream.toByteArray();
            final String imageString = Base64.encodeToString(b, Base64.DEFAULT);
            sendMessageRequest = new SendMessageRequest(currentGame.getGameId(),
                    new Message(new Player(), null, imageString));
        } else {
            final String messageText = gameRoomMessageText.getText().toString();
            sendMessageRequest = new SendMessageRequest(
                    currentGame.getGameId(),
                    new Message(new Player(), messageText, null)
            );
        }

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
                            attachedImage.setImageBitmap(null);
                            currentBitmap = null;
                            attachedImageLayout.setVisibility(View.GONE);
                            messageLayout.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(getContext(), getString(R.string.send_message_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

    @OnClick(R.id.add_image)
    public void addImage() {
        final Context context = getContext();
        if (context != null) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_RESULT_CODE);
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        final boolean permissionGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        if (requestCode == CAMERA_PERMISSION_REQUEST && permissionGranted) {
            addImage();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }

        if (requestCode == CAMERA_RESULT_CODE && resultCode == Activity.RESULT_OK && data != null && data.getExtras() != null) {
            final Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            if (bitmap != null) {
                showBitmap(bitmap);
            }
        }
    }

    private void showBitmap(Bitmap bitmap) {
        attachedImage.setImageBitmap(bitmap);
        currentBitmap = bitmap;
        attachedImageLayout.setVisibility(View.VISIBLE);
        messageLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.attached_image_x)
    public void removeAttachedImage() {
        attachedImage.setImageBitmap(null);
        currentBitmap = null;
        attachedImageLayout.setVisibility(View.GONE);
        messageLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.game_room_start_game)
    public void startGameButtonPressed() {
        final Observable<StartGameResponse> startGameResponseObservable = gameService.startGame(new StartGameRequest(currentGame));
        startGameResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.err.println(throwable.getMessage());
                    }
                })
                .subscribe(new Consumer<StartGameResponse>() {
                    @Override
                    public void accept(StartGameResponse startGameResponse) throws Exception {
                        if (startGameResponse != null && "success".equals(startGameResponse.getMessageStatus())) {
                            startGame.setVisibility(View.GONE);
                            final GameInfo gameInfo = currentGame.getGameInfo();
                            if (gameInfo != null) {
                                Toast.makeText(getContext(), getString(R.string.start_game_success_name, gameInfo.getGameRoomName()), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), getString(R.string.start_game_success), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), getString(R.string.send_message_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
