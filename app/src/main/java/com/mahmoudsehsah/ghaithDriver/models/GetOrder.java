package com.mahmoudsehsah.ghaithDriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahmoud on 3/11/2018.
 */

public class GetOrder {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("id_user")
    @Expose
    private String idUser;
    @SerializedName("name_user")
    @Expose
    private String name_user;
    @SerializedName("km")
    @Expose
    private String km;
    @SerializedName("id_delivery")
    @Expose
    private Object idDelivery;
    @SerializedName("places")
    @Expose
    private String places;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("images")
    @Expose
    private String images;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private String lng;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("creatd_at")
    @Expose
    private String creatdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("lat_market")
    @Expose
    private  String lat_market;

    @SerializedName("lang_market")
    @Expose
    private  String lang_market;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public Object getIdDelivery() {
        return idDelivery;
    }

    public void setIdDelivery(Object idDelivery) {
        this.idDelivery = idDelivery;
    }

    public String getPlaces() {
        return places;
    }

    public void setPlaces(String places) {
        this.places = places;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat_market() {
        return lat_market;
    }

    public void setLat_market(String lat_market) {
        this.lat_market = lat_market;
    }

    public String getLng_market() {
        return lang_market;
    }

    public void setLng_market(String lang_market) {
        this.lang_market = lang_market;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatdAt() {
        return creatdAt;
    }

    public void setCreatdAt(String creatdAt) {
        this.creatdAt = creatdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
