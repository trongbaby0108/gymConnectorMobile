package com.example.lib.Repository;

import com.example.lib.Model.Request.Comment;
import com.example.lib.Model.Request.Gym;
import com.example.lib.Model.Request.Trainer;
import com.example.lib.Model.Request.combo;
import com.example.lib.Model.Response.ptImgResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Home {
    @GET("home/getPT")
    Call<List<Trainer>> getPT();

    @GET("home/getGym")
    Call<List<Gym>> getGym();

    @GET("home/getCombo")
    Call<List<combo>> getCombo();

    @GET("home/getComboByGym/{id}")
    Call<List<combo>> getComboByGym(@Path("id") int id);

    @GET("home/getPTByGym/{id}")
    Call<List<Trainer>> getPTByGym(@Path("id")int id);

    @GET("home/getRateByPT/{id}")
    Call<List<Comment>> getJudgeByPT(@Path("id")int id);

    @GET("home/getRateByGym/{id}")
    Call<List<Comment>> getJudgeByGym(@Path("id")int id);

    @GET("home/getPicByPt/{id}")
    Call<List<ptImgResponse>> getByPt(@Path("id") int id);
}
