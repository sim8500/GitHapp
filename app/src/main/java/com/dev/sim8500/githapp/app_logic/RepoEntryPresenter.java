package com.dev.sim8500.githapp.app_logic;

import android.content.Intent;
import android.view.View;

import com.dev.sim8500.githapp.GitHappApp;
import com.dev.sim8500.githapp.RepoBrowserActivity;
import com.dev.sim8500.githapp.interfaces.IRepoEntryListener;
import com.dev.sim8500.githapp.interfaces.IRepoView;
import com.dev.sim8500.githapp.models.RepoModel;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by sbernad on 16.04.16.
 */
public class RepoEntryPresenter extends PresenterViewHolder<RepoModel, IRepoView>
                                implements IRepoEntryListener {

    @Inject
    protected GitHappCurrents appCurrents;

    public RepoEntryPresenter(View itemView) {
        super(itemView);

        ((GitHappApp) itemView.getContext().getApplicationContext()).inject(this);
    }

    @Override
    public void updateView() {

        viewInterface.setName(model.name);
        viewInterface.setUrl(model.url);
        viewInterface.setDescription(model.description);

        PrettyTime pt = new PrettyTime(Locale.getDefault());

        viewInterface.setCreatedDate(pt.format(model.getCreatedAtDate()));
        viewInterface.setUpdatedDate(pt.format(model.getUpdatedAtDate()));

        viewInterface.setUserId(model.owner.login, model.owner.avatarUrl);
    }

    @Override
    public void onRepoChosen() {
        appCurrents.setCurrent("Repo", model);

        Intent repoBrowserIntent = RepoBrowserActivity.prepareIntent(itemView.getContext());
        itemView.getContext().startActivity(repoBrowserIntent);
    }
}
