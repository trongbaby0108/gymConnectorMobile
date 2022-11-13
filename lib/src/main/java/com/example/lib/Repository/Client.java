package com.example.lib.Repository;

import com.example.lib.Model.Request.loginRequest;
import com.example.lib.Model.Request.ptSignIn;
import com.example.lib.Model.Request.userSignIn;
import com.example.lib.Model.Response.PTInfoResponse;
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
    @GET("/signUser/save")
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

    @GET("/signInPersonalTrainer/save")
    Call<String> signUpPT(
            @Body ptSignIn ptSignIn
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



    @POST("auth/login")
    Call<String> login(
            @Body loginRequest loginRequest);

    @GET("auth/getUserInfo")
    Call<userInfoResponse> getUser(@Query("jwt") String token);

    @GET("auth/getPTInfo")
    Call<PTInfoResponse> getPTInfo(@Query("jwt") String token);

    @GET("client/addComment")
    Call<String> addComment(@Query("content") String content,
                            @Query("vote") float vote ,
                            @Query("gymId") int gymId,
                            @Query("userId") int userId);


    @GET("client/addCommentPT")
    Call<String> addCommentPT(@Query("content") String content,
                              @Query("vote") float vote ,
                              @Query("ptID") int ptID,
                              @Query("userId") int userId);

}
