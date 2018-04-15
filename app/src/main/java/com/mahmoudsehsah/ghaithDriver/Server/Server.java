package com.mahmoudsehsah.ghaithDriver.Server;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

/**
 * Created by android on 17/3/17.
 */

public class Server {

    public static final String BASE_URL = "http://www.ghaithksa.com/";            // Your admin panel URL

    private static final String TAG = "server";
    private static AsyncHttpClient client = new AsyncHttpClient();



    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(7000);
        client.get(getAbsoluteUrl(url), params, responseHandler);

        Log.e(TAG, getAbsoluteUrl(url)+params.toString());
    }

    public static void postSync(String url, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
        try {
            SyncHttpClient client = new SyncHttpClient();
            client.post(getAbsoluteUrl(url), params, jsonHttpResponseHandler);
        } catch (Exception e) {

        }
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(AsyncHttpClient.DEFAULT_MAX_CONNECTIONS);
        client.post(getAbsoluteUrl(url), params, responseHandler);
        Log.d(TAG, getAbsoluteUrl(url));
    }

    public static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;

    }
    public static void getPublic(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(3000);
        client.get(url, params, responseHandler);

        Log.d(TAG, getAbsoluteUrl(url));
    }

    public static void setHeader(String header) {
        client.addHeader("X-API-KEY", header);
    }

    public static void setContetntType() {
        client.addHeader("Content-Type", "application/x-www-form-urlencoded");
    }

}
