package com.dev.sim8500.githapp.app_logic;

import android.view.View;

import com.dev.sim8500.githapp.interfaces.IUserIdView;
import com.dev.sim8500.githapp.models.UserModel;

/**
 * Created by sbernad on 16/03/2017.
 */

public class UserIdPresenter extends PresenterViewHolder<UserModel, IUserIdView> {


    public UserIdPresenter(View itemView) { super(itemView); }

    @Override
    public void updateView() {
        viewInterface.setAvatar(model.avatarUrl);
        viewInterface.setUsername(model.login);
    }
}
