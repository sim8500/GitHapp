package com.dev.sim8500.githapp.app_logic;

import android.content.Intent;

import com.dev.sim8500.githapp.FrameActivity;
import com.dev.sim8500.githapp.GitHappApp;
import com.dev.sim8500.githapp.models.CommitModel;
import com.dev.sim8500.githapp.models.RepoModel;
import com.dev.sim8500.githapp.presentation.CommitView;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

import javax.inject.Inject;

/**
 * Created by sbernad on 15.03.16.
 */
public class CommitPresenter extends PresenterViewHolder<CommitModel, CommitView>
                             implements IEntryViewListener {

    public CommitPresenter(CommitView view) {
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
        view.setAuthor(model.commit.author.name);
    }

    protected void updateMessage() {
        view.setMessage(model.commit.message);
    }

    protected void updateCommitDate() {
        Date commitDate = model.commit.author.getParsedDate();
        PrettyTime pt = new PrettyTime();

        view.setCommitDate(commitDate == null ? "" : pt.format(commitDate));
    }

    @Override
    public void onEntryViewChosen() {

        appCurrents.setCurrent("Commit", model);
        RepoModel repo = appCurrents.getCurrent("Repo");

        Intent frameIntent = FrameActivity.prepareIntent(view.getContext(), model.sha);
        view.getContext().startActivity(frameIntent);
    }

    @Inject
    protected GitHappCurrents appCurrents;
}
