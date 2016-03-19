package com.dev.sim8500.githapp;

import javax.inject.Singleton;

import com.dev.sim8500.githapp.app_logic.AuthRequestsManager;
import com.dev.sim8500.githapp.app_logic.CommitPresenter;
import com.dev.sim8500.githapp.app_logic.GitHappCurrents;
import com.dev.sim8500.githapp.app_logic.TreeEntryPresenter;
import com.dev.sim8500.githapp.presentation.UserView;

import dagger.Module;
import dagger.Provides;
/**
 * Created by sbernad on 11.01.16.
 */

@Module(
        injects = {
                MainActivity.class,
                SingleIssueActivity.class,
                ContainerActivity.class,
                ContentFragment.class,
                RepoCommitsFragment.class,
                CommitTreeFragment.class,
                UserView.class,
                CommitPresenter.class,
                TreeEntryPresenter.class
        }
)
public final class GitHappModule {

    @Provides @Singleton
    AuthRequestsManager provideAuthRequestManager()
    {
        return AuthRequestsManager.getInstance();
    }

    @Provides @Singleton
    GitHappCurrents provideGitHappCurrents() {
        return GitHappCurrents.getInstance();
    }
}
