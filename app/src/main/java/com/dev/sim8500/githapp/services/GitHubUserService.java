package com.dev.sim8500.githapp.services;

import com.dev.sim8500.githapp.models.UserModel;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by sbernad on 20.12.15.
 */
public interface GitHubUserService
{
    @GET("/user")
    public Call<UserModel> getUser();

    @GET("/users/{username}")
    public Observable<UserModel> getUserByLogin(@Path("username") String login);

    @GET("/users/{username}/followers")
    public Observable<List<UserModel>> getFollowersOfUser(@Path("username") String login);

    @GET("/users/{username}/followers")
    public Call<List<UserModel>> getFollowersListFor(@Path("username") String login, @Query("page")int page);
}
