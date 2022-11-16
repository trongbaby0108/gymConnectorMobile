package com.example.lib.Repository;

import com.example.lib.Model.Request.Trainer;
import com.example.lib.Model.Request.updateUser;
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



    @GET("user/getUserByPT")
    Call<List<userInfoResponse>> getUserByPT(@Query("idPT") int idPT);


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
    