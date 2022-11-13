package com.example.lib.Repository;

import com.example.lib.Model.Request.Trainer;
import com.example.lib.Model.Request.loginRequest;
import com.example.lib.Model.Request.ptSignIn;
import com.example.lib.Model.Request.userImg;
import com.example.lib.Model.Request.userSignIn;
import com.example.lib.Model.Response.PTInfoResponse;
import com.example.lib.Model.Response.billGymResponse;
import com.example.lib.Model.Response.billPTResponse;
import com.example.lib.Model.Response.userInfoResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface Admin {
    @POST("gym/addGym")
    Call<String> addGym(
            @Header("Authorization") String auth,
            @Query("email") String email ,
            @Query("address") String address ,
            @Query("name") String name ,
            @Query("phone") String phone);

    @POST("gym/updateGym")
    Call<String> updateGym(
            @Header("Authorization") String auth,
            @Query("id") int id,
            @Query("email") String email ,
            @Query("address") String address ,
            @Query("name") String name ,
            @Query("phone") String phone);


    @Multipart
    @POST("/gym/addGymImg")
    Call<String> addGymImg(
            @Header("Authorization") String auth,
            @Part("name") RequestBody name,
            @Part MultipartBody.Part file
    );


    @POST("combo/addCombo")
    Call<String> addCombo(
            @Header("Authorization") String auth,
            @Query("name") String name ,
            @Query("price") int price,
            @Query("gymId") int gymId
    );

    @POST("combo/updateCombo")
    Call<String> updateCombo(
            @Header("Authorization") String auth,
            @Query("id") int id,
            @Query("name") String name ,
            @Query("price") int price,
            @Query("gymId") int gymId
    );

    @GET("bill_gym/checkout")
    Call<Boolean> checkout(@Query("idUser") int idUser,
                           @Query("idGym") int idGym,
                           @Query("idCombo") int idCombo);

    @GET("bill_gym/checkGymExit")
    Call<billGymResponse> checkGymExit(@Query("idUser") int idUser);

    @GET("bill_pt/checkout")
    Call<Boolean> checkoutPT(@Query("idUser") int idUser,
                           @Query("idPt") int idPT);

    @GET("bill_pt/checkPTExit")
    Call<billPTResponse> checkPTExit(@Query("idUser") int idUser);

    @GET("user/getUserByPT")
    Call<List<userInfoResponse>> getUserByPT(@Query("idPT") int idPT);

    @GET("user/update")
    Call<String> update(
            @Query("id") int id,
            @Query("name") String name,
            @Query("phone") String phone,
            @Query("email") String email,
            @Query("address") String address
    );

    @GET("personal_trainer/update")
    Call<PTInfoResponse> updatePT(
            @Query("id") int id,
            @Query("name") String name,
            @Query("phone") String phone,
            @Query("email") String email,
            @Query("address") String address,
            @Query("price") int price
    );

    @GET("userAdmin/getUser")
    Call<List<userInfoResponse>> getUserAdmin(
            @Header("Authorization") String auth
    );

    @GET("userAdmin/disableUser")
    Call<String> disableUser(
            @Header("Authorization") String auth,
            @Query("idUser") int idUser
    );

    @GET("userAdmin/enableUser")
    Call<String> enableUser(
            @Header("Authorization") String auth,
            @Query("idUser") int idUser
    );


    @Multipart
    @POST("picPT/save")
    Call<String> newImgPT(
            @Part("id") RequestBody id,
            @Part MultipartBody.Part pic
    );

    @GET("personal_trainerAdmin/enablePT")
    Call<String> getALlPT(
        @Header("Authorization") String auth,
        @Query("idPT") int idPT
    );

    @GET("personal_trainerAdmin/disablePT")
    Call<String> disablePT(
            @Header("Authorization") String auth,
            @Query("idPT") int idPT
    );

    @GET("personal_trainerAdmin/enablePT")
    Call<String> enablePT(
            @Header("Authorization") String auth,
            @Query("idPT") int idPT
    );

    @GET("personal_trainerAdmin/getALlPT")
    Call<List<Trainer>> getALlPTAdmin(
            @Header("Authorization") String auth
    );






}
    