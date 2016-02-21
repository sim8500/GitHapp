package com.dev.sim8500.githapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.dev.sim8500.githapp.models.RepoModel;
import com.dev.sim8500.githapp.presentation.RepoView;
import com.dev.sim8500.githapp.services.GitHubUserReposService;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by sbernad on 06.02.16.
 */
public class ReposListFragment extends ContentFragment implements RepoView.OnRepoChosenListener {

    public static final String REPO_NAME = "com.dev.sim8500.githapp.REPO_NAME";
    public static final String REPO_OWNER = "com.dev.sim8500.githapp.REPO_OWNER";

    @Inject
    protected AuthRequestsManager authReqMngr;
    private ReposAdapter reposAdapter = new ReposAdapter();
    private ReposListCallback callback;

    public static class ReposListCallback implements Callback<List<RepoModel>> {

        WeakReference<ReposListFragment> fragmentRef;

        public ReposListCallback(ReposListFragment frag) {
            fragmentRef = new WeakReference<ReposListFragment>(frag);
        }

        @Override
        public void onResponse(Response<List<RepoModel>> response, Retrofit retrofit) {
            if(fragmentRef.get() != null) {
                fragmentRef.get().onUserReposReceived(response.body());
            }
        }

        @Override
        public void onFailure(Throwable t) {
            if(fragmentRef.get() != null) {
                fragmentRef.get().onRequestFailed();
            }
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        GitHappApp.getInstance().inject(this);
        callback = new ReposListCallback(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        requestRepos();
    }

    private void requestRepos()
    {
        progressBar.setVisibility(View.VISIBLE);
        authReqMngr.getService(GitHubUserReposService.class)
                .getUserRepos()
                .enqueue(callback);
    }


    @UiThread
    public void onUserReposReceived(List<RepoModel> repos)
    {
        if(repos != null)
        {
            if(repos.size() > 1)
            {
                reposAdapter.clearItems();
                reposAdapter.initAdapter(this.getContext(), this, repos);
                recyclerView.setAdapter(reposAdapter);
            }
            else
            {
                onRepoChosen(repos.get(0).name, repos.get(0).owner.login);
            }
        }
        progressBar.setVisibility(View.GONE);
    }

    @UiThread
    public void onRequestFailed()
    {
        Toast.makeText(this.getActivity(), "Something went wrong.", Toast.LENGTH_SHORT);
    }

    @Override
    public void onRepoChosen(String repoName, String repoOwner) {
        if(!TextUtils.isEmpty(repoName))
        {
            Intent repoIssuesIntent = new Intent(this.getContext(), RepoIssuesActivity.class);
            repoIssuesIntent.putExtra(REPO_NAME, repoName);
            repoIssuesIntent.putExtra(REPO_OWNER, repoOwner);

            this.startActivity(repoIssuesIntent);
        }
    }
}
