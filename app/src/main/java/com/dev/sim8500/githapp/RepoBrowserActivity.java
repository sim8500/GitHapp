package com.dev.sim8500.githapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GitHappApp.getInstance().inject(this);
        setContentView(R.layout.activity_container);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if(!authReqMngr.hasTokenStored(this)) {
            startActivity(new Intent(this, MainActivity.class));
        }

        requestRepos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.repos_screen_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_search) {
            //Toast.makeText(this, "Run search.", Toast.LENGTH_SHORT).show();
            Intent searchIntent = new Intent(this, RepoSearchActivity.class);
            startActivity(searchIntent);
        }

        return true;
    }

    private void requestRepos()
    {
        //progressBar.setVisibility(View.VISIBLE);
        callback = new ReposListCallback(this);
        authReqMngr.getService(GitHubUserReposService.class)
                .getUserRepos()
                .enqueue(callback);
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

}
