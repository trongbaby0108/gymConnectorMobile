package com.example.lib;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit ;
    private static String Base_URL= "http://10.0.2.2:8080/";

    public static String getBase_URL() {
        return Base_URL;
    }
    public static void setBase_URL(String base_URL) {
        Base_URL = base_URL;
    }

    public static Retrofit getRetrofit(){
        if(retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_URL)
                    .addConverterFactory(
                            GsonConverterFactory
                                    .create(gson))
                    .build();
        }
        return retrofit;
    }
}
