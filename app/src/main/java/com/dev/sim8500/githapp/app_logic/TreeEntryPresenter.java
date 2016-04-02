package com.dev.sim8500.githapp.app_logic;

import android.content.Intent;

import com.dev.sim8500.githapp.FrameActivity;
import com.dev.sim8500.githapp.GitHappApp;
import com.dev.sim8500.githapp.interfaces.IEntryViewListener;
import com.dev.sim8500.githapp.interfaces.ITreeEntryView;
import com.dev.sim8500.githapp.models.RepoModel;
import com.dev.sim8500.githapp.models.TreeEntryModel;
import com.dev.sim8500.githapp.presentation.TreeEntryView;

import javax.inject.Inject;

/**
 * Created by sbernad on 16.03.16.
 */
public class TreeEntryPresenter extends PresenterViewHolder<TreeEntryModel, ITreeEntryView>
                                implements IEntryViewListener {

    public TreeEntryPresenter(TreeEntryView entryView) {
        super(entryView);

        ((GitHappApp) itemView.getContext().getApplicationContext()).inject(this);
    }

    @Override
    public void updateView() {
        viewInterface.setTitle(model.path);
        //view.setType(model.type.toString());
    }

    @Override
    public void onEntryViewChosen() {

        if(model.type.equals(TreeEntryModel.GitTreeType.tree)) {
            RepoModel repo = appCurrents.getCurrent("Repo");

            Intent frameIntent = FrameActivity.prepareTreeIntent(itemView.getContext(), repo, model.sha);
            itemView.getContext().startActivity(frameIntent);
        }
    }

    @Override
    public void onEntryViewPressed() {
    }

    @Inject
    protected GitHappCurrents appCurrents;
}
