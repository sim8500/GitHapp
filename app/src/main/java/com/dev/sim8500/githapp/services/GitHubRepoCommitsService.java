package com.dev.sim8500.githapp.services;

import com.dev.sim8500.githapp.models.CommitModel;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by sbernad on 21.02.16.
 */
public interface GitHubRepoCommitsService {
    @GET("/repos/{owner}/{repo}/commits")
    Call<List<CommitModel>> getRepoCommits(@Path("owner") String owner, @Path("repo") String repo);
}
