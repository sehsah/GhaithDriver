package com.mahmoudsehsah.ghaithDriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahmoud on 3/24/2018.
 */

public class UpdateBill {
    @SerializedName("id_order")
    @Expose
    private String id_order;

    @SerializedName("price2")
    @Expose
    private String price2;

    public String getid_order() {
        return id_order;
    }
    public void setid_order(String id_order) {
        this.id_order = id_order;
    }

    public String getprice2() {
        return price2;
    }
    public void setprice2(String price2) {
        this.price2 = price2;
    }
}
