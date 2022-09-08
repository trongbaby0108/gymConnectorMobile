package com.example.lib.Repository;

import com.example.lib.Model.*;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface Methods {

    @GET("/signUser/save")
    Call<String> signUpUser(
            @Query("username") String username,
            @Query("password") String password,
            @Query("name") String fullName,
            @Query("email") String email,
            @Query("address") String address,
            @Query("phone") String phone
    );
    @Multipart
    @POST("/signUser/uploadAvatar")
    Call<String> uploadAvatar(
            @Part("username") RequestBody username,
            @Part MultipartBody.Part file
    );

    @GET("signUser/sendToken")
    Call<String> sendTokenUser(@Query("username")String username);

    @GET("signUser/confirmToken")
    Call<String> confirmTokenUser(@Query("username")String username ,
                                  @Query("token")String token );

    @GET("signUser/createGoogleUser")
    Call<userInfoResponse> createGoogleUser(@Query("email")String email ,
                                            @Query("name")String name,
                                            @Query("avatar") String avatar);

    @GET("/signInPersonalTrainer/save")
    Call<String> signUpPT(
            @Query("username") String username,
            @Query("password") String password,
            @Query("name") String fullName,
            @Query("email") String email,
            @Query("address") String address,
            @Query("phone") String phone,
            @Query("price") int price,
            @Query("gym") int id
    );

    @Multipart
    @POST("/signInPersonalTrainer/uploadAvatar")
    Call<String> uploadAvatarPT(
            @Part("username") RequestBody username,
            @Part MultipartBody.Part file
    );

    @GET("signInPersonalTrainer/sendToken")
    Call<String> sendTokenPT(
            @Query("username") String username
    );

    @GET("signInPersonalTrainer/confirmToken")
    Call<String> confirmTokenPT(@Query("username")String username ,
                                  @Query("token")String token );

    @GET("home/getPT")
    Call<List<Trainer>> getPT();

    @GET("home/getGym")
    Call<List<Gym>> getGym();

    @GET("home/getCombo")
    Call<List<combo>> getCombo();

    @GET("home/getByGym")
    Call<List<combo>> getComboByGym(@Query("id")int id);

    @GET("home/getPTByGym")
    Call<List<Trainer>> getPTByGym(@Query("id")int id);

    @GET("home/getJudgeByGym")
    Call<List<Comment>> getJudgeByGym(@Query("id")int id);

    @GET("home/addComment")
    Call<String> addComment(@Query("content") String content,
                                      @Query("vote") float vote ,
                                      @Query("gymId") int gymId,
                                      @Query("userId") int userId);
    @GET("home/getJudgeByPT")
    Call<List<Comment>> getJudgeByPT(@Query("id")int id);

    @GET("home/addCommentPT")
    Call<String> addCommentPT(@Query("content") String content,
                              @Query("vote") float vote ,
                              @Query("ptID") int ptID,
                              @Query("userId") int userId);

    @POST("auth/login")
    Call<String> login(
            @Body loginRequest loginRequest);

    @GET("auth/getUserInfo")
    Call<userInfoResponse> getUser(@Query("jwt") String token);


    @GET("auth/getPTInfo")
    Call<PTInfoResponse> getPTInfo(@Query("jwt") String token);

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

    @GET("picPT/getByPt")
    Call<List<userImg>> getByPt(@Query("id") int id);

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
    