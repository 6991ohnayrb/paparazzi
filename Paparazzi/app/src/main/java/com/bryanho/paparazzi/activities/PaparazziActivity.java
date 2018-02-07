package com.bryanho.paparazzi.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.bryanho.paparazzi.R;
import com.bryanho.paparazzi.services.GameService;

import retrofit2.Retrofit;

public abstract class PaparazziActivity extends AppCompatActivity {

    public GameService gameService;

    public PaparazziActivity() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .build();
        gameService = retrofit.create(GameService.class);
    }

    public void navigateToFragment(Fragment fragment) {
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commitAllowingStateLoss();
    }
}
