package com.mahmoudsehsah.ghaithDriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahmoud on 3/22/2018.
 */

public class FinishOrder {
    @SerializedName("id_order")
    @Expose
    private String id_order;

    public String getid_order() {
        return id_order;
    }
    public void setid_order(String id_order) {
        this.id_order = id_order;
    }

}
