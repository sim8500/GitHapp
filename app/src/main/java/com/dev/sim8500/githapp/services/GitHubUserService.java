package com.dev.sim8500.githapp.services;

import com.dev.sim8500.githapp.models.UserModel;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by sbernad on 20.12.15.
 */
public interface GitHubUserService
{
    @GET("/user")
    public Call<UserModel> getUser();
}
