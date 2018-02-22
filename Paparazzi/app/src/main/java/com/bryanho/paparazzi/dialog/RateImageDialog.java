package com.bryanho.paparazzi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bryanho.paparazzi.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RateImageDialog extends Dialog {

    @BindView(R.id.rate_this_image) TextView rateThisImage;
    @BindView(R.id.your_rating) TextView yourRating;
    @BindView(R.id.rate_image_image) ImageView image;
    @BindView(R.id.star_1) ImageView star1;
    @BindView(R.id.star_2) ImageView star2;
    @BindView(R.id.star_3) ImageView star3;
    @BindView(R.id.star_4) ImageView star4;
    @BindView(R.id.star_5) ImageView star5;

    private Context context;
    private Bitmap bitmap;
    private int previousRating = 0;
    private final List<ImageView> starList = new ArrayList<>();

    public RateImageDialog(@NonNull Context context, Bitmap bitmap, int previousRating) {
        super(context);
        this.context = context;
        this.bitmap = bitmap;
        this.previousRating = previousRating;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        final Window window = getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_rate_image);

        ButterKnife.bind(this);

        if (previousRating == 0) {
            rateThisImage.setVisibility(View.VISIBLE);
            yourRating.setVisibility(View.GONE);
        } else {
            rateThisImage.setVisibility(View.GONE);
            yourRating.setVisibility(View.VISIBLE);
        }

        if (bitmap != null) {
            image.setImageBitmap(bitmap);
        } else {
            Toast.makeText(context, context.getString(R.string.login_error_message), Toast.LENGTH_SHORT).show();
        }

        starList.add(star1);
        starList.add(star2);
        starList.add(star3);
        starList.add(star4);
        starList.add(star5);
    }

    @OnClick(R.id.dismiss_dialog)
    void dismissDialog() {
        dismiss();
    }

    @OnClick(R.id.star_1)
    void star1Clicked() {
        setStars(1);
    }

    @OnClick(R.id.star_2)
    void star2Clicked() {
        setStars(2);
    }

    @OnClick(R.id.star_3)
    void star3Clicked() {
        setStars(3);
    }

    @OnClick(R.id.star_4)
    void star4Clicked() {
        setStars(4);
    }

    @OnClick(R.id.star_5)
    void star5Clicked() {
        setStars(5);
    }

    private void setStars(int rating) {
        for (int i = 0; i < starList.size(); i++) {
            final ImageView imageView = starList.get(i);
            if (i < rating) {
                imageView.setColorFilter(context.getColor(R.color.gold));
            } else {
                imageView.setColorFilter(context.getColor(R.color.lightGray));
            }
        }
    }

    @OnClick(R.id.rate_image_button)
    void rateImageClicked() {
        // TODO: API call to rate image
        dismiss();
    }
}
