package com.mahmoudsehsah.ghaithDriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahmoud on 3/18/2018.
 */

public class SendNotiFirbaseClient {

    @SerializedName("id_client")
    @Expose
    private String id_client;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("message")
    @Expose
    private String message;


    public String getid_client() {
        return id_client;
    }
    public String gettitle() {
        return title;
    }
    public String getmessage() {
        return message;
    }

}
