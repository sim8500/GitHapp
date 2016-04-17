package com.dev.sim8500.githapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.sim8500.githapp.app_logic.RepoEntryPresenter;
import com.dev.sim8500.githapp.app_logic.RepoPagerAdapter;
import com.dev.sim8500.githapp.models.RepoModel;
import com.dev.sim8500.githapp.presentation.RepoView;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sbernad on 06.02.16.
 */
public class RepoFragment extends Fragment implements RepoPagerAdapter.OnRepoSetListener {

    public static final String REPO_MODEL_ARG = "com.dev.sim8500.githapp.REPO_MODEL_ARG";

    @Bind(R.id.repo_view) protected RepoView repoView;

    protected RepoEntryPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View inflatedView = inflater.inflate(R.layout.fragment_repo, container, false);
        ButterKnife.bind(this, inflatedView);

        presenter = new RepoEntryPresenter(repoView);
        return inflatedView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(presenter.getModel() == null && getArguments() != null) {
            presenter.setModel((RepoModel)getArguments().getParcelable(REPO_MODEL_ARG));
        }
    }

    @Override
    public void onRepoSet(RepoModel repo) {
        presenter.setModel(repo);
    }
}
