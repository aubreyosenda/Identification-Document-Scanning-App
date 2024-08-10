package com.android.example.cameraxapp.Interfaces;

import com.android.example.cameraxapp.Model.SignOutRequest;
import com.android.example.cameraxapp.Model.Vistors;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitApi {
    @POST("register")
    Call<Vistors> vistors(@Body Vistors vistors);

    @POST("signout")
    Call<String> signOutVisitor(@Body SignOutRequest signOutRequest);
}
