package com.bryanho.paparazzi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bryanho.paparazzi.R;
import com.bryanho.paparazzi.objects.LoginStatus;
import com.bryanho.paparazzi.objects.Player;
import com.bryanho.paparazzi.requests.LoginRequest;
import com.bryanho.paparazzi.responses.LoginResponse;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends PaparazziActivity {
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppEventsLogger.activateApp(getApplication());
        setContentView(R.layout.activity_login);

        initializeStethoscope(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Toast.makeText(LoginActivity.this, getString(R.string.login_success_message), Toast.LENGTH_SHORT).show();

                // Login result from backend
                if (loginResult != null && loginResult.getAccessToken() != null) {
                    final Profile profile = Profile.getCurrentProfile();
                    if (profile == null) {
                        new ProfileTracker() {
                            @Override
                            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                                sendLoginRequest(currentProfile);
                            }
                        };
                    } else {
                        sendLoginRequest(profile);
                    }
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, getString(R.string.login_cancelled_message), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, getString(R.string.login_error_message), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendLoginRequest(final Profile profile) {
        final LoginRequest loginRequest = new LoginRequest(new Player(profile));
        final Observable<LoginResponse> loginResponseObservable = gameService.login(loginRequest);
        loginResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.err.println(throwable.getMessage());
                    }
                })
                .subscribe(new Consumer<LoginResponse>() {
                    @Override
                    public void accept(LoginResponse loginResponse) throws Exception {
                        if (loginResponse != null) {
                            switch (loginResponse.getLoginStatus()) {
                                case LoginStatus.newPlayer:
                                case LoginStatus.returningPlayer:
                                    // Put extras for Facebook User ID
                                    final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra(getString(R.string.facebook_user_id_key), profile.getId());
                                    startActivity(intent);
                                    finish();
                                    break;

                                case LoginStatus.failed:
                                    Toast.makeText(LoginActivity.this, getString(R.string.login_error_message), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
