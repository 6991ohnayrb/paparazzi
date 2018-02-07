package com.bryanho.paparazzi.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bryanho.paparazzi.R;

public class NearbyGameFragment extends PaparazziFragment {

    public NearbyGameFragment() {
    }

    public static NearbyGameFragment newInstance() {
        return new NearbyGameFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setupGameService();
        return inflater.inflate(R.layout.fragment_my_games, container, false);
    }
}
