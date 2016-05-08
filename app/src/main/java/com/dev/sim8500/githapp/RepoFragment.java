package com.dev.sim8500.githapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.sim8500.githapp.app_logic.AuthRequestsManager;
import com.dev.sim8500.githapp.app_logic.FileLineBinder;
import com.dev.sim8500.githapp.app_logic.FileLinePresenter;
import com.dev.sim8500.githapp.app_logic.GitHappCurrents;
import com.dev.sim8500.githapp.app_logic.RecyclerBaseAdapter;
import com.dev.sim8500.githapp.app_logic.RepoBranchBinder;
import com.dev.sim8500.githapp.app_logic.RepoBranchPresenter;
import com.dev.sim8500.githapp.app_logic.RepoEntryPresenter;
import com.dev.sim8500.githapp.app_logic.RepoPagerAdapter;
import com.dev.sim8500.githapp.models.BranchModel;
import com.dev.sim8500.githapp.models.FileLineModel;
import com.dev.sim8500.githapp.models.RepoModel;
import com.dev.sim8500.githapp.presentation.RepoView;
import com.dev.sim8500.githapp.services.GitHubRepoCommitsService;

import org.ocpsoft.prettytime.PrettyTime;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sbernad on 06.02.16.
 */
public class RepoFragment extends Fragment implements RepoPagerAdapter.OnRepoSetListener {

    public static final String REPO_MODEL_ARG = "com.dev.sim8500.githapp.REPO_MODEL_ARG";

    @Bind(R.id.repo_view) protected RepoView repoView;
    @Bind(R.id.branches_container) protected RecyclerView branchesContainer;

    @Inject
    protected AuthRequestsManager authReqMngr;

    @Inject
    protected GitHappCurrents appCurrents;

    protected RepoEntryPresenter presenter;

    protected RecyclerBaseAdapter<BranchModel, RepoBranchPresenter> adapterInstance
                                                = new RecyclerBaseAdapter<>(new RepoBranchBinder());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GitHappApp.getInstance().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View inflatedView = inflater.inflate(R.layout.fragment_repo, container, false);
        ButterKnife.bind(this, inflatedView);

        LinearLayoutManager lm = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        branchesContainer.setLayoutManager(lm);

        presenter = new RepoEntryPresenter(repoView);
        return inflatedView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(presenter.getModel() == null && getArguments() != null) {
            onRepoSet((RepoModel)getArguments().getParcelable(REPO_MODEL_ARG));
        }
    }

    @Override
    public void onRepoSet(RepoModel repo) {

        presenter.setModel(repo);
    }
}
