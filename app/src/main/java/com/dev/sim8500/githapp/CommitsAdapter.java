package com.dev.sim8500.githapp;

import android.view.ViewGroup;

import com.dev.sim8500.githapp.models.CommitModel;
import com.dev.sim8500.githapp.presentation.CommitView;

/**
 * Created by sbernad on 22.02.16.
 */
public class CommitsAdapter extends AbstractAdapter<CommitModel> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommitView cview = new CommitView(context);
        ViewHolder vh = new ViewHolder(cview);

        return vh;
    }
}
