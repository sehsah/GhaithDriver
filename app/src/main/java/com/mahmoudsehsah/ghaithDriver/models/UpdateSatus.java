package com.mahmoudsehsah.ghaithDriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahmoud on 4/4/2018.
 */

public class UpdateSatus {
    @SerializedName("id_driver")
    @Expose
    private String id_driver;

    @SerializedName("onilne")
    @Expose
    private String onilne;

    public String getId_driver() {
        return id_driver;
    }
    public void setId_driver(String id_driver) {
        this.id_driver = id_driver;
    }

    public String getOnilne() {
        return onilne;
    }
    public void setOnilne(String onilne) {
        this.onilne = onilne;
    }
}
