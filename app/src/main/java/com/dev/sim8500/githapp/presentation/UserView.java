package com.dev.sim8500.githapp.presentation;

import android.content.Context;
import android.support.annotation.UiThread;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.sim8500.githapp.AuthRequestsManager;
import com.dev.sim8500.githapp.GitHappApp;
import com.dev.sim8500.githapp.R;
import com.dev.sim8500.githapp.models.UserModel;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sbernad on 19.12.15.
 */
public class UserView extends LinearLayout
                      implements ModelView
{
    public UserView(Context context)
    {
        super(context);
        init();
    }

    public UserView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public UserView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.user_panel, this);

        ButterKnife.bind(this);

        if(!this.isInEditMode()) {
            ((GitHappApp) getContext().getApplicationContext()).inject(this);
            webView.getSettings().setJavaScriptEnabled(true);
        }

        if(logOutListener != null)
        {
            logOutButton.setOnClickListener(logOutListener);
        }
    }

    public void loadLoginPage()
    {
        webView.loadUrl(authReqMngr.getOAuthURL().toString());
    }

    @UiThread
    public void changeUserDataVisibility()
    {
        boolean visible = (user != null);

        int visibility = visible ? View.VISIBLE : View.GONE;
        int webViewVisibility = visible ? View.GONE : View.VISIBLE;

        loggedUserPanel.setVisibility(visibility);
        infoTxtView.setText(getContext().getString(visible ? R.string.you_re_logged : R.string.log_in));

        webView.setVisibility(webViewVisibility);
        octoCatImgView.setVisibility(visibility);
    }

    @Override
    public void applyModel(Object model)
    {
        if(model instanceof UserModel)
        {
            applyUser((UserModel)model);
        }
    }

    public void applyUser(UserModel model)
    {
        this.user = model;
        if(user != null)
        {
            nameTxtView.setText(model.name);
        }

        changeUserDataVisibility();
    }

    public WebView getWebView() { return webView; }

    public void setLogOutButtonListener(Button.OnClickListener lstnr)
    {
        logOutListener = lstnr;
        if(logOutButton != null)
        {
            logOutButton.setOnClickListener(logOutListener);
        }
    }

    @Inject protected AuthRequestsManager authReqMngr;

    @Bind(R.id.panel_info) protected TextView infoTxtView;
    @Bind(R.id.user_name) protected TextView nameTxtView;
    @Bind(R.id.log_out_button) protected Button logOutButton;
    @Bind(R.id.logged_user_panel) protected LinearLayout loggedUserPanel;
    @Bind(R.id.webView) protected WebView webView;
    @Bind(R.id.octocat_img) protected ImageView octoCatImgView;

    private Button.OnClickListener logOutListener;

    private UserModel user;
}
