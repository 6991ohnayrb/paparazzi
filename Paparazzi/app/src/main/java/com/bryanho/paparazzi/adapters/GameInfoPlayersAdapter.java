package com.bryanho.paparazzi.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bryanho.paparazzi.R;
import com.bryanho.paparazzi.objects.Player;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GameInfoPlayersAdapter extends ArrayAdapter<Player> {

    public GameInfoPlayersAdapter(@NonNull Context context, @NonNull List<Player> playerList) {
        super(context, 0, playerList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Player player = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.player_item, parent, false);
        }

        if (player != null) {
            final ImageView playerImage = convertView.findViewById(R.id.player_image);
            Picasso.with(getContext()).load(getContext().getString(R.string.facebook_profile_pic_url, player.getFacebookUserId())).into(playerImage);

            final TextView playerName = convertView.findViewById(R.id.player_name);
            playerName.setText(player.getFirstName());
        }

        return convertView;
    }
}
