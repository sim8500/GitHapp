package com.dev.sim8500.githapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.dev.sim8500.githapp.app_logic.GitHappCurrents;
import com.dev.sim8500.githapp.app_logic.RecyclerBaseAdapter;
import com.dev.sim8500.githapp.app_logic.TreeEntryBinder;
import com.dev.sim8500.githapp.app_logic.TreeEntryPresenter;
import com.dev.sim8500.githapp.models.RepoModel;
import com.dev.sim8500.githapp.models.TreeEntryModel;
import com.dev.sim8500.githapp.models.TreeModel;
import com.dev.sim8500.githapp.services.GitHubRepoCommitsService;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sbernad on 16.03.16.
 */
public class CommitTreeFragment extends ContentFragment {

    public static class TreeSub extends Subscriber<TreeModel> {

        WeakReference<CommitTreeFragment> fragmentRef;

        public TreeSub(CommitTreeFragment frag) {
            fragmentRef = new WeakReference<CommitTreeFragment>(frag);
        }

        @Override
        public void onCompleted() {
            Log.d("CommitTreeFragment", "onCompleted()");
        }

        @Override
        public void onError(Throwable e) {
            Log.e("CommitTreeFragment", e.toString());
        }

        @Override
        public void onNext(TreeModel treeModel) {
            if(fragmentRef.get() != null) {
                if(treeModel != null && treeModel.tree != null && !treeModel.tree.isEmpty()) {
                    fragmentRef.get().onEntriesReceived(treeModel.tree);
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GitHappApp.getInstance().inject(this);

    }

    @Override
    public void onStart() {
        super.onStart();

        loadEntries();
    }

    protected void onEntriesReceived(List<TreeEntryModel> entries) {

        Collections.sort(entries, new Comparator<TreeEntryModel>() {
            @Override
            public int compare(TreeEntryModel lhs, TreeEntryModel rhs) {
                return lhs.type.getSortValue() - rhs.type.getSortValue();
            }
        });
        entriesAdapter.clearItems();
        entriesAdapter.initAdapter(this.getContext(), entries);
        recyclerView.setAdapter(entriesAdapter);
    }

    private void loadEntries() {
        Log.d("CommitTreeFragment", "Params valid");

        RepoModel repo = appCurrents.getCurrent("Repo");
        String commitSha = appCurrents.getCurrent("CommitSha");

        if(repo != null && !TextUtils.isEmpty(commitSha)) {
            authReqMngr.getObservableService(GitHubRepoCommitsService.class)
                    .getCommitTree(repo.owner.login, repo.name, commitSha)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new TreeSub(CommitTreeFragment.this));
        }
    }

    @Inject
    protected GitHappCurrents appCurrents;
    protected RecyclerBaseAdapter<TreeEntryModel, TreeEntryPresenter> entriesAdapter = new RecyclerBaseAdapter<>(new TreeEntryBinder());
}
