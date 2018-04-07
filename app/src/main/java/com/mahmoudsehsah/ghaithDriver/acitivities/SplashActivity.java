package com.mahmoudsehsah.ghaithDriver.acitivities;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.mahmoudsehsah.ghaithDriver.R;
import com.mahmoudsehsah.ghaithDriver.Server.FCMRegistrationService;
import com.mahmoudsehsah.ghaithDriver.session.SessionManager;
import com.thebrownarrow.permissionhelper.ActivityManagePermission;
import com.thebrownarrow.permissionhelper.PermissionResult;
import com.thebrownarrow.permissionhelper.PermissionUtils;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by android on 7/3/17.
 */

public class SplashActivity extends ActivityManagePermission {
    String permissionAsk[] = {
            PermissionUtils.Manifest_CAMERA,
            PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE,
            PermissionUtils.Manifest_READ_EXTERNAL_STORAGE,
            PermissionUtils.Manifest_ACCESS_FINE_LOCATION,
            PermissionUtils.Manifest_ACCESS_COARSE_LOCATION
    };


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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                askCompactPermissions(permissionAsk, new PermissionResult() {
                    @Override
                    public void permissionGranted() {
                        changement();
                    }

                    @Override
                    public void permissionDenied() {
                        changement();
                    }

                    @Override
                    public void permissionForeverDenied() {
                        changement();
                    }
                });
            }
        }, SPLASH_TIME_OUT);


    }
    public void changement() {
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        if (sessionManager.isLoggedIn()) {
            Log.d("isLoggedIn","isLoggedIn");
            sessionManager = new SessionManager(SplashActivity.this);
            HashMap<String, String> user = sessionManager.getUserDetails();
            String type = user.get(SessionManager.TYPE);
            String username = user.get(SessionManager.KEY_NAME);
            Log.e("username",username);
            Log.e("type",type);

            if(type.equals("driver")){
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            }else {
                startActivity(new Intent(SplashActivity.this, ShowMarketActivity.class));

            }        } else {
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
        }
        finish();
        isGooglePlayServicesAvailable(SplashActivity.this);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(SplashActivity.this, "غير قادر علس السماح بالصلاحيات", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
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
