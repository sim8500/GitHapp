package com.dev.sim8500.githapp.app_logic;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.dev.sim8500.githapp.FrameActivity;
import com.dev.sim8500.githapp.GitHappApp;
import com.dev.sim8500.githapp.R;
import com.dev.sim8500.githapp.RepoBrowserActivity;
import com.dev.sim8500.githapp.interfaces.IRepoEntryListener;
import com.dev.sim8500.githapp.interfaces.IRepoView;
import com.dev.sim8500.githapp.models.RepoModel;
import com.dev.sim8500.githapp.models.UserModel;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sbernad on 16.04.16.
 */
public class RepoEntryPresenter extends PresenterViewHolder<RepoModel, IRepoView>
                                implements IRepoEntryListener {

    @Inject
    protected GitHappCurrents appCurrents;


    protected boolean listenToRepoChosen = true;

    public RepoEntryPresenter(View itemView) {
        super(itemView);

        ((GitHappApp) itemView.getContext().getApplicationContext()).inject(this);

        viewInterface.setListener(this);
    }

    public void setRepoChosenListening(boolean doListen) {
        listenToRepoChosen = doListen;
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

        UserModel user = appCurrents.getCurrent("User");

    }

    @Override
    public void onRepoChosen() {

        if(!listenToRepoChosen) {
            return; // nothing to do here
        }

        appCurrents.setCurrent("Repo", model);

        Intent repoBrowserIntent = RepoBrowserActivity.prepareIntent(itemView.getContext());
        itemView.getContext().startActivity(repoBrowserIntent);
    }

    @Override
    public void onRepoOwnerClicked() {
        UserModel user = appCurrents.getCurrent("User");
        if(!model.owner.login.equals(user.login)) {
            Context context = itemView.getContext();
            context.startActivity(FrameActivity.prepareUserProfileIntent(context, model.owner.login));
        }
    }

}
