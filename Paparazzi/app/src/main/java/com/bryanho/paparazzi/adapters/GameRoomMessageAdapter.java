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
import com.bryanho.paparazzi.objects.Message;

import java.util.List;

public class GameRoomMessageAdapter extends ArrayAdapter<Message> {

    public GameRoomMessageAdapter(@NonNull Context context, @NonNull List<Message> messages) {
        super(context, 0, messages);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Message message = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_item, parent, false);
        }

        final TextView messageFromSelf = convertView.findViewById(R.id.message_from_self);
        final TextView messageFromOther = convertView.findViewById(R.id.message_from_other);

        if (message != null && messageFromSelf != null && messageFromOther != null) {
            if (message.isFromMyself()) {
                messageFromSelf.setVisibility(View.VISIBLE);
                messageFromOther.setVisibility(View.GONE);
                messageFromSelf.setText(message.getMessage());
            } else {
                messageFromSelf.setVisibility(View.GONE);
                messageFromOther.setVisibility(View.VISIBLE);
                messageFromOther.setText(message.getMessage());
            }
        }

        return convertView;
    }
}
