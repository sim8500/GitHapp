package com.dev.sim8500.githapp.app_logic;

import android.view.View;

import com.dev.sim8500.githapp.interfaces.IRepoView;
import com.dev.sim8500.githapp.models.RepoModel;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Locale;

/**
 * Created by sbernad on 16.04.16.
 */
public class RepoEntryPresenter extends PresenterViewHolder<RepoModel, IRepoView> {

    public RepoEntryPresenter(View itemView) {
        super(itemView);
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

}
