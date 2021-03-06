package com.dev.sim8500.githapp;

import android.app.Application;

import com.dev.sim8500.githapp.app_logic.FavReposStore;

import com.activeandroid.ActiveAndroid;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * Created by sbernad on 11.01.16.
 */
public class GitHappApp extends Application {
    private ObjectGraph graph;
    private static GitHappApp instance;

    @Override public void onCreate() {
        super.onCreate();
        instance = this;
        graph = ObjectGraph.create(getModules().toArray());

        FavReposStore.getInstance().getFavRepos();
        ActiveAndroid.initialize(this);
    }

    protected List<Object> getModules() {
        return Arrays.asList((Object)new GitHappModule());
    }

    public void inject(Object object) {
        graph.inject(object);
    }

    public static GitHappApp getInstance() {
        return instance;
    }

    public static final String REPO_NAME = "com.dev.sim8500.githapp.REPO_NAME";
    public static final String REPO_OWNER = "com.dev.sim8500.githapp.REPO_OWNER";
    public static final String COMMIT_SHA = "com.dev.sim8500.githapp.COMMIT_SHA";
    public static final String SHOW_COMMIT_TREE = "com.dev.sim8500.githapp.SHOW_COMMIT_TREE";
    public static final String SHOW_COMMIT_FILES = "com.dev.sim8500.githapp.SHOW_COMMIT_FILES";
    public static final String SHOW_FILE_CONTENT = "com.dev.sim8500.githapp.SHOW_FILE_CONTENT";
    public static final String SHOW_SINGLE_REPO = "com.dev.sim8500.githapp.SHOW_SINGLE_REPO";
    public static final String SHOW_SINGLE_REPO_OWNER = "com.dev.sim8500.githapp.SHOW_SINGLE_REPO_OWNER";
    public static final String SHOW_SINGLE_REPO_NAME = "com.dev.sim8500.githapp.SHOW_SINGLE_REPO_NAME";
    public static final String FILE_CONTENT_DOWNLOAD_DATA = "com.dev.sim8500.githapp.SHOW_SINGLE_REPO";
    public static final String SHOW_FAV_REPOS_LIST = "com.dev.sim8500.githapp.SHOW_FAV_REPOS_LIST";
    public static final String USER_PROFILE_MODEL = "com.dev.sim8500.githapp.USER_PROFILE_MODEL";
    public static final String SHOW_USER_REPOS_LIST = "com.dev.sim8500.githapp.SHOW_USER_REPOS_LIST";
    public static final String SHOW_FOLLOWERS_OF_USER = "com.dev.sim8500.githapp.SHOW_FOLLOWERS_OF_USER";
    public static final String SHOW_USER_FOLLOWING = "com.dev.sim8500.githapp.SHOW_USER_FOLLOWING";

}