package com.example.lib.Repository;

import com.example.lib.Model.Request.addGymComment;
import com.example.lib.Model.Request.addPtComment;
import com.example.lib.Model.Request.loginRequest;
import com.example.lib.Model.Request.ptSignIn;
import com.example.lib.Model.Request.updateUser;
import com.example.lib.Model.Request.userSignIn;
import com.example.lib.Model.Response.PTInfoResponse;
import com.example.lib.Model.Response.billGymResponse;
import com.example.lib.Model.Response.billPTResponse;
import com.example.lib.Model.Response.userInfoResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Client {
    @POST("/signUser/save")
    Call<String> signUpUser(@Body userSignIn userSignIn);

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

    @POST("/signInPersonalTrainer/signIn")
    Call<String> signUpPT(@Body ptSignIn ptSignIn);

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



    @POST("auth/login")
    Call<String> login(
            @Body loginRequest loginRequest);

    @GET("auth/getUserInfo")
    Call<userInfoResponse> getUser(@Query("jwt") String token);

    @GET("auth/getPTInfo")
    Call<PTInfoResponse> getPTInfo(@Query("jwt") String token);

    @POST("client/comment/addPtComment")
    Call<String> addPtComment(@Body addPtComment addCommentPt);


    @POST("client/comment/addGymComment")
    Call<String> addGymComment(@Body addGymComment addGymComment);


    @GET("billGym/checkout")
    Call<Boolean> checkout(@Query("idUser") int idUser,
                           @Query("idGym") int idGym,
                           @Query("idCombo") int idCombo);

    @GET("billGym/checkGymExit")
    Call<billGymResponse> checkGymExit(@Query("idUser") int idUser);

    @GET("billPt/checkout")
    Call<Boolean> checkoutPT(@Query("idUser") int idUser,
                             @Query("idPt") int idPT);

    @GET("billPt/checkPTExit")
    Call<billPTResponse> checkPTExit(@Query("idUser") int idUser);

    @POST("user/update")
    Call<userInfoResponse> update(
            @Body updateUser updateUser);

    @GET("personal_trainer/update")
    Call<PTInfoResponse> updatePT(
            @Query("id") int id,
            @Query("name") String name,
            @Query("phone") String phone,
            @Query("email") String email,
            @Query("address") String address,
            @Query("price") int price
    );

}
