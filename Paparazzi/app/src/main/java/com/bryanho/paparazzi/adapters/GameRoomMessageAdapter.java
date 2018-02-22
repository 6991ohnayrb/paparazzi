package com.bryanho.paparazzi.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bryanho.paparazzi.R;
import com.bryanho.paparazzi.dialog.InviteDialog;
import com.bryanho.paparazzi.dialog.RateImageDialog;
import com.bryanho.paparazzi.objects.Message;
import com.squareup.picasso.Picasso;

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

        final Context context = getContext();
        final TextView messageFromSelf = convertView.findViewById(R.id.message_from_self);
        final ImageView imageFromSelf = convertView.findViewById(R.id.image_from_self);
        final LinearLayout messageFromOtherLayout = convertView.findViewById(R.id.message_from_other_layout);
        final ImageView imageFromOther = convertView.findViewById(R.id.image_from_other);
        final TextView messageFromOther = convertView.findViewById(R.id.message_from_other);
        final ImageView messageFromOtherImage = convertView.findViewById(R.id.message_from_other_image);

        if (message != null && messageFromSelf != null && imageFromSelf != null && messageFromOther != null) {
            if (!message.isFromMyself()) {
                if (message.getImage() != null) {
                    messageFromSelf.setVisibility(View.GONE);
                    imageFromSelf.setVisibility(View.VISIBLE);
                    final Bitmap bitmap = getBitmapFromString(message.getImage());
                    if (bitmap != null) {
                        imageFromSelf.setImageBitmap(bitmap);
                    }
                } else {
                    messageFromSelf.setVisibility(View.VISIBLE);
                    imageFromSelf.setVisibility(View.GONE);
                    messageFromSelf.setText(message.getMessage());
                }
                messageFromOtherLayout.setVisibility(View.GONE);
            } else {
                messageFromSelf.setVisibility(View.GONE);
                messageFromOtherLayout.setVisibility(View.VISIBLE);
                Picasso.with(context).load(context.getString(R.string.facebook_profile_pic_url, message.getSentFrom().getFacebookUserId())).into(messageFromOtherImage);
                if (message.getImage() != null) {
                    imageFromOther.setVisibility(View.VISIBLE);
                    messageFromOther.setVisibility(View.GONE);
                    final Bitmap bitmap = getBitmapFromString(message.getImage());
                    if (bitmap != null) {
                        imageFromOther.setImageBitmap(bitmap);
                        imageFromOther.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // TODO: Get accurate previousRating value
                                new RateImageDialog(getContext(), bitmap, 0).show();
                            }
                        });
                    }
                } else {
                    messageFromOther.setText(message.getMessage());
                    imageFromOther.setVisibility(View.GONE);
                }
            }
        }
        return convertView;
    }

    private Bitmap getBitmapFromString(String string) {
        try {
            final byte[] encodeByte = Base64.decode(string, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception ex) {
            return null;
        }
    }
}
