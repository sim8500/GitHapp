package com.dev.sim8500.githapp;

import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.text.TextUtils;

import com.dev.sim8500.githapp.models.CommitModel;
import com.dev.sim8500.githapp.models.RepoModel;
import com.dev.sim8500.githapp.services.GitHubRepoCommitsService;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by sbernad on 21.02.16.
 */
public class RepoCommitsFragment extends ContentFragment implements RepoPagerAdapter.OnRepoSetListener {

    public static class CommitsCallback implements Callback<List<CommitModel>> {

        WeakReference<RepoCommitsFragment> fragmentRef;

        public CommitsCallback(RepoCommitsFragment frag) {
            fragmentRef = new WeakReference<RepoCommitsFragment>(frag);
        }

        @Override
        public void onResponse(Response<List<CommitModel>> response, Retrofit retrofit) {
            if(fragmentRef.get() != null) {
                fragmentRef.get().onCommitsReceived(response.body());
            }
        }

        @Override
        public void onFailure(Throwable t) {
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callback = new CommitsCallback(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!areRepoParamsValid())
        {
            if(getArguments() != null) {
                repo = getArguments().getString(GitHappApp.REPO_NAME);
                owner = getArguments().getString(GitHappApp.REPO_OWNER);
            }
        }

        loadCommitsList();
    }

    private boolean areRepoParamsValid() {

        return !(TextUtils.isEmpty(repo) || TextUtils.isEmpty(owner));
    }

    private void loadCommitsList() {

        if(areRepoParamsValid()) {
            authReqMngr.getService(GitHubRepoCommitsService.class)
                    .getRepoCommits(owner, repo)
                    .enqueue(callback);
        }
    }

    @Override
    public void onRepoSet(RepoModel repo) {

        this.repo = repo.name;
        this.owner = repo.owner.login;

        loadCommitsList();
    }

    public void onCommitsReceived(List<CommitModel> commits) {

        if(commits != null) {
            commitsAdapter.clearItems();
            commitsAdapter.initAdapter(this.getContext(), commits);

            recyclerView.setAdapter(commitsAdapter);
        }
    }

    protected CommitsAdapter commitsAdapter = new CommitsAdapter();
    protected String repo;
    protected String owner;
    protected CommitsCallback callback;
}
