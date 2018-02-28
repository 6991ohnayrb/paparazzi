package com.bryanho.paparazzi.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.bryanho.paparazzi.R;
import com.bryanho.paparazzi.activities.MainActivity;
import com.bryanho.paparazzi.adapters.GameInfoImagesAdapter;
import com.bryanho.paparazzi.adapters.GameInfoPlayersAdapter;
import com.bryanho.paparazzi.objects.Game;
import com.bryanho.paparazzi.objects.GameInfo;
import com.bryanho.paparazzi.objects.Image;
import com.bryanho.paparazzi.objects.Message;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameInfoDialog extends BottomSheetDialogFragment {

    private Game currentGame;

    @BindView(R.id.game_info_room_name) TextView gameInfoRoomName;
    @BindView(R.id.game_info_players) ListView gameInfoPlayers;
    @BindView(R.id.game_info_pictures) GridView gameInfoPictures;

    public GameInfoDialog() {
        super();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        final Context context = getContext();
        if (context == null) {
            throw new IllegalStateException("Context cannot be null!");
        }

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        bottomSheetDialog.setContentView(R.layout.dialog_game_info);

        ButterKnife.bind(this, bottomSheetDialog);

        final Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            currentGame = ((MainActivity) activity).currentGame;
            if (currentGame == null) {
                throw new IllegalStateException("Current game cannot be null!");
            }
        }

        populateFields();

        return bottomSheetDialog;
    }

    private void populateFields() {
        final GameInfo gameInfo = currentGame.getGameInfo();
        if (gameInfo != null) {
            gameInfoRoomName.setText(gameInfo.getGameRoomName());
        }

        final Context context = getContext();
        if (context != null) {
            final GameInfoPlayersAdapter gameInfoPlayersAdapter = new GameInfoPlayersAdapter(context, currentGame.getPlayers());
            gameInfoPlayers.setAdapter(gameInfoPlayersAdapter);

            final List<Image> images = new ArrayList<>();
            for (Message message : currentGame.getMessages()) {
                final Image image = message.getImage();
                if (image != null) {
                    images.add(image);
                }
            }

            final GameInfoImagesAdapter gameInfoImagesAdapter = new GameInfoImagesAdapter(context, images);
            gameInfoPictures.setAdapter(gameInfoImagesAdapter);
        }
    }
}
