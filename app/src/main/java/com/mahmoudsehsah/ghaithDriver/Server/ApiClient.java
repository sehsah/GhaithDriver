package com.mahmoudsehsah.ghaithDriver.Server;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mahmoud on 2/14/2018.
 */

public class ApiClient {
    public static final String URL      = "http://ghaithksa.com/admin/";
    public static Retrofit RETROFIT     = null;

    public static Retrofit getClient(){
        if(RETROFIT==null){
            OkHttpClient client = new OkHttpClient.Builder()
                   .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    . writeTimeout(120, TimeUnit.SECONDS)
                    .addInterceptor(new GetOrderCars())
                    .build();
            RETROFIT = new Retrofit.Builder()
                    .baseUrl(URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    //.addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return RETROFIT;
    }
}
