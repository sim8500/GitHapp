package com.dev.sim8500.githapp.services;

import com.dev.sim8500.githapp.models.RepoModel;
import com.dev.sim8500.githapp.models.RepoSearchModel;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by sbernad on 18.12.15.
 */
public interface GitHubUserReposService {

    @GET("/user/repos")
    Call<List<RepoModel>> getUserRepos();

    @GET("/search/repositories")
    Observable<RepoSearchModel> getSearchReposResult(@Query("q") String query);

    @GET("/repos/{owner}/{repo}")
    Observable<RepoModel> getRepo(@Path("owner") String owner, @Path("repo") String repo);

    @GET("/users/{user}/repos")
    Observable<List<RepoModel>> getUserReposList(@Path("user") String owner);

}
