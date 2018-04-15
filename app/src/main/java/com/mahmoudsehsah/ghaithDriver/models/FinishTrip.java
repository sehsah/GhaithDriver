package com.mahmoudsehsah.ghaithDriver.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mahmoud on 4/8/2018.
 */

public class FinishTrip implements Parcelable {

    @SerializedName("id_driver")
    private String id_driver;

    @SerializedName("id_trip")
    private String id_trip;

    @SerializedName("id_user")
    private String id_user;

    @SerializedName("price")
    private String price;

    public FinishTrip() {
    }

    protected FinishTrip(Parcel in) {
        id_driver = in.readString();
        id_user = in.readString();
        id_trip = in.readString();
        price = in.readString();
    }

    public static final Creator<FinishTrip> CREATOR = new Creator<FinishTrip>() {
        @Override
        public FinishTrip createFromParcel(Parcel in) {
            return new FinishTrip(in);
        }

        @Override
        public FinishTrip[] newArray(int size) {
            return new FinishTrip[size];
        }
    };

    public String getId_driver() {
        return id_driver;
    }

    public void setId_driver(String id_driver) {
        this.id_driver = id_driver;
    }


    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_trip() {
        return id_trip;
    }

    public void setId_trip(String id_trip) {
        this.id_trip = id_trip;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id_driver);
        dest.writeString(id_user);
        dest.writeString((price));
        dest.writeString((id_trip));
    }
}
