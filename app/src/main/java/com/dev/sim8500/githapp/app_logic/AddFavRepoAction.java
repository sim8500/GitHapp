package com.dev.sim8500.githapp.app_logic;

import com.dev.sim8500.githapp.models.RepoModel;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.exceptions.OnErrorThrowable;

/**
 * Created by sbernad on 28.11.2016.
 */

public class AddFavRepoAction implements Observable.OnSubscribe<List<RepoModel>> {

    private RepoModel repo;
    private FavReposStore favReposStore;

    public AddFavRepoAction(RepoModel repo, FavReposStore favReposStore) {
        this.repo = repo;
        this.favReposStore = favReposStore;
    }

    @Override
    public void call(Subscriber<? super List<RepoModel>> subscriber) {
        List<RepoModel> result = favReposStore.addFavRepo(repo);
        if(result != null) {
            subscriber.onNext(result);
        }
        else {
            throw OnErrorThrowable.from(new Throwable("Null repos list received..."));
        }
    }
}
