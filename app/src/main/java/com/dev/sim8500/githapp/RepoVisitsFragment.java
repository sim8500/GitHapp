package com.dev.sim8500.githapp;


import com.activeandroid.query.Select;
import com.dev.sim8500.githapp.app_logic.GitHappCurrents;
import com.dev.sim8500.githapp.app_logic.RecyclerBaseAdapter;
import com.dev.sim8500.githapp.app_logic.RepoVisitEntryBinder;
import com.dev.sim8500.githapp.app_logic.RepoVisitEntryPresenter;
import com.dev.sim8500.githapp.db.RepoVisits;
import com.dev.sim8500.githapp.models.UserModel;

import java.util.List;

/**
 * Created by sbernad on 26.02.2017.
 */

public class RepoVisitsFragment extends ContentFragment {

    protected RecyclerBaseAdapter<RepoVisits, RepoVisitEntryPresenter> rvAdapter = new RecyclerBaseAdapter<>(new RepoVisitEntryBinder());

    @Override
    public void onStart() {
        super.onStart();

        UserModel user = GitHappCurrents.getInstance().getCurrent("User");
        List<RepoVisits> resList = new Select().from(RepoVisits.class)
                                                .where("Owner NOT LIKE ?", user.login)
                                                .execute();

        rvAdapter.clearItems();
        rvAdapter.initAdapter(this.getContext(), resList);

        recyclerView.setAdapter(rvAdapter);
    }
}
