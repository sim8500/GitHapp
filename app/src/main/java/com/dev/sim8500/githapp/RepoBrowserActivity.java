package com.dev.sim8500.githapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.sim8500.githapp.app_logic.AuthRequestsManager;
import com.dev.sim8500.githapp.app_logic.GitHappCurrents;
import com.dev.sim8500.githapp.app_logic.RepoPagerAdapter;
import com.dev.sim8500.githapp.models.RepoModel;
import com.dev.sim8500.githapp.services.GitHubUserReposService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sbernad on 06.02.16.
 */
public class RepoBrowserActivity extends AppCompatActivity {

    @Bind(R.id.main_container) protected LinearLayout mainContainer;
    @Bind(R.id.container_pager) protected ViewPager pager;
    @Bind(R.id.toolbar) protected Toolbar toolbar;
    @Bind(R.id.tab_layout) protected TabLayout tabLayout;
    @Bind(R.id.toolbar_spinner) protected Spinner toolbarSpinner;

    protected RepoPagerAdapter pagerAdapter;
    protected ReposListCallback callback;
    protected List<RepoModel> userRepos;

    @Inject protected AuthRequestsManager authReqMngr;
    @Inject protected GitHappCurrents appCurrents;


    public static class ReposListCallback implements Callback<List<RepoModel>> {

        WeakReference<RepoBrowserActivity> actRef;

        public ReposListCallback(RepoBrowserActivity act) {
            actRef = new WeakReference<RepoBrowserActivity>(act);
        }

        @Override
        public void onResponse(Response<List<RepoModel>> response, Retrofit retrofit) {
            if(actRef.get() != null) {
                actRef.get().onUserReposReceived(response.body());
            }
        }

        @Override
        public void onFailure(Throwable t) {
            if(actRef.get() != null) {
                actRef.get().onRequestFailed();
            }
        }
    }

    protected static class RepoSub extends Subscriber<RepoModel> {

        protected WeakReference<RepoBrowserActivity>  activityRef;

        public RepoSub(RepoBrowserActivity activity) {
            activityRef = new WeakReference<RepoBrowserActivity>(activity);
        }

        @Override
        public void onCompleted() {
            // no action for now
        }

        @Override
        public void onError(Throwable e) {
            // no action for now
        }

        @Override
        public void onNext(RepoModel repoModel) {
            if(activityRef.get() != null) {
                activityRef.get().setUpLoadedRepo(repoModel);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GitHappApp.getInstance().inject(this);
        setContentView(R.layout.activity_container);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if(!authReqMngr.hasTokenStored(this)) {
            startActivity(new Intent(this, MainActivity.class));
        }

        String action = getIntent().getAction();
        if(action != null && action.equals(GitHappApp.SHOW_SINGLE_REPO)) {
            setUpSingleRepo();
        }
        else {
            requestRepos();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.repos_screen_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if(itemId == R.id.action_search) {
            //Toast.makeText(this, "Run search.", Toast.LENGTH_SHORT).show();
            Intent searchIntent = new Intent(this, RepoSearchActivity.class);
            startActivity(searchIntent);
        }
        else if(itemId == R.id.action_fav) {
            goToFavReposScreen();
        }
        else if(itemId == android.R.id.home) {
            finish();
        }

        return true;
    }

    public static Intent prepareIntent(Context context, String owner, String repo) {

        Intent resIntent = prepareIntent(context);

        resIntent.putExtra(GitHappApp.SHOW_SINGLE_REPO_OWNER, owner);
        resIntent.putExtra(GitHappApp.SHOW_SINGLE_REPO_NAME, repo);

        return resIntent;
    }

    public static Intent prepareIntent(Context context) {

        Intent resIntent = new Intent(context, RepoBrowserActivity.class);
        resIntent.setAction(GitHappApp.SHOW_SINGLE_REPO);

        return resIntent;
    }

    private void requestRepos()
    {
        //progressBar.setVisibility(View.VISIBLE);
        toolbarSpinner.setVisibility(View.VISIBLE);
        callback = new ReposListCallback(this);
        authReqMngr.getService(GitHubUserReposService.class)
                .getUserRepos()
                .enqueue(callback);
    }

    private void setUpSingleRepo() {

        Intent inputIntent = getIntent();
        String repoName = inputIntent.getStringExtra(GitHappApp.SHOW_SINGLE_REPO_NAME);
        String repoOwner = inputIntent.getStringExtra(GitHappApp.SHOW_SINGLE_REPO_OWNER);

        boolean hasRepoArgs = !TextUtils.isEmpty(repoName) &&
                                !TextUtils.isEmpty(repoOwner);

        if(hasRepoArgs) {
            loadRequestedRepo(repoName, repoOwner);
        }
        else {
            setUpCurrentRepo((RepoModel)appCurrents.getCurrent("Repo"));
        }

    }

    private void loadRequestedRepo(String repo, String owner) {

        authReqMngr.getObservableService(GitHubUserReposService.class)
                .getRepo(owner, repo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new RepoSub(RepoBrowserActivity.this));

    }

    protected void setUpLoadedRepo(RepoModel repoModel) {
        appCurrents.setCurrent("Repo", repoModel);
        setUpCurrentRepo(repoModel);
    }

    private void setUpCurrentRepo(RepoModel repo) {

        if(repo != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.spinner_row,
                    R.id.row_txtview,
                    new String[] {repo.name} );
            toolbarSpinner.setAdapter(adapter);
            toolbarSpinner.setEnabled(false);
            setUpContentAdapter(repo);
        }

    }

    @UiThread
    public void onUserReposReceived(List<RepoModel> repos)
    {
        if(repos != null && repos.size() > 0)
        {
            userRepos = repos;

            List<String> reposStrings = new ArrayList<String>(userRepos.size());
            for (RepoModel repo : userRepos) {
                reposStrings.add(repo.name);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_row, R.id.row_txtview, reposStrings);
            toolbarSpinner.setAdapter(adapter);
            toolbarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position >= 0 && position < userRepos.size()) {
                        setUpContentAdapter(userRepos.get(position));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    @UiThread
    public void onRequestFailed()
    {
        Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT);
    }

    @UiThread
    protected void setUpContentAdapter(RepoModel repo) {

        appCurrents.setCurrent("Repo", repo);
        if(pager.getAdapter() != null)
            pagerAdapter = (RepoPagerAdapter)pager.getAdapter();

        if(pagerAdapter == null) {
            pagerAdapter = new RepoPagerAdapter(getSupportFragmentManager(), repo);
            pager.setAdapter(pagerAdapter);
        }
        else {
            pagerAdapter.setRepoModel(repo);
        }

        tabLayout.setupWithViewPager(pager);
    }

    protected void goToFavReposScreen() {
        startActivity(FrameActivity.prepareFavReposIntent(this));
    }

}
