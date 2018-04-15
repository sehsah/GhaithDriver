package com.mahmoudsehsah.ghaithDriver.custom;

import android.support.multidex.MultiDexApplication;

import com.mahmoudsehsah.ghaithDriver.Server.ConnectionReceiver;


/**
 * Created by android on 15/3/17.
 */

public class MyApplication extends MultiDexApplication {
    private static MyApplication mInstance;

    @Override
    public void onCreate() {

        super.onCreate();
        mInstance = this;


    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectionListener(ConnectionReceiver.ConnectionReceiverListener listener) {
        ConnectionReceiver.connectionReceiverListener = listener;
    }
}
