package com.example.android.myapp.network;


import com.example.android.myapp.remote.DishCSClass;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface  RemService {


    @POST("upcs/")
    Call<Void> addUser(@Body List<DishCSClass> user);

    @GET("csdishes/")
    Call<List<DishCSClass>> getcsdishes();

//    @POST("login1")
//    Call<Void> gettoken(@Body User body);

}
