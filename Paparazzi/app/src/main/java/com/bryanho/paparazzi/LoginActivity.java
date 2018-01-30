package com.bryanho.paparazzi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

public class LoginActivity extends Activity {
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppEventsLogger.activateApp(getApplication());
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LoginActivity.this, getString(R.string.login_success_message), Toast.LENGTH_SHORT).show();
                // TODO: Navigate to MainActivity
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, getString(R.string.login_cancelled_message), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
