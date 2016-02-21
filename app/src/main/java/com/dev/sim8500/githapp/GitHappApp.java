package com.dev.sim8500.githapp;

import android.app.Application;

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
}