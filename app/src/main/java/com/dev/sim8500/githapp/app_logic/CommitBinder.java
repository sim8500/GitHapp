package com.dev.sim8500.githapp.app_logic;

import android.content.Context;
import android.view.ViewGroup;

import com.dev.sim8500.githapp.models.CommitModel;
import com.dev.sim8500.githapp.presentation.CommitView;

/**
 * Created by sbernad on 15.03.16.
 */
public class CommitBinder implements IRecyclerBinder<CommitModel, CommitPresenter> {

    @Override
    public CommitPresenter createHolderInstance(Context context, ViewGroup parent, int viewType) {

        CommitView bindedView = new CommitView(context);
        CommitPresenter presenter = new CommitPresenter(bindedView);
        bindedView.setViewListener(presenter);

        return presenter;
    }

    @Override
    public void bindHolder(CommitPresenter commitPresenter, CommitModel model) {
        commitPresenter.setModel(model);
    }

    @Override
    public int getViewTypeForModel(CommitModel model) {
        return 0;
    }

}
