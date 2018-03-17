package com.mahmoudsehsah.ghaithDriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahmoud on 3/14/2018.
 */

public class updateUserId {
    @SerializedName("id_user")
    @Expose
    private String id_user;

    @SerializedName("userId")
    @Expose
    private String userId;

    public String getid_user() {
        return id_user;
    }
    public void setid_user(String id_user) {
        this.id_user = id_user;
    }

    public String getuserId() {
        return userId;
    }
    public void setuserId(String userId) {
        this.userId = userId;
    }
}
