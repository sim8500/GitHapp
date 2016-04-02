package com.dev.sim8500.githapp.app_logic;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dev.sim8500.githapp.FrameActivity;
import com.dev.sim8500.githapp.GitHappApp;
import com.dev.sim8500.githapp.interfaces.ICommitEntryDialogListener;
import com.dev.sim8500.githapp.interfaces.ICommitView;
import com.dev.sim8500.githapp.interfaces.IDetailedCommitView;
import com.dev.sim8500.githapp.interfaces.IEntryViewListener;
import com.dev.sim8500.githapp.models.CommitModel;
import com.dev.sim8500.githapp.models.DetailedCommitModel;
import com.dev.sim8500.githapp.models.RepoModel;
import com.dev.sim8500.githapp.presentation.CommitEntryDialog;
import com.dev.sim8500.githapp.presentation.CommitView;
import com.dev.sim8500.githapp.services.GitHubRepoCommitsService;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sbernad on 15.03.16.
 */
public class CommitPresenter extends PresenterViewHolder<CommitModel, ICommitView>
                             implements IEntryViewListener, ICommitEntryDialogListener {

    @Inject
    protected GitHappCurrents appCurrents;
    @Inject
    protected AuthRequestsManager authRequestsManager;

    protected IDetailedCommitView detailedView;

    public class CommitSub extends Subscriber<DetailedCommitModel> {

        @Override
        public void onCompleted() {
            Log.d("CommitSub", "onCompleted()");
        }

        @Override
        public void onError(Throwable e) {
            Log.e("CommitSub", e.toString());
        }

        @Override
        public void onNext(DetailedCommitModel detailedCommitModel) {

            appCurrents.setCurrent("DetailedCommitModel", detailedCommitModel);
            detailedView.setStats(detailedCommitModel.stats.additions,
                    detailedCommitModel.stats.deletions,
                    detailedCommitModel.stats.total);

            detailedView.setReadyState(true);
        }
    }

    public CommitPresenter(View view) {
        super(view);

        ((GitHappApp) view.getContext().getApplicationContext()).inject(this);
    }

    @Override
    public void updateView() {
        updateAuthorName();
        updateMessage();
        updateCommitDate();
    }

    protected void updateAuthorName() {
        viewInterface.setAuthor(model.commit.author.name);
    }

    protected void updateMessage() {
        viewInterface.setMessage(model.commit.message);
    }

    protected void updateCommitDate() {
        Date commitDate = model.commit.author.getParsedDate();
        PrettyTime pt = new PrettyTime();

        viewInterface.setCommitDate(commitDate == null ? "" : pt.format(commitDate));
    }

    @Override
    public void onEntryViewChosen() {

        appCurrents.setCurrent("Commit", model);
        RepoModel repo = appCurrents.getCurrent("Repo");

        // this line should be a call to some factory
        detailedView = new CommitEntryDialog(itemView.getContext());
        detailedView.setListener(CommitPresenter.this);
        detailedView.show();

        authRequestsManager.getObservableService(GitHubRepoCommitsService.class)
                .getDetailedCommit(repo.owner.login,repo.name, model.sha)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new CommitSub());
    }

    @Override
    public void onEntryViewPressed() {
        Toast.makeText(itemView.getContext(), "ENTRY VIEW PRESSED", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewFiles() {
        itemView.getContext().startActivity(FrameActivity.prepareFilesIntent(itemView.getContext()));
    }

    @Override
    public void onViewTree() {
        RepoModel repo = appCurrents.getCurrent("Repo");

        Intent frameIntent = FrameActivity.prepareTreeIntent(itemView.getContext(), repo, model.sha);
        itemView.getContext().startActivity(frameIntent);
    }
}
