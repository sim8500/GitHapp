package com.dev.sim8500.githapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sbernad on 19.12.15.
 */
public class AuthData
{
    @SerializedName("client_id")
    public String clientId;

    @SerializedName("client_secret")
    public String clientSecret;

    @SerializedName("code")
    public String code;

    @SerializedName("redirect_uri")
    public String redirect;

    public AuthData(String clientId, String clientSecret, String code)
    {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.code = code;
    }

}
