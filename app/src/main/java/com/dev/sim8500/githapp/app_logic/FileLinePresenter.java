package com.dev.sim8500.githapp.app_logic;

import android.view.View;

import com.dev.sim8500.githapp.interfaces.IFileLineView;
import com.dev.sim8500.githapp.models.FileLineModel;

/**
 * Created by sbernad on 09.04.16.
 */
public class FileLinePresenter extends PresenterViewHolder<FileLineModel, IFileLineView> {


    public FileLinePresenter(View itemView) {
        super(itemView);
    }

    @Override
    public void updateView() {

        if(model.lineStatus == FileLineModel.PATCH_STATUS_DELETED) {
            viewInterface.setLineNumber("x");
        }
        else {
            viewInterface.setLineNumber(String.valueOf(model.lineNumber));
        }
        viewInterface.setLineContent(model.lineContent);
        viewInterface.setLineStatus(model.lineStatus);

    }
}
