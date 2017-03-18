package com.dev.sim8500.githapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.dev.sim8500.githapp.app_logic.FavReposStore;
import com.dev.sim8500.githapp.app_logic.FileDownloadAction;
import com.dev.sim8500.githapp.app_logic.RecyclerBaseAdapter;
import com.dev.sim8500.githapp.app_logic.RepoEntryBinder;
import com.dev.sim8500.githapp.app_logic.RepoEntryPresenter;
import com.dev.sim8500.githapp.models.FileLineModel;
import com.dev.sim8500.githapp.models.RepoModel;
import com.dev.sim8500.githapp.models.RepoSearchModel;
import com.dev.sim8500.githapp.services.GitHubUserReposService;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func0;
import rx.functions.Func1;
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
            if (fragmentRef.get() != null) {
                fragmentRef.get().progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public void onError(Throwable e) {
            if (fragmentRef.get() != null) {
                fragmentRef.get().progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public void onNext(RepoSearchModel repoSearchModel) {
            if (fragmentRef.get() != null) {
                fragmentRef.get().onReposListReceived(repoSearchModel.items);
            }
        }
    }

    public static class UserReposListSub extends Subscriber<List<RepoModel>> {

        WeakReference<ReposListFragment> fragmentRef;

        public UserReposListSub(ReposListFragment frag) {
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
        public void onNext(List<RepoModel> reposList) {
            if(fragmentRef.get() != null) {
                fragmentRef.get().onReposListReceived(reposList);
            }
        }
    }

    

    protected RecyclerBaseAdapter<RepoModel, RepoEntryPresenter> reposAdapter = new RecyclerBaseAdapter<>(new RepoEntryBinder(true));

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GitHappApp.getInstance().inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();

        if(args != null && !TextUtils.isEmpty(args.getString(GitHappApp.USER_PROFILE_MODEL))) {

            authReqMngr.getObservableService(GitHubUserReposService.class)
                        .getUserReposList(args.getString(GitHappApp.USER_PROFILE_MODEL))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .unsubscribeOn(Schedulers.io())
                        .subscribe(new UserReposListSub(this));
        }
    }


}
