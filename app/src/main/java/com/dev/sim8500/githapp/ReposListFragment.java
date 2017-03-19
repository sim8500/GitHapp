package com.dev.sim8500.githapp;

import android.os.Bundle;
import android.support.annotation.UiThread;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.dev.sim8500.githapp.app_logic.AuthRequestsManager;
import com.dev.sim8500.githapp.app_logic.FavReposStore;
import com.dev.sim8500.githapp.app_logic.FileDownloadAction;
import com.dev.sim8500.githapp.app_logic.RecyclerBaseAdapter;
import com.dev.sim8500.githapp.app_logic.RecyclerViewLoadMoreListener;
import com.dev.sim8500.githapp.app_logic.RepoEntryBinder;
import com.dev.sim8500.githapp.app_logic.RepoEntryPresenter;
import com.dev.sim8500.githapp.models.FileLineModel;
import com.dev.sim8500.githapp.models.RepoModel;
import com.dev.sim8500.githapp.models.RepoSearchModel;
import com.dev.sim8500.githapp.services.GitHubUserReposService;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
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
public class ReposListFragment extends ContentFragment
                                implements RecyclerViewLoadMoreListener.IRequestHandler {

    public static class ReposListSub implements Callback<RepoSearchModel> {

        WeakReference<ReposListFragment> fragmentRef;

        public ReposListSub(ReposListFragment frag) {
            fragmentRef = new WeakReference<ReposListFragment>(frag);
        }

        @Override
        public void onResponse(Response<RepoSearchModel> response, Retrofit retrofit) {
            if (fragmentRef.get() != null) {
                int nextPage = AuthRequestsManager.extractNextPageNumberFromResponseHeaders(response.headers());
                fragmentRef.get().onReposListReceived(response.body().items, nextPage);
            }
        }

        @Override
        public void onFailure(Throwable t) {

        }
    }

    public static class UserReposListSub implements Callback<List<RepoModel>> {

        WeakReference<ReposListFragment> fragmentRef;

        public UserReposListSub(ReposListFragment frag) {
            fragmentRef = new WeakReference<ReposListFragment>(frag);
        }

        @Override
        public void onResponse(Response<List<RepoModel>> response, Retrofit retrofit) {
            if (fragmentRef.get() != null) {
                int nextPage = AuthRequestsManager.extractNextPageNumberFromResponseHeaders(response.headers());
                fragmentRef.get().onReposListReceived(response.body(), nextPage);
            }
        }

        @Override
        public void onFailure(Throwable t) {

        }

    }

    protected RecyclerBaseAdapter<RepoModel, RepoEntryPresenter> reposAdapter = new RecyclerBaseAdapter<>(new RepoEntryBinder(true));

    public void runQuery(String query) {

        this.query = query;
        this.queryFirstRun = true;
        this.queryBasedList = true;

        authReqMngr.getService(GitHubUserReposService.class)
                    .getPaginatedSearchReposResult(query, nextPage)
                    .enqueue(new ReposListSub(this));

        progressBar.setVisibility(View.VISIBLE);
    }

    @UiThread
    public void onReposListReceived(List<RepoModel> reposList, int nextPage) {

        this.nextPage = nextPage;

        if((queryBasedList && queryFirstRun) || reposAdapter.getItemCount() == 0) {
            reposAdapter.clearItems();

            reposAdapter.initAdapter(this.getContext(), reposList);
            recyclerView.setAdapter(reposAdapter);
            progressBar.setVisibility(View.GONE);
            queryFirstRun = false;
        }
        else {
            reposAdapter.initAdapter(this.getContext(), reposList);
            reposAdapter.notifyDataSetChanged();
            footerProgress.setVisibility(View.GONE);
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

        recyclerView.addOnScrollListener(new RecyclerViewLoadMoreListener(this));

        Bundle args = getArguments();

        if(args != null && !TextUtils.isEmpty(args.getString(GitHappApp.USER_PROFILE_MODEL))) {

            queryBasedList = false;
            username = args.getString(GitHappApp.USER_PROFILE_MODEL);
            authReqMngr.getService(GitHubUserReposService.class)
                        .getUserReposList(username, nextPage)
                        .enqueue(new UserReposListSub(this));
        }
    }

    @Override
    public void doLoadMore() {
        if(nextPage > 1) {
            if(queryBasedList) {
                authReqMngr.getService(GitHubUserReposService.class)
                        .getPaginatedSearchReposResult(query, nextPage)
                        .enqueue(new ReposListSub(this));
            }
            else if(!TextUtils.isEmpty(username)) {
                authReqMngr.getService(GitHubUserReposService.class)
                            .getUserReposList(username, nextPage)
                            .enqueue(new UserReposListSub(this));
            }

            footerProgress.setVisibility(View.VISIBLE);
        }
    }

    protected boolean queryFirstRun = true;
    protected String query;
    protected boolean queryBasedList = true;

    protected int nextPage = 1;
    protected String username;
}
