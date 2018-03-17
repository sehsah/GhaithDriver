package com.mahmoudsehsah.ghaithDriver.custom;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.mahmoudsehsah.ghaithDriver.R;
import com.mahmoudsehsah.ghaithDriver.Server.ApiClient;
import com.mahmoudsehsah.ghaithDriver.adapter.APIRequests;
import com.mahmoudsehsah.ghaithDriver.models.updateUserId;
import com.mahmoudsehsah.ghaithDriver.session.SessionManager;
import com.onesignal.OneSignal;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by android on 15/3/17.
 */

public class MyApplication extends MultiDexApplication {
    @Override
    public void onCreate() {

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .init();

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                Log.d("debug userId =", userId);
                SessionManager sessionManager = new SessionManager(MyApplication.this);
                HashMap<String, String> user = sessionManager.getUserDetails();
                String id_user = user.get(SessionManager.USER_ID);
                APIRequests APIRequests = ApiClient.getClient().create(APIRequests.class);
                Call<updateUserId> call = APIRequests.updateUserId(id_user,userId);
                call.enqueue(new Callback<updateUserId>() {
                    @Override
                    public void onResponse(Call<updateUserId> call, retrofit2.Response<updateUserId> response) {
                        Log.d("suceess", "suceess");
                    }

                    @Override
                    public void onFailure(Call<updateUserId> call, Throwable t) {
                        Log.d("Error", t.getMessage());
                    }

                });
                if(registrationId != null){
                    Log.d("debug registrationId = " ,registrationId);
                }
            }
        });
        super.onCreate();


    }


}
