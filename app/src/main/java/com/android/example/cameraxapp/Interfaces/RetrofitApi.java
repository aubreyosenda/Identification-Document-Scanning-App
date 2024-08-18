package com.android.example.cameraxapp.Interfaces;

import com.android.example.cameraxapp.Model.Organization;
import com.android.example.cameraxapp.Model.SecurityPersonelDetails;
import com.android.example.cameraxapp.Model.SignOutRequest;
import com.android.example.cameraxapp.Model.Visitors;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitApi {

    String BASE_URL = "http://sidar.us-east-2.elasticbeanstalk.com/api/v1/";

    @POST("visitor/register")
    Call<String> registerVisitor(@Body Visitors visitors); // Return type is Call<String>

    @POST("visitor/signout")
    Call<String> signOutVisitor(@Body SignOutRequest signOutRequest);

    @GET("company/building/organizations/list")
    Call<List<Organization>> getOrganizations(@Query("BuildingID") String buildingId);

    @GET("company/personnel/login")
    Call<SecurityPersonelDetails> login(@Query("phoneNumber") String phoneNumber, @Query("nationalIdNumber") String nationalIdNumber);


}
