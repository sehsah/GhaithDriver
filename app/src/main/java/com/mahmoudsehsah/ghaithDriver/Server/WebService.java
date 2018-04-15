package com.mahmoudsehsah.ghaithDriver.Server;

import com.mahmoudsehsah.ghaithDriver.adapter.APIRequests;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mahmoud on 3/29/2018.
 */

public class WebService {
    private static WebService instance;
    private APIRequests api;
    public static final String URL      = "http://ghaithksa.com/";

    public WebService() {

        OkHttpClient client = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder().client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(URL)
                .build();

        api = retrofit.create(APIRequests.class);
    }

    public static WebService getInstance() {
        if (instance == null) {
            instance = new WebService();
        }
        return instance;
    }

    public APIRequests getApi() {
        return api;
    }
}