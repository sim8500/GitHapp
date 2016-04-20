package com.dev.sim8500.githapp.interfaces;

import com.dev.sim8500.githapp.models.FileLineModel;

/**
 * Created by sbernad on 09.04.16.
 */
public interface IFileLineView {

    void setLineNumber(CharSequence number);
    void setLineContent(CharSequence lineContent);
    void setLineStatus(@FileLineModel.PatchStatus int lineStatus);
}
