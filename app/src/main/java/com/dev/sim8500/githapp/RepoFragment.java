package com.dev.sim8500.githapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.sim8500.githapp.app_logic.RepoPagerAdapter;
import com.dev.sim8500.githapp.models.RepoModel;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sbernad on 06.02.16.
 */
public class RepoFragment extends Fragment implements RepoPagerAdapter.OnRepoSetListener {

    public static final String REPO_MODEL_ARG = "com.dev.sim8500.githapp.REPO_MODEL_ARG";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View inflatedView = inflater.inflate(R.layout.fragment_repo, container, false);
        ButterKnife.bind(this, inflatedView);

        return inflatedView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(model == null && getArguments() != null) {
            model = getArguments().getParcelable(REPO_MODEL_ARG);
        }

        applyModel();
    }

    protected void applyModel() {

        nameTxtView.setText(model.name);
        urlTxtView.setText(model.url);
        descTxtView.setText(model.description);
    }

    @Bind(R.id.nameTxtView) protected TextView nameTxtView;
    @Bind(R.id.urlTxtView) protected TextView urlTxtView;
    @Bind(R.id.descTxtView) protected TextView descTxtView;

    protected RepoModel model;

    @Override
    public void onRepoSet(RepoModel repo) {
        model = repo;
        if(isVisible()) {
            applyModel();
        }
    }
}
