package com.example.lib.Repository;

import com.example.lib.Model.Request.Comment;
import com.example.lib.Model.Request.Gym;
import com.example.lib.Model.Request.Trainer;
import com.example.lib.Model.Request.combo;
import com.example.lib.Model.Request.userImg;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Home {
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

    @GET("home/getJudgeByPT")
    Call<List<Comment>> getJudgeByPT(@Query("id")int id);

    @GET("home/getJudgeByGym")
    Call<List<Comment>> getJudgeByGym(@Query("id")int id);

    @GET("home/getByPt")
    Call<List<userImg>> getByPt(@Query("id") int id);
}
