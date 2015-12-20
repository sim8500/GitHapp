package com.dev.sim8500.githapp.services;

import com.dev.sim8500.githapp.models.IssueModel;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by sbernad on 19.12.15.
 */
public interface GitHubRepoIssuesService
{
    @GET("/repos/{owner}/{repo}/issues")
    Call<List<IssueModel>> getRepoIssues(@Path("owner") String owner, @Path("repo") String repo, @Query("state") String withState);
}
