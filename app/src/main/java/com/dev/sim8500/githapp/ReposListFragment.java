package com.dev.sim8500.githapp;

import android.view.View;

import com.dev.sim8500.githapp.app_logic.RecyclerBaseAdapter;
import com.dev.sim8500.githapp.app_logic.RepoEntryBinder;
import com.dev.sim8500.githapp.app_logic.RepoEntryPresenter;
import com.dev.sim8500.githapp.models.RepoModel;
import com.dev.sim8500.githapp.models.RepoSearchModel;
import com.dev.sim8500.githapp.services.GitHubUserReposService;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sbernad on 16.04.16.
 */
public class ReposListFragment extends ContentFragment {

    public static class ReposListSub extends Subscriber<RepoSearchModel> {

        WeakReference<ReposListFragment> fragmentRef;

        public ReposListSub(ReposListFragment frag) {
            fragmentRef = new WeakReference<ReposListFragment>(frag);
        }

        @Override
        public void onCompleted() {
            if(fragmentRef.get() != null) {
                fragmentRef.get().progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public void onError(Throwable e) {
            if(fragmentRef.get() != null) {
                fragmentRef.get().progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public void onNext(RepoSearchModel repoSearchModel) {
            if(fragmentRef.get() != null) {
                fragmentRef.get().onReposListReceived(repoSearchModel.items);
            }
        }
    }

    protected RecyclerBaseAdapter<RepoModel, RepoEntryPresenter> reposAdapter = new RecyclerBaseAdapter<>(new RepoEntryBinder());

    public void runQuery(String query) {

        authReqMngr.getObservableService(GitHubUserReposService.class)
                    .getSearchReposResult(query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new ReposListSub(this));

        progressBar.setVisibility(View.VISIBLE);
    }

    protected void onReposListReceived(List<RepoModel> reposList) {
        reposAdapter.clearItems();

        reposAdapter.initAdapter(this.getContext(), reposList);
        recyclerView.setAdapter(reposAdapter);
    }

}
