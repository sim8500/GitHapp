package com.dev.sim8500.githapp;

import com.dev.sim8500.githapp.app_logic.FileEntryBinder;
import com.dev.sim8500.githapp.app_logic.FileEntryPresenter;
import com.dev.sim8500.githapp.app_logic.GitHappCurrents;
import com.dev.sim8500.githapp.app_logic.RecyclerBaseAdapter;
import com.dev.sim8500.githapp.models.DetailedCommitModel;
import com.dev.sim8500.githapp.models.FileModel;

import javax.inject.Inject;

/**
 * Created by sbernad on 02.04.16.
 */
public class CommitFilesFragment extends ContentFragment {


    protected RecyclerBaseAdapter<FileModel, FileEntryPresenter> entriesAdapter = new RecyclerBaseAdapter<>(new FileEntryBinder());

    @Override
    public void onStart() {
        super.onStart();

        loadEntries();
    }

    protected void loadEntries()
    {
        DetailedCommitModel commitModel = appCurrents.getCurrent("DetailedCommitModel");

        entriesAdapter.clearItems();
        entriesAdapter.initAdapter(this.getContext(), commitModel.files);
        recyclerView.setAdapter(entriesAdapter);
    }
}
