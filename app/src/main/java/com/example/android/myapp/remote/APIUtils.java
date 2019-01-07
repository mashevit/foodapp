package com.example.android.myapp.remote;

import com.example.android.myapp.network.RemService;


public class APIUtils {

    private APIUtils(){
    };

    public static final String API_URL = "http://10.0.2.2:8080/";//https://foodapi8.herokuapp.com/";

    public static RemService getUserService(){
        return RetrofitClient.getClient(API_URL).create(RemService.class);
    }

}