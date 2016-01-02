package com.dev.sim8500.githapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dev.sim8500.githapp.models.IssueModel;
import com.dev.sim8500.githapp.presentation.IssueView;
import com.dev.sim8500.githapp.presentation.ModelView;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Szymon on 2016-01-01.
 */
public abstract class AbstractAdapter<E> extends RecyclerView.Adapter<AbstractAdapter.ViewHolder>
{
    public void initAdapter(Context context, List<E> items)
    {
        this.context = context;
        this.itemsList.addAll(items);
    }

    @Override
    public abstract ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.getView().applyModel(itemsList.get(position));
    }

    @Override
    public int getItemCount()
        {
        return itemsList.size();
        }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(ModelView v)
        {
            super((View)v);
        }

        public ModelView getView() { return (ModelView)itemView; }
    }

    public void clearItems()
    {
        itemsList.clear();
        notifyDataSetChanged();
    }

    protected Context context;
    protected List<E> itemsList = new ArrayList<E>();
}
