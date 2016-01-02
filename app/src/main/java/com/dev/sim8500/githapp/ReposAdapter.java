package com.dev.sim8500.githapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.dev.sim8500.githapp.models.RepoModel;
import com.dev.sim8500.githapp.presentation.RepoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbernad on 19.12.15.
 */
public class ReposAdapter extends AbstractAdapter<RepoModel>
{

    public void initAdapter(Context context, RepoView.OnRepoChosenListener repoListener, List<RepoModel> repos)
    {
        super.initAdapter(context, repos);

        this.repoChosenListener = repoListener;
    }

    public List<RepoModel> getRepos() { return this.itemsList; }

    public void addRepo(RepoModel repo)
    {
        itemsList.add(repo);
        if(itemsList.size() == 1)
            notifyDataSetChanged();
        else
            notifyItemInserted(itemsList.size()-1);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        RepoView rv = new RepoView(context);
        rv.setOnRepoChosenListener(this.repoChosenListener);

        ViewHolder rvh = new ViewHolder(rv);
        return rvh;
    }

    private RepoView.OnRepoChosenListener repoChosenListener;
}
