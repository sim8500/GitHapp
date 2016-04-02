package com.dev.sim8500.githapp.app_logic;

import android.view.View;

import com.dev.sim8500.githapp.interfaces.IFileEntryView;
import com.dev.sim8500.githapp.models.FileModel;

/**
 * Created by sbernad on 01.04.16.
 */
public class FileEntryPresenter extends PresenterViewHolder<FileModel, IFileEntryView> {

    public FileEntryPresenter(View itemView) {
        super(itemView);
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
}
