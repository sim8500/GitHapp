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
        GitHappApp.getInstance().inject(this);

        setContentView(R.layout.activity_frame);
        ButterKnife.bind(this);

        handleIntent(getIntent(), false);
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
        if (intent != null && intent.getAction().equals(GitHappApp.SHOW_COMMIT_TREE)) {

            resFragment = new CommitTreeFragment();
            resFragment.setArguments(intent.getExtras());
        }

        return resFragment;
    }

    public static Intent prepareIntent(Context context, String urlSha) {

        Intent resIntent = new Intent(context, FrameActivity.class);
        resIntent.setAction(GitHappApp.SHOW_COMMIT_TREE);
        resIntent.putExtra(GitHappApp.COMMIT_SHA, urlSha);

        return resIntent;
    }

    @Inject
    protected GitHappCurrents appCurrents;
}
