package com.mahmoudsehsah.ghaithDriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahmoud on 2/20/2018.
 */

public class Register {

    @SerializedName("success")
    @Expose
    private String success;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("customers_telephone")
    @Expose
    private String customers_telephone;

    @SerializedName("customers_password")
    @Expose
    private String customers_password;

    @SerializedName("customers_username")
    @Expose
    private String customers_username;

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

    public String getCustomers_telephone() {
        return customers_telephone;
    }
    public void setCustomers_telephone(String customer_telephone) {
        this.customers_telephone = customer_telephone;
    }


    public String getCustomers_password() {
        return customers_password;
    }
    public void setCustomers_password(String customers_password) {
        this.customers_password = customers_password;
    }


    public String getCustomers_username() {
        return customers_username;
    }
    public void setCustomers_username(String customers_username) {
        this.customers_username = customers_username;
    }

}
