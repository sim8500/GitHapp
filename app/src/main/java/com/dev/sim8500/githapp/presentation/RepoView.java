package com.dev.sim8500.githapp.presentation;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.sim8500.githapp.R;
import com.dev.sim8500.githapp.interfaces.IRepoEntryListener;
import com.dev.sim8500.githapp.interfaces.IRepoView;
import com.dev.sim8500.githapp.models.IssueModel;
import com.dev.sim8500.githapp.models.RepoModel;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sbernad on 19.12.15.
 */

public class RepoView extends FrameLayout
                      implements IRepoView
{
    @Bind(R.id.nameTxtView) protected TextView nameTxtView;
    @Bind(R.id.urlTxtView) protected TextView urlTxtView;
    @Bind(R.id.descTxtView) protected TextView descTxtView;
    @Bind(R.id.row_container) protected LinearLayout rowContainer;
    @Bind(R.id.user_id_view) protected UserIdView userIdView;
    @Bind(R.id.created_txtView) protected TextView createdTxtView;
    @Bind(R.id.updated_txtView) protected TextView updatedTxtView;
    @Bind(R.id.fav_button) protected Button favButton;
    protected IRepoEntryListener listener;

    @Override
    public void setName(CharSequence name) {
        nameTxtView.setText(name);
    }

    @Override
    public void setUrl(CharSequence url) {
        urlTxtView.setText(url);
    }

    @Override
    public void setDescription(CharSequence description) {
        descTxtView.setText(description);
    }

    @Override
    public void setUserId(CharSequence username, String avatarUrl) {
        userIdView.setUsername(username);
        userIdView.setAvatar(avatarUrl);
    }

    @Override
    public void setCreatedDate(CharSequence createdAt) {
        createdTxtView.setText(createdAt);
    }

    @Override
    public void setUpdatedDate(CharSequence updatedAt) {
        updatedTxtView.setText(updatedAt);
    }

    @Override
    public void setListener(IRepoEntryListener listener) {
        this.listener = listener;
    }

    @Override
    public void showFavButton(boolean doShow) {
        this.favButton.setVisibility(doShow ? VISIBLE : GONE);
    }

    @Override
    public void setFavButtonText(@StringRes int textId) {
        this.favButton.setText(textId);
    }

    @OnClick(R.id.row_container)
    protected void onEntryClicked() {
        if(listener != null)
            listener.onRepoChosen();
    }

    @OnClick(R.id.fav_button)
    protected void onFavClicked() {
        if(listener != null) {
            listener.onRepoFav();
        }
    }

    public RepoView(Context context)
    {
        super(context);
        init();
    }

    public RepoView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public RepoView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    private void init()
    {
        inflate(getContext(), R.layout.row_repo, this);
        ButterKnife.bind(this, this);

        userIdView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onRepoOwnerClicked();
                }
            }
        });
    }
}
