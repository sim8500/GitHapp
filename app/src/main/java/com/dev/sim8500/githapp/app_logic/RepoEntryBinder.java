package com.dev.sim8500.githapp.app_logic;

import android.content.Context;
import android.view.ViewGroup;

import com.dev.sim8500.githapp.interfaces.IRecyclerBinder;
import com.dev.sim8500.githapp.models.RepoModel;
import com.dev.sim8500.githapp.presentation.RepoView;

/**
 * Created by sbernad on 16.04.16.
 */
public class RepoEntryBinder implements IRecyclerBinder<RepoModel, RepoEntryPresenter> {

    private boolean bindListeners = false;

    public RepoEntryBinder(boolean withListener) {
        bindListeners = withListener;
    }
    @Override
    public RepoEntryPresenter createHolderInstance(Context context, ViewGroup parent, int viewType) {

        RepoView bindedView = new RepoView(context);

        RepoEntryPresenter presenter = new RepoEntryPresenter(bindedView);
        presenter.setRepoChosenListening(bindListeners);
        presenter.setRepoFavListening(bindListeners);

        return presenter;
    }

    @Override
    public void bindHolder(RepoEntryPresenter repoEntryPresenter, RepoModel model) {
        repoEntryPresenter.setModel(model);
    }

    @Override
    public int getViewTypeForModel(RepoModel model) {
        return 0;
    }
}
