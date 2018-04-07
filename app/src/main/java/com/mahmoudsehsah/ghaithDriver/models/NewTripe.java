package com.mahmoudsehsah.ghaithDriver.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mahmoud on 4/4/2018.
 */

public class NewTripe implements Parcelable {

    public static final Parcelable.Creator<NewTripe> CREATOR = new Parcelable.Creator<NewTripe>() {
        @Override
        public NewTripe createFromParcel(Parcel in) {
            return new NewTripe(in);
        }

        @Override
        public NewTripe[] newArray(int size) {
            return new NewTripe[size];
        }
    };


    @SerializedName("id")
    private String id;

    @SerializedName("id_user")
    private String id_user;

    @SerializedName("lat_user")
    private double lat_user;

    @SerializedName("lng_user")
    private double lng_user;

    @SerializedName("places")
    private String places;

    @SerializedName("time")
    private String time;

    @SerializedName("lat")
    private double lat;

    @SerializedName("lng")
    private double lng;

    @SerializedName("name")
    private String name;

    @SerializedName("pickup_location")
    private String pickup_location;

    @SerializedName("drop_location")
    private String drop_location;

    @SerializedName("price")
    private String price;


    @SerializedName("status")
    private String status;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    public NewTripe() {
    }

    protected NewTripe(Parcel in) {
        id = in.readString();
        id_user = in.readString();
        lat_user = Double.parseDouble(in.readString());
        lng_user = Double.parseDouble(in.readString());
        places = in.readString();
        lng = Double.parseDouble(in.readString());
        name = in.readString();
        pickup_location = in.readString();
        drop_location = in.readString();
        time = in.readString();
        status = in.readString();
        createdAt = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public double getLat_user() {
        return lat_user;
    }

    public void setLat_user(double lat_user) {
        this.lat_user = lat_user;
    }

    public double getLng_user() {return lng_user;}

    public void setLng_user(double lng_user) {
        this.lng_user = lng_user;
    }

    public String getPlaces() {
        return places;
    }

    public void setPlaces(String places) {
        this.places = places;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {return lng;}

    public void setLng(double lng) {this.lng = lng;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPickup_location() {
        return pickup_location;
    }

    public void setPickup_location(String pickup_location) {
        this.pickup_location = pickup_location;}

    public String getDrop_location() {
        return drop_location;
    }

    public void setDrop_location(String drop_location) {
        this.drop_location = drop_location;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(id_user);
        dest.writeString(String.valueOf(lat_user));
        dest.writeString(String.valueOf(lng_user));
        dest.writeString(String.valueOf(lat));
        dest.writeString(String.valueOf(lng));
        dest.writeString(String.valueOf(price));
        dest.writeString(String.valueOf(pickup_location));
        dest.writeString(String.valueOf(drop_location));
        dest.writeString(createdAt);
    }
}
