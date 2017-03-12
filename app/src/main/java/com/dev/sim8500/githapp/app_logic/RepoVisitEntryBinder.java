package com.dev.sim8500.githapp.app_logic;

import android.content.Context;
import android.view.ViewGroup;

import com.dev.sim8500.githapp.db.RepoVisits;
import com.dev.sim8500.githapp.interfaces.IRecyclerBinder;
import com.dev.sim8500.githapp.presentation.RepoVisitView;

/**
 * Created by sbernad on 26.02.2017.
 */

public class RepoVisitEntryBinder implements IRecyclerBinder<RepoVisits, RepoVisitEntryPresenter> {

    @Override
    public RepoVisitEntryPresenter createHolderInstance(Context context, ViewGroup parent, int viewType) {

        RepoVisitView bindedView = new RepoVisitView(context);
        RepoVisitEntryPresenter presenter = new RepoVisitEntryPresenter(bindedView);
        bindedView.setListener(presenter);
        
        return presenter;
    }

    @Override
    public void bindHolder(RepoVisitEntryPresenter repoVisitEntryPresenter, RepoVisits model) {
        repoVisitEntryPresenter.setModel(model);
    }

    @Override
    public int getViewTypeForModel(RepoVisits model) {
        return 0;
    }
}
