package com.dev.sim8500.githapp.app_logic;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by sbernad on 15.03.16.
 */
public abstract class PresenterViewHolder<M, V extends View> extends RecyclerView.ViewHolder {

    public PresenterViewHolder(V itemView) {
        super(itemView);

        this.view = itemView;
    }

    public void setModel(M model) {
        this.model = model;

        if(view != null) {
            updateView();
        }
    }

    public abstract void updateView();

    protected M model;
    protected V view;
}
