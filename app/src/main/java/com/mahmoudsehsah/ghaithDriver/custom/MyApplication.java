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

        super.onCreate();


    }


}
