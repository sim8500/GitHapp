package com.dev.sim8500.githapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.sim8500.githapp.app_logic.AuthRequestsManager;
import com.dev.sim8500.githapp.app_logic.GitHappCurrents;
import com.dev.sim8500.githapp.app_logic.RepoEntryPresenter;
import com.dev.sim8500.githapp.models.RepoModel;
import com.dev.sim8500.githapp.models.UserModel;
import com.dev.sim8500.githapp.presentation.CircularFrameTransformBuilder;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sbernad on 14.06.16.
 */
public class UserFragment extends Fragment {

    @Inject protected AuthRequestsManager authReqMngr;
    @Inject protected GitHappCurrents appCurrents;

    @Bind(R.id.avatar_imgView) protected ImageView avatarImgView;
    @Bind(R.id.username_txtView) protected TextView usernameTxtView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GitHappApp.getInstance().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View inflatedView = inflater.inflate(R.layout.user_panel, container, false);
        ButterKnife.bind(this, inflatedView);

        return inflatedView;
    }

    @Override
    public void onStart() {
        super.onStart();

        UserModel user = appCurrents.getCurrent("User");
        if(user != null)
        {
            usernameTxtView.setText(user.login);

            Picasso.with(getContext())
                    .load(user.avatarUrl)
                    .resizeDimen(R.dimen.large_avatar_size, R.dimen.large_avatar_size)
                    .noFade()
                    .transform(CircularFrameTransformBuilder.build())
                    .into(avatarImgView);
        }
    }
}
