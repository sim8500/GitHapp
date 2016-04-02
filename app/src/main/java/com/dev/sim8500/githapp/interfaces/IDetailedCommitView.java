package com.dev.sim8500.githapp.interfaces;

/**
 * Created by sbernad on 01.04.16.
 */
public interface IDetailedCommitView {

    void setStats(int additions, int deletions, int total);

    void setReadyState(boolean isReady);

    void setListener(ICommitEntryDialogListener listener);

    void show();
}
