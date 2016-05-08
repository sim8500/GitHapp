package com.dev.sim8500.githapp.app_logic;

import android.view.View;

import com.dev.sim8500.githapp.GitHappApp;
import com.dev.sim8500.githapp.interfaces.IRepoBranchView;
import com.dev.sim8500.githapp.models.BranchModel;

import javax.inject.Inject;

/**
 * Created by sbernad on 30.04.16.
 */
public class RepoBranchPresenter extends PresenterViewHolder<BranchModel, IRepoBranchView> {

    public RepoBranchPresenter(View itemView) {
        super(itemView);
    }

    @Override
    public void updateView() {

        viewInterface.setName(model.name);
    }
}
