package com.dev.sim8500.githapp;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sbernad on 15.04.16.
 */
public class RepoSearchActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) protected Toolbar toolbar;
    @Bind(R.id.search_view) protected SearchView searchView;
    protected ReposListFragment resultFragment;
    protected String    query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            query = savedInstanceState.getString(SearchManager.QUERY);
        }

        GitHappApp.getInstance().inject(this);
        setContentView(R.layout.activity_search_repos);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setUpSearchView();
        setUpResultFragment();
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putString(SearchManager.QUERY, query);
    }

    @Override
    protected void onNewIntent(Intent intent) {

        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            resultFragment.runQuery(query);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if(itemId == android.R.id.home) {
            finish();
        }

        return true;
    }

    protected void setUpSearchView() {

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        if(query != null) {
            searchView.setQuery(query, false);
        }
    }
    protected void setUpResultFragment() {

        FragmentManager fm = getSupportFragmentManager();
        resultFragment = (ReposListFragment)fm.findFragmentById(R.id.result_fragment);

        if(resultFragment == null)
        {
            resultFragment = new ReposListFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.result_fragment, resultFragment);
            ft.commit();
        }

        if(query != null) {
            resultFragment.runQuery(query);
        }
    }
}
