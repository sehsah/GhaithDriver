package com.mahmoudsehsah.ghaithDriver.Server;

import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by mahmoud on 3/19/2018.
 */

public class FCMTokenRefreshListenerService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {

        Intent intent = new Intent(this, FCMRegistrationService.class);
        intent.putExtra("refreshed", true);
        startService(intent);
    }
}