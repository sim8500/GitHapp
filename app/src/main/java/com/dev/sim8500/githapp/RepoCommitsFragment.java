package com.dev.sim8500.githapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.dev.sim8500.githapp.app_logic.CommitBinder;
import com.dev.sim8500.githapp.app_logic.CommitPresenter;
import com.dev.sim8500.githapp.app_logic.RecyclerBaseAdapter;
import com.dev.sim8500.githapp.app_logic.RepoPagerAdapter;
import com.dev.sim8500.githapp.models.CommitModel;
import com.dev.sim8500.githapp.models.RepoModel;
import com.dev.sim8500.githapp.services.GitHubRepoCommitsService;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sbernad on 21.02.16.
 */
public class RepoCommitsFragment extends ContentFragment implements RepoPagerAdapter.OnRepoSetListener {

    public static class CommitsSub extends Subscriber<List<CommitModel>> {

        WeakReference<RepoCommitsFragment> fragmentRef;

        public CommitsSub(RepoCommitsFragment frag) {
            fragmentRef = new WeakReference<>(frag);
        }

        @Override
        public void onCompleted() {
            Log.d("RepoCommitsFragment", "onCompleted()");
        }

        @Override
        public void onError(Throwable e) {
            Log.e("RepoCommitsFragment", String.format("onError(): %s", e.toString()));
        }

        @Override
        public void onNext(List<CommitModel> commitModels) {

            Log.d("RepoCommitsFragment", "onNext()");
            if(fragmentRef.get() != null) {
                fragmentRef.get().onCommitsReceived(commitModels);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //subscriber = new CommitsSub(this);
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
            authReqMngr.getObservableService(GitHubRepoCommitsService.class)
                    .getRepoCommits(owner, repo)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new CommitsSub(RepoCommitsFragment.this));
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

    protected RecyclerBaseAdapter<CommitModel, CommitPresenter> commitsAdapter = new RecyclerBaseAdapter<>(new CommitBinder());
    protected String repo;
    protected String owner;
}
