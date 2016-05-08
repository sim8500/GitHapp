package com.dev.sim8500.githapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.dev.sim8500.githapp.app_logic.CommitBinder;
import com.dev.sim8500.githapp.app_logic.CommitPresenter;
import com.dev.sim8500.githapp.app_logic.GitHappCurrents;
import com.dev.sim8500.githapp.app_logic.RecyclerBaseAdapter;
import com.dev.sim8500.githapp.app_logic.RepoPagerAdapter;
import com.dev.sim8500.githapp.models.BranchModel;
import com.dev.sim8500.githapp.models.CommitModel;
import com.dev.sim8500.githapp.models.FileLineModel;
import com.dev.sim8500.githapp.models.RepoModel;
import com.dev.sim8500.githapp.services.GitHubRepoCommitsService;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by sbernad on 21.02.16.
 */
public class RepoCommitsFragment extends ContentFragment implements RepoPagerAdapter.OnRepoSetListener {

    protected RecyclerBaseAdapter<CommitModel, CommitPresenter> commitsAdapter = new RecyclerBaseAdapter<>(new CommitBinder());
    protected String repo;
    protected String owner;
    protected String defaultBranch;
    protected Spinner branchSpinner;
    protected List<BranchModel> branchModels;

    public static class BranchesSub extends Subscriber<List<BranchModel>> {

        protected WeakReference<RepoCommitsFragment> fragRef;

        public BranchesSub(RepoCommitsFragment fragment) {
            fragRef = new WeakReference<RepoCommitsFragment>(fragment);
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(List<BranchModel> branches) {
            if(fragRef.get() != null) {
                fragRef.get().onBranchesListReceived(branches);
            }
        }
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflated = super.onCreateView(inflater, container, savedInstanceState);

        if(inflated != null) {
            View header = inflater.inflate(R.layout.header_branches, container, false);
            this.branchSpinner = ButterKnife.findById(header, R.id.branch_spinner);

            this.headerLayout.addView(header);
        }

        return inflated;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //subscriber = new CommitsSub(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        RepoModel repo = appCurrents.getCurrent("Repo");
        onRepoSet(repo);
    }

    private void loadCommitsList(String sha) {

        authReqMngr.getObservableService(GitHubRepoCommitsService.class)
                .getRepoCommits(owner, repo, sha)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new CommitsSub(RepoCommitsFragment.this));
    }

    @Override
    public void onRepoSet(RepoModel repo) {

        this.repo = repo.name;
        this.owner = repo.owner.login;
        this.defaultBranch = repo.defaultBranch;
        loadBranches();
    }

    public void onCommitsReceived(List<CommitModel> commits) {

        if(commits != null) {
            commitsAdapter.clearItems();
            commitsAdapter.initAdapter(this.getContext(), commits);

            recyclerView.setAdapter(commitsAdapter);
        }
    }

    private void loadBranches() {

        authReqMngr.getObservableService(GitHubRepoCommitsService.class)
                .getRepoBranches(owner, repo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new BranchesSub(RepoCommitsFragment.this));
    }

    public void onBranchesListReceived(final List<BranchModel> branches) {
        if(branches != null) {
            branchModels = branches;
            if(TextUtils.isEmpty(defaultBranch)) {
                defaultBranch = "master";
            }

            boolean defaultFound = false;
            int defaultIndex = 0;
            ArrayList<String> branchNames = new ArrayList<>(branchModels.size());
            for (BranchModel bm : branchModels) {
                branchNames.add(bm.name);

                if(!defaultFound && bm.name.toLowerCase().equals(defaultBranch))
                {
                    defaultFound = true;
                }
                else defaultIndex++;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), R.layout.row_repo_branch, R.id.name_txtView, branchNames);
            branchSpinner.setAdapter(adapter);
            branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position >= 0 && position < branchModels.size()) {
                        loadCommitsList(branchModels.get(position).commit.sha);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            branchSpinner.setSelection(defaultIndex);
        }
    }
}
