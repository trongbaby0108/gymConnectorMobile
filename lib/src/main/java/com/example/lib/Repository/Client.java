package com.example.lib.Repository;

import com.example.lib.Model.Request.addGymComment;
import com.example.lib.Model.Request.addPtComment;
import com.example.lib.Model.Request.loginRequest;
import com.example.lib.Model.Request.ptSignIn;
import com.example.lib.Model.Request.updatePt;
import com.example.lib.Model.Request.updateUser;
import com.example.lib.Model.Request.userSignIn;
import com.example.lib.Model.Response.PTInfoResponse;
import com.example.lib.Model.Response.billGymResponse;
import com.example.lib.Model.Response.billPTResponse;
import com.example.lib.Model.Response.userInfoResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
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

    @POST("signUser/sendToken")
    Call<String> sendTokenUser(@Query("username")String username);

    @GET("signUser/confirmToken")
    Call<String> confirmTokenUser(@Query("username")String username ,
                                  @Query("token")String token);

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

    @POST("auth/getUserInfo")
    Call<userInfoResponse> getUser(@Query("jwt") String token);

    @POST("auth/getPTInfo")
    Call<PTInfoResponse> getPTInfo(@Query("jwt") String token);

    @POST("client/comment/addPtComment")
    Call<String> addPtComment(
            @Header("Authorization") String auth,
            @Body addPtComment addCommentPt);


    @POST("client/comment/addGymComment")
    Call<String> addGymComment(
            @Header("Authorization") String auth,
            @Body addGymComment addGymComment);


    @POST("client/billGym/checkout")
    Call<Boolean> checkout(
            @Header("Authorization") String auth,
            @Query("idUser") int idUser,
                           @Query("idCombo") int idCombo);

    @GET("client/billGym/checkGymExit/{id}")
    Call<billGymResponse> checkGymExit(
            @Header("Authorization") String auth,
            @Path("id") int id);


    @POST("client/billPt/checkout")
    Call<Boolean> checkoutPT(
            @Header("Authorization") String auth,
            @Query("idUser") int idUser,
            @Query("idPt") int idPT);

    @GET("client/billPt/checkPTExit/{id}")
    Call<billPTResponse> checkPTExit(
            @Header("Authorization") String auth,
            @Path("id") int id);

    @POST("client/user/update")
    Call<userInfoResponse> update(
            @Header("Authorization") String auth,
            @Body updateUser updateUser);

    @POST("/client/personalTrainer/update")
    Call<PTInfoResponse> updatePT(
            @Header("Authorization") String auth,
            @Body updatePt updatePt
    );

    @POST("client/personalTrainer/getUserByPT")
    Call<List<userInfoResponse>> getUserByPT(
            @Header("Authorization") String auth,
            @Query("idPT") int idPT);

    @Multipart
    @POST("/client/personalTrainer/addMoreImg")
    Call<String> newImgPT(
            @Header("Authorization") String auth,
            @Part("id") RequestBody id,
            @Part MultipartBody.Part pic
    );
}
