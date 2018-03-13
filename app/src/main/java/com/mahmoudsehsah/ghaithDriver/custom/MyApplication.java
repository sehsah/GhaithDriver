package com.mahmoudsehsah.ghaithDriver.custom;

import android.support.multidex.MultiDexApplication;
import com.mahmoudsehsah.ghaithDriver.R;


/**
 * Created by android on 15/3/17.
 */

public class MyApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
//
//        OneSignal.startInit(this)
//                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                .init();
//
//        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
//            @Override
//            public void idsAvailable(String userId, String registrationId) {
//                Log.d("debug userId =", userId);
//                if(registrationId != null){
//                    Log.d("debug registrationId = " ,registrationId);
//                }
//            }
//        });
        super.onCreate();


    }


}
