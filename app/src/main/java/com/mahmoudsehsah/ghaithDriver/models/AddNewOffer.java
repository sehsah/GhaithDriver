package com.mahmoudsehsah.ghaithDriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahmoud on 3/12/2018.
 */

public class AddNewOffer {

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("id_driver")
    @Expose
    private String id_driver;

    @SerializedName("id_order")
    @Expose
    private String id_order;

    @SerializedName("photo_driver")
    @Expose
    private String photo_driver;

    @SerializedName("name_driver")
    @Expose
    private String name_driver;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("success")
    @Expose
    private String success;

    @SerializedName("message")
    @Expose
    private String message;


    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getid_driver() {
        return id_driver;
    }
    public void setid_driver (String id_driver) {
        this.id_driver = id_driver;
    }

    public String getprice() {
        return price;
    }
    public void setprice(String price) {
        this.price = price;
    }

    public String getid_order() {
        return id_order;
    }
    public void setid_order(String id_order) {
        this.price = id_order;
    }

    public String gephoto_driver() {
        return photo_driver;
    }
    public void setphoto_driver(String photo_driver) {
        this.photo_driver = photo_driver;
    }

    public String getname_driver() {
        return name_driver;
    }
    public void setname_driver(String name_driver) {
        this.name_driver = name_driver;
    }

    public String gettext() {
        return text;
    }
    public void settext(String text) {
        this.text = text;
    }

}
