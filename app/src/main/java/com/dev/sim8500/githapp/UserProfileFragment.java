package com.dev.sim8500.githapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.sim8500.githapp.app_logic.AuthRequestsManager;
import com.dev.sim8500.githapp.interfaces.IUserIdView;
import com.dev.sim8500.githapp.models.UserModel;
import com.dev.sim8500.githapp.presentation.CircularFrameTransformBuilder;
import com.dev.sim8500.githapp.services.GitHubUserService;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sbernad on 13.12.2016.
 */

public class UserProfileFragment extends Fragment {

    public static class UserProfileSub extends Subscriber<UserModel> {

        WeakReference<UserProfileFragment> fragmentRef;

        public UserProfileSub(UserProfileFragment frag) {
            fragmentRef = new WeakReference<UserProfileFragment>(frag);
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(UserModel userModel) {
            if(fragmentRef.get() != null) {
                fragmentRef.get().onUserModelReceived(userModel);
            }
        }
    }
    @Inject protected AuthRequestsManager authReqMngr;

    @Bind(R.id.avatar_imgView) protected ImageView avatarImgView;
    @Bind(R.id.username_txtView) protected TextView usernameTxtView;
    @Bind(R.id.name_txtView)    protected TextView nameTxtView;
    @Bind(R.id.fwerCount_txtView) protected TextView followersTxtView;
    @Bind(R.id.fwingCount_txtView) protected TextView followingTxtView;
    @Bind(R.id.reposCount_txtView) protected TextView reposTxtView;
    @Bind(R.id.gistsCount_txtView) protected TextView gistsTxtView;
    @Bind(R.id.hireable_checkBox)  protected CheckBox hireableCheckBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GitHappApp.getInstance().inject(this);
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

        String userLogin = args.getString(GitHappApp.USER_PROFILE_MODEL);
        authReqMngr.getObservableService(GitHubUserService.class)
                    .getUserByLogin(userLogin)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io())
                    .subscribe(new UserProfileSub(this));
    }

    public void onUserModelReceived(final UserModel user) {

        if(user != null)
        {
            usernameTxtView.setText(user.login);
            nameTxtView.setText(user.name);

            if(user.hireable) {
                usernameTxtView.setText(user.login);
            }

            followersTxtView.setText(String.valueOf(user.followers));

            followingTxtView.setText(String.valueOf(user.following));

            reposTxtView.setText(String.valueOf(user.publicRepos));

            //gistsTxtView.setText(String.valueOf(user.publicGists));

            hireableCheckBox.setChecked(user.hireable);

            Picasso.with(getContext())
                    .load(user.avatarUrl)
                    .noFade()
                    .transform(CircularFrameTransformBuilder.build())
                    .into(avatarImgView);

            reposTxtView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = FrameActivity.prepareUserReposIntent(getContext(), user.login);
                    startActivity(intent);
                }
            });

            followersTxtView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = FrameActivity.prepareUserFollowersIntent(getContext(), user.login);
                    startActivity(intent);
                }
            });
        }
    }
}
