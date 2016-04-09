package com.dev.sim8500.githapp.app_logic;

import android.content.Context;
import android.view.ViewGroup;

import com.dev.sim8500.githapp.interfaces.IRecyclerBinder;
import com.dev.sim8500.githapp.models.FileLineModel;
import com.dev.sim8500.githapp.presentation.FileEntryView;
import com.dev.sim8500.githapp.presentation.FileLineView;

/**
 * Created by sbernad on 09.04.16.
 */
public class FileLineBinder implements IRecyclerBinder<FileLineModel, FileLinePresenter> {
    @Override
    public FileLinePresenter createHolderInstance(Context context, ViewGroup parent, int viewType) {
        FileLineView bindedView = new FileLineView(context);
        FileLinePresenter result = new FileLinePresenter(bindedView);
        return result;
    }

    @Override
    public void bindHolder(FileLinePresenter fileLinePresenter, FileLineModel model) {
        fileLinePresenter.setModel(model);
    }

    @Override
    public int getViewTypeForModel(FileLineModel model) {
        return 0;
    }
}
