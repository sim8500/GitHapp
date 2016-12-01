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

public class FavRepoAction implements Observable.OnSubscribe<List<RepoModel>> {

    private RepoModel repo;
    private FavReposStore favReposStore;
    private boolean isFavAction = true;

    public FavRepoAction(RepoModel repo, FavReposStore favReposStore, boolean isFavAction) {
        this.repo = repo;
        this.favReposStore = favReposStore;
        this.isFavAction = isFavAction;
    }

    @Override
    public void call(Subscriber<? super List<RepoModel>> subscriber) {
        List<RepoModel> result = isFavAction ?
                                    favReposStore.addFavRepo(repo) :
                                    favReposStore.unfavRepo(repo.url);

        if(result != null) {
            subscriber.onNext(result);
        }
        else {
            throw OnErrorThrowable.from(new Throwable("Null repos list received..."));
        }
    }
}
