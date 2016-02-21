package com.dev.sim8500.githapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dev.sim8500.githapp.models.RepoModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbernad on 20.02.16.
 */
public class RepoPagerAdapter extends FragmentPagerAdapter {

    public static interface OnRepoSetListener {
        public void onRepoSet(RepoModel repo);
    }

    public RepoPagerAdapter(FragmentManager fm, RepoModel repo) {
        super(fm);
        model = repo;
    }

    @Override
    public Fragment getItem(int position) {
        RepoFragment fragment = new RepoFragment();
        Bundle args = new Bundle();
        args.putParcelable(RepoFragment.REPO_MODEL_ARG, model);
        fragment.setArguments(args);
        repoSetListeners.add(new WeakReference<OnRepoSetListener>(fragment));
        return fragment;
    }

    public void setRepoModel(RepoModel repo) {
        model = repo;

        for(WeakReference<OnRepoSetListener> listenerRef : repoSetListeners) {
            if(listenerRef.get() != null) {
                listenerRef.get().onRepoSet(repo);
            }
        }
    }

    @Override
    public int getCount() {
        return NUM_FRAGS;
    }

    public CharSequence getPageTitle (int position) {
        return position > 0 ? "Details" : "Info";
    }

    private final static int NUM_FRAGS = 2;
    private RepoModel model;
    private List<WeakReference<OnRepoSetListener>> repoSetListeners = new ArrayList<>(NUM_FRAGS);
}
