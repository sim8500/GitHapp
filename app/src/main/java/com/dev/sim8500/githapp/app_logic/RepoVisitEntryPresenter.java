package com.dev.sim8500.githapp.app_logic;

import android.content.Intent;
import android.view.View;

import com.dev.sim8500.githapp.RepoBrowserActivity;
import com.dev.sim8500.githapp.db.RepoVisits;
import com.dev.sim8500.githapp.interfaces.IRepoEntryListener;
import com.dev.sim8500.githapp.interfaces.IRepoVisitView;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.Locale;

/**
 * Created by sbernad on 26.02.2017.
 */

public class RepoVisitEntryPresenter extends PresenterViewHolder<RepoVisits, IRepoVisitView> implements IRepoEntryListener {


    public RepoVisitEntryPresenter(View itemView) {
        super(itemView);
    }

    @Override
    public void updateView() {
        viewInterface.setName(model.name == null ? " " : model.name);
        viewInterface.setUrl(model.repoUrl);
        viewInterface.setOwner(model.owner);

        Date visitDate = new Date(model.visitedAt);
        PrettyTime pt = new PrettyTime(Locale.getDefault());

        viewInterface.setVisitDate(pt.format(visitDate));
    }

    @Override
    public void onRepoChosen() {
        Intent repoBrowserIntent = RepoBrowserActivity.prepareIntent(itemView.getContext(), model.owner, model.name);
        itemView.getContext().startActivity(repoBrowserIntent);
    }

    @Override
    public void onRepoOwnerClicked() {
        // no action here...
    }
}
