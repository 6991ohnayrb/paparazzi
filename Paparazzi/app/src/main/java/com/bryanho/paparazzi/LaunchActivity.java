package com.bryanho.paparazzi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this);
        Intent intent;
        if (AccessToken.getCurrentAccessToken() == null) {
            intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Player is logged in via Facebook
            // TODO: Navigate to MainActivity
        }
//        startActivity(intent);
//        finish();
    }
}
