package com.bryanho.paparazzi;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.Profile;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsFragment extends Fragment {

    @BindView(R.id.first_name) TextView firstNameTextView;
    @BindView(R.id.last_name) TextView lastNameTextView;

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        populateFields();
        return view;
    }

    private void populateFields() {
        firstNameTextView.setText(Profile.getCurrentProfile().getFirstName());
        lastNameTextView.setText(Profile.getCurrentProfile().getLastName());
    }
}
