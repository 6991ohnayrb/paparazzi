package com.bryanho.paparazzi.activities;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.bryanho.paparazzi.R;
import com.bryanho.paparazzi.services.GameService;
import com.facebook.stetho.Stetho;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class PaparazziActivity extends AppCompatActivity {

    public GameService gameService;

    public PaparazziActivity() {
        final Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://127.0.0.1:8000")
                .baseUrl("http://www.google.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        gameService = retrofit.create(GameService.class);
    }

    public void navigateToFragment(Fragment fragment) {
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commitAllowingStateLoss();
    }

    public void initializeStethoscope(Context context) {
        Stetho.initializeWithDefaults(context);
    }
}
