package com.dev.sim8500.githapp.app_logic;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by sbernad on 15.03.16.
 */
public abstract class PresenterViewHolder<M, VInterface> extends RecyclerView.ViewHolder {

    public PresenterViewHolder(View itemView) {
        super(itemView);

        viewInterface = (VInterface)itemView;
    }

    public void setModel(M model) {
        this.model = model;

        if(super.itemView != null) {
            updateView();
        }
    }

    public M getModel() {
        return model;
    }

    public abstract void updateView();

    protected M model;
    protected VInterface viewInterface;
}
