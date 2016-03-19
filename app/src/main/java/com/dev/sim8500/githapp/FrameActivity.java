package com.dev.sim8500.githapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.dev.sim8500.githapp.app_logic.AuthRequestsManager;
import com.dev.sim8500.githapp.models.RepoModel;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sbernad on 16.03.16.
 */
public class FrameActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_frame);

        ButterKnife.bind(this);

        Fragment resFragment = createFragmentForIntent(getIntent());
        if(resFragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.main_fragment, resFragment);
            ft.commit();
        }
    }

    protected Fragment createFragmentForIntent(Intent intent) {

        Fragment resFragment = null;
        if (intent != null && intent.getAction().equals(GitHappApp.SHOW_COMMIT_TREE)) {

            resFragment = new CommitTreeFragment();
            resFragment.setArguments(intent.getExtras());
        }

        return resFragment;
    }

    public static Intent prepareIntent(Context context, RepoModel repo, String urlSha) {

        Intent resIntent = new Intent(context, FrameActivity.class);
        resIntent.setAction(GitHappApp.SHOW_COMMIT_TREE);
        resIntent.putExtra(GitHappApp.REPO_NAME, repo.name);
        resIntent.putExtra(GitHappApp.REPO_OWNER, repo.owner.login);
        resIntent.putExtra(GitHappApp.COMMIT_SHA, urlSha);

        return resIntent;
    }
}
