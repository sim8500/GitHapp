package com.dev.sim8500.githapp.presentation;

import android.content.Context;
import android.support.annotation.UiThread;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.sim8500.githapp.app_logic.AuthRequestsManager;
import com.dev.sim8500.githapp.GitHappApp;
import com.dev.sim8500.githapp.R;
import com.dev.sim8500.githapp.models.UserModel;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sbernad on 19.12.15.
 */
public class UserPanelView extends LinearLayout
                      implements ModelView<UserModel>
{
    @Inject protected AuthRequestsManager authReqMngr;

    @Bind(R.id.panel_info) protected TextView infoTxtView;
    @Bind(R.id.view_user) protected UserIdView userIdView;
    @Bind(R.id.log_out_button) protected Button logOutButton;
    @Bind(R.id.logged_user_panel) protected LinearLayout loggedUserPanel;
    @Bind(R.id.webView) protected WebView webView;
    @Bind(R.id.greetParent) protected View greetParent;
    @Bind(R.id.greetTxtView) protected TextView greetTextView;

    private Button.OnClickListener logOutListener;
    private UserModel user;

    public UserPanelView(Context context)
    {
        super(context);
        init();
    }

    public UserPanelView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public UserPanelView(Context context, AttributeSet attrs, int defStyleAttr)
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
        greetParent.setVisibility(visibility);
    }

    @Override
    public void applyModel(UserModel model)
    {
        this.user = model;
        if(user != null)
        {
            userIdView.setUsername(model.login);
            userIdView.setAvatar(model.avatarUrl);
            greetTextView.setText(this.getResources().getString(R.string.greet_user, user.name));
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
}
