package com.android.example.cameraxapp.Interfaces;

import com.android.example.cameraxapp.Model.SignOutRequest;
import com.android.example.cameraxapp.Model.Vistors;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitApi {

    String BASE_URL = "https://284c-102-219-208-45.ngrok-free.app/api/v1/visitor/";

    @POST("register")
    Call<String> registerVisitor(@Body Vistors vistors); // Return type is Call<String>

    @POST("signout")
    Call<String> signOutVisitor(@Body SignOutRequest signOutRequest);
}
