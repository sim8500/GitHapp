package com.dev.sim8500.githapp.app_logic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Szymon on 2016-01-01.
 */
public class RecyclerBaseAdapter<M, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>
{

    public RecyclerBaseAdapter(IRecyclerBinder<M, VH> recyclerBinder) {
        this.binder = recyclerBinder;
    }

    public void initAdapter(Context context, List<M> items)
    {
        this.context = context;
        this.itemsList.addAll(items);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return binder.createHolderInstance(context, parent, viewType);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        binder.bindHolder(holder, itemsList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return binder.getViewTypeForModel(itemsList.get(position));
    }

    public void clearItems()
    {
        itemsList.clear();
        notifyDataSetChanged();
    }

    protected Context context;
    protected List<M> itemsList = new ArrayList<M>();
    protected IRecyclerBinder<M, VH> binder;
}
