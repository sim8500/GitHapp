package com.dev.sim8500.githapp.app_logic;

import android.view.View;

import com.dev.sim8500.githapp.FrameActivity;
import com.dev.sim8500.githapp.GitHappApp;
import com.dev.sim8500.githapp.interfaces.IEntryViewListener;
import com.dev.sim8500.githapp.interfaces.IFileEntryView;
import com.dev.sim8500.githapp.models.FileModel;

import javax.inject.Inject;

/**
 * Created by sbernad on 01.04.16.
 */
public class FileEntryPresenter extends PresenterViewHolder<FileModel, IFileEntryView> implements IEntryViewListener {

    @Inject
    protected GitHappCurrents appCurrents;
    @Inject
    protected AuthRequestsManager authRequestsManager;


    public FileEntryPresenter(View itemView) {
        super(itemView);

        ((GitHappApp)itemView.getContext().getApplicationContext()).inject(this);
    }

    @Override
    public void updateView() {
        updateFilename();
        updateStatus();
        updateChangesCount();
    }

    protected void updateStatus() {
        viewInterface.setStatus(model.status);
    }

    protected void updateChangesCount() {
        viewInterface.setChangesCount(model.changes);
    }

    protected void updateFilename() {
        viewInterface.setFilename(model.filename);
    }

    @Override
    public void onEntryViewChosen() {
        appCurrents.setCurrent("FileModel", model);

        itemView.getContext().startActivity(FrameActivity.prepareFileContentIntent(itemView.getContext()));
    }

    @Override
    public void onEntryViewPressed() {

    }
}
