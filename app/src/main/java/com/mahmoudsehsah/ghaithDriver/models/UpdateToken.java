package com.mahmoudsehsah.ghaithDriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahmoud on 3/19/2018.
 */

public class UpdateToken {
    @SerializedName("id_user")
    @Expose
    private String id_user;

    @SerializedName("token")
    @Expose
    private String token;

    public String getid_user() {
        return id_user;
    }
    public void setid_user(String id_user) {
        this.id_user = id_user;
    }

    public String gettoken() {
        return token;
    }
    public void settoken(String token) {
        this.token = token;
    }
}
