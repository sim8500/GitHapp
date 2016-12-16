package com.dev.sim8500.githapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.sim8500.githapp.interfaces.IUserIdView;
import com.dev.sim8500.githapp.models.UserModel;
import com.dev.sim8500.githapp.presentation.CircularFrameTransformBuilder;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sbernad on 13.12.2016.
 */

public class UserProfileFragment extends Fragment {

    @Bind(R.id.avatar_imgView) protected ImageView avatarImgView;
    @Bind(R.id.username_txtView) protected TextView usernameTxtView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View inflatedView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ButterKnife.bind(this, inflatedView);

        return inflatedView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        if(args == null) {
            return;
        }

        UserModel user = args.getParcelable(GitHappApp.USER_PROFILE_MODEL);
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
