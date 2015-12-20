package com.dev.sim8500.githapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dev.sim8500.githapp.models.IssueModel;
import com.dev.sim8500.githapp.models.RepoModel;
import com.dev.sim8500.githapp.presentation.IssueView;
import com.dev.sim8500.githapp.presentation.RepoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbernad on 19.12.15.
 */
public class IssuesAdapter extends RecyclerView.Adapter<IssuesAdapter.IssueViewHolder>
{

    public void initAdapter(Context context, List<IssueModel> issues) {

        this.context = context;
        this.issuesList.addAll(issues);
    }

    @Override
    public IssueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        IssueView iv = new IssueView(context);

        IssueViewHolder ivh = new IssueViewHolder(iv);
        return ivh;
    }

    @Override
    public void onBindViewHolder(IssueViewHolder holder, int position) {
        holder.getView().applyModel(issuesList.get(position));
    }

    @Override
    public int getItemCount() {
        return issuesList.size();
    }

    public static class IssueViewHolder extends RecyclerView.ViewHolder {

        public IssueViewHolder(IssueView v) {
            super(v);
        }

        public IssueView getView() { return (IssueView)itemView; }
    }

    public void clearIssues() {
        issuesList.clear();
        notifyDataSetChanged();
    }

    private Context context;
    private List<IssueModel> issuesList = new ArrayList<IssueModel>();
}
