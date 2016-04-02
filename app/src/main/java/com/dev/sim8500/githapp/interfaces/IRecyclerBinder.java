package com.dev.sim8500.githapp.interfaces;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by sbernad on 15.03.16.
 */
public interface IRecyclerBinder<M, VH extends RecyclerView.ViewHolder> {

    public VH createHolderInstance(Context context, ViewGroup parent, int viewType);

    public void bindHolder(VH vh, M model);

    public int getViewTypeForModel(M model);
}
