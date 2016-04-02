package com.dev.sim8500.githapp.services;

import com.dev.sim8500.githapp.models.CommitModel;
import com.dev.sim8500.githapp.models.DetailedCommitModel;
import com.dev.sim8500.githapp.models.TreeModel;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by sbernad on 21.02.16.
 */
public interface GitHubRepoCommitsService {
    @GET("/repos/{owner}/{repo}/commits")
    Observable<List<CommitModel>> getRepoCommits(@Path("owner") String owner, @Path("repo") String repo);

    @GET("/repos/{owner}/{repo}/git/trees/{sha}")
    Observable<TreeModel> getCommitTree(@Path("owner") String owner, @Path("repo") String repo, @Path("sha") String sha);

    @GET("/repos/{owner}/{repo}/commits/{sha}")
    Observable<DetailedCommitModel> getDetailedCommit(@Path("owner") String owner, @Path("repo") String repo, @Path("sha") String sha);
}
