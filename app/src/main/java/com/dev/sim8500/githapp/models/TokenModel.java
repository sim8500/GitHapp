package com.dev.sim8500.githapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sbernad on 18.12.15.
 */
public class TokenModel
{
    @SerializedName("access_token")
    public String accessToken;

    public String scope;

    @SerializedName("token_type")
    public String tokenType;

}
