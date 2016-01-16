package com.dev.sim8500.githapp;

import javax.inject.Singleton;

import com.dev.sim8500.githapp.presentation.UserView;

import dagger.Module;
import dagger.Provides;
/**
 * Created by sbernad on 11.01.16.
 */

@Module(
        injects = {
                MainActivity.class,
                RepoIssuesActivity.class,
                SingleIssueActivity.class,
                UserView.class
        }
)
public final class GitHappModule {

    @Provides @Singleton AuthRequestsManager provideAuthRequestManager()
    {
        return AuthRequestsManager.getInstance();
    }
}
