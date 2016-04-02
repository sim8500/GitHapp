package com.dev.sim8500.githapp.app_logic;

import android.content.Context;
import android.view.ViewGroup;

import com.dev.sim8500.githapp.interfaces.IRecyclerBinder;
import com.dev.sim8500.githapp.models.FileModel;
import com.dev.sim8500.githapp.presentation.FileEntryView;

/**
 * Created by sbernad on 01.04.16.
 */
public class FileEntryBinder implements IRecyclerBinder<FileModel, FileEntryPresenter> {
    @Override
    public FileEntryPresenter createHolderInstance(Context context, ViewGroup parent, int viewType) {
        FileEntryView bindedView = new FileEntryView(context);
        FileEntryPresenter result = new FileEntryPresenter(bindedView);
        return result;
    }

    @Override
    public void bindHolder(FileEntryPresenter fileEntryPresenter, FileModel model) {
        fileEntryPresenter.setModel(model);
    }

    @Override
    public int getViewTypeForModel(FileModel model) {
        return 0;
    }
}
