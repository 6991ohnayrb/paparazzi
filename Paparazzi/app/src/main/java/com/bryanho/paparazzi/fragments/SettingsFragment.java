package com.bryanho.paparazzi.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bryanho.paparazzi.R;
import com.bryanho.paparazzi.activities.MainActivity;
import com.bryanho.paparazzi.util.FacebookUtil;
import com.squareup.picasso.Picasso;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsFragment extends PaparazziFragment {

    @BindView(R.id.profile_picture) ImageView profilePicture;
    @BindView(R.id.first_name) TextView firstNameTextView;
    @BindView(R.id.last_name) TextView lastNameTextView;

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@Nonnull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);

        final Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            final MainActivity mainActivity = (MainActivity) activity;
            mainActivity.setToolbarTitle(getString(R.string.settings));
        }

        setupGameService();
        populateFields();
        return view;
    }

    private void populateFields() {
        firstNameTextView.setText(FacebookUtil.getFirstName());
        lastNameTextView.setText(FacebookUtil.getLastName());
        Picasso.with(getContext()).load(getString(R.string.facebook_profile_pic_url, FacebookUtil.getProfileId())).into(profilePicture);
    }
}
