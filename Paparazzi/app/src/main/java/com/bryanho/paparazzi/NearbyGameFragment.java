package com.bryanho.paparazzi;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NearbyGameFragment extends Fragment {

    public NearbyGameFragment() {
    }

    public static NearbyGameFragment newInstance() {
        return new NearbyGameFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_new_game, container, false);
    }
}