package com.dev.sim8500.githapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.dev.sim8500.githapp.app_logic.AuthRequestsManager;
import com.dev.sim8500.githapp.app_logic.RecyclerBaseAdapter;
import com.dev.sim8500.githapp.app_logic.UserIdBinder;
import com.dev.sim8500.githapp.app_logic.UserIdPresenter;
import com.dev.sim8500.githapp.models.UserModel;
import com.dev.sim8500.githapp.services.GitHubUserService;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.net.Uri.parse;

/**
 * Created by sbernad on 16/03/2017.
 */

public class UserListFragment extends ContentFragment {

    protected RecyclerBaseAdapter<UserModel, UserIdPresenter>  usersAdapter = new RecyclerBaseAdapter<>(new UserIdBinder());

    protected boolean isListInited = false;
    protected int nextPage = -1;
    protected String userLogin;

    public static class UsersListSub extends Subscriber<List<UserModel>> {

        WeakReference<UserListFragment> fragmentRef;

        public UsersListSub(UserListFragment fragment) {
            fragmentRef = new WeakReference<UserListFragment>(fragment);
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
                Toast.makeText(fragmentRef.get().getContext(), "Something went wrong with loading users list.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onNext(List<UserModel> userModels) {
            if(fragmentRef.get() != null) {
                fragmentRef.get().setUpUsers(userModels);
            }
        }
    }

    public static class UsersListCallback implements Callback<List<UserModel>> {

        WeakReference<UserListFragment> fragmentRef;

        public UsersListCallback(UserListFragment fragment) {
            fragmentRef = new WeakReference<UserListFragment>(fragment);
        }

        @Override
        public void onResponse(Response<List<UserModel>> response, Retrofit retrofit) {
            if(fragmentRef.get() != null) {

                fragmentRef.get().setUpUsers(response.body());

                fragmentRef.get().setNextPage(-1);

                int nextPage = AuthRequestsManager.extractNextPageNumberFromResponseHeaders(response.headers());

                if(nextPage > 0) {
                    fragmentRef.get().setNextPage(nextPage);
                }
            }
        }

        @Override
        public void onFailure(Throwable t) {
            if(fragmentRef.get() != null) {
                fragmentRef.get().handleLoadFailure(t);
            }
        }
    }
    @Override
    public void onStart() {

        super.onStart();

        isListInited = false;
        recyclerView.setAdapter(usersAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(nextPage > 0) {
                    LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int visible = llm.findFirstVisibleItemPosition() + llm.getChildCount();
                    int allItems = llm.getItemCount();

                    if (visible >= allItems) {
                        authReqMngr.getService(GitHubUserService.class)
                                .getFollowersListFor(userLogin, nextPage)
                                .enqueue(new UsersListCallback(UserListFragment.this));
                        footerProgress.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });

        Bundle args = getArguments();

        if(args != null) {
            userLogin = args.getString(GitHappApp.SHOW_FOLLOWERS_OF_USER);

            if(!TextUtils.isEmpty(userLogin)) {
                authReqMngr.getService(GitHubUserService.class)
                            .getFollowersListFor(userLogin, 1)
                            .enqueue(new UsersListCallback(this));
            }
        }
    }

    @UiThread
    public void setUpUsers(List<UserModel> users) {

        if(!isListInited) {
            usersAdapter.clearItems();
            usersAdapter.initAdapter(this.getContext(), users);
            isListInited = true;
        }
        else {
            footerProgress.setVisibility(View.GONE);
            usersAdapter.initAdapter(this.getContext(), users);
            usersAdapter.notifyDataSetChanged();
        }

    }

    @UiThread
    public void handleLoadFailure(Throwable t) {
        Toast.makeText(getContext(), "Something went wrong with loading users list.", Toast.LENGTH_SHORT).show();
    }

    protected void setNextPage(int page) {
        this.nextPage = page;
    }


}
