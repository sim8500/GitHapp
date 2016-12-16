package com.dev.sim8500.githapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.dev.sim8500.githapp.app_logic.AuthRequestsManager;
import com.dev.sim8500.githapp.app_logic.GitHappCurrents;
import com.dev.sim8500.githapp.models.DownloadFileModel;
import com.dev.sim8500.githapp.models.FileModel;
import com.dev.sim8500.githapp.models.RepoModel;
import com.dev.sim8500.githapp.models.UserModel;

import java.text.ParseException;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sbernad on 16.03.16.
 */
public class FrameActivity extends AppCompatActivity {

    @Inject
    protected GitHappCurrents appCurrents;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GitHappApp.getInstance().inject(this);

        setContentView(R.layout.activity_frame);
        ButterKnife.bind(this);

        handleIntent(getIntent(), true);
    }

    @Override
    public void onBackPressed() {
        appCurrents.setCurrent("CommitSha", null);

        super.onBackPressed();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent, true);
    }

    protected void handleIntent(Intent intent, boolean overrideExisting) {

        boolean areParamsOk = true;
        if(intent.getAction().equals(GitHappApp.SHOW_COMMIT_TREE))
        {
            areParamsOk = intent.hasExtra(GitHappApp.COMMIT_SHA);
            if(areParamsOk) {
                String existingParam = appCurrents.getCurrent("CommitSha");
                if(overrideExisting || TextUtils.isEmpty(existingParam)) {
                    appCurrents.setCurrent("CommitSha", intent.getStringExtra(GitHappApp.COMMIT_SHA));
                }
            }
        }

        if(areParamsOk) {
            Fragment resFragment = createFragmentForIntent(intent);
            if (resFragment != null) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.main_fragment, resFragment);
                ft.commit();
            }
        }
    }

    protected Fragment createFragmentForIntent(Intent intent) {

        Fragment resFragment = null;
        if (intent == null) {
            return resFragment;
        }
        Bundle simpleArgs = null;

        switch(intent.getAction()) {
            case GitHappApp.SHOW_COMMIT_TREE:
                resFragment = new CommitTreeFragment();
                resFragment.setArguments(intent.getExtras());
                break;
            case GitHappApp.SHOW_COMMIT_FILES:
                resFragment = new CommitFilesFragment();
                break;
            case GitHappApp.SHOW_FILE_CONTENT:
                resFragment = new FileContentFragment();
                resFragment.setArguments(intent.getExtras());
                break;
            case GitHappApp.SHOW_FAV_REPOS_LIST:
                resFragment = new ReposListFragment();
                simpleArgs = new Bundle();
                simpleArgs.putBoolean(GitHappApp.SHOW_FAV_REPOS_LIST, true);

                resFragment.setArguments(simpleArgs);
                break;
            case GitHappApp.USER_PROFILE_MODEL:
                resFragment = new UserProfileFragment();
                simpleArgs = new Bundle();
                simpleArgs.putParcelable(GitHappApp.USER_PROFILE_MODEL,
                                            intent.getParcelableExtra(GitHappApp.USER_PROFILE_MODEL));
                resFragment.setArguments(simpleArgs);
                break;
        }

        return resFragment;
    }

    public static Intent prepareTreeIntent(Context context, RepoModel repo, String urlSha) {

        Intent resIntent = new Intent(context, FrameActivity.class);
        resIntent.setAction(GitHappApp.SHOW_COMMIT_TREE);
        resIntent.putExtra(GitHappApp.REPO_NAME, repo.name);
        resIntent.putExtra(GitHappApp.REPO_OWNER, repo.owner.login);
        resIntent.putExtra(GitHappApp.COMMIT_SHA, urlSha);

        return resIntent;
    }

    public static Intent prepareFilesIntent(Context context) {
        Intent resIntent = new Intent(context, FrameActivity.class);
        resIntent.setAction(GitHappApp.SHOW_COMMIT_FILES);

        return resIntent;
    }

    public static Intent prepareFavReposIntent(Context context) {
        Intent resIntent = new Intent(context, FrameActivity.class);
        resIntent.setAction(GitHappApp.SHOW_FAV_REPOS_LIST);

        return resIntent;
    }

    public static Intent prepareFileContentIntent(Context context, DownloadFileModel fileModel) {
        Intent resIntent = new Intent(context, FrameActivity.class);
        resIntent.setAction(GitHappApp.SHOW_FILE_CONTENT);
        resIntent.putExtra(GitHappApp.FILE_CONTENT_DOWNLOAD_DATA, fileModel);

        return resIntent;
    }

    public static Intent prepareUserProfileIntent(Context context, UserModel user) {
        Intent resIntent = new Intent(context, FrameActivity.class);
        resIntent.setAction(GitHappApp.USER_PROFILE_MODEL);
        resIntent.putExtra(GitHappApp.USER_PROFILE_MODEL, user);

        return resIntent;
    }
}
