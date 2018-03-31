package com.mahmoudsehsah.ghaithDriver.Server;

/**
 * Created by mahmoud on 3/19/2018.
 */


import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mahmoudsehsah.ghaithDriver.adapter.APIRequests;
import com.mahmoudsehsah.ghaithDriver.models.UpdateToken;
import com.mahmoudsehsah.ghaithDriver.models.updateUserId;
import com.mahmoudsehsah.ghaithDriver.session.SessionManager;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by mahmoud on 3/18/2018.
 */

public class FCMRegistrationService extends IntentService {

    SharedPreferences preferences;

    public FCMRegistrationService() {
        super("FCM");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // get Default Shard Preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        Log.e("Welocom to firebase"," ^_^");
        // get token from Firebase
        String tokens = FirebaseInstanceId.getInstance().getToken();
        SessionManager sessionManager = new SessionManager(FCMRegistrationService.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        String id_user = user.get(SessionManager.USER_ID);
        // check if intent is null or not if it isn't null we will ger refreshed value and
        // if its true we will override token_sent value to false and apply
        if (intent.getExtras() != null) {
            boolean refreshed = intent.getExtras().getBoolean("refreshed");
            if (refreshed) preferences.edit().putBoolean("token_sent", false).apply();
        }

        // if token_sent value is false then use method sendTokenToServer to send token to server
        if (!preferences.getBoolean("token_sent", false))
            sendTokenToServer(tokens,id_user);

    }

    private void sendTokenToServer(final String tokens,final String id_user) {
        APIRequests APIRequests = ApiClient.getClient().create(APIRequests.class);
        Call<UpdateToken> call = APIRequests.UpdateToken(id_user,tokens);
        call.enqueue(new Callback<UpdateToken>() {
            @Override
            public void onResponse(Call<UpdateToken> call, retrofit2.Response<UpdateToken> response) {
                Log.d("success Update token", "success");
            }

            @Override
            public void onFailure(Call<UpdateToken> call, Throwable t) {
                Log.d("Error Update token", t.getMessage());
            }

        });

    }


}