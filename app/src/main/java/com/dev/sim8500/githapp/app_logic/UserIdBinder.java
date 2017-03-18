package com.dev.sim8500.githapp.app_logic;

import android.content.Context;
import android.view.ViewGroup;

import com.dev.sim8500.githapp.interfaces.IRecyclerBinder;
import com.dev.sim8500.githapp.models.UserModel;
import com.dev.sim8500.githapp.presentation.UserIdView;

/**
 * Created by sbernad on 16/03/2017.
 */

public class UserIdBinder implements IRecyclerBinder<UserModel, UserIdPresenter> {

    @Override
    public UserIdPresenter createHolderInstance(Context context, ViewGroup parent, int viewType) {

        UserIdView bindedView = new UserIdView(context);
        UserIdPresenter presenter = new UserIdPresenter(bindedView);

        return presenter;
    }

    @Override
    public void bindHolder(UserIdPresenter userIdPresenter, UserModel model) {
        userIdPresenter.setModel(model);
    }

    @Override
    public int getViewTypeForModel(UserModel model) {
        return 0;
    }
}
