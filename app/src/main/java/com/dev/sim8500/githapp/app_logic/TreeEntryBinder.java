package com.dev.sim8500.githapp.app_logic;

import android.content.Context;
import android.view.ViewGroup;

import com.dev.sim8500.githapp.R;
import com.dev.sim8500.githapp.interfaces.IRecyclerBinder;
import com.dev.sim8500.githapp.models.TreeEntryModel;
import com.dev.sim8500.githapp.presentation.TreeEntryView;

/**
 * Created by sbernad on 16.03.16.
 */
public class TreeEntryBinder implements IRecyclerBinder<TreeEntryModel, TreeEntryPresenter> {
    @Override
    public TreeEntryPresenter createHolderInstance(Context context, ViewGroup parent, int viewType) {

        TreeEntryView bindedView = new TreeEntryView(context, getLayoutForViewType(viewType));
        TreeEntryPresenter presenter = new TreeEntryPresenter(bindedView);
        bindedView.setViewListener(presenter);
        return presenter;
    }

    @Override
    public void bindHolder(TreeEntryPresenter treeEntryPresenter, TreeEntryModel model) {
        treeEntryPresenter.setModel(model);
    }

    @Override
    public int getViewTypeForModel(TreeEntryModel model) {

        int resType = 0;
        if(model.type.equals(TreeEntryModel.GitTreeType.tree)) {
            resType = 1;
        }

        return resType;
    }

    protected int getLayoutForViewType(int viewType) {
        return (viewType == 0) ? R.layout.row_commit_file : R.layout.row_commit_folder;
    }
}
