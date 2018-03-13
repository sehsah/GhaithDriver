package com.mahmoudsehsah.ghaithDriver.acitivities;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mahmoudsehsah.ghaithDriver.R;
import com.mahmoudsehsah.ghaithDriver.session.SessionManager;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by android on 7/3/17.
 */

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        int SPLASH_TIME_OUT = 1000;
        changement();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Configuration configuration = getResources().getConfiguration();
            configuration.setLayoutDirection(new Locale("en"));
            getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        }

    }
    public void changement() {
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        if (sessionManager.isLoggedIn()) {
            sessionManager = new SessionManager(SplashActivity.this);
            HashMap<String, String> user = sessionManager.getUserDetails();
            String type = user.get(SessionManager.TYPE);
            if(type == "driver"){
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            }else{
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));

            }
           // startActivity(new Intent(SplashActivity.this, OrdersActivity.class));
        } else {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
        }
        finish();
    }
}
