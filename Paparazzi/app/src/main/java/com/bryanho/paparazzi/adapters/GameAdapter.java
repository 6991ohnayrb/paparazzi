package com.bryanho.paparazzi.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bryanho.paparazzi.R;
import com.bryanho.paparazzi.objects.Game;
import com.bryanho.paparazzi.objects.GameInfo;

import java.util.List;

public class GameAdapter extends ArrayAdapter<Game> {

    public GameAdapter(@NonNull Context context, @NonNull List<Game> gameList) {
        super(context, 0, gameList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Game game = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.game_item, parent, false);
        }

        if (game != null) {
            final TextView gameRoomName = convertView.findViewById(R.id.game_room_name);
            final GameInfo gameInfo = game.getGameInfo();
            if (gameRoomName != null && gameInfo != null) {
                gameRoomName.setText(gameInfo.getGameRoomName());
            }
        }

        return convertView;
    }
}
