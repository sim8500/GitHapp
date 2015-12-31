package com.dev.sim8500.githapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;

import com.dev.sim8500.githapp.models.AuthData;
import com.dev.sim8500.githapp.models.TokenModel;
import com.dev.sim8500.githapp.services.GitHubAuthTokenService;
import com.dev.sim8500.githapp.services.GitHubRepoIssuesService;
import com.dev.sim8500.githapp.services.GitHubUserReposService;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;

import java.io.IOException;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by sbernad on 19.12.15.
 */
public class AuthRequestsManager {

    public static AuthRequestsManager getInstance()
    {
        return instance;
    }

    public interface AuthTokenRequestListener
    {
        void onTokenReceived();
        void onTokenFailed();
    }

    public Uri getOAuthURL()
    {
        Uri.Builder builder = Uri.parse(OAUTH_URL).buildUpon();
        builder.appendQueryParameter("client_id", CLIENT_ID);

        return builder.build();
    }

    public boolean hasTokenStored(Context ctx)
    {
        if(!TextUtils.isEmpty(accessToken))
            return true;

        return readTokenFromPrefs(ctx);
    }

    public boolean obtainAuthToken(String callbackURL, final Context ctx, final AuthTokenRequestListener listener)
    {
        Uri destURL = Uri.parse(callbackURL);

        // We hijack the GET request to extract the OAuth parameters
        boolean foundCode = (destURL.getQueryParameter("code") != null);
        if (foundCode) {
            // the GET request contains an authorization code
            String accessCode = destURL.getQueryParameter("code");

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GITHUB_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                    .build();

            GitHubAuthTokenService service = retrofit.create(GitHubAuthTokenService.class);
            Call<TokenModel> tokenModelCall = service.getAccessToken(new AuthData(CLIENT_ID, CLIENT_SECRET, accessCode));

            tokenModelCall.enqueue(new Callback<TokenModel>()
            {
                @Override
                public void onResponse(Response<TokenModel> response, Retrofit retrofit)
                {

                    storeTokenInPrefs(ctx, response.body().accessToken);

                    listener.onTokenReceived();
                }

                @Override
                public void onFailure(Throwable t) {
                    listener.onTokenFailed();
                }
            });
        }

        return foundCode;
    }

    public <T> T getService(Class<T> service)
    {
        return prepareAuthRetrofitInstance().create(service);
    }

    public boolean logOutUser(Context ctx)
    {
        if(!TextUtils.isEmpty(accessToken))
        {
            return storeTokenInPrefs(ctx, null);
        }

        return false;
    }

    private Retrofit prepareAuthRetrofitInstance()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GITHUB_API_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();

        appendAuthHeaders(retrofit);

        return retrofit;
    }

    private void appendAuthHeaders(Retrofit retrofit)
    {
        retrofit.client().interceptors().add(new Interceptor()
        {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException
            {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("User-Agent", "GitHapp")
                        .addHeader("Authorization", String.format("token %s", accessToken)).build();

                return chain.proceed(newRequest);
            }
        });


    }

    private boolean readTokenFromPrefs(Context ctx)
    {

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(AUTH_TOKEN))
        {
            accessToken = new String(Base64.decode(sharedPreferences.getString(AUTH_TOKEN, "").getBytes(), Base64.DEFAULT));
        }

        return (!TextUtils.isEmpty(accessToken));
    }

    private boolean storeTokenInPrefs(Context ctx, String token)
    {
        accessToken = token;
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(TextUtils.isEmpty(accessToken))
        {
            editor.remove(AUTH_TOKEN);
        }
        else
        {
            editor.putString(AUTH_TOKEN, Base64.encodeToString(token.getBytes(), Base64.DEFAULT));
        }

        return editor.commit();
    }

    private static final String CLIENT_ID = "48879b01722691b21da0";
    private static final String CLIENT_SECRET = "44e2fe5836ba69a4c0d0e12839fa125e6f79ccd0";
    private static final String OAUTH_URL = "https://github.com/login/oauth/authorize";
    private static final String GITHUB_BASE_URL = "https://github.com";
    private static final String GITHUB_API_URL = "https://api.github.com";
    private static final String SHARED_PREFERENCES = "com.dev.sim8500.GitHapp.shared_prefs";
    private static final String AUTH_TOKEN = "com.dev.sim8500.GitHapp.auth_token";

    private static final AuthRequestsManager instance = new AuthRequestsManager();

    private String accessToken;

    private AuthRequestsManager() { }

}
