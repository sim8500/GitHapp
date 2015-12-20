package com.dev.sim8500.githapp.services;

import com.dev.sim8500.githapp.models.AuthData;
import com.dev.sim8500.githapp.models.TokenModel;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by sbernad on 18.12.15.
 */
public interface GitHubAuthTokenService
{
    @Headers({"Accept: application/json", "User-Agent: GitHapp"})
    @POST("/login/oauth/access_token")
    Call<TokenModel> getAccessToken(@Body AuthData authData);
}
