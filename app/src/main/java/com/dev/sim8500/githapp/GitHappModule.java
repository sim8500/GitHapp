package com.dev.sim8500.githapp;

import javax.inject.Singleton;

import com.dev.sim8500.githapp.app_logic.AuthRequestsManager;
import com.dev.sim8500.githapp.app_logic.CommitPresenter;
import com.dev.sim8500.githapp.app_logic.FavReposStore;
import com.dev.sim8500.githapp.app_logic.FileEntryPresenter;
import com.dev.sim8500.githapp.app_logic.GitHappCurrents;
import com.dev.sim8500.githapp.app_logic.RepoEntryPresenter;
import com.dev.sim8500.githapp.app_logic.TreeEntryPresenter;
import com.dev.sim8500.githapp.presentation.UserPanelView;

import dagger.Module;
import dagger.Provides;
/**
 * Created by sbernad on 11.01.16.
 */

@Module(
        injects = {
                    MainActivity.class,
                    SingleIssueActivity.class,
                    RepoBrowserActivity.class,
                    FrameActivity.class,
                    ContentFragment.class,
                    RepoCommitsFragment.class,
                    CommitTreeFragment.class,
                    UserPanelView.class,
                    CommitPresenter.class,
                    TreeEntryPresenter.class,
                    CommitFilesFragment.class,
                    FileContentFragment.class,
                    FileEntryPresenter.class,
                    RepoSearchActivity.class,
                    ReposListFragment.class,
                    RepoEntryPresenter.class,
                    RepoFragment.class,
                    UserProfileFragment.class
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

    @Provides @Singleton
    FavReposStore provideFavReposStore() { return FavReposStore.getInstance(); }
}
