package com.dev.sim8500.githapp.presentation;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.sim8500.githapp.R;
import com.dev.sim8500.githapp.interfaces.IUserIdView;
import com.dev.sim8500.githapp.models.UserModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sbernad on 13.03.16.
 */
public class UserIdView extends LinearLayout implements IUserIdView
{

    public UserIdView(Context context) {
        super(context);
        init();
    }

    public UserIdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UserIdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_user, this);

        ButterKnife.bind(this);
    }

    @Bind(R.id.username_txtView) protected TextView usernameTextView;
    @Bind(R.id.avatar_imgView) protected ImageView avatarImgView;

    @Override
    public void setUsername(CharSequence username) {
        usernameTextView.setText(username);
    }

    @Override
    public void setAvatar(String url) {
        Picasso.with(getContext())
                .load(url)
                .resizeDimen(R.dimen.avatar_size, R.dimen.avatar_size)
                .noFade()
                .transform(CircularFrameTransformBuilder.build())
                .into(avatarImgView);
    }
}
