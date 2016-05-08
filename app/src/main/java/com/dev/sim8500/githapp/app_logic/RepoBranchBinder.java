package com.dev.sim8500.githapp.app_logic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dev.sim8500.githapp.interfaces.IRecyclerBinder;
import com.dev.sim8500.githapp.interfaces.IRepoBranchView;
import com.dev.sim8500.githapp.models.BranchModel;
import com.dev.sim8500.githapp.presentation.RepoBranchView;

/**
 * Created by sbernad on 30.04.16.
 */
public class RepoBranchBinder implements IRecyclerBinder<BranchModel, RepoBranchPresenter> {

    @Override
    public RepoBranchPresenter createHolderInstance(Context context, ViewGroup parent, int viewType) {

        RepoBranchView branchView = new RepoBranchView(context);
        RepoBranchPresenter presenter = new RepoBranchPresenter(branchView);

        return presenter;
    }

    @Override
    public int getViewTypeForModel(BranchModel model) {
        return 0;
    }

    @Override
    public void bindHolder(RepoBranchPresenter presenter, BranchModel model) {

        presenter.setModel(model);
    }
}
