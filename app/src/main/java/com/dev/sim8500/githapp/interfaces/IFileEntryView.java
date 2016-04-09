package com.dev.sim8500.githapp.interfaces;

/**
 * Created by sbernad on 01.04.16.
 */
public interface IFileEntryView {

    void setFilename(CharSequence filename);

    void setChangesCount(int count);

    void setStatus(CharSequence status);

    void setPatch(CharSequence patch);
}
