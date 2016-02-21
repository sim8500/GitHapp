package com.dev.sim8500.githapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dev.sim8500.githapp.models.IssueModel;
import com.dev.sim8500.githapp.models.RepoModel;
import com.dev.sim8500.githapp.presentation.RepoView;
import com.dev.sim8500.githapp.services.GitHubRepoIssuesService;
import com.dev.sim8500.githapp.services.GitHubUserReposService;

import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by sbernad on 19.12.15.
 */
public class RepoIssuesActivity extends AppCompatActivity implements RepoView.OnRepoChosenListener
{
    public static final String REPO_NAME = "com.dev.sim8500.githapp.REPO_NAME";
    public static final String REPO_OWNER = "com.dev.sim8500.githapp.REPO_OWNER";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        ((GitHappApp)getApplication()).inject(this);

        //progressBar = (ProgressBar)findViewById(R.id.progressBar);

        //recyclerContainer = (RecyclerView)findViewById(R.id.issue_container);
        recyclerContainer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        Intent currentIntent = this.getIntent();
        if(currentIntent.hasExtra(REPO_NAME) && currentIntent.hasExtra(REPO_OWNER))
        {
            owner = currentIntent.getStringExtra(REPO_OWNER);
            repo = currentIntent.getStringExtra(REPO_NAME);
        }

        if(!authReqMngr.hasTokenStored(this))
        {
            startActivity(new Intent(this, MainActivity.class));
        }

        boolean isReadyToLoadIssues = !(TextUtils.isEmpty(owner) || TextUtils.isEmpty(repo));

        if(isReadyToLoadIssues)
        {
            requestIssues();
        }
        else
        {
            requestRepos();
        }

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if(!TextUtils.isEmpty(repo))
        {
            ActionBar actionBar = getSupportActionBar();
            if(actionBar != null)
            {
                actionBar.setTitle(repo);
            }
        }
    }

    private void requestIssues()
    {
        progressBar.setVisibility(View.VISIBLE);
        authReqMngr.getService(GitHubRepoIssuesService.class)
                    .getRepoIssues(owner, repo, "all")
                    .enqueue(new Callback<List<IssueModel>>() {
                        @Override
                        public void onResponse(Response<List<IssueModel>> response, Retrofit retrofit) {
                            onIssuesReceived(response.body());
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            onRequestFailed();
                        }
                    });
    }

    private void requestRepos()
    {
        progressBar.setVisibility(View.VISIBLE);
        authReqMngr.getService(GitHubUserReposService.class)
                .getUserRepos()
                .enqueue(new Callback<List<RepoModel>>()
                {
                    @Override
                    public void onResponse(Response<List<RepoModel>> response, Retrofit retrofit)
                    {
                        List<RepoModel> resultList = response.body();
                        onUserReposReceived(resultList);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        onRequestFailed();
                    }
                });
    }

    @UiThread
    public void onIssuesReceived(List<IssueModel> issues)
    {
        if(issues != null)
        {
            issuesAdapter.clearItems();

            issuesAdapter.initAdapter(this, issues);
            recyclerContainer.setAdapter(issuesAdapter);
        }
        progressBar.setVisibility(View.GONE);
    }

    @UiThread
    public void onUserReposReceived(List<RepoModel> repos)
    {
        if(repos != null)
        {
            if(repos.size() > 1)
            {
                reposAdapter.clearItems();
                reposAdapter.initAdapter(this, this, repos);
                recyclerContainer.setAdapter(reposAdapter);
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
        Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT);
    }

    @Override
    public void onRepoChosen(String repoName, String repoOwner) {
        if(!TextUtils.isEmpty(repoName))
        {
            Intent repoIssuesIntent = new Intent(this, RepoIssuesActivity.class);
            repoIssuesIntent.putExtra(REPO_NAME, repoName);
            repoIssuesIntent.putExtra(REPO_OWNER, repoOwner);

            this.startActivity(repoIssuesIntent);
        }
    }

    @Inject protected AuthRequestsManager authReqMngr;
    private RecyclerView recyclerContainer;
    private ProgressBar progressBar;
    private ReposAdapter reposAdapter = new ReposAdapter();
    private String repo;
    private String owner;

    private IssuesAdapter issuesAdapter = new IssuesAdapter();

}
