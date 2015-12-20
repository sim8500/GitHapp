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
public class ReposAdapter extends RecyclerView.Adapter<ReposAdapter.RepoViewHolder>
{

    public static class RepoViewHolder extends RecyclerView.ViewHolder
    {
        public RepoView getView() {
            return (RepoView)itemView;
        }
        public RepoViewHolder(RepoView v) {
            super(v);
        }
    }

    public void initAdapter(Context context, RepoView.OnRepoChosenListener repoListener, List<RepoModel> repos)
    {
        this.context = context;
        this.repoChosenListener = repoListener;
        this.reposList.addAll(repos);
    }

    public List<RepoModel> getRepos() { return this.reposList; }

    public void addRepo(RepoModel repo)
    {
        reposList.add(repo);
        if(reposList.size() == 1)
            notifyDataSetChanged();
        else
            notifyItemInserted(reposList.size()-1);
    }

    public void clearRepos()
    {
        reposList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return reposList.size();
    }


    @Override
    public RepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        RepoView rv = new RepoView(context);
        rv.setOnRepoChosenListener(this.repoChosenListener);

        RepoViewHolder rvh = new RepoViewHolder(rv);
        return rvh;
    }

    @Override
    public void onBindViewHolder(RepoViewHolder holder, int position)
    {
        holder.getView().applyModel(reposList.get(position));
    }

    @Override
    public long getItemId(int i) {
        return super.getItemId(i);
    }

    private Context context;
    private List<RepoModel> reposList = new ArrayList<RepoModel>();
    private RepoView.OnRepoChosenListener repoChosenListener;
}
