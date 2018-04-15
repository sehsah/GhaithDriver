package com.mahmoudsehsah.ghaithDriver.adapter;

import com.mahmoudsehsah.ghaithDriver.models.AcceptTrips;
import com.mahmoudsehsah.ghaithDriver.models.AddNewOffer;
import com.mahmoudsehsah.ghaithDriver.models.CancelTrip;
import com.mahmoudsehsah.ghaithDriver.models.ChatList;
import com.mahmoudsehsah.ghaithDriver.models.FinishOrder;
import com.mahmoudsehsah.ghaithDriver.models.FinishRegister;
import com.mahmoudsehsah.ghaithDriver.models.FinishTrip;
import com.mahmoudsehsah.ghaithDriver.models.Login;
import com.mahmoudsehsah.ghaithDriver.models.NewTripe;
import com.mahmoudsehsah.ghaithDriver.models.Register;
import com.mahmoudsehsah.ghaithDriver.models.RegisterNewMarket;
import com.mahmoudsehsah.ghaithDriver.models.RejectTrips;
import com.mahmoudsehsah.ghaithDriver.models.SendMessage;
import com.mahmoudsehsah.ghaithDriver.models.SendNotiFirbaseClient;
import com.mahmoudsehsah.ghaithDriver.models.UpdateBill;
import com.mahmoudsehsah.ghaithDriver.models.UpdateSatus;
import com.mahmoudsehsah.ghaithDriver.models.UpdateToken;
import com.mahmoudsehsah.ghaithDriver.models.UpdateUnformation;
import com.mahmoudsehsah.ghaithDriver.models.updateLocation;
import com.mahmoudsehsah.ghaithDriver.models.updateUserId;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by mahmoud on 2/9/2018.
 */

public interface APIRequests {

    ////////////////////////////////// Auth  //////////////////////////////////
    @Multipart
    @POST("processRegistrationDriver")
    Call<Register> register(
            @Part("type") String type,
            @Part("driver_telephone") String driver_telephone,
            @Part("driver_password") String driver_password,
            @Part("driver_username") String driver_username);

    @Multipart
    @POST("FinishRegister")
    Call<FinishRegister> FinishRegister(
            @Part("driver_id") String driver_id,
            @Part MultipartBody.Part file,
            @Part MultipartBody.Part file2,
            @Part MultipartBody.Part file3,
            @Part MultipartBody.Part fil4);

    @FormUrlEncoded
    @POST("processLoginDriver")
    Call<Login> login(
            @Field("driver_telephone") String driver_telephone,
            @Field("driver_password") String driver_password);

    @Multipart
    @POST("update_informationDriver")
    Call<UpdateUnformation> updateInformation(
            @Part("driver_username") String driver_username,
            @Part("driver_telephone") String driver_telephone,
            @Part("driver_password") String driver_password,
            @Part("driver_id") int driver_id,
            @Part MultipartBody.Part customers_photo);

    @FormUrlEncoded
    @POST("update_locationDriver")
    Call<updateLocation> updateLocation(
            @Field("driver_id") String driver_id,
            @Field("lat") double lat,
            @Field("lng") double lng);

    @FormUrlEncoded
    @POST("updateUserId2")
    Call<updateUserId> updateUserId(
            @Field("id_user") String id_user,
            @Field("userId") String userId);

    @FormUrlEncoded
    @POST("UpdateTokenDriver")
    Call<UpdateToken> UpdateToken(
            @Field("id_user") String id_user,
            @Field("token") String token);

    @FormUrlEncoded
    @POST("UpdateStatus")
    Call<UpdateSatus> UpdateSatus(
            @Field("id_driver") String id_driver,
            @Field("online") String online);



    ////////////////////////////////// Trips   //////////////////////////////////


    @FormUrlEncoded
    @POST("finishTrip")
    Call<FinishTrip> FinishTrip(
            @Field("id_user") String id_user,
            @Field("id_trip") String id_trip,
            @Field("price") String price,
            @Field("id_driver") String id_driver
    );

    @FormUrlEncoded
    @POST("AcceptTrips")
    Call<AcceptTrips> AcceptTrips(
            @Field("id_user") String id_user,
            @Field("id_trip") String id_trip,
            @Field("id_driver") String id_driver
    );

    @FormUrlEncoded
    @POST("RejectTrips")
    Call<RejectTrips> RejectTrips(
            @Field("id_order") String id_order,
            @Field("id_driver") String id_driver
    );

    ///////////////////////////////// Orders ////////////////////////////////////

    @GET("getOrders")
    Call<JSONResponseGetOrders> getJSONGetOrder(@Query("id_market") String id_market);

    @GET("getmyOrders")
    Call<JSONResponseGetMyOrders> JSONResponseGetMyOrders(@Query("id_user") String id_user);

    @FormUrlEncoded
    @POST("AddNewOffer")
    Call<AddNewOffer> AddNewOffer(
            @Field("id_user") String id_user,
            @Field("id_driver") String id_driver,
            @Field("photo_driver") String photo_driver,
            @Field("name_driver") String name_driver,
            @Field("text") String text,
            @Field("price") String price,
            @Field("time") String time,
            @Field("description") String description,
            @Field("image_order") String image_order,
            @Field("id_order") String id_order
    );

    @GET("getnotifcation")
    Call<JSONResponseGetNotfcation> getJSONNotfcation(@Query("id") String id);

    @GET("ChatListDriver")
    Call<JSONResponseGetChatListUsrer> getJSONChatListUsrer(@Query("id_user") String id_user);

    @GET("Masseges")
    Call<JSONResponseGetMasseges> getJSONMasseges(@Query("id_user") String id_user, @Query("id_driver") String id_driver,@Query("id_order") String id_order);

    @GET("Masseges")
    Call<List<ChatList>> getMessages(@Query("id_user") String id_user, @Query("id_driver") String id_driver, @Query("id_order") String id_order);

    @FormUrlEncoded
    @POST("SendMessageToClient")
    Call<SendMessage> SendMessagee(
            @Field("message") String message,
            @Field("client_id") String client_id,
            @Field("driver_id") String driver_id,
            @Field("send") String send,
            @Field("id_order") String id_order,
            @Field("type") String type
    );

    @Multipart
    @POST("SendMessagePhotoToClient")
    Call<SendMessage> SendMessagePhoto(
            @Part MultipartBody.Part images,
            @Part("client_id") String client_id,
            @Part("driver_id") String driver_id,
            @Part("send") String send,
            @Part("id_order") String id_order,
            @Part("type") String type
    );

    @FormUrlEncoded
    @POST("UpdateBill")
    Call<UpdateBill> UpdateBill(
            @Field("price2") String price2,
            @Field("id_order") String id_order


    );

    @FormUrlEncoded
    @POST("SendNotiFirbaseClient")
    Call<SendNotiFirbaseClient> SendNotiFirbaseClient(
            @Field("id_client") String client_id,
            @Field("title") String title
    );


    @FormUrlEncoded
    @POST("RegisterNewMarket")
    Call<RegisterNewMarket> RegisterNewMarket(
            @Field("id_driver") String id_driver,
            @Field("id_market") String id_market
    );

    @GET("FinishOrder")
    Call<FinishOrder> FinishOrder(@Query("id_order") String id_order);


    @FormUrlEncoded
    @POST("DriverCancelTrips")
    Call<CancelTrip> CancelTrip(
            @Field("id_user") String id_user,
            @Field("id_trip") String id_trip
    );

}


