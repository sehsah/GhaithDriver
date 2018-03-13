package com.mahmoudsehsah.ghaithDriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mahmoud on 2/20/2018.
 */

public class Login {

    @SerializedName("success")
    @Expose
    private String success;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("driver_telephone")
    @Expose
    private String driver_telephone;

    @SerializedName("driver_password")
    @Expose
    private String driver_password;

    @SerializedName("driver_photo")
    @Expose
    private String driver_photo;

    @SerializedName("data")
    @Expose
    private List<User> data = null;


    public List<User> getData() {
        return data;
    }

    public void setData(List<User> data) {
        this.data = data;
    }

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

    public String getdriver_telephone() {
        return driver_telephone;
    }
    public void setdriver_telephone(String driver_telephone) {
        this.driver_telephone = driver_telephone;
    }


    public String getdriver_password() {
        return driver_password;
    }
    public void setdriver_password(String driver_password) {
        this.driver_password = driver_password;
    }


}
