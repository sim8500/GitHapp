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
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>
{
    public void initAdapter(Context context, List<CommentModel> comments)
    {
        this.context = context;
        this.commentsList.addAll(comments);
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        CommentView cv = new CommentView(context);

        CommentViewHolder ivh = new CommentViewHolder(cv);
        return ivh;
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position)
    {
        holder.getView().applyModel(commentsList.get(position));
    }

    @Override
    public int getItemCount()
    {
        return commentsList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder
    {
        public CommentViewHolder(CommentView v)
        {
            super(v);
        }

        public CommentView getView() { return (CommentView)itemView; }
    }

    public void clearComments()
    {
        commentsList.clear();
        notifyDataSetChanged();
    }

    private Context context;
    private List<CommentModel> commentsList = new ArrayList<CommentModel>();
}
