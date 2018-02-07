package com.bryanho.paparazzi.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.bryanho.paparazzi.activities.PaparazziActivity;
import com.bryanho.paparazzi.services.GameService;

public abstract class PaparazziFragment extends Fragment {

    protected GameService gameService;

    protected void setupGameService() {
        final Activity activity = getActivity();
        if (activity instanceof PaparazziActivity) {
            final PaparazziActivity paparazziActivity = (PaparazziActivity) activity;
            if (paparazziActivity.gameService != null) {
                gameService = paparazziActivity.gameService;
            } else {
                throw new IllegalStateException("Activity's GameService should not be null!");
            }
        } else {
            throw new IllegalStateException("Activity must be instance of PaparazziActivity");
        }
    }
}
