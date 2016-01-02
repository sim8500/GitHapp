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
public class IssuesAdapter extends AbstractAdapter<IssueModel>
{
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        IssueView view = new IssueView(context);
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }
}
