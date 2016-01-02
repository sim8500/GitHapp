package com.dev.sim8500.githapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dev.sim8500.githapp.models.CommentModel;
import com.dev.sim8500.githapp.models.CommentModel;
import com.dev.sim8500.githapp.models.RepoModel;
import com.dev.sim8500.githapp.presentation.CommentView;
import com.dev.sim8500.githapp.presentation.CommentView;
import com.dev.sim8500.githapp.presentation.RepoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbernad on 19.12.15.
 */
public class CommentsAdapter extends AbstractAdapter<CommentModel>
{

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        CommentView cv = new CommentView(context);
        ViewHolder ivh = new ViewHolder(cv);

        return ivh;
    }
}
