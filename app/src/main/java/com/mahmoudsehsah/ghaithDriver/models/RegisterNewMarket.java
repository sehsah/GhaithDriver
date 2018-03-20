package com.mahmoudsehsah.ghaithDriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahmoud on 3/19/2018.
 */

public class RegisterNewMarket {

    @SerializedName("id_driver")
    @Expose
    private String id_driver;


    @SerializedName("id_market")
    @Expose
    private String id_market;


    public String getid_driver() {
        return id_driver;
    }

    public void setid_driver(String id_driver) {
        this.id_driver = id_driver;
    }

    public String getid_market() {
        return id_market;
    }

    public void setid_market(String id_market) {
        this.id_market = id_market;
    }

}
