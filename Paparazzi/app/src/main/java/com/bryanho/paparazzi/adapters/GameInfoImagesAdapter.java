package com.bryanho.paparazzi.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bryanho.paparazzi.objects.Image;

import java.util.List;

public class GameInfoImagesAdapter extends BaseAdapter {
    private Context context;
    private List<Image> imageList;

    public GameInfoImagesAdapter(Context context, List<Image> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    public Image getItem(int position) {
        return imageList != null ? imageList.get(position) : null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getCount() {
        return imageList != null ? imageList.size() : 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap(getBitmapFromString(getItem(position).getImageContent()));

        return imageView;
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
