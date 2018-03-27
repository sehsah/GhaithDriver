package com.mahmoudsehsah.ghaithDriver.acitivities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.mahmoudsehsah.ghaithDriver.R;
import com.mahmoudsehsah.ghaithDriver.Server.FCMRegistrationService;
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
        startService(new Intent(this,FCMRegistrationService.class));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Configuration configuration = getResources().getConfiguration();
            configuration.setLayoutDirection(new Locale("en"));
            getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        }

    }
    public void changement() {
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        if (sessionManager.isLoggedIn()) {
            Log.d("isLoggedIn","isLoggedIn");
            sessionManager = new SessionManager(SplashActivity.this);
            HashMap<String, String> user = sessionManager.getUserDetails();
            String type = user.get(SessionManager.TYPE);
//             Log.d("type",type);
//            if(type.equals("driver")){
//                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
//            }else {
//                startActivity(new Intent(SplashActivity.this, OrdersActivity.class));
//
//            }
           startActivity(new Intent(SplashActivity.this, ShowMarketActivity.class));
        } else {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
        }
        finish();
        isGooglePlayServicesAvailable(SplashActivity.this);

    }


    public boolean isGooglePlayServicesAvailable(Context context) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);
        return resultCode == ConnectionResult.SUCCESS;
    }

    public boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        } else {

        }
        return true;
    }
}
