package com.mahmoudsehsah.ghaithDriver.adapter;

import com.mahmoudsehsah.ghaithDriver.models.AddNewOffer;
import com.mahmoudsehsah.ghaithDriver.models.Login;
import com.mahmoudsehsah.ghaithDriver.models.Register;
import com.mahmoudsehsah.ghaithDriver.models.UpdateUnformation;
import com.mahmoudsehsah.ghaithDriver.models.updateLocation;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by mahmoud on 2/9/2018.
 */

public interface APIRequests {

    ////////////////////////////////// Auth  //////////////////////////////////
    @FormUrlEncoded
    @POST("android/ghaith/processRegistrationDriver")
    Call<Register> register(
            @Field("driver_telephone") String driver_telephone,
            @Field("driver_password") String driver_password,
            @Field("driver_username") String driver_username);

    @FormUrlEncoded
    @POST("android/ghaith/processLoginDriver")
    Call<Login> login(
            @Field("driver_telephone") String driver_telephone,
            @Field("driver_password") String driver_password);

    @Multipart
    @POST("android/ghaith/update_informationDriver")
    Call<UpdateUnformation> updateInformation(
            @Part("driver_username") String driver_username,
            @Part("driver_telephone") String driver_telephone,
            @Part("driver_password") String driver_password,
            @Part("driver_id") int driver_id,
            @Part MultipartBody.Part customers_photo);

    @FormUrlEncoded
    @POST("android/ghaith/update_locationDriver")
    Call<updateLocation> updateLocation(
            @Field("driver_id") String driver_id,
            @Field("lat") double lat,
            @Field("lng") double lng);

    ////////////////////////////////// Trips   //////////////////////////////////
    @GET("android/ghaith/getNewTrip")
    Call<JSONResponseGetNewTrip> getJSONGetTrip();

    ///////////////////////////////// Orders ////////////////////////////////////

    @GET("android/ghaith/getOrders")
    Call<JSONResponseGetOrders> getJSONGetOrder();

    @FormUrlEncoded
    @POST("android/ghaith/AddNewOffer")
    Call<AddNewOffer> AddNewOffer(
            @Field("id_user") String id_user,
            @Field("id_driver") String id_driver,
            @Field("photo_driver") String photo_driver,
            @Field("name_driver") String name_driver,
            @Field("text") String text,
            @Field("price") String price,
            @Field("id_order") String id_order
    );

    @GET("android/ghaith/getOffer")
    Call<JSONResponseGetNotfcation> getJSONNotfcation(@Query("id_user") String id_user);

}


