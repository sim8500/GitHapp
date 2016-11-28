package com.dev.sim8500.githapp.app_logic;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.dev.sim8500.githapp.GitHappApp;
import com.dev.sim8500.githapp.RepoBrowserActivity;
import com.dev.sim8500.githapp.ReposListFragment;
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

    @Inject
    protected FavReposStore favReposStore;

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

        UserModel user = appCurrents.getCurrent("User");

        viewInterface.showFavButton(!(user.login.equals(model.owner.login)));

    }

    @Override
    public void onRepoChosen() {
        appCurrents.setCurrent("Repo", model);

        Intent repoBrowserIntent = RepoBrowserActivity.prepareIntent(itemView.getContext());
        itemView.getContext().startActivity(repoBrowserIntent);
    }

    @Override
    public void onRepoFav() {

        addFavRepo(model).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<RepoModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(itemView.getContext(), "Something went wrong...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(List<RepoModel> value) {
                        Toast.makeText( itemView.getContext(),
                                String.format("You now have %s repos fav'ed!", value.size()),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    protected Observable<List<RepoModel>> addFavRepo(RepoModel repo) {
        return Observable.create(new AddFavRepoAction(repo, favReposStore));
    }
}
