package com.dev.sim8500.githapp.app_logic;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.dev.sim8500.githapp.FrameActivity;
import com.dev.sim8500.githapp.GitHappApp;
import com.dev.sim8500.githapp.R;
import com.dev.sim8500.githapp.interfaces.IEntryViewListener;
import com.dev.sim8500.githapp.interfaces.ITreeEntryView;
import com.dev.sim8500.githapp.models.DetailedCommitModel;
import com.dev.sim8500.githapp.models.DownloadFileModel;
import com.dev.sim8500.githapp.models.RepoModel;
import com.dev.sim8500.githapp.models.TreeEntryModel;
import com.dev.sim8500.githapp.presentation.TreeEntryView;

import java.io.File;

import javax.inject.Inject;

/**
 * Created by sbernad on 16.03.16.
 */
public class TreeEntryPresenter extends PresenterViewHolder<TreeEntryModel, ITreeEntryView>
                                implements IEntryViewListener {

    public TreeEntryPresenter(View entryView) {
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

        TreeEntryModel.GitTreeType entryType = model.type;

        if(TreeEntryModel.GitTreeType.tree.equals(entryType)) {
            RepoModel repo = appCurrents.getCurrent("Repo");

            Intent frameIntent = FrameActivity.prepareTreeIntent(itemView.getContext(), repo, model.sha);
            itemView.getContext().startActivity(frameIntent);
        }
        else if(TreeEntryModel.GitTreeType.blob.equals(entryType)) {

            DownloadFileModel downloadModel = new DownloadFileModel(model.path, model.url);
            downloadModel.sha = model.sha;
            itemView.getContext().startActivity(FrameActivity.prepareFileContentIntent(itemView.getContext(), downloadModel));
        }
    }


    @Override
    public void onEntryViewPressed() {
    }

    @Inject
    protected GitHappCurrents appCurrents;
}
